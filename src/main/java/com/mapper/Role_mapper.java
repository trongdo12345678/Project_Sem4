package com.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.models.Role;
import com.utils.Views;

public class Role_mapper implements RowMapper<Role> {
	public Role mapRow(ResultSet rs , int rowNum) throws SQLException{
		Role item = new Role();
		item.setId(rs.getInt(Views.COL_ROLE_ID));
		item.setName(rs.getString(Views.COL_ROLE_NAME));
		return item;
	}

}
