package com.customer.repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.models.Feedback;
import com.utils.Views;
import com.mapper.FeedbackWithCustomerMapper;

@Repository
public class FeedbackRepository {

	@Autowired
	private JdbcTemplate db;

	public boolean addFeedback(Feedback feedback) {
		try {
			// 1. Thêm feedback
			String sqlFeedback = String.format("INSERT INTO %s (%s, %s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?, ?)",
					Views.TBL_FEEDBACK, Views.COL_FEEDBACK_PROID, Views.COL_FEEDBACK_ORDID, Views.COL_FEEDBACK_RATE,
					Views.COL_FEEDBACK_COMMENT, Views.COL_FEEDBACK_CREATEDATE, Views.COL_FEEDBACK_STATUS);

			LocalDateTime creationDateTime = LocalDateTime.now();
			Timestamp creationTimestamp = Timestamp.valueOf(creationDateTime);

			int feedbackRows = db.update(sqlFeedback, feedback.getProduct_Id(), feedback.getOrderDetail_Id(),
					feedback.getRating(), feedback.getComment(), creationTimestamp, true);

			// 2. Cập nhật trạng thái OrderDetail
			if (feedbackRows > 0) {
				String sqlUpdateOrderDetail = String.format("UPDATE %s SET Status = 'Reviewed' WHERE %s = ?",
						Views.TBL_ORDER_DETAIL, Views.COL_ORDER_DETAIL_ID);

				int orderDetailRows = db.update(sqlUpdateOrderDetail, feedback.getOrderDetail_Id());

				return orderDetailRows > 0;
			}

			return false;

		} catch (DataAccessException e) {
			System.err.println("Error adding feedback: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public double calculateAverageRating(int productId) {
		try {
			String sql = String.format("SELECT AVG(CAST(%s AS FLOAT)) as average_rating " + "FROM %s " + "WHERE %s = ?",
					Views.COL_FEEDBACK_RATE, Views.TBL_FEEDBACK, Views.COL_FEEDBACK_PROID);

			Double avgRating = db.queryForObject(sql, Double.class, productId);

			if (avgRating != null) {
				return Math.round(avgRating * 10.0) / 10.0;
			}
			return 0.0;

		} catch (DataAccessException e) {
			System.err.println("Error calculating average rating: " + e.getMessage());
			return 0.0;
		}
	}

	public List<Feedback> getProductFeedbacks(int productId) {
		try {
			String sql = String.format(
					"SELECT f.*, " + "CONCAT(c.%s, ' ', c.%s) as CustomerName " + "FROM %s f "
							+ "INNER JOIN %s od ON f.%s = od.%s " + "INNER JOIN [%s] o ON od.%s = o.%s "
							+ "INNER JOIN %s c ON o.%s = c.%s " + "WHERE f.%s = ? " + "ORDER BY f.%s DESC",
					Views.COL_CUSTOMER_FIRST_NAME, Views.COL_CUSTOMER_LAST_NAME, Views.TBL_FEEDBACK,
					Views.TBL_ORDER_DETAIL, Views.COL_FEEDBACK_ORDID, Views.COL_ORDER_DETAIL_ID, Views.TBL_ORDER,
					Views.COL_ORDER_DETAIL_ORDER_ID, Views.COL_ORDER_ID, Views.TBL_CUSTOMER,
					Views.COL_ORDER_CUSTOMER_ID, Views.COL_CUSTOMER_ID, Views.COL_FEEDBACK_PROID,
					Views.COL_FEEDBACK_CREATEDATE);

			return db.query(sql, new FeedbackWithCustomerMapper(), productId);

		} catch (DataAccessException e) {
			System.err.println("Error getting product feedbacks: " + e.getMessage());
			return new ArrayList<>();
		}
	}
}