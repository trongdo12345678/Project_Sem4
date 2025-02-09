package com.admin.repository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mapper.Employee_mapper;
import com.models.Employee;
import com.utils.Views;

@Repository
public class LoginEmploy {
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	//find warehouse_id
	public Integer findWarehouseIdByEmployeeId(int employeeId) {
	    String sql = "SELECT Warehouse_Id FROM employee_warehouse WHERE Employee_Id = ?";
	    try {
	        return jdbcTemplate.queryForObject(sql, Integer.class, employeeId);
	    } catch (EmptyResultDataAccessException e) {
	        System.err.println("No Warehouse_Id found for Employee_Id: " + employeeId);
	        return null; 
	    } catch (DataAccessException e) {
	        System.err.println("Database error: " + e.getMessage());
	        return null;
	    }
	}
	
    public Employee login(String uid, String pwd) {
    	 String sql = "SELECT * FROM " + Views.TBL_EMPLOYEE + " WHERE " + Views.COL_EMPLOYEE_PHONE + " = ?";
        
        try {
        	Employee emp = jdbcTemplate.queryForObject(sql, new Employee_mapper(), uid);
        	
        	if (emp != null &&  BCrypt.checkpw(pwd, emp.getPassword())) {
                return emp;
            } else {
                return null;
            }
        } catch (Exception e) {
        	e.printStackTrace();
            return null;
        }
    }    
    
}
