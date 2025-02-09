package com.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.models.Employee;
import com.utils.Views;

public class Employee_mapper implements RowMapper<Employee>{
	public Employee mapRow(ResultSet rs , int rowNow) throws SQLException{
		Employee item = new Employee();
		item.setId(rs.getInt(Views.COL_EMPLOYEE_ID));
		item.setFirst_name(rs.getString(Views.COL_EMPLOYEE_FIRST_NAME));
		item.setLast_name(rs.getString(Views.COL_EMPLOYEE_LAST_NAME));
		item.setPassword(rs.getString(Views.COL_EMPLOYEE_PASSWORD));
		item.setPhone(rs.getString(Views.COL_EMPLOYEE_PHONE));
		item.setRole_id(rs.getInt(Views.COL_EMPLOYEE_ROLE_ID));
		 try {
	            if (hasColumn(rs, "role_name")) {
	                item.setRole_name(rs.getString("role_name"));
	            }
	        } catch (SQLException e) {
	        	e.printStackTrace();
	        }
		return item;
	}
	
	 private boolean hasColumn(ResultSet rs, String columnName) throws SQLException {
	        try {
	            rs.findColumn(columnName);
	            return true;
	        } catch (SQLException e) {
	            return false;
	        }
	    }
}
