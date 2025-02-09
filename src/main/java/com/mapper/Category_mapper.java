package com.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.models.Category_Product;
import com.utils.Views;

public class Category_mapper implements RowMapper<Category_Product> {
	public Category_Product mapRow(ResultSet rs,int rowNum) throws SQLException{
		Category_Product item = new Category_Product();
		item.setId(rs.getInt(Views.COL_CATEGORY_ID));
		item.setName(rs.getString(Views.COL_CATEGORY_NAME));
		return item;
	}
}
