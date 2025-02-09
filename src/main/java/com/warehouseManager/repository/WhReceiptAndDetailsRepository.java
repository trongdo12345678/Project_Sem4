package com.warehouseManager.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;


import com.mapper.Warehouse_recript_mapper;
import com.models.Conversion;
import com.models.Employee;
import com.models.PageView;
import com.models.Product;
import com.models.Warehouse_receipt;
import com.models.Warehouse_receipt_detail;
import com.utils.Views;

import jakarta.servlet.http.HttpSession;

@Repository
public class WhReceiptAndDetailsRepository {
	@Autowired
	private final JdbcTemplate dbwhd;
	public WhReceiptAndDetailsRepository(JdbcTemplate jdbc) {
		this.dbwhd = jdbc;
	}
	
	//Phần Warehouse_receipt 
	
	public List<Warehouse_receipt> findAll(PageView ItemPage, int employeeId) {
	    try {
	        String str_query = String.format(
	            "SELECT wr.%s as id, wr.%s as name, wr.%s as Wh_Id, wr.%s as date, wr.%s as status, " +
	            "w.%s as wh_name, wr.%s as shipping_fee, wr.%s as other_fee, wr.%s as total_fee, wr.%s as employee_id " +
	            "FROM %s wr " +
	            "INNER JOIN %s w ON wr.%s = w.%s " +
	            "WHERE wr.%s = ? " +
	            "ORDER BY wr.%s DESC",
	            Views.COL_WAREHOUSE_RECEIPT_ID, Views.COL_WAREHOUSE_RECEIPT_NAME,
	            Views.COL_WAREHOUSE_RECEIPT_IDWH, Views.COL_WAREHOUSE_RECEIPT_DATE,
	            Views.COL_WAREHOUSE_RECEIPT_STATUS, Views.COL_WAREHOUSE_NAME,
	            Views.COL_WAREHOUSE_RECEIPT_SHIPPINGFEE, Views.COL_WAREHOUSE_RECEIPT_OTHERFEE,
	            Views.COL_WAREHOUSE_RECEIPT_TOTALFEE, Views.COL_WAREHOUSE_RECEIPT_EMP_ID,
	            Views.TBL_WAREHOUSE_RECEIPT, Views.TBL_WAREHOUSE,
	            Views.COL_WAREHOUSE_RECEIPT_IDWH, Views.COL_WAREHOUSE_ID,
	            Views.COL_WAREHOUSE_RECEIPT_EMP_ID, Views.COL_WAREHOUSE_RECEIPT_ID
	        );
 
	        if (ItemPage != null && ItemPage.isPaginationEnabled()) {
	            int count = dbwhd.queryForObject(
	                "SELECT COUNT(*) FROM " + Views.TBL_WAREHOUSE_RECEIPT + " WHERE " + Views.COL_WAREHOUSE_RECEIPT_EMP_ID + " = ?",
	                Integer.class, employeeId
	            );
	            int total_page = (int) Math.ceil((double) count / ItemPage.getPage_size());
	            ItemPage.setTotal_page(total_page);
	            return dbwhd.query(
	                str_query + " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY",
	                new Warehouse_recript_mapper(),
	                employeeId,
	                (ItemPage.getPage_current() - 1) * ItemPage.getPage_size(),
	                ItemPage.getPage_size()
	            );
	        } else {
	            return dbwhd.query(str_query, new Warehouse_recript_mapper(), employeeId);
	        }
	    } catch (DataAccessException e) {
	        System.err.println("Error fetching warehouse receipts: " + e.getMessage());
	        return Collections.emptyList();
	    }
	}
	
	// show list để thêm sản phẩm
	public List<Product> findAllPro(){
		try {
			String sql = "SELECT * FROM Product";
			return dbwhd.query(sql, (rs, rowNum) -> {
	            Product item = new Product();
	            item.setId(rs.getInt(Views.COL_PRODUCT_ID));
	            item.setProduct_name(rs.getString(Views.COL_PRODUCT_NAME));
	            return item;
	        });
		} catch (Exception e) {
			e.printStackTrace();
			return null;
			
		}
	}
	
	// lấy kho theo nhân viên
	public Integer findWarehouseIdByEmployeeId(int employeeId) {
	    String sql = "SELECT w.Id FROM Warehouse w " +
	                 "INNER JOIN Employee e ON w.Id = e.Id " +
	                 "WHERE e.Id = ?";
	    try {
	        return dbwhd.queryForObject(sql, Integer.class, employeeId);
	    } catch (EmptyResultDataAccessException e) {
	        System.err.println("Không tìm thấy kho cho nhân viên ID: " + employeeId);
	        return null;
	    }
	}

	public Warehouse_receipt findId(int id) {
	    try {
	    	String sql = "SELECT wr.*, w.Name AS warehouse_name, " +
	                "wr.Shipping_fee, " +
	                "wr.Other_fee, " +
	                "wr.Total_fee, " +
	                "wr.Employee_Id, " +
	                "e.First_name AS employee_first_name, " +
	                "e.Last_name AS employee_last_name " +
	                "FROM Warehouse_receipt wr " +
	                "LEFT JOIN Warehouse w ON wr.Wh_Id = w.Id " +
	                "LEFT JOIN Employee e ON wr.Employee_Id = e.Id " +
	                "WHERE wr.Id = ?";

	    	return dbwhd.queryForObject(sql, (rs, rowNum) -> {
	    	    Warehouse_receipt wr = new Warehouse_receipt();
	    	    wr.setId(rs.getInt("Id"));
	    	    wr.setName(rs.getString("Name"));
	    	    wr.setWh_id(rs.getInt("Wh_Id"));
	    	    wr.setStatus(rs.getString("Status"));
	    	    Timestamp timestamp = rs.getTimestamp("Date");
	    	    if (timestamp != null) {
	    	        wr.setDate(timestamp.toLocalDateTime());
	    	    }
	    	    wr.setWh_name(rs.getString("warehouse_name"));
	    	    wr.setShipping_fee(rs.getDouble("Shipping_fee"));
	    	    wr.setOther_fee(rs.getDouble("Other_fee"));
	    	    wr.setTotal_fee(rs.getDouble("Total_fee"));
	    	    wr.setEmployee_id(rs.getInt("Employee_Id"));
	    	    String employeeFullName = rs.getString("employee_first_name") + " " + rs.getString("employee_last_name");
	    	    wr.setEmployee_name(employeeFullName);
	    	    
	    	    return wr;
	    	}, id);

	    } catch (EmptyResultDataAccessException e) {
	        System.err.println("Không tìm thấy biên nhận kho với ID: " + id);
	        return null;
	    } catch (DataAccessException e) {
	        System.err.println("Lỗi khi truy vấn biên nhận kho với ID: " + id + " - " + e.getMessage());
	        return null;
	    }
	}
	
	// Hàm lấy conversion_rate cho product_id từ bảng Conversion
	private int getConversionRateByProductId(int productId, int conversionId) {
	    String sql = "SELECT " + Views.COL_CONVERSION_RATE +
	                 " FROM " + Views.TBL_CONVERSION +
	                 " WHERE " + Views.COL_CONVERSION_PRODUCT_ID + " = ? AND " + Views.COL_CONVERSION_ID + " = ?";
	    try {
	        List<Integer> conversionRates = dbwhd.queryForList(sql, Integer.class, productId, conversionId);
	        if (conversionRates.isEmpty()) {
	            return 0;
	        }
	        return conversionRates.get(0);
	    } catch (DataAccessException e) {
	        e.printStackTrace();
	        return 0;
	    }
	}


		
		@SuppressWarnings("deprecation")
		public List<Conversion> getAllConversions(int productId) {
		    String sql = "SELECT c.*, u_from.Name AS fromUnitName, u_to.Name AS toUnitName " +
		                 "FROM " + Views.TBL_CONVERSION + " c " +
		                 "LEFT JOIN " + Views.TBL_UNIT + " u_from ON c.From_unit_id = u_from.Id " +
		                 "LEFT JOIN " + Views.TBL_UNIT + " u_to ON c.To_unit_id = u_to.Id " +
		                 "WHERE c.Product_id = ?";

		    return dbwhd.query(sql, new Object[]{productId}, new RowMapper<Conversion>() {
		        @Override
		        public Conversion mapRow(ResultSet rs, int rowNum) throws SQLException {
		            Conversion conversion = new Conversion();
		            conversion.setId(rs.getInt(Views.COL_CONVERSION_ID));
		            conversion.setProduct_id(rs.getInt(Views.COL_CONVERSION_PRODUCT_ID));
		            conversion.setFrom_unit_id(rs.getInt(Views.COL_CONVERSION_FROM_UNIT_ID));
		            conversion.setTo_unit_id(rs.getInt(Views.COL_CONVERSION_TO_UNIT_ID));
		            conversion.setConversion_rate(rs.getInt(Views.COL_CONVERSION_RATE));
		            
		            conversion.setFromUnitName(rs.getString("fromUnitName"));
		            conversion.setToUnitName(rs.getString("toUnitName"));
		            
		            return conversion;
		        }
		    });
		}


		
	//thêm 3 bản Warehouse_receipt , Warehouse_receipt_detail và stock
		@Transactional
		public int addRequestOrderWithDetails(Warehouse_receipt receipt, List<Warehouse_receipt_detail> details) {
		    try {
		        // Tính tổng `totalFee`
		        double totalFee = details.stream()
		                .mapToDouble(detail -> {
		                    int conversionRate = getConversionRateByProductId(detail.getProduct_id(), detail.getConversion_id());
		                    int convertedQuantity = detail.getQuantity() * conversionRate;
		                    return convertedQuantity * detail.getWh_price();
		                })
		                .sum();

		        totalFee += receipt.getShipping_fee() + receipt.getOther_fee();
		        receipt.setTotal_fee(totalFee);

		        // Thêm dữ liệu vào bảng `Warehouse_receipt`
		        String sql1 = "INSERT INTO " + Views.TBL_WAREHOUSE_RECEIPT + " (" +
		                Views.COL_WAREHOUSE_RECEIPT_NAME + ", " +
		                Views.COL_WAREHOUSE_RECEIPT_IDWH + ", " +
		                Views.COL_WAREHOUSE_RECEIPT_DATE + ", " +
		                Views.COL_WAREHOUSE_RECEIPT_STATUS + ", " +
		                Views.COL_WAREHOUSE_RECEIPT_SHIPPINGFEE + ", " +
		                Views.COL_WAREHOUSE_RECEIPT_OTHERFEE + ", " +
		                Views.COL_WAREHOUSE_RECEIPT_TOTALFEE + ", " +
		                Views.COL_WAREHOUSE_RECEIPT_EMP_ID + ") " +
		                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		        KeyHolder receiptKeyHolder = new GeneratedKeyHolder();
		        int result1 = dbwhd.update(connection -> {
		            var ps = connection.prepareStatement(sql1, new String[]{Views.COL_WAREHOUSE_RECEIPT_ID});
		            ps.setString(1, receipt.getName());
		            ps.setInt(2, receipt.getWh_id());
		            ps.setDate(3, java.sql.Date.valueOf(receipt.getDate().toLocalDate()));
		            ps.setString(4, receipt.getStatus());
		            ps.setDouble(5, receipt.getShipping_fee());
		            ps.setDouble(6, receipt.getOther_fee());
		            ps.setDouble(7, receipt.getTotal_fee());
		            ps.setInt(8, receipt.getEmployee_id());
		            return ps;
		        }, receiptKeyHolder);

		        int generatedReceiptId = receiptKeyHolder.getKey().intValue();

		        // Thêm dữ liệu vào bảng `Warehouse_receipt_detail` và cập nhật bảng `Stock`
		        String sql2 = "INSERT INTO " + Views.TBL_WAREHOUSE_RECEIPT_DETAIL + " (" +
		                Views.COL_DETAIL_WAREHOUSE_RECEIPT_ID + ", " +
		                Views.COL_WAREHOUSE_RECEIPT_DETAIL_WH_PRICE + ", " +
		                Views.COL_WAREHOUSE_RECEIPT_DETAIL_QUANTITY + ", " +
		                Views.COL_WAREHOUSE_RECEIPT_DETAIL_PRODUCT_ID + ", " +
		                Views.COL_WAREHOUSE_RECEIPT_DETAIL_CONVERSION + ", " +
		                Views.COL_WAREHOUSE_RECEIPT_DETAILS_STATUS + ") VALUES (?, ?, ?, ?, ?, ?)";

		        String sql3 = "INSERT INTO " + Views.TBL_STOCK + " (" +
		                Views.COL_STOCK_PRODUCT_ID + ", " +
		                Views.COL_STOCK_QUANTITY + ", " +
		                Views.COL_STOCK_STATUS + ", " +
		                Views.COL_STOCK_WARERCDT_ID + ") VALUES (?, ?, ?, ?)";

		        for (Warehouse_receipt_detail detail : details) {
		            int conversionRate = getConversionRateByProductId(detail.getProduct_id(), detail.getConversion_id());
		            int originalQuantity = detail.getQuantity();
		            int convertedQuantity = originalQuantity * conversionRate;

		            detail.setWh_receipt_id(generatedReceiptId);

		            // Thêm vào bảng `Warehouse_receipt_detail` và lấy ID mới
		            KeyHolder detailKeyHolder = new GeneratedKeyHolder();
		            dbwhd.update(connection -> {
		                var ps = connection.prepareStatement(sql2, new String[]{Views.COL_WAREHOUSE_RECEIPT_DETAIL_ID});
		                ps.setInt(1, detail.getWh_receipt_id());
		                ps.setDouble(2, detail.getWh_price());
		                ps.setInt(3, detail.getQuantity());
		                ps.setInt(4, detail.getProduct_id());
		                ps.setInt(5, detail.getConversion_id());
		                ps.setString(6, detail.getStatus());
		                return ps;
		            }, detailKeyHolder);

		            int generatedDetailId = detailKeyHolder.getKey().intValue();

		            // Thêm vào bảng `Stock`
		            dbwhd.update(sql3,
		                    detail.getProduct_id(),
		                    convertedQuantity,
		                    detail.getStatus(),
		                    generatedDetailId
		            );
		        }
		        return generatedReceiptId;
		    } catch (DataAccessException e) {
		        e.printStackTrace();
		        return -1;
		    }
		}
		
	//randum tên phiếu nhập kho 
	public String generateReceiptName() {
	    String prefix = "ES01/";
	    Integer maxNumber = null;
	    try {

	        String sql = "SELECT MAX(SUBSTRING(Name, 6, 3)) FROM Warehouse_receipt WHERE Name LIKE ?";
	        maxNumber = dbwhd.queryForObject(sql, Integer.class, prefix + "%");
	    } catch (DataAccessException e) {
	        System.err.println("Error querying max number: " + e.getMessage());
	    }

	    int nextNumber = (maxNumber == null) ? 1 : maxNumber + 1;
	    if (nextNumber > 999) {
	        try {
	            int currentPrefixNumber = Integer.parseInt(prefix.substring(2, 4)) + 1;
	            prefix = "ES" + String.format("%02d", currentPrefixNumber) + "/";
	            nextNumber = 1;
	        } catch (NumberFormatException nfe) {
	            System.err.println("Error parsing prefix number: " + nfe.getMessage());
	        }
	    }
	    return prefix + String.format("%03d", nextNumber);
	}
	public int getEmployeeIdFromSession(HttpSession session) {
	    try {
	        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
	        if (loggedInEmployee != null) {
	            return loggedInEmployee.getId();
	        }
	        throw new IllegalStateException("Employee not found in session");
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("Error retrieving employee ID from session", e);
	    }
	}

	@SuppressWarnings("deprecation")
	public Integer getWarehouseIdByEmployeeId(int employeeId) {
	    try {
	        String sql = "SELECT " + Views.COL_EMPLOYEE_WAREHOUS_WAREHOUSE_ID +
	                     " FROM " + Views.TBL_EMPLOYEE_WAREHOUSE +
	                     " WHERE " + Views.COL_EMPLOYEE_WAREHOUS_EMPLOYEE_ID + " = ?";
	        return dbwhd.queryForObject(sql, new Object[]{employeeId}, Integer.class);
	    } catch (EmptyResultDataAccessException e) {
	        return null;
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("Error retrieving warehouse ID for employee ID: " + employeeId, e);
	    }
	}


	

	// Phần Warehouse_receipt_detail ====================================================
	
	//show list chi tiết phiếu nhập kho
	public List<Warehouse_receipt_detail> findDetailsByReceiptId(int whReceiptId, PageView pageView) {
	    try {
	        String baseSql = "SELECT wrd.*, p.product_name, wrd." + Views.COL_WAREHOUSE_RECEIPT_DETAILS_STATUS
	                + ", wrd." + Views.COL_WAREHOUSE_RECEIPT_DETAIL_CONVERSION + ", " +
	                "u2.Name AS to_unit_name, " +
	                "u1.Name AS from_unit_name, " +
	                "c." + Views.COL_CONVERSION_RATE + " AS conversion_rate " +
	                "FROM " + Views.TBL_WAREHOUSE_RECEIPT_DETAIL + " wrd " +
	                "JOIN Product p ON wrd." + Views.COL_WAREHOUSE_RECEIPT_DETAIL_PRODUCT_ID + " = p.id " +
	                "JOIN Conversion c ON wrd." + Views.COL_WAREHOUSE_RECEIPT_DETAIL_CONVERSION + " = c.id " +
	                "JOIN Unit u2 ON c." + Views.COL_CONVERSION_TO_UNIT_ID + " = u2." + Views.COL_UNIT_ID + " " +
	                "JOIN Unit u1 ON c." + Views.COL_CONVERSION_FROM_UNIT_ID + " = u1." + Views.COL_UNIT_ID + " " +
	                "WHERE wrd." + Views.COL_DETAIL_WAREHOUSE_RECEIPT_ID + " = ?";

	        if (pageView != null && pageView.isPaginationEnabled()) {
	            String countSql = "SELECT COUNT(*) FROM " + Views.TBL_WAREHOUSE_RECEIPT_DETAIL +
	                              " WHERE " + Views.COL_DETAIL_WAREHOUSE_RECEIPT_ID + " = ?";
	            int totalCount = dbwhd.queryForObject(countSql, Integer.class, whReceiptId);

	            int totalPage = (int) Math.ceil((double) totalCount / pageView.getPage_size());
	            pageView.setTotal_page(totalPage);

	            baseSql += " ORDER BY wrd." + Views.COL_WAREHOUSE_RECEIPT_DETAIL_ID + " DESC " +
	                       "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

	            return dbwhd.query(baseSql, (rs, rowNum) -> {
	                Warehouse_receipt_detail wrd = mapRowToWarehouseReceiptDetail(rs);
	                return wrd;
	            }, whReceiptId, (pageView.getPage_current() - 1) * pageView.getPage_size(), pageView.getPage_size());
	        } else {
	            return dbwhd.query(baseSql, (rs, rowNum) -> {
	                Warehouse_receipt_detail wrd = mapRowToWarehouseReceiptDetail(rs);
	                return wrd;
	            }, whReceiptId);
	        }
	    } catch (Exception e) {
	        System.err.println("Error while fetching details by receipt ID: " + e.getMessage());
	        e.printStackTrace();
	        return Collections.emptyList();
	    }
	}

	private Warehouse_receipt_detail mapRowToWarehouseReceiptDetail(ResultSet rs) throws SQLException {
	    Warehouse_receipt_detail wrd = new Warehouse_receipt_detail();
	    wrd.setId(rs.getInt(Views.COL_WAREHOUSE_RECEIPT_DETAIL_ID));
	    wrd.setWh_receipt_id(rs.getInt(Views.COL_DETAIL_WAREHOUSE_RECEIPT_ID));
	    int originalQuantity = rs.getInt(Views.COL_WAREHOUSE_RECEIPT_DETAIL_QUANTITY);
	    int conversionRate = rs.getInt("conversion_rate");
	    int convertedQuantity = originalQuantity * conversionRate;
	    wrd.setQuantity(rs.getInt(Views.COL_WAREHOUSE_RECEIPT_DETAIL_QUANTITY));
	    wrd.setWh_price(rs.getDouble(Views.COL_WAREHOUSE_RECEIPT_DETAIL_WH_PRICE));
	    wrd.setProduct_id(rs.getInt(Views.COL_WAREHOUSE_RECEIPT_DETAIL_PRODUCT_ID));
	    wrd.setProduct_name(rs.getString("product_name"));
	    wrd.setStatus(rs.getString(Views.COL_WAREHOUSE_RECEIPT_DETAILS_STATUS));
	    wrd.setConversion_id(rs.getInt(Views.COL_WAREHOUSE_RECEIPT_DETAIL_CONVERSION));
	    wrd.setToUnitName(rs.getString("to_unit_name"));
	    wrd.setFromUnitName(rs.getString("from_unit_name"));
	    wrd.setConversion_rate(convertedQuantity);
	    return wrd;
	}


//	public Conversion findConversionByProductId(int productId) {
//	    Conversion conversion = null;
//	    try {
//	        String sql = "SELECT c.* FROM " + Views.TBL_CONVERSION + " c " +
//	                     "WHERE c." + Views.COL_CONVERSION_PRODUCT_ID + " = ?";
//	        
//	        List<Conversion> conversions = dbwhd.query(sql, (rs, rowNum) -> {
//	        	Conversion item = new Conversion();
//	    		item.setId(rs.getInt(Views.COL_CONVERSION_ID));
//	    		item.setConversion_rate(rs.getInt(Views.COL_CONVERSION_RATE));
//	    		item.setFrom_unit_id(rs.getInt(Views.COL_CONVERSION_FROM_UNIT_ID));
//	    		item.setTo_unit_id(rs.getInt(Views.COL_CONVERSION_TO_UNIT_ID));    
//	    		item.setProduct_id(rs.getInt(Views.COL_PRODUCT_ID));
//
//	    		return item;
//	        }, productId);
//
//	        if (!conversions.isEmpty()) {
//	            conversion = conversions.get(0); 
//	        }
//	    } catch (Exception e) {
//	        System.err.println("Error while fetching conversion by product ID: " + e.getMessage());
//	        e.printStackTrace();
//	    }
//	    return conversion;
//	}
	
}