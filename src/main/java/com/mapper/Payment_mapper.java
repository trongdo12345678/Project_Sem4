package com.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.models.Payment;
import com.utils.Views;

public class Payment_mapper implements RowMapper<Payment>{
	public Payment mapRow(ResultSet rs , int rowNum) throws SQLException{
		Payment item = new Payment();
		item.setId(rs.getInt(Views.COL_PAYMENT_ID));
		item.setName(rs.getString(Views.COL_PAYMENT_NAME));
		return item;
	}

}
