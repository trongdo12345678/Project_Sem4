package com.warehouseManager.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mapper.Order_mapper;
import com.mapper.Request_mapper;
import com.mapper.Warehouse_releasenote_mapper;
import com.models.Order;
import com.models.Order_detail;
import com.models.PageView;
import com.models.Request;
import com.models.Request_detail;
import com.models.Warehouse_releasenote;
import com.models.Warehouse_rn_detail;
import com.utils.Views;

@Repository
public class ReleasenoteRepository {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	// status order Completed
	public boolean completeDelivery(int orderId) {
	    String sql = "UPDATE [Order] SET Status = 'Completed' WHERE Id = ?";
	    try {
	        int rowsAffected = jdbcTemplate.update(sql, orderId);
	        return rowsAffected > 0; // 
	    } catch (DataAccessException e) {
	        e.printStackTrace(); // 
	        return false;
	    }
	}

	
	//get list order take status
	public List<Order> getOrderByStatus(int employeeId) {
		String sql = """
				SELECT 
					o.Id AS id,
					o.OrderID AS OrderId,
					o.Cus_Name AS Name,
					o.Phone As Phone,
					o.Address As Address,
					o.Status As Status
				FROM [Order] o
				WHERE
				o.Employee_id = ? 
				AND o.Status = 'waiting for shipping'
				""";
		try {
		    return jdbcTemplate.query(sql, (rs, rowNum) -> {
		        Order detail = new Order();
		        detail.setId(rs.getInt("id"));
		        detail.setOrderID(rs.getString("OrderId"));
		        detail.setCus_Name(rs.getString("Name"));
		        detail.setPhone(rs.getString("Phone"));
		        detail.setAddress(rs.getString("Address"));
		        detail.setStatus(rs.getString("Status"));
		        return detail;
		    }, employeeId);
		} catch (Exception e) {
		    e.printStackTrace();
		    return new ArrayList<>();
		}

	}
	//update employee_id bang request
	public void updateEmployeeId(int requestId, int employeeId, int warehouseId) {
	    String sql = "UPDATE Request SET Employee_Id = ?, Warehouse_Id = ? WHERE Id = ?";
	    try {
	    	
	        jdbcTemplate.update(sql, employeeId, warehouseId, requestId);
	        

	    } catch (DataAccessException e) {
	        System.err.println("Error updating Employee_Id and Warehouse_Id: " + e.getMessage());
	    }
	}

	
	//update employee_id bang order
	public void updateEmployeeIdInOrder(int orderId, int employeeId) {
	    String sql = "UPDATE [Order] SET Employee_Id = ? WHERE Id = ?";
	    try {
	        jdbcTemplate.update(sql, employeeId, orderId);
	    } catch (DataAccessException e) {
	        System.err.println("Error updating employee ID in order: " + e.getMessage());
	    }
	}

	
	// update status order
	public void updateStatusInOrder(int orderId) {
	    String sql = "UPDATE [Order] SET Status = 'Confirm' WHERE Id = ?";
	    jdbcTemplate.update(sql, orderId);

	    String sql1 = "UPDATE [Order_detail] SET Status = 'Processing' WHERE Order_Id = ?";
	    jdbcTemplate.update(sql1, orderId); 
	}


	// update status request
	public void updateStatusToProcessing(int requestId) {
		String sql = "UPDATE Request SET Status = 'Confirm' WHERE Id = ?";
		jdbcTemplate.update(sql, requestId);
	}

	//hien thi bang request theo employee_id, warehouse_id and order_id = null
	public List<Request> findAllByEmployeeId(PageView itemPage, int employeeId, int warehouseId) {
	    try {
	        
	    	String sql = String.format(
	    		    "SELECT * FROM %s WHERE %s = ? AND (%s IS NULL OR %s = 0) AND %s IS NULL AND %s = 'Confirm' AND %s = ? ORDER BY %s ASC",
	    		    Views.TBL_REQUEST, 
	    		    Views.COL_REQUEST_EMPLOYEE_ID, 
	    		    Views.COL_REQUEST_ORDERID, 
	    		    Views.COL_REQUEST_ORDERID,
                    Views.COL_REQUEST_TYPE, 
	    		    Views.COL_REQUEST_STATUS, 
	    		    Views.COL_REQUEST_WAREHOUSE, 
	    		    Views.COL_REQUEST_ID
	    		);



	        if (itemPage != null && itemPage.isPaginationEnabled()) {
	            String countSql = String.format(
	                    "SELECT COUNT(*) FROM %s WHERE %s = ? AND (%s IS NULL OR %s = 0) AND %s IS NULL AND %s = 'Confirm' AND %s = ?",
	                    Views.TBL_REQUEST, 
	                    Views.COL_REQUEST_EMPLOYEE_ID, 
	                    Views.COL_REQUEST_ORDERID,
	                    Views.COL_REQUEST_ORDERID,
	                    Views.COL_REQUEST_TYPE, 
	                    Views.COL_REQUEST_STATUS, 
	                    Views.COL_REQUEST_WAREHOUSE);

	            int count = jdbcTemplate.queryForObject(countSql, Integer.class, employeeId, warehouseId);
	            int totalPage = (int) Math.ceil((double) count / itemPage.getPage_size());
	            itemPage.setTotal_page(totalPage);

	            return jdbcTemplate.query(sql + " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY",
	                    new Request_mapper(),
	                    employeeId,
	                    warehouseId,
	                    (itemPage.getPage_current() - 1) * itemPage.getPage_size(),
	                    itemPage.getPage_size());
	        } else {
	            return jdbcTemplate.query(sql, new Request_mapper(), employeeId, warehouseId);
	        }
	    } catch (DataAccessException e) {
	        System.err.println("Error fetching requests: " + e.getMessage());
	        return Collections.emptyList();
	    }
	}


	//hien thi bang request theo employee_id and Type =?
	public List<Request> findAllByEmployeeIdAndType(PageView itemPage, int employeeId) {
	    try {
	        String sql = String.format(
	                "SELECT * FROM %s WHERE %s = ? AND (%s = 'Request' OR %s = 'Order') AND %s = 'Processing' ORDER BY %s ASC",
	                Views.TBL_REQUEST, Views.COL_REQUEST_EMPLOYEE_ID, Views.COL_REQUEST_TYPE, Views.COL_REQUEST_TYPE, Views.COL_REQUEST_STATUS, Views.COL_REQUEST_ID);

	        if (itemPage != null && itemPage.isPaginationEnabled()) {
	            int count = jdbcTemplate.queryForObject(
	                    String.format("SELECT COUNT(*) FROM %s WHERE %s = ? AND (%s = 'Request' OR %s = 'Order') AND %s = 'Processing'",
	                            Views.TBL_REQUEST, Views.COL_REQUEST_EMPLOYEE_ID, Views.COL_REQUEST_TYPE, Views.COL_REQUEST_TYPE, Views.COL_REQUEST_STATUS),
	                    Integer.class, employeeId);
	            int totalPage = (int) Math.ceil((double) count / itemPage.getPage_size());
	            itemPage.setTotal_page(totalPage);

	            return jdbcTemplate.query(sql + " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY",
	                    new Request_mapper(),
	                    employeeId,
	                    (itemPage.getPage_current() - 1) * itemPage.getPage_size(),
	                    itemPage.getPage_size());
	        } else {
	            return jdbcTemplate.query(sql, new Request_mapper(), employeeId);
	        }
	    } catch (DataAccessException e) {
	        System.err.println("Error fetching requests: " + e.getMessage());
	        return Collections.emptyList();
	    }
	}


	
	//hien thi bang order theo employee_id
	public List<Order> findOrderByEmployeeId(PageView itemPage, int employeeId) {
	    try {
	        String sql = String.format("SELECT * FROM [%s] WHERE %s = ? AND %s = 'Confirm' ORDER BY %s ASC",
	                Views.TBL_ORDER, Views.COL_ORDER_EMPLOYEE, Views.COL_ORDER_STATUS, Views.COL_ORDER_ID);

	        if (itemPage != null && itemPage.isPaginationEnabled()) {

	            int count = jdbcTemplate.queryForObject(
	                    String.format("SELECT COUNT(*) FROM [%s] WHERE %s = ?", Views.TBL_ORDER, Views.COL_ORDER_EMPLOYEE),
	                    Integer.class, employeeId);
	            int totalPage = (int) Math.ceil((double) count / itemPage.getPage_size());
	            itemPage.setTotal_page(totalPage);


	            return jdbcTemplate.query(sql + " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY",
	                    new Order_mapper(),
	                    employeeId,
	                    (itemPage.getPage_current() - 1) * itemPage.getPage_size(),
	                    itemPage.getPage_size());
	        } else {
	            return jdbcTemplate.query(sql, new Order_mapper(), employeeId);
	        }
	    } catch (DataAccessException e) {
	        System.err.println("Error fetching requests: " + e.getMessage());
	        return Collections.emptyList();
	    }
	}

	//hien thi bang warehouse_releasenote theo employee_id
	public List<Warehouse_releasenote> findWareAllByEmployeeId(PageView itemPage, int employeeId) {
	    try {
	    	String sql = String.format ("SELECT * FROM %s WHERE %s = ? ORDER BY %s DESC",
					Views.TBL_WAREHOUSE_RELEASENOTE, Views.COL_WAREHOUSE_RELEASENOTE_EMPLOYEEID, Views.COL_WAREHOUSE_RELEASENOTE_ID);

	        if (itemPage != null && itemPage.isPaginationEnabled()) {

	            int count = jdbcTemplate.queryForObject(
	                    String.format("SELECT COUNT(*) FROM %s WHERE %s = ?", Views.TBL_WAREHOUSE_RELEASENOTE, Views.COL_WAREHOUSE_RELEASENOTE_EMPLOYEEID),
	                    Integer.class, employeeId);
	            int totalPage = (int) Math.ceil((double) count / itemPage.getPage_size());
	            itemPage.setTotal_page(totalPage);


	            return jdbcTemplate.query(sql + " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY",
	                    new Warehouse_releasenote_mapper(),
	                    employeeId,
	                    (itemPage.getPage_current() - 1) * itemPage.getPage_size(),
	                    itemPage.getPage_size());
	        } else {
	        	return jdbcTemplate.query(sql, new Warehouse_releasenote_mapper(), employeeId);
	        }
	    } catch (DataAccessException e) {
	        System.err.println("Error fetching requests: " + e.getMessage());
	        return Collections.emptyList();
	    }
				
		
	}
	
	// hien thi bang request khi employee_id = null
	public List<Request> findAllByEmployeeIdIsNull(PageView itemPage) {
	    try {
	    	String sql = String.format(
	    		    "SELECT * FROM %s WHERE (%s IS NULL OR %s = 0) AND %s IS NULL AND %s = 'Pending Approval' ORDER BY %s ASC",
	    		    Views.TBL_REQUEST, 
	    		    Views.COL_REQUEST_EMPLOYEE_ID, 
	    		    Views.COL_REQUEST_EMPLOYEE_ID, 
	    		    Views.COL_REQUEST_WAREHOUSE, 
	    		    Views.COL_REQUEST_STATUS,
	    		    Views.COL_REQUEST_ID
	    		);


	        if (itemPage != null && itemPage.isPaginationEnabled()) {
	            int count = jdbcTemplate.queryForObject(
	                String.format(
	                    "SELECT COUNT(*) FROM %s WHERE (%s IS NULL OR %s = 0) AND %s IS NULL AND %s = 'Pending Approval'",
	                    Views.TBL_REQUEST, 
	                    Views.COL_REQUEST_EMPLOYEE_ID,
	                    Views.COL_REQUEST_EMPLOYEE_ID,
	                    Views.COL_REQUEST_WAREHOUSE, 
	                    Views.COL_REQUEST_STATUS
	                ),
	                Integer.class
	            );
	            
	            int totalPage = (int) Math.ceil((double) count / itemPage.getPage_size());
	            itemPage.setTotal_page(totalPage);
	            return jdbcTemplate.query(
	                sql + " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY",
	                new Request_mapper(),
	                (itemPage.getPage_current() - 1) * itemPage.getPage_size(),
	                itemPage.getPage_size()
	            );
	        } else {
	            return jdbcTemplate.query(sql, new Request_mapper());
	        }
	    } catch (DataAccessException e) {
	        System.err.println("Error fetching requests: " + e.getMessage());
	        return Collections.emptyList();
	    }
	}


	
	// hien thi bang order khi employee_id = null
	public List<Order> findAllOrderByEmployeeIdIsNull(PageView itemPage, int warehouseId) {
	    try {
	    	String sql = String.format(
	    		    "SELECT * FROM [%s] WHERE (%s IS NULL OR %s = 0) AND %s = 'Waiting for confirmation' AND %s = ? ORDER BY %s ASC",
	    		    Views.TBL_ORDER, 
	    		    Views.COL_ORDER_EMPLOYEE, 
	    		    Views.COL_ORDER_EMPLOYEE, 
	    		    Views.COL_ORDER_STATUS, 
	    		    Views.COL_ORDER_WAREHOUSE_ID, 
	    		    Views.COL_ORDER_ID
	    		);

	        if (itemPage != null && itemPage.isPaginationEnabled()) {
	            String countSql = String.format(
	                    "SELECT COUNT(*) FROM [%s] WHERE %s IS NULL AND %s = 'Waiting for confirmation' AND %s = ?",
	                    Views.TBL_ORDER, Views.COL_ORDER_EMPLOYEE, Views.COL_ORDER_STATUS, Views.COL_ORDER_WAREHOUSE_ID);

	            int count = jdbcTemplate.queryForObject(countSql, Integer.class, warehouseId);
	            int totalPage = (int) Math.ceil((double) count / itemPage.getPage_size());
	            itemPage.setTotal_page(totalPage);

	            return jdbcTemplate.query(sql + " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY",
	                    new Order_mapper(),
	                    warehouseId,
	                    (itemPage.getPage_current() - 1) * itemPage.getPage_size(),
	                    itemPage.getPage_size());
	        } else {
	            return jdbcTemplate.query(sql, new Order_mapper(), warehouseId);
	        }
	    } catch (DataAccessException e) {
	        System.err.println("Error fetching orders: " + e.getMessage());
	        return Collections.emptyList();
	    }
	}


	//xoa employee_id bang request
	public void deleteEmployeeIdByRequestId(int id) {
	    String sql = "UPDATE Request SET Employee_Id = NULL, status = 'Pending Approval' WHERE Id = ?";
	    jdbcTemplate.update(sql, id);
	}
	
	//xoa employee_id bang order
	public void deleteEmployeeIdByOrderId(int id) {
	    String sql = "UPDATE [Order] SET Employee_Id = NULL, status = 'Waiting for confirmation' WHERE Id = ?";
	    jdbcTemplate.update(sql, id);
	}
	
	//hien thi bang order theo id
	public Order findOrderById(int id) {
        String sql = "SELECT * FROM [Order] WHERE Id = ?";
        List<Order> orders = jdbcTemplate.query(sql, (rs, rowNum) -> {
        	Order order = new Order();
        	order.setId(rs.getInt(Views.COL_ORDER_ID));
        	order.setCus_Name(rs.getString(Views.COL_ORDER_CUSNAME));
        	order.setDate(rs.getTimestamp(Views.COL_REQUEST_DATE).toLocalDateTime().toLocalDate());
        	order.setStatus(rs.getString(Views.COL_ORDER_STATUS));
        	order.setOrderID(rs.getString(Views.COL_ORDER_ORDERID));
            return order;
        }, id);
        
        return orders.stream().findFirst().orElse(null);
	}
	
	// hiển thị bảng Order_detail
	public List<Order_detail> findOrderDetail(int OrderId) {
	    String sql = "SELECT od.*, p.Product_name AS Product_name, u.Name AS Unit_name " +
	                 "FROM Order_detail od " +
	                 "JOIN Product p ON od.Product_Id = p.id " +
	                 "JOIN Unit u ON p.Unit_id = u.id " +
	                 "WHERE od.Order_Id = ?";
	    return jdbcTemplate.query(sql, (rs, rowNum) -> {
	    	Order_detail detail = new Order_detail();
	        detail.setOrder_id(rs.getInt(Views.COL_ORDER_DETAIL_ORDER_ID));
	        detail.setProduct_Id(rs.getInt(Views.COL_ORDER_DETAIL_PRODUCT_ID));
	        detail.setQuantity(rs.getInt(Views.COL_ORDER_DETAIL_QUANTITY));
	        detail.setStatus(rs.getString(Views.COL_ORDER_DETAIL_STATUS));
	        detail.setProduct_name(rs.getString(Views.COL_PRODUCT_NAME));
	        detail.setUnit_name(rs.getString("Unit_name"));
	        
	        return detail;
	    }, OrderId);
	}

	//hien thi bang warehouse_releate theo id
    public Warehouse_releasenote findWarehouseReleasenoteById(int id) {
        String sql = "SELECT * FROM Warehouse_releasenote WHERE Id = ?";
        List<Warehouse_releasenote> releasenotes = jdbcTemplate.query(sql, (rs, rowNum) -> {
        	Warehouse_releasenote releasenote = new Warehouse_releasenote();
        	releasenote.setId(rs.getInt(Views.COL_WAREHOUSE_RELEASENOTE_ID));
        	releasenote.setName(rs.getString(Views.COL_WAREHOUSE_RELEASENOTE_NAME));
        	releasenote.setDate(rs.getTimestamp(Views.COL_WAREHOUSE_RELEASENOTE_DATE).toLocalDateTime());
        	releasenote.setStatusWr(rs.getString(Views.COL_WAREHOUSE_RELEASENOTE_STATUS));
            return releasenote;
        }, id);
        
        return releasenotes.stream().findFirst().orElse(null);
    }
    
	// hiển thị bảng Warehouse_rn_detail

	public List<Warehouse_rn_detail> findWarehouseRnDetail(int wgrnId) {
	    String sql = "SELECT wd.*, p.Product_name AS Product_name, u.Name As Unit_name " +
	                 "FROM Warehouse_rn_detail wd " +
	                 "JOIN Product p ON wd.id_product = p.id " +
	                 "JOIN Unit u ON p.Unit_id = u.id " +
	                 "WHERE wd.Wgrn_Id = ?";
	    return jdbcTemplate.query(sql, (rs, rowNum) -> {
	    	Warehouse_rn_detail detail = new Warehouse_rn_detail();
	        detail.setWgrn_id(rs.getInt(Views.COL_WAREHOUSE_RNOTE_ID));
	        detail.setId_product(rs.getInt(Views.COL_WAREHOUSE_RN_DETAIL_PRODUCTID));
	        detail.setQuantity(rs.getInt(Views.COL_WAREHOUSE_RN_DETAIL_QUANTITY));
	        detail.setStatus(rs.getString(Views.COL_WAREHOUSE_RN_DETAIL_STATUS));
	        detail.setProductName(rs.getString(Views.COL_PRODUCT_NAME));  
	        detail.setUnit_name(rs.getString("Unit_name"));
	        return detail;
	    }, wgrnId);
	}
	

	public List<Request_detail> findDetailsByRequestId(int requesId) {
	    String sql = "SELECT rd.*,rd.Id AS RqDetailId, p.Product_name AS Product_name, u.Name As Unit_name " +
	                 "FROM Request_detail rd " +
	                 "JOIN Product p ON rd.id_product = p.id " +
	                 "JOIN Unit u ON p.Unit_id = u.id " +
	                 "WHERE rd.Request_Id = ?";
	    return jdbcTemplate.query(sql, (rs, rowNum) -> {
	        Request_detail detail = new Request_detail();
	        detail.setRequest_id(rs.getInt(Views.COL_REQUEST_DETAIL_ID));
	        detail.setId_product(rs.getInt(Views.COL_REQUEST_DETAIL_ID_PRODUCT));
	        detail.setQuantity_requested(rs.getInt(Views.COL_REQUEST_DETAIL_QUANTITY_REQUESTED));
	        detail.setQuantity_exported(rs.getInt(Views.COL_REQUEST_DETAIL_QUANTITY_EXPORTED));
	        detail.setStatus(rs.getString(Views.COL_REQUEST_DETAIL_STATUS));
	        detail.setProductName(rs.getString(Views.COL_PRODUCT_NAME)); 
	        detail.setUnit_name(rs.getString("Unit_name"));
	        detail.setId(rs.getInt("RqDetailId"));
	        return detail;
	    }, requesId);
	}

	// hiển thị request theo employee và id
	public Request findRequestByIdEmp(int id, int employeeId) {
	    String sql = "SELECT * FROM Request WHERE Id = ? AND Employee_Id = ?";

	    List<Request> requests = jdbcTemplate.query(sql, (rs, rowNum) -> {
	        Request request = new Request();
	        request.setId(rs.getInt(Views.COL_REQUEST_ID));
	        request.setEmployee_Id(rs.getInt(Views.COL_REQUEST_EMPLOYEE_ID));
	        request.setName(rs.getString(Views.COL_REQUEST_NAME));
	        request.setDate(rs.getTimestamp(Views.COL_REQUEST_DATE).toLocalDateTime());
	        request.setStatusRequest(rs.getString(Views.COL_REQUEST_STATUS));
	        return request;
	    }, id, employeeId);


	    if (requests.isEmpty()) {
	        return null;
	    }

	    return requests.get(0);  
	}
	// hiển thị order theo employee và id
	public Order findOrderByIdEmp(int id, int employeeId) {
	    String sql = "SELECT * FROM [Order] WHERE Id = ? AND Employee_Id = ?";

	    List<Order> orders = jdbcTemplate.query(sql, (rs, rowNum) -> {
	    	Order order = new Order();
	    	order.setId(rs.getInt(Views.COL_ORDER_ID));
	    	order.setEmployee_id(rs.getInt(Views.COL_ORDER_EMPLOYEE));
	    	order.setCus_Name(rs.getString(Views.COL_ORDER_CUSNAME));
	    	order.setDate(rs.getTimestamp(Views.COL_REQUEST_DATE).toLocalDateTime().toLocalDate());
	    	order.setStatus(rs.getString(Views.COL_ORDER_STATUS));
	    	order.setOrderID(rs.getString(Views.COL_ORDER_ORDERID));
	        return order;
	    }, id, employeeId);


	    if (orders.isEmpty()) {
	        return null;
	    }

	    return orders.get(0);  
	}


	//add warehouse_releasenote theo RequestId
	@Transactional
	public boolean addWarehouseReleasenote(Warehouse_releasenote releasenote, List<Warehouse_rn_detail> details, int warehouseId) {
	   
		try {
	    	String sqlCheckStock = """
	                SELECT 
					    SUM(st.Quantity) AS TotalStock
					FROM 
					        stock st
					    JOIN Product p ON st.Id_product = p.Id
						JOIN Unit u ON p.Unit_id = u.Id
					    JOIN Warehouse_receipt_detail wrd ON st.Wh_rc_dt_Id = wrd.Id
					    JOIN Warehouse_receipt whr ON wrd.Wh_receiptId = whr.Id
					    JOIN Warehouse wh ON whr.Wh_Id = wh.Id
					    JOIN employee_warehouse ew ON wh.Id = ew.Warehouse_Id
					WHERE 
					    st.Id_product = ?
					    AND ew.Warehouse_Id = ?
	            """;
	    	
	    	Map<Integer, Integer> productQuantities = new HashMap<>();

	    	for (Warehouse_rn_detail detail : details) {
	    	    productQuantities.merge(detail.getId_product(), detail.getQuantity(), Integer::sum);
	    	    
	    	}


	    	for (Map.Entry<Integer, Integer> entry : productQuantities.entrySet()) {
	    	    Integer productId = entry.getKey();
	    	    Integer requiredQuantity = entry.getValue();

	    	    Integer totalStock = jdbcTemplate.queryForObject(sqlCheckStock, Integer.class, productId, warehouseId);
	    	    if (totalStock == null || totalStock < requiredQuantity) {
	    	        return false;
	    	    }
	    	}

	        
	        String sql1 = "INSERT INTO Warehouse_releasenote (Name, Date, Status, Request_Id, Employee_Id) VALUES (?, ?, ?, ?, ?)";
	        KeyHolder keyHolder = new GeneratedKeyHolder();
	        int result1 = jdbcTemplate.update(connection -> {
	            var ps = connection.prepareStatement(sql1, new String[] { "Id" });
	            ps.setString(1, releasenote.getName());
	            ps.setDate(2, java.sql.Date.valueOf(releasenote.getDate().toLocalDate()));
	            ps.setString(3, releasenote.getStatusWr());
	            ps.setInt(4, releasenote.getRequest_id());
	            ps.setInt(5, releasenote.getEmployee_Id());
	            return ps;
	        }, keyHolder);

	        int generatedId = keyHolder.getKey().intValue();
	        releasenote.setId(generatedId);
	        


	        String sql2 = "INSERT INTO Warehouse_rn_detail (Wgrn_Id, Status, Id_product, Quantity) VALUES (?, ?, ?, ?)";
	        for (Warehouse_rn_detail detail : details) {
	            detail.setWgrn_id(generatedId);
	            jdbcTemplate.update(sql2,
	                detail.getWgrn_id(),
	                detail.getStatus(),
	                detail.getId_product(),
	                detail.getQuantity()
	            );

	            int remainingQuantity = detail.getQuantity();

	            String sqlGetStocks = """
	                    SELECT 
	                        st.Id AS Id,
	                        st.Id_product,
	                        st.Quantity,
	                        ew.Warehouse_Id as warehouseId
	                    FROM 
	                        stock st
	                        JOIN Product p ON st.Id_product = p.Id
	                        JOIN Unit u ON p.Unit_id = u.Id
	                        JOIN Warehouse_receipt_detail wrd ON st.Wh_rc_dt_Id = wrd.Id
	                        JOIN Warehouse_receipt whr ON wrd.Wh_receiptId = whr.Id
	                        JOIN Warehouse wh ON whr.Wh_Id = wh.Id
	                        JOIN employee_warehouse ew ON wh.Id = ew.Warehouse_Id
	                    WHERE  
	                        st.Id_product = ?
	                        AND ew.Warehouse_Id = ?
	                    ORDER BY st.Quantity ASC
	                """;
	            List<Map<String, Object>> stockList = jdbcTemplate.queryForList(sqlGetStocks, detail.getId_product(), warehouseId);

	            for (Map<String, Object> stock : stockList) {
	                Integer stockQuantity = (Integer) stock.get("Quantity");
	                Integer stockId = (Integer) stock.get("Id");

	                if (stockQuantity == null || stockId == null || stockQuantity <= 0) {
	                    continue; 
	                }

	                int quantityToDeduct = Math.min(stockQuantity, remainingQuantity);
	                String sqlUpdateStock = """
						UPDATE st
						SET st.Quantity = st.Quantity - ?
					FROM 
					        stock st
					    JOIN Product p ON st.Id_product = p.Id
						JOIN Unit u ON p.Unit_id = u.Id
					    JOIN Warehouse_receipt_detail wrd ON st.Wh_rc_dt_Id = wrd.Id
					    JOIN Warehouse_receipt whr ON wrd.Wh_receiptId = whr.Id
					    JOIN Warehouse wh ON whr.Wh_Id = wh.Id
					    JOIN employee_warehouse ew ON wh.Id = ew.Warehouse_Id
					WHERE st.Id = ?	
					    AND st.Id_product = ?
					    AND ew.Warehouse_Id = ?
	                    """;
	                jdbcTemplate.update(sqlUpdateStock, quantityToDeduct, stockId, detail.getId_product(), warehouseId);

	                String sqlUpdateDetailStockId = """
	                        UPDATE Warehouse_rn_detail
	                        SET Stock_Id = ?
	                        WHERE Wgrn_Id = ? AND Id_product = ?
	                    """;
	                jdbcTemplate.update(sqlUpdateDetailStockId, stockId, generatedId, detail.getId_product());

	                remainingQuantity -= quantityToDeduct;

	                if (remainingQuantity == 0) {                    
	                    break; 
	                }
	            }

	            
	            if (remainingQuantity > 0) {
	                return false; 
	            }
	        }

	        return result1 > 0; 
	    } catch (DataAccessException e) {
	        e.printStackTrace();
	        return false; 
	    }
	}
	
    
	//add warehouse_releasenote with OrderId

    @Transactional
    public boolean addWarehouseReleasenoteByOrder(Warehouse_releasenote releasenote, List<Warehouse_rn_detail> details, int warehouseId) {
        try {
        	String sqlCheckStock = """
	                SELECT 
					    SUM(st.Quantity) AS TotalStock
					FROM 
					        stock st
					    JOIN Product p ON st.Id_product = p.Id
						JOIN Unit u ON p.Unit_id = u.Id
					    JOIN Warehouse_receipt_detail wrd ON st.Wh_rc_dt_Id = wrd.Id
					    JOIN Warehouse_receipt whr ON wrd.Wh_receiptId = whr.Id
					    JOIN Warehouse wh ON whr.Wh_Id = wh.Id
					    JOIN employee_warehouse ew ON wh.Id = ew.Warehouse_Id
					WHERE 
					    st.Id_product = ?
					    AND ew.Warehouse_Id = ?
	            """;
	    	
	    	Map<Integer, Integer> productQuantities = new HashMap<>();

	    	for (Warehouse_rn_detail detail : details) {
	    	    productQuantities.merge(detail.getId_product(), detail.getQuantity(), Integer::sum);
	    	    
	    	}


	    	for (Map.Entry<Integer, Integer> entry : productQuantities.entrySet()) {
	    	    Integer productId = entry.getKey();
	    	    Integer requiredQuantity = entry.getValue();

	    	    Integer totalStock = jdbcTemplate.queryForObject(sqlCheckStock, Integer.class, productId, warehouseId);
	    	    if (totalStock == null || totalStock < requiredQuantity) {
	    	        return false;
	    	    }
	    	}
        	       	
            String sql1 = "INSERT INTO Warehouse_releasenote (Name, Date, Status, Order_Id, Employee_Id) VALUES (?, ?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            int result1 = jdbcTemplate.update(connection -> {
                var ps = connection.prepareStatement(sql1, new String[] { "Id" });
                ps.setString(1, releasenote.getName());
                ps.setDate(2, java.sql.Date.valueOf(releasenote.getDate().toLocalDate()));
                ps.setString(3, releasenote.getStatusWr());
                ps.setInt(4, releasenote.getOrder_id());
                ps.setInt(5, releasenote.getEmployee_Id());
                return ps;
            }, keyHolder);

            int generatedId = keyHolder.getKey().intValue();
            
            releasenote.setId(generatedId);

            String sql2 = "INSERT INTO Warehouse_rn_detail (Wgrn_Id, Status, Id_product, Quantity) VALUES ( ?, ?, ?, ?)";
            for (Warehouse_rn_detail detail : details) {
                detail.setWgrn_id(generatedId);
                jdbcTemplate.update(sql2,
                    detail.getWgrn_id(),
                    detail.getStatus(),
                    detail.getId_product(),
                    detail.getQuantity()
                );
                
                
				
                int remainingQuantity = detail.getQuantity();

	            String sqlGetStocks = """
	                    SELECT 
	                        st.Id AS Id,
	                        st.Id_product,
	                        st.Quantity,
	                        ew.Warehouse_Id as warehouseId
	                    FROM 
	                        stock st
	                        JOIN Product p ON st.Id_product = p.Id
	                        JOIN Unit u ON p.Unit_id = u.Id
	                        JOIN Warehouse_receipt_detail wrd ON st.Wh_rc_dt_Id = wrd.Id
	                        JOIN Warehouse_receipt whr ON wrd.Wh_receiptId = whr.Id
	                        JOIN Warehouse wh ON whr.Wh_Id = wh.Id
	                        JOIN employee_warehouse ew ON wh.Id = ew.Warehouse_Id
	                    WHERE  
	                        st.Id_product = ?
	                        AND ew.Warehouse_Id = ?
	                    ORDER BY st.Quantity ASC
	                """;
	            
	            List<Map<String, Object>> stockList = jdbcTemplate.queryForList(sqlGetStocks, detail.getId_product(), warehouseId);

	            for (Map<String, Object> stock : stockList) {
	                Integer stockQuantity = (Integer) stock.get("Quantity");
	                Integer stockId = (Integer) stock.get("Id");

	                if (stockQuantity == null || stockId == null || stockQuantity <= 0) {
	                    continue; 
	                }

	                int quantityToDeduct = Math.min(stockQuantity, remainingQuantity);
	                String sqlUpdateStock = """
						UPDATE st
						SET st.Quantity = st.Quantity - ?
					FROM 
					        stock st
					    JOIN Product p ON st.Id_product = p.Id
						JOIN Unit u ON p.Unit_id = u.Id
					    JOIN Warehouse_receipt_detail wrd ON st.Wh_rc_dt_Id = wrd.Id
					    JOIN Warehouse_receipt whr ON wrd.Wh_receiptId = whr.Id
					    JOIN Warehouse wh ON whr.Wh_Id = wh.Id
					    JOIN employee_warehouse ew ON wh.Id = ew.Warehouse_Id
					WHERE st.Id = ?	
					    AND st.Id_product = ?
					    AND ew.Warehouse_Id = ?
	                    """;
	                jdbcTemplate.update(sqlUpdateStock, quantityToDeduct, stockId, detail.getId_product(), warehouseId);

	                String sqlUpdateDetailStockId = """
	                        UPDATE Warehouse_rn_detail
	                        SET Stock_Id = ?
	                        WHERE Wgrn_Id = ? AND Id_product = ?
	                    """;
	                jdbcTemplate.update(sqlUpdateDetailStockId, stockId, generatedId, detail.getId_product());

	                remainingQuantity -= quantityToDeduct;

	                if (remainingQuantity == 0) {                    
	                    break; 
	                }
	            }

	            
	            if (remainingQuantity > 0) {
	                return false; 
	            }
				 
            }
            
            return result1 > 0;  
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    //hiển thị bảng request theo id
    public Request findRequestById(int requestId) {
        String sql = "SELECT * FROM " + Views.TBL_REQUEST + " WHERE " + Views.COL_REQUEST_ID + " = ?";
        
        return jdbcTemplate.queryForObject(sql, new Request_mapper(), requestId);
    }

        
	// số lượng warehouse_rn_detail
	  public int QuantityByReleaseNoteId(int warehouseReleaseNoteId) { 
		  String sql = "SELECT SUM(wd." + Views.COL_WAREHOUSE_RN_DETAIL_QUANTITY +
		  ") AS totalQuantity " + "FROM " + Views.TBL_WAREHOUSE_RN_DETAIL + " wd " +
		  "JOIN " + Views.TBL_WAREHOUSE_RELEASENOTE + " wr ON wd." +
		  Views.COL_WAREHOUSE_RNOTE_ID + " = wr." + Views.COL_WAREHOUSE_RELEASENOTE_ID
		  + " " + "WHERE wr." + Views.COL_WAREHOUSE_RELEASENOTE_ID + " = ?";
		  
		  Integer totalQuantity = jdbcTemplate.queryForObject(sql, Integer.class,
		  warehouseReleaseNoteId);
		  
		  return (totalQuantity != null) ? totalQuantity : 0; 
	  
	  }
	  	  
		// số lượng order_detail
	  public int QuantityOrder(int orderId) { 
		  String sql = "SELECT SUM(" +
		  Views.COL_ORDER_DETAIL_QUANTITY + ") AS totalQuantity " + "FROM "
		  + Views.TBL_ORDER_DETAIL + " " + "WHERE " +
		  Views.COL_ORDER_DETAIL_ORDER_ID + " = ?";
		  
		  Integer totalQuantity = jdbcTemplate.queryForObject(sql, Integer.class,
				  orderId);
		  
		  return (totalQuantity != null) ? totalQuantity : 0; 
	  }
	  
	// số lượng request_detail QuantityRequested
	  public int QuantityRequested(int requestId) { 
		  String sql = "SELECT SUM(" +
		  Views.COL_REQUEST_DETAIL_QUANTITY_REQUESTED + ") AS totalQuantity " + "FROM "
		  + Views.TBL_REQUEST_DETAIL + " " + "WHERE " +
		  Views.COL_REQUEST_DETAIL_REQUEST_ID + " = ?";
		  
		  Integer totalQuantity = jdbcTemplate.queryForObject(sql, Integer.class,
		  requestId);
		  
		  return (totalQuantity != null) ? totalQuantity : 0; 
	  }
	  
		// số lượng request_detail QuantityExported
	  public int QuantityExported(int requestId) { 
		  String sql = "SELECT SUM(" +
		  Views.COL_REQUEST_DETAIL_QUANTITY_EXPORTED + ") AS totalQuantity " + "FROM "
		  + Views.TBL_REQUEST_DETAIL + " " + "WHERE " +
		  Views.COL_REQUEST_DETAIL_REQUEST_ID + " = ?";
		  
		  Integer totalQuantity = jdbcTemplate.queryForObject(sql, Integer.class,
		  requestId);
		  
		  return (totalQuantity != null) ? totalQuantity : 0; 
	  }
	  
	  public int findOrderIdByRequest(int requestId) {
		    String sql = "SELECT Order_Id FROM Request WHERE Id = ?";

		    return jdbcTemplate.queryForObject(sql, Integer.class, requestId);
		}

	  public boolean isRequestComplete(int requestId, int orderId) {
		    int totalQuantityExported = QuantityExported(requestId);
		    int totalRequestedQuantity = QuantityRequested(requestId);
		    
		    if (totalQuantityExported >= totalRequestedQuantity) {
		        String sql = "UPDATE Request SET Status = 'waiting for shipping' WHERE Id = ?";
		        String sql1 = "UPDATE [Order] SET Status = 'waiting for shipping' WHERE Id = ?";
		        jdbcTemplate.update(sql, requestId);		      
		        jdbcTemplate.update(sql1, orderId);
		        return true;
		    } else { 
		    	String typeCheckSql = "SELECT Type FROM Request WHERE Id = ?";
		        String currentType = jdbcTemplate.queryForObject(typeCheckSql, String.class, requestId);
		        
		        if (!"Order".equals(currentType)) {
		            String sql = "UPDATE Request SET Type = 'Request', Status = 'Processing' WHERE Id = ?";
		            jdbcTemplate.update(sql, requestId);
		        } else {
		            String sql = "UPDATE Request SET Status = 'Processing' WHERE Id = ?";
		            jdbcTemplate.update(sql, requestId);
		        }	        
		    }
		    return false;
		}
	  
	  // so sánh quantity vs Order Id đổi status

	  public boolean isOrderComplete(int ReleaseDetailId, int orderId) {
		    int totalReleasedQuantity = QuantityByReleaseNoteId(ReleaseDetailId);
		    int totalOrderQuantity = QuantityOrder(orderId);
		    

		    if(totalReleasedQuantity >= totalOrderQuantity) {

		    	String sql = "UPDATE [Order] SET Status = 'waiting for shipping' WHERE Id = ?";
		    	jdbcTemplate.update(sql, orderId);
		    	return true;
		    } else {
		    	String sql = "UPDATE [Order] SET Status = 'Processing' WHERE Id = ?";
		    	jdbcTemplate.update(sql, orderId);
		    }
		    return false;
		}
	  
	  //update status order_detail.
	  public boolean isRleComplete(int releaseDetailId, int orderId) {
		    int totalReleasedQuantity = QuantityByReleaseNoteId(releaseDetailId);
		    int totalOrderQuantity = QuantityOrder(orderId);
		    
		    String status = (totalReleasedQuantity >= totalOrderQuantity) ? "Completed" : "Processing";

		    String sql = "UPDATE Order_detail SET Status = ? WHERE Order_Id = ?";
		    jdbcTemplate.update(sql, status, orderId);

		    return totalReleasedQuantity >= totalOrderQuantity;
		}

	  

	  
	
	  public int getQuantityRequested(int id, int requestId, int idProduct) {

		    String sql = "SELECT " + Views.COL_REQUEST_DETAIL_QUANTITY_REQUESTED + " " +
		                 "FROM " + Views.TBL_REQUEST_DETAIL + " " +
		                 "WHERE " + Views.COL_REQUEST_DETAIL_ID + " = ? AND " +
		                  Views.COL_REQUEST_DETAIL_REQUEST_ID + " = ? " +
		                 "AND " + Views.COL_REQUEST_DETAIL_ID_PRODUCT + " = ?";
		    
		    Integer quantityRequested = jdbcTemplate.queryForObject(sql, Integer.class, id, requestId, idProduct);
		    

		    return (quantityRequested != null) ? quantityRequested : 0;

		}
	  //total quantity_exported
	  public int getQuantityExported(int id, int requestId, int idProduct) {
		  String checkSql = "SELECT Quantity_exported FROM Request_detail WHERE id = ? AND Request_Id = ? AND Id_product = ?";

		    Integer quantityExported = jdbcTemplate.queryForObject(checkSql, Integer.class,id, requestId, idProduct);

		    return (quantityExported != null) ? quantityExported : 0;
		}


	 //update_request_detail
	 public void updateQuantityExportedz(int id, int productId, int requestId, int quantityToAdd) {
		    String sql = "UPDATE Request_detail " +
		                 "SET Quantity_exported = Quantity_exported + ? " +
		                 "WHERE id = ? AND Id_product = ? AND Request_Id = ?";
		    jdbcTemplate.update(sql, quantityToAdd, id, productId, requestId);
		}
	  //update status request_detail
	  public int updateStatusRequestDetail(int id, int requestId, int idProduct ) {
		    int totalQuantityRequested = getQuantityRequested(id, requestId, idProduct);
		    int totalQuantityExported = getQuantityExported(id, requestId, idProduct);

		    if (totalQuantityExported >= totalQuantityRequested) {
		    	String updateStatusSql = "UPDATE " + Views.TBL_REQUEST_DETAIL + " SET " +
                        Views.COL_REQUEST_DETAIL_STATUS + " = 'Completed' " + 
                        "WHERE " + Views.COL_REQUEST_DETAIL_ID + " =? AND " +
                        Views.COL_REQUEST_DETAIL_REQUEST_ID + " = ? AND " +
                        Views.COL_REQUEST_DETAIL_ID_PRODUCT + " = ?";
		    			jdbcTemplate.update(updateStatusSql, id, requestId, idProduct);
		    }

		    return totalQuantityRequested;
		}

}
