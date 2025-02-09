package com.businessManager.repository;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import com.mapper.Request_mapper;
import com.models.Product;
import com.models.Request;
import com.models.Request_detail;
import com.utils.Views;

@Repository
public class RequestOrderRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    
    @Transactional
    public boolean addRequestOrderWithDetails(Request request, List<Request_detail> details) {
        try {
            String sql1 = "INSERT INTO Request (Name, Date, Type, Status, Employee_Id, Order_Id) VALUES (?, ?, ?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            int result1 = jdbcTemplate.update(connection -> {
                var ps = connection.prepareStatement(sql1, new String[] { "Id" });
                ps.setString(1, request.getName());                
                ps.setDate(2, java.sql.Date.valueOf(request.getDate().toLocalDate()));
                ps.setString(3, request.getType());
                ps.setString(4, request.getStatusRequest());
                ps.setInt(5, request.getEmployee_Id());
                ps.setInt(6, request.getOrder_id());
                return ps;
            }, keyHolder);

            int generatedId = keyHolder.getKey().intValue();
            request.setId(generatedId);

            String sql2 = "INSERT INTO Request_detail (Request_Id, Status, Id_product, Quantity_requested, Quantity_exported) VALUES ( ?, ?, ?, ?, ?)";
            for (Request_detail detail : details) {
                detail.setRequest_id(generatedId);
                jdbcTemplate.update(sql2,
                    detail.getRequest_id(),
                    detail.getStatus(),
                    detail.getId_product(),
                    detail.getQuantity_requested(),
                    detail.getQuantity_exported()
                );
            }

            return result1 > 0;  
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    
    public void deleteOrderRequest(int releasenoteId) {    	
        String deleteOrderDetailsSql = "DELETE FROM Request_detail WHERE Request_Id = ?";
        jdbcTemplate.update(deleteOrderDetailsSql, releasenoteId);
        String deleteWarehouseReleaseNoteSql = "DELETE FROM Request WHERE id = ?";
        jdbcTemplate.update(deleteWarehouseReleaseNoteSql, releasenoteId);
    }
    
    public List<Request> findAllRequest() {
        String sql = "SELECT * FROM Request";
 
        return jdbcTemplate.query(sql, new Request_mapper());
    }




    public Request findRequestById(int id) {
        String sql = "SELECT * FROM Request WHERE Id = ?";
        List<Request> requests = jdbcTemplate.query(sql, (rs, rowNum) -> {
        	Request request = new Request();
        	request.setId(rs.getInt(Views.COL_REQUEST_ID));
        	request.setName(rs.getString(Views.COL_REQUEST_NAME));
        	request.setDate(rs.getTimestamp(Views.COL_REQUEST_DATE).toLocalDateTime());
        	request.setStatusRequest(rs.getString(Views.COL_REQUEST_STATUS));
            return request;
        }, id);
        
        return requests.stream().findFirst().orElse(null);
    }

	public List<Request_detail> findDetailsByRequestId(int wgrnId) {
	    String sql = "SELECT rd.*, p.Product_name AS Product_name " +
	                 "FROM Request_detail rd " +
	                 "JOIN Product p ON rd.id_product = p.id " +
	                 "WHERE rd.Request_Id = ?";
	    return jdbcTemplate.query(sql, (rs, rowNum) -> {
	        Request_detail detail = new Request_detail();
	        detail.setRequest_id(rs.getInt(Views.COL_REQUEST_DETAIL_ID));
	        detail.setId_product(rs.getInt(Views.COL_REQUEST_DETAIL_ID_PRODUCT));
	        detail.setQuantity_requested(rs.getInt(Views.COL_REQUEST_DETAIL_QUANTITY_REQUESTED));
	        detail.setStatus(rs.getString(Views.COL_REQUEST_DETAIL_STATUS));
	        detail.setProductName(rs.getString("Product_name")); 
	        return detail;
	    }, wgrnId);
	}


    public List<Request> searchReleaseNotes(String query) {
        String sql = "SELECT * FROM Request WHERE name LIKE ?";
        return jdbcTemplate.query(sql, new Request_mapper(), "%" + query + "%");
    }


    public List<Product> findAllProduct() {
        String sql = "SELECT Id, Product_name FROM Product"; 
        try {
            List<Product> products = jdbcTemplate.query(sql, (rs, rowNum) -> {
                Product item = new Product();
                item.setId(rs.getInt(Views.COL_PRODUCT_ID));
                item.setProduct_name(rs.getString(Views.COL_PRODUCT_NAME));
                return item;
            });
            

            if (products.isEmpty()) {
                throw new RuntimeException("No products found in the database.");
            }
            
            return products;
        } catch (DataAccessException e) {

            System.err.println("Database query error: " + e.getMessage());
            throw new RuntimeException("Error fetching products from the database.", e);
        }
    }
    
}
