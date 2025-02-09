package com.businessManager.repository;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.models.PageView;
import com.models.Warehouse_rn_detail;
import com.utils.Views;
import com.models.Customer;
import com.models.Order;
import com.models.Order_detail;
import com.mapper.Customer_mapper;
import com.mapper.Order_mapper;
@Repository
public class CustomerRepository {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<Customer> findAllCustomers(PageView itemPage) {
	    try {
	        String sql = String.format(
	            "SELECT * FROM %s ORDER BY %s ASC",
	            Views.TBL_CUSTOMER,
	            Views.COL_CUSTOMER_ID
	        );

	        if (itemPage != null && itemPage.isPaginationEnabled()) {
	            int count = jdbcTemplate.queryForObject(
	                String.format("SELECT COUNT(*) FROM %s", Views.TBL_CUSTOMER),
	                Integer.class
	            );
	            
	            int totalPage = (int) Math.ceil((double) count / itemPage.getPage_size());
	            itemPage.setTotal_page(totalPage);

	            return jdbcTemplate.query(
	                sql + " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY",
	                new Customer_mapper(),
	                (itemPage.getPage_current() - 1) * itemPage.getPage_size(),
	                itemPage.getPage_size()
	            );
	        } else {
	            return jdbcTemplate.query(sql, new Customer_mapper());
	        }
	    } catch (DataAccessException e) {
	        System.err.println("Error fetching customers: " + e.getMessage());
	        return Collections.emptyList();
	    }
	}

	public List<Order_detail> findCusOrderdetail(int orderid) {
	    String sql = """
	        SELECT 	 
	            p.Product_name AS ProductName, 
	            p.Img AS Img, 
	            od.Quantity AS quantity, 	            	           	           	            	           	            
	            od.Price AS price
	        FROM Order_detail od	        
	        JOIN [Order] o ON o.Id = od.Order_Id
	        JOIN Product p ON od.Product_Id = p.Id
	        WHERE o.Id = ?
	    """;
	    return jdbcTemplate.query(sql, (rs, rowNum) -> {
	        Order_detail orderdetail = new Order_detail();	        
	        orderdetail.setProduct_name(rs.getString("ProductName"));
	        orderdetail.setPrice(rs.getDouble("price"));
	        orderdetail.setQuantity(rs.getInt("quantity"));
	        orderdetail.setImg(rs.getString("Img"));
	        return orderdetail;
	    }, orderid);
	}

	public List<Order> findCusOrder(int id) {
	    String sql = """
	        SELECT 
	    		o.Id AS id,       
			    o.OrderID AS idorder,
			    o.Cus_Name AS cusName, 
			    o.totalAmount AS totalamount,
			    o.Status AS statuss, 
			    o.Phone AS phone, 
			    o.Address AS address, 
			    o.Pay_status AS StatusPay, 
			    o.Date AS date, 
			    o.shippingFee AS feeshipping
			FROM [Order] o
			JOIN Customer c ON o.Customer_Id = c.Id
			WHERE c.Id = ?
	    """;
	    return jdbcTemplate.query(sql, (rs, rowNum) -> {
	        Order order = new Order();	
	        order.setId(rs.getInt("id"));
	        order.setOrderID(rs.getString("idorder"));
	        order.setCus_Name(rs.getString("cusName"));
	        order.setTotalAmount(rs.getDouble("totalamount"));
	        order.setStatus(rs.getString("statuss"));
	        order.setPhone(rs.getString("phone"));
	        order.setAddress(rs.getString("address"));
	        order.setPay_status(rs.getString("StatusPay"));
	        java.sql.Date sqlDate = rs.getDate("date");
	        if (sqlDate != null) {
	            order.setDate(sqlDate.toLocalDate()); 
	        }
	        order.setShippingFee(rs.getDouble("feeshipping"));	        
	        return order;
	    }, id);
	}
}
