package com.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.models.Product_img;
import com.utils.Views;

public class Product_img_mapper implements RowMapper<Product_img> {
	public Product_img mapRow(ResultSet rs , int rownum) throws SQLException{
		Product_img proi = new Product_img();
		proi.setId(rs.getInt(Views.COL_PRODUCT_IMG_ID));
		proi.setImg_url(rs.getString(Views.COL_PRODUCT_IMG_URL));
		proi.setProduct_id(rs.getInt(Views.COL_PRODUCT_IMG_PRODUCT_ID));
	
		return proi;
		
	}
}
