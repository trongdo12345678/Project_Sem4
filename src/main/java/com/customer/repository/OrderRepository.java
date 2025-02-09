package com.customer.repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mapper.Order_mapper;
import com.models.ChatMessage;
import com.models.ChatRoom;
import com.models.Order;
import com.models.Order_detail;
import com.models.PageView;
import com.models.ReturnOrder;
import com.models.ReturnOrderDetail;
import com.utils.Views;

@Repository
public class OrderRepository {
	@Autowired
	JdbcTemplate db;
	
	@SuppressWarnings("deprecation")
	public List<Order> getOrdersByCustomerId(PageView pv, int customerId, String phone, LocalDate from, LocalDate to) {
		StringBuilder queryBuilder = new StringBuilder(
				String.format("SELECT * FROM [%s] WHERE [%s] = ?", Views.TBL_ORDER, Views.COL_ORDER_CUSTOMER_ID));

		List<Object> params = new ArrayList<>();
		params.add(customerId);

		// Filter by phone number if provided
		if (phone != null && !phone.isEmpty()) {
			queryBuilder.append(String.format(" AND [%s] = ?", Views.COL_ORDER_PHONE));
			params.add(phone);
		}

		// Filter by date range if provided
		if (from != null) {
			queryBuilder.append(String.format(" AND [%s] >= ?", Views.COL_ORDER_DATE));
			params.add(Date.valueOf(from));
		}
		if (to != null) {
			queryBuilder.append(String.format(" AND [%s] <= ?", Views.COL_ORDER_DATE));
			params.add(Date.valueOf(to));
		}

		// Count query for pagination
		String countQuery = "SELECT COUNT(*) FROM (" + queryBuilder.toString() + ") AS countQuery";

		// Execute count query to calculate the total number of pages
		int count = db.queryForObject(countQuery, Integer.class, params.toArray());
		int total_page = (int) Math.ceil((double) count / pv.getPage_size());
		pv.setTotal_page(total_page);

		// Handle pagination
		if (pv.isPaginationEnabled()) {
			queryBuilder.append(
					String.format(" ORDER BY [%s] DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY", Views.COL_ORDER_ID));
			params.add((pv.getPage_current() - 1) * pv.getPage_size());
			params.add(pv.getPage_size());
		}

		try {

			return db.query(queryBuilder.toString(), params.toArray(), new Order_mapper());
		} catch (DataAccessException e) {
			System.err.println("Error fetching orders for customer ID " + customerId + ": " + e.getMessage());
			return new ArrayList<>(); // Return an empty list in case of an error
		}
	}

	@SuppressWarnings("deprecation")
	public Order getOrderById(int orderid) {
		StringBuilder queryBuilder = new StringBuilder(
				String.format("SELECT * FROM [%s] WHERE [%s] = ?", Views.TBL_ORDER, Views.COL_ORDER_ID));

		try {
			// Execute the query, passing the order ID as a parameter
			return db.queryForObject(queryBuilder.toString(), new Object[] { orderid }, new Order_mapper());
		} catch (DataAccessException e) {
			System.err.println("Error fetching order for Order ID " + orderid + ": " + e.getMessage());
			return null; // Return null in case of an error
		}
	}

	@SuppressWarnings("deprecation")
	public boolean updateOrderStatusToCanceled(int orderid, String status) {
		StringBuilder queryBuilder = new StringBuilder(String.format("UPDATE [%s] SET [%s] = ? WHERE [%s] = ?",
				Views.TBL_ORDER, Views.COL_ORDER_STATUS, Views.COL_ORDER_ID));

		try {

			int rowsAffected = db.update(queryBuilder.toString(), new Object[] { status, orderid });

			return rowsAffected > 0;
		} catch (DataAccessException e) {
			System.err.println("Error updating status for Order ID " + orderid + ": " + e.getMessage());
			return false;
		}
	}

	@SuppressWarnings("deprecation")
	public boolean updateOrderpaymentstatus(int orderid, String status) {
		StringBuilder queryBuilder = new StringBuilder(String.format("UPDATE [%s] SET [%s] = ? WHERE [%s] = ?",
				Views.TBL_ORDER, Views.COL_ORDER_PAYSTATUS, Views.COL_ORDER_ID));

		try {

			int rowsAffected = db.update(queryBuilder.toString(), new Object[] { status, orderid });

			return rowsAffected > 0;
		} catch (DataAccessException e) {
			System.err.println("Error updating status for Order ID " + orderid + ": " + e.getMessage());
			return false;
		}
	}
	
	@SuppressWarnings("deprecation")
	public List<Order> getOrdersByCustomerIdnogapging(int customerId) {
		StringBuilder queryBuilder = new StringBuilder(
				String.format("SELECT * FROM [%s] WHERE [%s] = ?", Views.TBL_ORDER, Views.COL_ORDER_CUSTOMER_ID));

		List<Object> params = new ArrayList<>();
		params.add(customerId);

		
		try {

			return db.query(queryBuilder.toString(), params.toArray(), new Order_mapper());
		} catch (DataAccessException e) {
			System.err.println("Error fetching orders for customer ID " + customerId + ": " + e.getMessage());
			return new ArrayList<>(); // Return an empty list in case of an error
		}
	}
	
	
	
}
