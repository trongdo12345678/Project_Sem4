package com.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.models.Employee_warehouse;
import com.utils.Views;

public class Employee_warehouse_mapper implements RowMapper<Employee_warehouse> {
	public Employee_warehouse mapRow(ResultSet rs, int rownum) throws SQLException {
	    Employee_warehouse ew = new Employee_warehouse();
	    ew.setId(rs.getInt(Views.COL_EMPLOYEE_WAREHOUSE_ID));
	    ew.setEmployee_Id(rs.getInt(Views.COL_EMPLOYEE_WAREHOUS_EMPLOYEE_ID));
	    ew.setWarehouse_Id(rs.getInt(Views.COL_EMPLOYEE_WAREHOUS_WAREHOUSE_ID));
	    ew.setEmployee_name(rs.getString("Employee_name"));
	    ew.setWarehouse_name(rs.getString("Warehouse_name"));
	    return ew;
	}


}
