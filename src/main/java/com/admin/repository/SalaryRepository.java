package com.admin.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.models.Coupon;
import com.models.Employee;
import com.models.EmployeeSalaryHistory;
import com.models.SalaryType;
import com.mapper.*;
import com.utils.Views;

@Repository
public class SalaryRepository {
    @Autowired
    private JdbcTemplate db;
    @Autowired
    EmployeeRepository repem;

    public List<SalaryType> findAllTypes() {
        try {
            String sql = String.format("SELECT * FROM %s ORDER BY %s",
                    Views.TBL_SALARY_TYPES,
                    Views.COL_SALARY_TYPE_ID);
                    
            return db.query(sql, new SalaryTypeMapper());
        } catch (DataAccessException e) {
            System.err.println("Error in findAllTypes: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public SalaryType findTypeById(int id) {
        try {
            String sql = String.format("SELECT * FROM %s WHERE %s = ?",
                    Views.TBL_SALARY_TYPES,
                    Views.COL_SALARY_TYPE_ID);
                    
            return db.queryForObject(sql, new SalaryTypeMapper(), id);
        } catch (DataAccessException e) {
            System.err.println("Error in findTypeById: " + e.getMessage());
            return null;
        }
    }

    public boolean saveType(SalaryType type) {
        try {
            String sql = String.format(
                "INSERT INTO %s (%s, %s, %s) VALUES (?, ?, ?)",
                Views.TBL_SALARY_TYPES,
                Views.COL_SALARY_TYPE_NAME,
                Views.COL_SALARY_TYPE_DESC,
                Views.COL_SALARY_TYPE_IS_ACTIVE);

            return db.update(sql,
                    type.getName(),
                    type.getDescription(),
                    type.getIsActive()) > 0;
        } catch (DataAccessException e) {
            System.err.println("Error in saveType: " + e.getMessage());
            return false;
        }
    }

    public boolean updateType(SalaryType type) {
        try {
            String sql = String.format(
                "UPDATE %s SET %s = ?, %s = ?, %s = ? WHERE %s = ?",
                Views.TBL_SALARY_TYPES,
                Views.COL_SALARY_TYPE_NAME,
                Views.COL_SALARY_TYPE_DESC,
                Views.COL_SALARY_TYPE_IS_ACTIVE,
                Views.COL_SALARY_TYPE_ID);

            return db.update(sql,
                    type.getName(),
                    type.getDescription(),
                    type.getIsActive(),
                    type.getId()) > 0;
        } catch (DataAccessException e) {
            System.err.println("Error in updateType: " + e.getMessage());
            return false;
        }
    }
    
    public List<EmployeeSalaryHistory> getCurrentSalaries(int employeeId) {
        try {
            String sql = String.format(
                "SELECT ISNULL(h.%s, t.%s) as %s, h.* " +
                "FROM %s t " +
                "LEFT JOIN %s h ON t.%s = h.%s " +
                    "AND h.%s = ? " +
                    "AND h.%s IS NULL " +
                "ORDER BY t.%s",
                Views.COL_SALARY_HISTORY_TYPE_ID,
                Views.COL_SALARY_TYPE_ID,
                Views.COL_SALARY_HISTORY_TYPE_ID,
                Views.TBL_SALARY_TYPES,
                Views.TBL_EMPLOYEE_SALARY_HISTORY,
                Views.COL_SALARY_TYPE_ID,
                Views.COL_SALARY_HISTORY_TYPE_ID,
                Views.COL_SALARY_HISTORY_EMPLOYEE_ID,
                Views.COL_SALARY_HISTORY_END_DATE,
                Views.COL_SALARY_TYPE_ID
            );

            List<EmployeeSalaryHistory> histories = db.query(sql, new EmployeeSalaryHistoryMapper(), employeeId);
            
            // Gán thông tin bổ sung cho mỗi history
            for (EmployeeSalaryHistory history : histories) {
                // Gán SalaryType
                if (history.getSalaryTypeId() > 0) {
                    SalaryType type = findTypeById(history.getSalaryTypeId());
                    if (type != null) {
                        history.setSalaryType(type);
                    }
                }
                
                // Gán Employee
                if (history.getEmployeeId() > 0) {
                    Employee emp = repem.findId(history.getEmployeeId());
                    if (emp != null) {
                        history.setEmployee(emp);
                    }
                }
                
                // Gán CreatedByEmployee
                if (history.getCreatedBy() > 0) {
                    Employee creator = repem.findId(history.getCreatedBy());
                    if (creator != null) {
                        history.setCreatedByEmployee(creator);
                    }
                }
            }
            
            return histories;
            
        } catch (DataAccessException e) {
            System.err.println("Error in getCurrentSalaries: " + e.getMessage());
            return Collections.emptyList();
        }
    }
    public boolean updateSalary(int employeeId, int salaryTypeId, double newAmount, int createdBy) {
        try {
            String selectSql = String.format(
                "SELECT * FROM %s WHERE %s = ? AND %s = ? AND %s IS NULL",
                Views.TBL_EMPLOYEE_SALARY_HISTORY,
                Views.COL_SALARY_HISTORY_EMPLOYEE_ID,
                Views.COL_SALARY_HISTORY_TYPE_ID,
                Views.COL_SALARY_HISTORY_END_DATE
            );

            EmployeeSalaryHistory currentHistory = null;
            try {
                currentHistory = db.queryForObject(selectSql, new EmployeeSalaryHistoryMapper(), employeeId, salaryTypeId);
            } catch (EmptyResultDataAccessException e) {
                // Không có bản ghi hiện tại - thêm mới luôn
                String insertSql = String.format(
                    "INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    Views.TBL_EMPLOYEE_SALARY_HISTORY,
                    Views.COL_SALARY_HISTORY_EMPLOYEE_ID,
                    Views.COL_SALARY_HISTORY_TYPE_ID,
                    Views.COL_SALARY_HISTORY_AMOUNT,
                    Views.COL_SALARY_HISTORY_START_DATE,
                    Views.COL_SALARY_HISTORY_END_DATE,
                    Views.COL_SALARY_HISTORY_CREATED_BY,
                    Views.COL_SALARY_HISTORY_CREATED_AT
                );

                db.update(insertSql, 
                    employeeId, 
                    salaryTypeId, 
                    newAmount, 
                    LocalDate.now(), 
                    null, 
                    createdBy,  
                    LocalDateTime.now()
                );
                return true;
            }

            if (currentHistory.getAmount() != newAmount) {
                String updateSql = String.format(
                    "UPDATE %s SET %s = ? WHERE %s = ? AND %s = ? AND %s IS NULL",
                    Views.TBL_EMPLOYEE_SALARY_HISTORY,
                    Views.COL_SALARY_HISTORY_END_DATE,
                    Views.COL_SALARY_HISTORY_EMPLOYEE_ID,
                    Views.COL_SALARY_HISTORY_TYPE_ID,
                    Views.COL_SALARY_HISTORY_END_DATE
                );

                db.update(updateSql, LocalDate.now(), employeeId, salaryTypeId);

                // Thêm bản ghi mới
                String insertSql = String.format(
                    "INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    Views.TBL_EMPLOYEE_SALARY_HISTORY,
                    Views.COL_SALARY_HISTORY_EMPLOYEE_ID,
                    Views.COL_SALARY_HISTORY_TYPE_ID,
                    Views.COL_SALARY_HISTORY_AMOUNT,
                    Views.COL_SALARY_HISTORY_START_DATE,
                    Views.COL_SALARY_HISTORY_END_DATE,
                    Views.COL_SALARY_HISTORY_CREATED_BY,
                    Views.COL_SALARY_HISTORY_CREATED_AT
                );

                db.update(insertSql, 
                    employeeId, 
                    salaryTypeId, 
                    newAmount, 
                    LocalDate.now(), 
                    null, 
                    createdBy,  
                    LocalDateTime.now()
                );
                return true;
            }

            return false;
        } catch (DataAccessException e) {
            System.err.println("Error in updateSalary: " + e.getMessage());
            return false;
        }
    }
    
    
    
}