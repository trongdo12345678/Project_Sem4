package com.admin.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.models.PageView;
import com.models.TaxHistory;
import com.mapper.*;
import com.utils.Views;

@Repository
public class TaxRepository {
	@Autowired
	private JdbcTemplate db;

	public List<TaxHistory> findAll(PageView pageView) {
		try {
			// Base query
			StringBuilder str_query = new StringBuilder(String.format("SELECT * FROM %s ORDER BY %s DESC",
					Views.TBL_TAX_HISTORY, Views.COL_TAX_HISTORY_PERIOD_START));

			List<Object> params = new ArrayList<>();

			// Query đếm tổng số records
			String countQuery = String.format("SELECT COUNT(*) FROM %s", Views.TBL_TAX_HISTORY);
			int count = db.queryForObject(countQuery, Integer.class);

			// Tính total page
			int total_page = (int) Math.ceil((double) count / pageView.getPage_size());
			pageView.setTotal_page(total_page);

			// Thêm phân trang nếu được enable
			if (pageView.isPaginationEnabled()) {
				str_query.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
				params.add((pageView.getPage_current() - 1) * pageView.getPage_size());
				params.add(pageView.getPage_size());
			}

			// Thực hiện query và map kết quả
			return db.query(str_query.toString(), new TaxHistoryMapper(), params.toArray());

		} catch (DataAccessException e) {
			System.err.println("Error in findAll: " + e.getMessage());
			return Collections.emptyList();
		}
	}

	public List<TaxHistory> findByPeriod(LocalDate start, LocalDate end, PageView pageView) {
		try {
			// Base query với điều kiện period
			StringBuilder str_query = new StringBuilder(
					String.format("SELECT * FROM %s WHERE 1=1", Views.TBL_TAX_HISTORY));

			List<Object> params = new ArrayList<>();

			// Thêm điều kiện cho ngày bắt đầu
			if (start != null) {
				str_query.append(String.format(" AND %s >= ?", Views.COL_TAX_HISTORY_PERIOD_START));
				params.add(start);
			}

			// Thêm điều kiện cho ngày kết thúc
			if (end != null) {
				str_query.append(String.format(" AND %s <= ?", Views.COL_TAX_HISTORY_PERIOD_START));
				params.add(end);
			}

			str_query.append(" ORDER BY %s DESC".formatted(Views.COL_TAX_HISTORY_PERIOD_START));

			// Query đếm với điều kiện period
			String countQuery = String.format("SELECT COUNT(*) FROM %s WHERE 1=1", Views.TBL_TAX_HISTORY);

			if (start != null) {
				countQuery += String.format(" AND %s >= ?", Views.COL_TAX_HISTORY_PERIOD_START);
			}
			if (end != null) {
				countQuery += String.format(" AND %s <= ?", Views.COL_TAX_HISTORY_PERIOD_START);
			}

			// Đếm số lượng bản ghi
			int count = db.queryForObject(countQuery, Integer.class, params.toArray());

			// Tính tổng số trang
			int total_page = (int) Math.ceil((double) count / pageView.getPage_size());
			pageView.setTotal_page(total_page);

			// Thêm phân trang nếu được enable
			if (pageView.isPaginationEnabled()) {
				str_query.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
				params.add((pageView.getPage_current() - 1) * pageView.getPage_size());
				params.add(pageView.getPage_size());
			}

			// Thực hiện query và map kết quả
			return db.query(str_query.toString(), new TaxHistoryMapper(), params.toArray());

		} catch (DataAccessException e) {
			System.err.println("Error in findByPeriod: " + e.getMessage());
			return Collections.emptyList();
		}
	}

	public TaxHistory findById(int id) {
		try {
			String sql = String.format("SELECT * FROM %s WHERE %s = ?", Views.TBL_TAX_HISTORY,
					Views.COL_TAX_HISTORY_ID);
			return db.queryForObject(sql, new TaxHistoryMapper(), id);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public void save(TaxHistory tax) {
		String sql = String.format(
				"INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
				Views.TBL_TAX_HISTORY, Views.COL_TAX_HISTORY_TYPE, Views.COL_TAX_HISTORY_PERIOD_START,
				Views.COL_TAX_HISTORY_PERIOD_END, Views.COL_TAX_HISTORY_REVENUE, Views.COL_TAX_HISTORY_RATE,
				Views.COL_TAX_HISTORY_AMOUNT, Views.COL_TAX_HISTORY_STATUS, Views.COL_TAX_HISTORY_PAYMENT_DATE,
				Views.COL_TAX_HISTORY_NOTE, Views.COL_TAX_HISTORY_CREATED_BY);

		db.update(sql, tax.getTaxType(), tax.getPeriodStart(), tax.getPeriodEnd(), tax.getAmount(),
				tax.getTaxRate(), tax.getTaxAmount(), tax.getPaymentStatus(), tax.getPaymentDate(), tax.getNote(),
				tax.getCreatedBy());
	}

	public Double calculateRevenue(LocalDate startDate, LocalDate endDate) {
	    String sql = String.format(
	        "SELECT " + 
	        "    (SELECT COALESCE(SUM(%s), 0) " + 
	        "     FROM [%s] " + 
	        "     WHERE %s BETWEEN ? AND ? " +
	        "     AND %s = 'Completed') - " + 
	        "    (SELECT COALESCE(SUM(%s), 0) " + 
	        "     FROM %s " + 
	        "     WHERE %s BETWEEN ? AND ? " + 
	        "     AND %s IN ('Completed', 'Accepted') " +  
	        "     AND %s IS NOT NULL) as Total_Revenue",
	        Views.COL_ORDER_TOTALAMOUNT, 
	        Views.TBL_ORDER, 
	        Views.COL_ORDER_DATE, 
	        Views.COL_ORDER_STATUS,
	        Views.COL_RETURN_ORDER_FINAL_AMOUNT,
	        Views.TBL_RETURN_ORDER, 
	        Views.COL_RETURN_ORDER_DATE,
	        Views.COL_RETURN_ORDER_STATUS,
	        Views.COL_RETURN_ORDER_FINAL_AMOUNT);

	    try {
	        return db.queryForObject(sql, Double.class, startDate, endDate, startDate, endDate);
	    } catch (DataAccessException e) {
	        System.err.println("Error calculating revenue: " + e.getMessage());
	        return 0.0;
	    }
	}
	public void updateStatus(TaxHistory tax) {
		String sql = String.format("UPDATE %s SET %s = ?, %s = ? WHERE %s = ?", Views.TBL_TAX_HISTORY,
				Views.COL_TAX_HISTORY_STATUS, Views.COL_TAX_HISTORY_PAYMENT_DATE, Views.COL_TAX_HISTORY_ID);

		try {
			db.update(sql, tax.getPaymentStatus(), tax.getPaymentDate(), tax.getId());
		} catch (DataAccessException e) {
			System.err.println("Error updating tax status: " + e.getMessage());
			throw e;
		}
	}

}