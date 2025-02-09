package com.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.models.Warehouse_type;
import com.utils.Views;

public class Warehouse_type_mapper implements RowMapper<Warehouse_type>{
	public Warehouse_type mapRow(ResultSet rs,int rowNum) throws SQLException {
		Warehouse_type item = new Warehouse_type();
		item.setId(rs.getInt(Views.COL_WAREHOUSE_TYPE_ID));
		item.setName(rs.getString(Views.COL_WAREHOUSE_TYPE_NAME));
		
		return item;
		
	}
}
