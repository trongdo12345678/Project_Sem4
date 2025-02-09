package com.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.models.Conversion;
import com.utils.Views;

public class Conversion_mapper implements RowMapper<Conversion>{
	public Conversion mapRow(ResultSet rs,int rowNum) throws SQLException{
		Conversion item = new Conversion();
		item.setId(rs.getInt(Views.COL_CONVERSION_ID));
		item.setConversion_rate(rs.getInt(Views.COL_CONVERSION_RATE));
		item.setFrom_unit_id(rs.getInt(Views.COL_CONVERSION_FROM_UNIT_ID));
		item.setTo_unit_id(rs.getInt(Views.COL_CONVERSION_TO_UNIT_ID));
		item.setProduct_id(rs.getInt(Views.COL_CONVERSION_PRODUCT_ID));
		
		item.setProductName(rs.getString(Views.COL_PRODUCT_NAME));
		item.setFromUnitName(rs.getString("from_unit_name"));  
        item.setToUnitName(rs.getString("to_unit_name")); 
		return item;
	}

}