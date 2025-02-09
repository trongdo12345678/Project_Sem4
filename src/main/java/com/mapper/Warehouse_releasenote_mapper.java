package com.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

import com.models.Warehouse_releasenote;
import com.utils.Views;

public class Warehouse_releasenote_mapper implements RowMapper<Warehouse_releasenote>{
	public Warehouse_releasenote mapRow(ResultSet r,int rowNum) throws SQLException{
		Warehouse_releasenote item = new Warehouse_releasenote();
		item.setId(r.getInt(Views.COL_WAREHOUSE_RELEASENOTE_ID));
		item.setName(r.getString(Views.COL_WAREHOUSE_RELEASENOTE_NAME));
		item.setOrder_id(r.getInt(Views.COL_WAREHOUSE_RELEASENOTE_ORDER_ID));
		item.setStatusWr(r.getString(Views.COL_WAREHOUSE_RELEASENOTE_STATUS));
		item.setEmployee_Id(r.getInt(Views.COL_WAREHOUSE_RELEASENOTE_EMPLOYEEID));
		item.setWarehouse_id(r.getInt(Views.COL_WAREHOUSE_RELEASENOTE_WAREHOUSE_ID));
		item.setRequest_id(r.getInt(Views.COL_WAREHOUSE_RELEASENOTE_WAREHOUSE_REQUEST_ID));
		
		Timestamp ts = r.getTimestamp(Views.COL_WAREHOUSE_RELEASENOTE_DATE);
		
		if(ts != null) {
			item.setDate(ts.toLocalDateTime());
		}
		return item;
	}

}
