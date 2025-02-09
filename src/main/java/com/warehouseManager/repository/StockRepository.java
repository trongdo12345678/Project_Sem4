package com.warehouseManager.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mapper.Conversion_mapper;
import com.mapper.Product_mapper;
import com.mapper.StockSum_mapper;
import com.mapper.Stock_mapper;
import com.models.Conversion;
import com.models.ConversionShow;
import com.models.Product;
import com.models.Stock;
import com.models.StockSumByWarehouseId;
import com.utils.Views;
import java.util.Map;


@Repository
public class StockRepository {
	
    @Autowired
    private JdbcTemplate jdbcTemplate;
	

    public List<StockSumByWarehouseId> findAllStock(int warehouseId, LocalDate startDate, LocalDate endDate) {
        String sql = String.format("""
                SELECT 
                    p.Id,
                    p.Product_name,
                    u.Name, 
                    wh.Id AS Warehouse_Id,
                    whr.Date,                    
                    SUM(st.Quantity) AS total_Quantity,
                    AVG(wrd.Wh_price) AS Average_Wh_price                   
                FROM 
                    stock st
                    JOIN Product p ON st.Id_product = p.Id
                    JOIN Unit u ON p.Unit_id = u.Id 
                    JOIN Warehouse_receipt_detail wrd ON st.Wh_rc_dt_Id = wrd.Id
                    JOIN Warehouse_receipt whr ON wrd.Wh_receiptId = whr.Id
                    JOIN Warehouse wh ON whr.Wh_Id = wh.Id
                    JOIN employee_warehouse ew ON wh.Id = ew.Warehouse_Id
                WHERE 
                    ew.Warehouse_Id = ?
                    AND whr.Date BETWEEN ?  AND ?
                GROUP BY 
                    p.Id, p.Product_name, u.Name, wh.Id, whr.Date
                ORDER BY 
                      whr.Date DESC, wh.Id, p.Product_name;
                """);

        List<StockSumByWarehouseId> stocks = jdbcTemplate.query(sql, new StockSum_mapper(), warehouseId, startDate, endDate);

        
        for (StockSumByWarehouseId stock : stocks) {

            String sql1 = """
                    SELECT c.*, u.[Name] as fromName, u1.[Name] as toName 
                    FROM Conversion c
                    JOIN Unit u ON c.From_unit_id = u.Id
                    JOIN Unit u1 ON c.To_unit_id = u1.Id
                    WHERE c.Product_Id = ?;
                    """;

            List<Conversion> conversions = jdbcTemplate.query(sql1, (rs, rowNum) -> {
                Conversion conversion = new Conversion();
                conversion.setId(rs.getInt("Id"));
                conversion.setFrom_unit_id(rs.getInt("From_unit_id"));
                conversion.setTo_unit_id(rs.getInt("To_unit_id"));
                conversion.setConversion_rate(rs.getInt("Conversion_rate"));
                conversion.setFromUnitName(rs.getString("fromName"));
                conversion.setToUnitName(rs.getString("toName"));
                return conversion;
            }, stock.getProductId());

            // Now, process conversion and add to the list
            List<ConversionShow> conversionshows = new ArrayList<>();
            for (Conversion cs : conversions) {
                int convertedQuantity = (int) (stock.getQuantity() / cs.getConversion_rate());

                ConversionShow conversionShow = new ConversionShow(cs.getFromUnitName(), convertedQuantity);

                conversionshows.add(conversionShow);
            }
            stock.setConversions(conversionshows);
        }

        return stocks;
    }

    

    public List<Stock> findStockDetail(int warehouseId,  LocalDate startDate, LocalDate endDate) {
        String sql = String.format("""
                SELECT 
                    st.*,
                    p.Product_name,
                    p.id,
                    u.Name,
                    wrd.Wh_price,
                    ew.Employee_Id,
                    ew.Warehouse_Id,
                    wh.Name,
                    whr.Shipping_fee,
                    whr.Date
                FROM 
                    stock st
                    JOIN Product p ON st.Id_product = p.Id
                    JOIN Unit u ON p.Unit_id = u.Id
                    JOIN Warehouse_receipt_detail wrd ON st.Wh_rc_dt_Id = wrd.Id
                    JOIN Warehouse_receipt whr ON wrd.Wh_receiptId = whr.Id
                    JOIN Warehouse wh ON whr.Wh_Id = wh.Id
                    JOIN employee_warehouse ew ON wh.Id = ew.Warehouse_Id
                WHERE 
                    ew.Warehouse_Id = ?
                    AND whr.Date BETWEEN ? AND ? 
                ORDER BY 
                      whr.Date DESC, wh.Id, p.Product_name;
                """);

        List<Stock> stocks = jdbcTemplate.query(sql, new Stock_mapper(), warehouseId, startDate, endDate);

        for (Stock stock : stocks) {

            String sql1 = """
                     SELECT c.*, u.[Name] as fromName, u1.[Name] as toName 
                    FROM Conversion c
                    JOIN Unit u ON c.From_unit_id = u.Id
                    JOIN Unit u1 ON c.To_unit_id = u1.Id
                    WHERE c.Product_Id = ?;
                    """;

            List<Conversion> conversions = jdbcTemplate.query(sql1, (rs, rowNum) -> {
                Conversion conversion = new Conversion();
                conversion.setId(rs.getInt("Id"));
                conversion.setFrom_unit_id(rs.getInt("From_unit_id"));
                conversion.setTo_unit_id(rs.getInt("To_unit_id"));
                conversion.setConversion_rate(rs.getInt("Conversion_rate"));
                conversion.setFromUnitName(rs.getString("fromName"));
                conversion.setToUnitName(rs.getString("toName"));
                return conversion;
            }, stock.getProduct_id());

            List<ConversionShow> conversionshows = new ArrayList<>();
            for (Conversion cs : conversions) {
                int convertedQuantity = (int) (stock.getQuantity() / cs.getConversion_rate());

                ConversionShow conversionShow = new ConversionShow(cs.getFromUnitName(), convertedQuantity);

                conversionshows.add(conversionShow);
            }

            stock.setListconversion(conversionshows);
        }

        return stocks;
    }

        
    
    public List<Map<String, Object>> getInventoryStats(int warehouseId, LocalDate startDate, LocalDate endDate) {


    	String sql = String.format ("""
		            SELECT 
				    p.%s AS ProductName,
				    CONVERT(VARCHAR, whr.%s, 23) AS ImportDate,  
				    SUM(st.%s) AS StockQuantity,  
				    CASE 
				        WHEN SUM(st.%s) > 0 THEN 'in stock'  
				        ELSE 'out of stock'  
				    END AS StatusProduct
				FROM 
				    %s st
				    JOIN %s p ON st.%s = p.%s
				    JOIN %s wrd ON st.%s = wrd.%s
				    JOIN %s whr ON wrd.%s = whr.%s
				    JOIN %s wh ON whr.%s = wh.%s
				    JOIN %s ew ON wh.%s = ew.%s
				WHERE     
				    ew.%s = ?  
				    AND whr.%s BETWEEN ? AND ?
				GROUP BY 
				    p.%s, CONVERT(VARCHAR, whr.%s, 23), p.%s  
				ORDER BY 
				    CONVERT(VARCHAR, whr.%s, 23) DESC, p.%s;  
		        """, 
		        Views.COL_PRODUCT_NAME, Views.COL_WAREHOUSE_RECEIPT_DATE, Views.COL_STOCK_QUANTITY,
		        Views.COL_STOCK_QUANTITY, Views.TBL_STOCK,
		        Views.TBL_PRODUCT, Views.COL_STOCK_PRODUCT_ID, Views.COL_PRODUCT_ID,                
                Views.TBL_WAREHOUSE_RECEIPT_DETAIL, Views.COL_STOCK_WARERCDT_ID, Views.COL_WAREHOUSE_RECEIPT_DETAIL_ID,
                Views.TBL_WAREHOUSE_RECEIPT, Views.COL_DETAIL_WAREHOUSE_RECEIPT_ID,Views.COL_WAREHOUSE_RECEIPT_ID,
                Views.TBL_WAREHOUSE, Views.COL_WAREHOUSE_RECEIPT_IDWH, Views.COL_WAREHOUSE_ID,
		        Views.TBL_EMPLOYEE_WAREHOUSE, Views.COL_WAREHOUSE_ID, Views.COL_EMPLOYEE_WAREHOUS_WAREHOUSE_ID,
		        Views.COL_EMPLOYEE_WAREHOUS_WAREHOUSE_ID, Views.COL_WAREHOUSE_RECEIPT_DATE,
		        Views.COL_PRODUCT_NAME, Views.COL_WAREHOUSE_RECEIPT_DATE, Views.COL_PRODUCT_ID,
		        Views.COL_WAREHOUSE_RECEIPT_DATE,  Views.COL_PRODUCT_NAME
    	);

    	try {
            return jdbcTemplate.queryForList(sql, warehouseId, startDate, endDate);  
        } catch (Exception e) {            
            System.err.println("Error occurred while fetching inventory stats: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>(); 
        } 
    }
    
    
    public List<Map<String, Object>> findLowStock(int warehouseId,int minQuantity) {
        String sql = String.format("""
                SELECT 
                    p.%s AS productName,
                    SUM(st.%s) AS totalQuantity
                FROM 
                    %s st
                    JOIN %s p ON st.%s = p.%s
                    JOIN %s wrd ON st.%s = wrd.%s
                    JOIN %s whr ON wrd.%s = whr.%s
                    JOIN %s wh ON whr.%s = wh.%s
                    JOIN %s ew ON wh.%s = ew.%s
                WHERE 
                    ew.%s = ?
                GROUP BY 
                    p.%s
                HAVING 
                    SUM(st.%s) < ?
                """,
                Views.COL_PRODUCT_NAME, Views.COL_STOCK_QUANTITY, Views.TBL_STOCK, 
                Views.TBL_PRODUCT, Views.COL_STOCK_PRODUCT_ID, Views.COL_PRODUCT_ID,                
                Views.TBL_WAREHOUSE_RECEIPT_DETAIL, Views.COL_STOCK_WARERCDT_ID, Views.COL_WAREHOUSE_RECEIPT_DETAIL_ID,
                Views.TBL_WAREHOUSE_RECEIPT, Views.COL_DETAIL_WAREHOUSE_RECEIPT_ID,Views.COL_WAREHOUSE_RECEIPT_ID,
                Views.TBL_WAREHOUSE, Views.COL_WAREHOUSE_RECEIPT_IDWH, Views.COL_WAREHOUSE_ID,
                Views.TBL_EMPLOYEE_WAREHOUSE, Views.COL_WAREHOUSE_ID, Views.COL_EMPLOYEE_WAREHOUS_WAREHOUSE_ID,                
                Views.COL_EMPLOYEE_WAREHOUS_WAREHOUSE_ID, Views.COL_PRODUCT_NAME, Views.COL_STOCK_QUANTITY
                
        );
        return jdbcTemplate.queryForList(sql,warehouseId, minQuantity);
    }
    
    public List<Map<String, Object>> findAllLowStock(int warehouseId) {
        String sql = String.format("""
                SELECT 
                    p.%s AS productName,
                    SUM(st.%s) AS totalQuantity
                FROM 
                    %s st
                    JOIN %s p ON st.%s = p.%s
                    JOIN %s wrd ON st.%s = wrd.%s
                    JOIN %s whr ON wrd.%s = whr.%s
                    JOIN %s wh ON whr.%s = wh.%s
                    JOIN %s ew ON wh.%s = ew.%s
                WHERE 
                    ew.%s = ?
                GROUP BY 
                    p.%s
                ORDER BY 
                    SUM(st.%s) DESC
                """,
                Views.COL_PRODUCT_NAME, Views.COL_STOCK_QUANTITY, Views.TBL_STOCK, 
                Views.TBL_PRODUCT, Views.COL_STOCK_PRODUCT_ID, Views.COL_PRODUCT_ID,                
                Views.TBL_WAREHOUSE_RECEIPT_DETAIL, Views.COL_STOCK_WARERCDT_ID, Views.COL_WAREHOUSE_RECEIPT_DETAIL_ID,
                Views.TBL_WAREHOUSE_RECEIPT, Views.COL_DETAIL_WAREHOUSE_RECEIPT_ID,Views.COL_WAREHOUSE_RECEIPT_ID,
                Views.TBL_WAREHOUSE, Views.COL_WAREHOUSE_RECEIPT_IDWH, Views.COL_WAREHOUSE_ID,
                Views.TBL_EMPLOYEE_WAREHOUSE, Views.COL_WAREHOUSE_ID, Views.COL_EMPLOYEE_WAREHOUS_WAREHOUSE_ID,                
                Views.COL_EMPLOYEE_WAREHOUS_WAREHOUSE_ID, Views.COL_PRODUCT_NAME, Views.COL_STOCK_QUANTITY
                
        );
        return jdbcTemplate.queryForList(sql,warehouseId);
    }
    
    public List<Map<String, Object>> listProReceipt(int warehouseId, LocalDate startDate, LocalDate endDate) {


    	String sql = String.format ("""
		            SELECT 
				    p.%s AS ProductName,
				    c.%s AS conversionrate,
				    CONVERT(VARCHAR, wr.%s, 23) AS ImportDate,  
				    SUM(wrd.%s) AS Quantity,
				    SUM(wrd.%s * c.%s ) AS TrueQuantity  				      				    
				FROM 
				    %s  wr
				    JOIN %s w ON wr.%s = w.%s
				    JOIN %s ew ON w.%s = ew.%s
				    JOIN %s wrd ON wr.%s = wrd.%s
				    JOIN %s c ON wrd.%s = c.%s
				    JOIN %s p ON wrd.%s = p.%s
				WHERE     
				    ew.%s = ?  
				    AND wr.%s BETWEEN ? AND ?
				GROUP BY 
    				p.%s, CONVERT(VARCHAR, wr.%s, 23),  c.%s
				ORDER BY 
				    CONVERT(VARCHAR, wr.%s, 23) DESC, p.%s,  c.%s; 
		        """, 
		        Views.COL_PRODUCT_NAME, Views.COL_CONVERSION_RATE, Views.COL_WAREHOUSE_RECEIPT_DATE,
		        Views.COL_WAREHOUSE_RECEIPT_DETAIL_QUANTITY, Views.COL_WAREHOUSE_RECEIPT_DETAIL_QUANTITY, Views.COL_CONVERSION_RATE,
		        Views.TBL_WAREHOUSE_RECEIPT,
		        Views.TBL_WAREHOUSE, Views.COL_WAREHOUSE_RECEIPT_IDWH, Views.COL_WAREHOUSE_ID,
		        Views.TBL_EMPLOYEE_WAREHOUSE, Views.COL_WAREHOUSE_ID, Views.COL_EMPLOYEE_WAREHOUS_WAREHOUSE_ID,
                Views.TBL_WAREHOUSE_RECEIPT_DETAIL, Views.COL_WAREHOUSE_RECEIPT_ID, Views.COL_DETAIL_WAREHOUSE_RECEIPT_ID,
                Views.TBL_CONVERSION, Views.COL_WAREHOUSE_RECEIPT_DETAIL_CONVERSION,Views.COL_CONVERSION_ID,
                Views.TBL_PRODUCT, Views.COL_WAREHOUSE_RECEIPT_DETAIL_PRODUCT_ID, Views.COL_PRODUCT_ID,		        
		        Views.COL_EMPLOYEE_WAREHOUS_WAREHOUSE_ID, Views.COL_WAREHOUSE_RECEIPT_DATE, 
		        Views.COL_PRODUCT_NAME, Views.COL_WAREHOUSE_RECEIPT_DATE, Views.COL_CONVERSION_RATE,
		        Views.COL_WAREHOUSE_RECEIPT_DATE,Views.COL_PRODUCT_NAME, Views.COL_CONVERSION_RATE
    	);

    	try {
            return jdbcTemplate.queryForList(sql, warehouseId, startDate, endDate);  
        } catch (Exception e) {            
            System.err.println("Error occurred while fetching inventory stats: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>(); 
        } 
    }


	
	public List<Conversion> conversionByProductId(int product_Id) {
	    try {
	        String str_query = String.format(
	            "SELECT * FROM Conversion WHERE %s = ?",
	            Views.COL_CONVERSION_PRODUCT_ID
	        );
	        return jdbcTemplate.query(str_query, new Conversion_mapper(), product_Id);
	    } catch (Exception e) {
	        System.err.println("Error fetching conversions: " + e.getMessage());
	        return Collections.emptyList();
	    }
	}

	
	public List<Product> findAddPro(){
		try {
			String sql = String.format("SELECT * FROM %s ", Views.TBL_PRODUCT);
			return jdbcTemplate.query(sql, new Product_mapper());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
