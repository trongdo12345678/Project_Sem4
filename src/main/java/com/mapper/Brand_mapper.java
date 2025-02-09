package com.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.models.Brand;
import com.utils.Views;


public class Brand_mapper implements RowMapper<Brand> {
	public Brand mapRow(ResultSet rs,int rowNum) throws SQLException{
		Brand item = new Brand();
		   item.setId(rs.getInt(Views.COL_BRAND_ID));
		   item.setName(rs.getString(Views.COL_BRAND_NAME));
		   return item;
	   }
}
