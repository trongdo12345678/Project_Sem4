package com.customer.repository;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mapper.Shopping_cart_mapper;
import com.models.Shopping_cart;
import com.utils.Views;

@Repository
public class CartRepository {
	@Autowired
	JdbcTemplate db;

	public boolean checkProductInCart(Shopping_cart cart) {
		try {
			// Câu truy vấn để kiểm tra sự tồn tại của sản phẩm trong giỏ hàng
			String checkSql = String.format("SELECT COUNT(*) FROM %s WHERE %s = ? AND %s = ?", Views.TBL_SHOPING_CART,
					Views.COL_SHOPING_CART_CUSTOMER_ID, Views.COL_SHOPING_CART_PRODUCT_ID);

			// Thực thi truy vấn
			@SuppressWarnings("deprecation")
			int count = db.queryForObject(checkSql, new Object[] { cart.getCustomer_id(), cart.getProduct_id() },
					Integer.class);

			return count > 0;
		} catch (DataAccessException e) {
			// In lỗi ra console và trả về false trong trường hợp có lỗi
			System.err.println("Error checking product in cart: " + e.getMessage());
			return false;
		}
	}

	public boolean updateProductQuantityInCart(Shopping_cart cart) {
		try {
			// Câu lệnh SQL để cập nhật tăng thêm 1 cho số lượng sản phẩm trong giỏ hàng
			String updateSql = String.format("UPDATE %s SET %s = %s + ? WHERE %s = ? AND %s = ?",
					Views.TBL_SHOPING_CART, // Tên bảng giỏ hàng
					Views.COL_SHOPING_CART_QUANTITY, // Cột số lượng trong giỏ hàng
					Views.COL_SHOPING_CART_QUANTITY, // Cột số lượng cần cộng thêm
					Views.COL_SHOPING_CART_CUSTOMER_ID, // Cột ID khách hàng
					Views.COL_SHOPING_CART_PRODUCT_ID); // Cột ID sản phẩm

			// Thực thi câu lệnh cập nhật với các tham số từ đối tượng Shopping_cart
			int rowsAffected = db.update(updateSql, cart.getQuantity(), cart.getCustomer_id(), cart.getProduct_id());

			// Trả về true nếu cập nhật thành công (số hàng bị ảnh hưởng > 0)
			return rowsAffected > 0;
		} catch (DataAccessException e) {
			// In lỗi ra console và trả về false nếu có lỗi
			System.err.println("Error updating product quantity in cart: " + e.getMessage());
			return false;
		}
	}

	public boolean plusProductQuantityInCart(int id) {
		try {
			// Câu lệnh SQL để cập nhật tăng thêm 1 cho số lượng sản phẩm trong giỏ hàng
			String updateSql = String.format("UPDATE %s SET %s = %s + 1 WHERE %s = ? ", Views.TBL_SHOPING_CART, // Tên
																												// bảng
																												// giỏ
																												// hàng
					Views.COL_SHOPING_CART_QUANTITY, // Cột số lượng trong giỏ hàng
					Views.COL_SHOPING_CART_QUANTITY, // Cột số lượng cần cộng thêm
					Views.COL_SHOPING_CART_ID // Cột ID khách hàng
			); // Cột ID sản phẩm

			// Thực thi câu lệnh cập nhật với các tham số từ đối tượng Shopping_cart
			int rowsAffected = db.update(updateSql, id);

			// Trả về true nếu cập nhật thành công (số hàng bị ảnh hưởng > 0)
			return rowsAffected > 0;
		} catch (DataAccessException e) {
			// In lỗi ra console và trả về false nếu có lỗi
			System.err.println("Error updating product quantity in cart: " + e.getMessage());
			return false;
		}
	}

	public boolean minusProductQuantityInCart(int id) {
		try {

			String updateSql = String.format("UPDATE %s SET %s = CASE WHEN %s > 1 THEN %s - 1 ELSE 1 END WHERE %s = ?",
					Views.TBL_SHOPING_CART, // Tên bảng giỏ hàng
					Views.COL_SHOPING_CART_QUANTITY, // Cột số lượng trong giỏ hàng
					Views.COL_SHOPING_CART_QUANTITY, // Cột số lượng hiện tại
					Views.COL_SHOPING_CART_QUANTITY, // Cột số lượng cần trừ
					Views.COL_SHOPING_CART_ID // Cột ID sản phẩm
			);

			int rowsAffected = db.update(updateSql, id);

			return rowsAffected > 0;
		} catch (DataAccessException e) {

			System.err.println("Error updating product quantity in cart: " + e.getMessage());
			return false;
		}
	}

	public boolean deleteCartById(int cartId) {
		try {
			// Câu lệnh SQL để xoá bản ghi trong bảng giỏ hàng
			String deleteSql = String.format("DELETE FROM %s WHERE %s = ?", Views.TBL_SHOPING_CART, // Tên bảng giỏ hàng
					Views.COL_SHOPING_CART_ID // Cột ID sản phẩm
			);

			// Thực thi câu lệnh xoá với ID giỏ hàng
			int rowsAffected = db.update(deleteSql, cartId);

			// Trả về true nếu xoá thành công (số hàng bị ảnh hưởng > 0)
			return rowsAffected > 0;
		} catch (DataAccessException e) {
			// In lỗi ra console và trả về false nếu có lỗi
			System.err.println("Error deleting cart item: " + e.getMessage());
			return false;
		}
	}

	public boolean insertProductToCart(Shopping_cart cart) {
		try {
			// Câu lệnh SQL để chèn sản phẩm mới vào giỏ hàng
			String insertSql = String.format("INSERT INTO %s (%s, %s, %s) VALUES (?, ?, ?)", Views.TBL_SHOPING_CART,
					Views.COL_SHOPING_CART_CUSTOMER_ID, Views.COL_SHOPING_CART_PRODUCT_ID,
					Views.COL_SHOPING_CART_QUANTITY);

			// Thực thi chèn sản phẩm
			int rowsAffected = db.update(insertSql, cart.getCustomer_id(), cart.getProduct_id(), cart.getQuantity());

			// Trả về true nếu chèn thành công
			return rowsAffected > 0;
		} catch (DataAccessException e) {
			// In lỗi ra console và trả về false nếu có lỗi
			System.err.println("Error inserting product to cart: " + e.getMessage());
			return false;
		}
	}

	public boolean addToCartOrUpdate(Shopping_cart cart) {

		if (checkProductInCart(cart)) {

			return updateProductQuantityInCart(cart);
		} else {

			return insertProductToCart(cart);
		}
	}

	public List<Shopping_cart> findAllCartsByCustomerId(int customerId) {
		try {
			String str_query = String.format(
					"SELECT sc.*, p.%s, p.%s, p.%s, p.%s as cart_status, " + "p.%s, p.%s, p.%s, p.%s " + "FROM %s sc "
							+ "INNER JOIN %s p ON sc.%s = p.%s " + "INNER JOIN %s c ON sc.%s = c.%s "
							+ "WHERE sc.%s = ? ",
					Views.COL_PRODUCT_NAME, Views.COL_PRODUCT_PRICE, Views.COL_PRODUCT_IMG, Views.COL_PRODUCT_STATUS,
					Views.COL_PRODUCT_WELGHT, Views.COL_PRODUCT_HEIGHT, Views.COL_PRODUCT_WIDTH,
					Views.COL_PRODUCT_LENGTH, Views.TBL_SHOPING_CART, Views.TBL_PRODUCT,
					Views.COL_SHOPING_CART_PRODUCT_ID, Views.COL_PRODUCT_ID, Views.TBL_CUSTOMER,
					Views.COL_SHOPING_CART_CUSTOMER_ID, Views.COL_CUSTOMER_ID, Views.COL_SHOPING_CART_CUSTOMER_ID);

			return db.query(str_query, new Shopping_cart_mapper(), customerId);

		} catch (DataAccessException e) {
			System.err.println("Error fetching shopping carts: " + e.getMessage());
			return Collections.emptyList();
		}
	}

}
