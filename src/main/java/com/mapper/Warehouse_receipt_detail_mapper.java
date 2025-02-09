package com.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.models.Warehouse_receipt_detail;
import com.utils.Views;

public class Warehouse_receipt_detail_mapper implements RowMapper<Warehouse_receipt_detail> {
	public Warehouse_receipt_detail mapRow(ResultSet rs , int rowNum) throws SQLException{
		Warehouse_receipt_detail item = new Warehouse_receipt_detail();
		item.setId(rs.getInt(Views.COL_WAREHOUSE_RECEIPT_DETAIL_ID));
		item.setQuantity(rs.getInt(Views.COL_WAREHOUSE_RECEIPT_DETAIL_QUANTITY));
		item.setWh_receipt_id(rs.getInt(Views.COL_DETAIL_WAREHOUSE_RECEIPT_ID));
		item.setWh_price(rs.getDouble(Views.COL_WAREHOUSE_RECEIPT_DETAIL_WH_PRICE));
		item.setProduct_id(rs.getInt(Views.COL_WAREHOUSE_RECEIPT_DETAIL_PRODUCT_ID));
		item.setConversion_id(rs.getInt(Views.COL_WAREHOUSE_RECEIPT_DETAIL_CONVERSION));
		item.setStatus(rs.getString(Views.COL_WAREHOUSE_RECEIPT_DETAILS_STATUS));
		item.setProduct_name(rs.getString("product_name"));
		return item;
	}

}