package com.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.models.Warehouse_rn_detail;
import com.utils.Views;

public class Warehouse_rn_detail_mapper implements RowMapper<Warehouse_rn_detail>{
	public Warehouse_rn_detail mapRow(ResultSet s , int rowNum) throws SQLException{
		Warehouse_rn_detail item = new Warehouse_rn_detail();
		item.setId(s.getInt(Views.COL_WAREHOUSE_RN_DETAIL_ID));
		item.setQuantity(s.getInt(Views.COL_WAREHOUSE_RN_DETAIL_QUANTITY));
		item.setStatus(s.getString(Views.COL_WAREHOUSE_RN_DETAIL_STATUS));
		item.setId_product(s.getInt(Views.COL_WAREHOUSE_RN_DETAIL_PRODUCTID));
		item.setStock_id(s.getInt(Views.COL_WAREHOUSE_RN_DETAIL_STOCK_ID));
		item.setWgrn_id(s.getInt(Views.COL_WAREHOUSE_RNOTE_ID));
		
		return item;
	}

}
