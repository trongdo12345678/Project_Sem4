package com.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

import com.models.Product_price_change;
import com.utils.Views;

public class Product_price_change_mapper implements RowMapper<Product_price_change>{
	public Product_price_change mapRow(ResultSet r , int rowNum) throws SQLException{
		Product_price_change item = new Product_price_change();
		item.setId(r.getInt(Views.COL_PRICE_CHANGE_ID));
		item.setPrice(r.getDouble(Views.COL_PRICE_CHANGE_PRICE));
		item.setProduct_id(r.getInt(Views.COL_PRICE_CHANGE_PRODUCT_ID));
		Timestamp tsEnd = r.getTimestamp(Views.COL_PRICE_CHANGE_DATE_END);
		if(tsEnd != null) {
			item.setDate_end(tsEnd.toLocalDateTime());
		}
		Timestamp tsS = r.getTimestamp(Views.COL_PRICE_CHANGE_DATE_START); 
		if(tsS != null) {
			item.setDate_start(tsS.toLocalDateTime());
		}
		return item;
	}

}
