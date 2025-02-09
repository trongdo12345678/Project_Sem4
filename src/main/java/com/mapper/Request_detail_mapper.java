package com.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.models.Request_detail;
import com.utils.Views;

public class Request_detail_mapper implements RowMapper<Request_detail>{
	public Request_detail mapRow(ResultSet s , int rowNum) throws SQLException{
		Request_detail item = new Request_detail();
		item.setId(s.getInt(Views.COL_REQUEST_DETAIL_ID));
		item.setQuantity_requested(s.getInt(Views.COL_REQUEST_DETAIL_QUANTITY_REQUESTED));
		item.setQuantity_exported(s.getInt(Views.COL_REQUEST_DETAIL_QUANTITY_EXPORTED));
		item.setStatus(s.getString(Views.COL_REQUEST_DETAIL_STATUS));
		item.setId_product(s.getInt(Views.COL_REQUEST_DETAIL_ID_PRODUCT));
		item.setRequest_id(s.getInt(Views.COL_REQUEST_DETAIL_REQUEST_ID));		
		
		return item;
	}

}

