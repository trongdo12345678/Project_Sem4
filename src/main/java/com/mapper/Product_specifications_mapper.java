package com.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.models.Product_specifications;
import com.utils.Views;

public class Product_specifications_mapper implements RowMapper<Product_specifications> {
	public Product_specifications mapRow(ResultSet rs , int rownum) throws SQLException{
		Product_specifications pros = new Product_specifications();
		pros.setId(rs.getInt(Views.COL_PRODUCT_SPE_ID));
		pros.setName_spe(rs.getString(Views.COL_PRODUCT_SPE_NAME_SPE));
		pros.setDes_spe(rs.getString(Views.COL_PRODUCT_SPE_DES_SPE));
		pros.setProduct_id(rs.getInt(Views.COL_PRODUCT_IMG_PRODUCT_ID));
		pros.setProduct_name(rs.getString(Views.COL_PRODUCT_NAME));
		return pros;
	}
}
