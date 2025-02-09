package com.customer.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.admin.repository.EmployeeRepository;
import com.mapper.Coupon_mapper;
import com.models.Coupon;
import com.models.Employee;
import com.models.PageView;
import com.utils.Views;

@Repository
public class CouponRepository {
	@Autowired
	JdbcTemplate db;
	@Autowired
	EmployeeRepository repem;
	public Coupon findCouponByCode(String couponCode) {
		try {
			// Câu lệnh SQL để tìm coupon dựa trên code
			String sql = String.format("SELECT * FROM %s WHERE %s = ?", Views.TBL_DISCOUNT, Views.COL_DISCOUNT_CODE);

			return db.queryForObject(sql, new Object[] { couponCode }, new Coupon_mapper());
		} catch (DataAccessException e) {

			return null;
		}
	}
	public Coupon findCouponByCode(String couponCode, Integer excludeId) {
	    try {
	        String sql;
	        Object[] params;
	        
	        if (excludeId != null) {
	            // Nếu có excludeId, thêm điều kiện loại trừ ID hiện tại
	            sql = String.format("SELECT * FROM %s WHERE %s = ? AND %s != ?", 
	                              Views.TBL_DISCOUNT, 
	                              Views.COL_DISCOUNT_CODE,
	                              Views.COL_DISCOUNT_ID);
	            params = new Object[] { couponCode, excludeId };
	        } else {
	            // Nếu không có excludeId (trường hợp thêm mới)
	            sql = String.format("SELECT * FROM %s WHERE %s = ?", 
	                              Views.TBL_DISCOUNT, 
	                              Views.COL_DISCOUNT_CODE);
	            params = new Object[] { couponCode };
	        }

	        return db.queryForObject(sql, params, new Coupon_mapper());
	    } catch (DataAccessException e) {
	        return null;
	    }
	}
	public Coupon findCouponByid(int id) {
		try {

			String sql = String.format("SELECT * FROM %s WHERE %s = ?", Views.TBL_DISCOUNT, Views.COL_DISCOUNT_ID);

			return db.queryForObject(sql, new Object[] { id }, new Coupon_mapper());
		} catch (DataAccessException e) {

			return null;
		}
	}

	public double calculateTotalCartValueWithCoupon(double totalCartValue, Coupon cp) {

		double discount = 0.0;

		if (cp != null) {
			// Kiểm tra loại discount của coupon
			if (cp.getDiscountPercentage() != null) {

				discount = (cp.getDiscountPercentage() / 100.0) * totalCartValue;
			} else if (cp.getDiscountAmount() != null) {

				discount = cp.getDiscountAmount();
			}

			if (cp.getMaxDiscountAmount() != null && discount > cp.getMaxDiscountAmount()) {
				discount = cp.getMaxDiscountAmount();
			}
		}

		return discount;
	}
	
	public boolean insert(Coupon coupon) {
        try {
            String sql = String.format(
                "INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                Views.TBL_DISCOUNT,
                Views.COL_DISCOUNT_CODE,
                Views.COL_DISCOUNT_PERCENTAGE,
                Views.COL_DISCOUNT_AMOUNT,
                Views.COL_DISCOUNT_EXPIRY_DATE,
                Views.COL_DISCOUNT_MIN_ORDER_VALUE,
                Views.COL_DISCOUNT_MAX_DISCOUNT_AMOUNT,
                Views.COL_DISCOUNT_STATUS,
                Views.COL_DISCOUNT_EMPLOYEE_ID
            );

            int result = db.update(sql,
                coupon.getCode(),
                coupon.getDiscountPercentage(),
                coupon.getDiscountAmount(),
                coupon.getExpiryDate(),
                coupon.getMinOrderValue(),
                coupon.getMaxDiscountAmount(),
                coupon.getStatus(),
                coupon.getEmployee_Id()
            );

            return result > 0;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

	public List<Coupon> getAll(PageView pageView) {
	    try {
	        StringBuilder str_query = new StringBuilder(String.format(
	                "SELECT * FROM %s ORDER BY %s DESC",
	                Views.TBL_DISCOUNT, 
	                Views.COL_DISCOUNT_ID));

	        List<Object> params = new ArrayList<>();

	        // Đếm tổng số record
	        String countQuery = String.format("SELECT COUNT(*) FROM %s", Views.TBL_DISCOUNT);
	        int count = db.queryForObject(countQuery, Integer.class);

	        // Tính total page
	        int total_page = (int) Math.ceil((double) count / pageView.getPage_size());
	        pageView.setTotal_page(total_page);

	        // Thêm phân trang
	        if (pageView.isPaginationEnabled()) {
	            str_query.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
	            params.add((pageView.getPage_current() - 1) * pageView.getPage_size());
	            params.add(pageView.getPage_size());
	        }

	        // Thực hiện query và map kết quả
	        List<Coupon> coupons = db.query(str_query.toString(), new Coupon_mapper(), params.toArray());

	        // Lấy thông tin Employee cho mỗi Coupon nếu có employee_id
	        for (Coupon coupon : coupons) {
	            if (coupon.getEmployee_Id() != 0) {
	                Employee employee = repem.findId(coupon.getEmployee_Id());
	                coupon.setEmployee(employee);
	            }
	        }

	        return coupons;

	    } catch (DataAccessException e) {
	        System.err.println("Error in getAll coupons: " + e.getMessage());
	        return Collections.emptyList();
	    }
	}
	
	public boolean updateDiscount(Coupon coupon) {
	    try {
	        String sql;
	        Object[] params;
	        
	        if (isDiscountUsedInOrders(coupon.getId())) {
	            // Nếu đã sử dụng, chỉ update status và expiry date
	            sql = String.format("UPDATE %s SET %s = ?, %s = ?, %s = ? WHERE %s = ?",
	                Views.TBL_DISCOUNT,
	                Views.COL_DISCOUNT_STATUS,
	                Views.COL_DISCOUNT_EXPIRY_DATE,
	                Views.COL_DISCOUNT_EMPLOYEE_ID,
	                Views.COL_DISCOUNT_ID);
	            
	            params = new Object[] { 
	                coupon.getStatus(),
	                coupon.getExpiryDate(),
	                coupon.getEmployee_Id(),
	                coupon.getId()
	            };
	        } else {
	            // Nếu chưa sử dụng, update tất cả thông tin
	            sql = String.format("UPDATE %s SET %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ? WHERE %s = ?",
	                Views.TBL_DISCOUNT,
	                Views.COL_DISCOUNT_CODE,
	                Views.COL_DISCOUNT_PERCENTAGE,
	                Views.COL_DISCOUNT_AMOUNT,
	                Views.COL_DISCOUNT_EXPIRY_DATE,
	                Views.COL_DISCOUNT_MIN_ORDER_VALUE,
	                Views.COL_DISCOUNT_MAX_DISCOUNT_AMOUNT,
	                Views.COL_DISCOUNT_STATUS,
	                Views.COL_DISCOUNT_EMPLOYEE_ID,
	                Views.COL_DISCOUNT_ID);
	            
	            params = new Object[] { 
	                coupon.getCode(),
	                coupon.getDiscountPercentage(),
	                coupon.getDiscountAmount(),
	                coupon.getExpiryDate(),
	                coupon.getMinOrderValue(),
	                coupon.getMaxDiscountAmount(),
	                coupon.getStatus(),
	                coupon.getEmployee_Id(),
	                coupon.getId()
	            };
	        }
	        
	        return db.update(sql, params) > 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
    public boolean isDiscountUsedInOrders(int discountId) {
        try {
            String sql = String.format(
                "SELECT COUNT(*) FROM [%s] WHERE %s = ?", 
                Views.TBL_ORDER, 
                Views.COL_ORDER_COUPONID
            );
            int count = db.queryForObject(sql, Integer.class, discountId);
            return count > 0;
        } catch (DataAccessException e) {
            System.err.println("Error in isDiscountUsedInOrders: " + e.getMessage());
            return false;
        }
    }

    public Coupon findById(int id) {
        try {
            String sql = String.format(
                "SELECT * FROM %s WHERE %s = ?",
                Views.TBL_DISCOUNT,
                Views.COL_DISCOUNT_ID
            );
            return db.queryForObject(sql, new Object[]{id}, new Coupon_mapper());
        } catch (DataAccessException e) {
            System.err.println("Error in findById: " + e.getMessage());
            return null;
        }
    }

    
	
}
