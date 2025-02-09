package com.warehouseManager.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.models.Employee;
import com.utils.Views;

@Repository
public class inforEmpWarehouse {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Employee findId(int employeeId) {
		try {
			String sql = """
					SELECT e.Id AS Id, e.First_name AS first_name, e.Last_name AS last_name, e.Phone AS phone,
							w.Name AS warehouseName, r.Name AS roleName, e.Password AS password
					FROM Employee e
					JOIN Role r ON e.Role_Id = r.Id
					JOIN employee_warehouse ew ON e.Id = ew.Employee_Id
					JOIN Warehouse w ON ew.Warehouse_Id = w.Id
					WHERE ew.Employee_Id = ?;
					       		""";

			return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
				Employee emp = new Employee();
				emp.setId(rs.getInt("Id"));
				emp.setFirst_name(rs.getString("first_name"));
				emp.setLast_name(rs.getString("last_name"));
				emp.setPhone(rs.getString("phone"));
				emp.setRole_name(rs.getString("roleName"));
				emp.setWarehouse_name(rs.getString("warehouseName"));
				emp.setPassword(rs.getString("password"));
				return emp;
			}, employeeId);

		} catch (DataAccessException e) {
			System.err.println("Error fetching employee with ID: " + employeeId + " - " + e.getMessage());
			return null;
		}
	}
	public Employee findIdBuniss(int employeeId) {
		try {
			String sql = """
					SELECT e.Id AS Id, e.First_name AS first_name, e.Last_name AS last_name, e.Phone AS phone,
							 r.Name AS roleName, e.Password AS password
					FROM Employee e
					JOIN Role r ON e.Role_Id = r.Id										
					WHERE e.Id = ?;
					       		""";

			return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
				Employee emp = new Employee();
				emp.setId(rs.getInt("Id"));
				emp.setFirst_name(rs.getString("first_name"));
				emp.setLast_name(rs.getString("last_name"));
				emp.setPhone(rs.getString("phone"));
				emp.setRole_name(rs.getString("roleName"));
				emp.setPassword(rs.getString("password"));
				return emp;
			}, employeeId);

		} catch (DataAccessException e) {
			System.err.println("Error fetching employee with ID: " + employeeId + " - " + e.getMessage());
			return null;
		}
	}
	public void updateEmployee(Employee emp) {
	    String sql = "UPDATE Employee SET First_name = ?, Last_name = ? WHERE Id = ?";
	    jdbcTemplate.update(sql, emp.getFirst_name(), emp.getLast_name(), emp.getId());
	}
	


	public void updatePassword(Employee emp) {
		try {
			String sql = "UPDATE " + Views.TBL_EMPLOYEE + " SET " + Views.COL_EMPLOYEE_PASSWORD + " = ? " + "WHERE "
					+ Views.COL_EMPLOYEE_ID + " = ?";
			jdbcTemplate.update(sql, emp.getPassword(), emp.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
