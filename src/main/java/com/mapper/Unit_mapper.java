package com.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.models.Unit;
import com.utils.Views;

public class Unit_mapper implements RowMapper<Unit>{
	public Unit mapRow(ResultSet rs , int mapRow) throws SQLException{
		Unit item = new Unit();
		item.setId(rs.getInt(Views.COL_UNIT_ID));
		item.setName(rs.getString(Views.COL_UNIT_NAME));
		return item;
	}

}
