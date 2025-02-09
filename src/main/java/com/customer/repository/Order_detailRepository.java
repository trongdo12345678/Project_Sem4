package com.customer.repository;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mapper.Order_detail_mapper;
import com.models.Order_detail;
import com.utils.Views;

@Repository
public class Order_detailRepository {
	@Autowired
	JdbcTemplate db;

	@Autowired
	CartRepository repca;

	public boolean insertOrderDetail(Order_detail od, int cart_id) {
		try {
			String insertSql = String.format("INSERT INTO %s (%s, %s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?, ?)",
					Views.TBL_ORDER_DETAIL, Views.COL_ORDER_DETAIL_STOCK_ID, Views.COL_ORDER_DETAIL_ORDER_ID,
					Views.COL_ORDER_DETAIL_PRICE, Views.COL_ORDER_DETAIL_STATUS, Views.COL_ORDER_DETAIL_QUANTITY,
					Views.COL_ORDER_DETAIL_PRODUCT_ID);

			int rowsAffected = db.update(insertSql, od.getStock_id() != 0 ? od.getStock_id() : null, od.getOrder_id(),
					od.getPrice(), od.getStatus(), od.getQuantity(), od.getProduct_Id());
			if (rowsAffected > 0) {
				repca.deleteCartById(cart_id);
			}

			return rowsAffected > 0;
		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	public List<Order_detail> findAllOrderDetailsByOrderId(int orderId) {
		try {
			String str_query = String.format(
					"SELECT od.*, p.%s, p.%s " + "FROM %s od " + "LEFT JOIN %s p ON od.%s = p.%s " + "WHERE od.%s = ?",
					Views.COL_PRODUCT_NAME, Views.COL_PRODUCT_IMG, Views.TBL_ORDER_DETAIL, Views.TBL_PRODUCT,
					Views.COL_ORDER_DETAIL_PRODUCT_ID, Views.COL_PRODUCT_ID, Views.COL_ORDER_DETAIL_ORDER_ID);

			return db.query(str_query, new Order_detail_mapper(), orderId);

		} catch (DataAccessException e) {
			System.err.println("Error fetching order details: " + e.getMessage());
			return Collections.emptyList();
		}
	}

}
