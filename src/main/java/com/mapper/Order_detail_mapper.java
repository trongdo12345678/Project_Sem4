package com.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.models.Order_detail;
import com.utils.Views;

public class Order_detail_mapper implements RowMapper<Order_detail>{
	public Order_detail mapRow(ResultSet rs,int rowNow) throws SQLException {
		Order_detail item = new Order_detail();
		item.setId(rs.getInt(Views.COL_ORDER_DETAIL_ID));
		item.setOrder_id(rs.getInt(Views.COL_ORDER_DETAIL_ORDER_ID));
		item.setStatus(rs.getString(Views.COL_ORDER_DETAIL_STATUS));
		item.setStock_id(rs.getInt(Views.COL_ORDER_DETAIL_STOCK_ID));
		item.setProduct_Id(rs.getInt(Views.COL_ORDER_DETAIL_PRODUCT_ID));
		item.setPrice(rs.getDouble(Views.COL_ORDER_DETAIL_PRICE));
		item.setQuantity(rs.getInt(Views.COL_ORDER_DETAIL_QUANTITY));
		item.setProduct_name(rs.getString(Views.COL_PRODUCT_NAME));
		item.setImg(rs.getString(Views.COL_PRODUCT_IMG));
		return item;
	}

}