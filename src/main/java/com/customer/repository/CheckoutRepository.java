package com.customer.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.mapper.*;
import com.models.Order;
import com.models.Order_detail;
import com.models.Payment;
import com.models.Shopping_cart;
import com.models.Warehouse;
import com.utils.Views;

@Repository
public class CheckoutRepository {
	@Autowired
	JdbcTemplate db;

	@Autowired
	Order_detailRepository repod;

	public List<Payment> takeall() {
		try {
			// Define the SQL query to retrieve the customer by email
			String str_query = String.format("SELECT * FROM %s ", Views.TBL_PAYMENT);

			// Fetch the customer details

			return db.query(str_query, new Payment_mapper());

		} catch (DataAccessException e) {

		}
		return null; // Return null if login fails
	}

	public boolean Checkout(Order order, List<Shopping_cart> listc) {
		try {

			String insertSql = String.format(
					"INSERT INTO [%s] (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s) "
							+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
					Views.TBL_ORDER, Views.COL_ORDER_CUSTOMER_ID, Views.COL_ORDER_CUSNAME, Views.COL_ORDER_PHONE,
					Views.COL_ORDER_STATUS, Views.COL_ORDER_ADDRESS, Views.COL_ORDER_PAYSTATUS,
					Views.COL_ORDER_EMPLOYEE, Views.COL_ORDER_PAYMENT_ID, Views.COL_ORDER_DATE,
					Views.COL_ORDER_COUPONID, Views.COL_ORDER_DISCOUNT, Views.COL_ORDER_TOTALAMOUNT,
					Views.COL_ORDER_SHIPPINGFEE, Views.COL_ORDER_ORDERID, Views.COL_ORDER_NOTES,
					Views.COL_ORDER_TRANSMOMOID, Views.COL_ORDER_PROVINCE_ID, Views.COL_ORDER_DISTRICT_ID,
					Views.COL_ORDER_WARD_ID, Views.COL_ORDER_WAREHOUSE_ID);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			int rowsAffected = db.update(connection -> {
				PreparedStatement ps = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, order.getCustomer_id());
				ps.setString(2, order.getCus_Name());
				ps.setString(3, order.getPhone());
				ps.setString(4, order.getStatus());
				ps.setString(5, order.getAddress());
				ps.setString(6, order.getPay_status());
				ps.setObject(7, order.getEmployee_id() != 0 ? order.getEmployee_id() : null);
				ps.setInt(8, order.getPayment_id());
				ps.setDate(9, Date.valueOf(order.getDate()));
				ps.setObject(10, order.getCoupon_id() != 0 ? order.getCoupon_id() : null);
				ps.setDouble(11, order.getDiscount());
				ps.setDouble(12, order.getTotalAmount());
				ps.setDouble(13, order.getShippingFee());
				ps.setString(14, order.getOrderID());
				ps.setString(15, order.getNotes());
				ps.setString(16, order.getTransactionId());
				ps.setInt(17, order.getProvince_Id());
				ps.setInt(18, order.getDistrict_Id());
				ps.setString(19, order.getWard_Id());
				ps.setObject(20, order.getWareHouse_Id() != 0 ? order.getWareHouse_Id() : null);
				return ps;
			}, keyHolder);

			if (rowsAffected > 0) {
				// Fetch the auto-generated Id from the KeyHolder
				order.setId(keyHolder.getKey().intValue());

				for (Shopping_cart cart : listc) {
					Order_detail od = new Order_detail();

					od.setStock_id(0);
					od.setPrice(cart.getPrice());
					od.setStatus(null);
					od.setOrder_id(order.getId());
					od.setProduct_Id(cart.getProduct_id());
					od.setQuantity(cart.getQuantity());
					boolean check = repod.insertOrderDetail(od, cart.getId());

					if (!check) {
						System.err.println("Failed to insert order detail for product: " + cart.getProduct_id());
						return false;
					}
				}
				return true;
			} else {
				return false;
			}

		} catch (DataAccessException e) {
			System.err.println("Error inserting order: " + e.getMessage());
			return false;
		}
	}

	public List<Warehouse> findAll() {
		try {
			String sql = String.format("SELECT * FROM %s", Views.TBL_WAREHOUSE);

			List<Warehouse> warehouses = db.query(sql, new WarehouseDISTANCE_mapper());

			return warehouses;
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

}
