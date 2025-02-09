package com.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.models.Coupon;
import com.utils.Views;

public class Coupon_mapper implements RowMapper<Coupon> {
	@Override
	public Coupon mapRow(ResultSet rs, int rowNum) throws SQLException {
		Coupon coupon = new Coupon();
		coupon.setId(rs.getInt(Views.COL_DISCOUNT_ID));
		coupon.setCode(rs.getString(Views.COL_DISCOUNT_CODE));
		coupon.setDiscountPercentage(rs.getDouble(Views.COL_DISCOUNT_PERCENTAGE));
		coupon.setDiscountAmount(rs.getDouble(Views.COL_DISCOUNT_AMOUNT));
		coupon.setExpiryDate(rs.getDate(Views.COL_DISCOUNT_EXPIRY_DATE).toLocalDate());
		coupon.setMinOrderValue(rs.getDouble(Views.COL_DISCOUNT_MIN_ORDER_VALUE));
		coupon.setMaxDiscountAmount(rs.getDouble(Views.COL_DISCOUNT_MAX_DISCOUNT_AMOUNT));
		coupon.setStatus(rs.getString(Views.COL_DISCOUNT_STATUS));
		coupon.setEmployee_Id(rs.getInt(Views.COL_DISCOUNT_EMPLOYEE_ID));
		
		return coupon;
	}
}
