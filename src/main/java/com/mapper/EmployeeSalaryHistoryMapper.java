package com.mapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

import com.models.EmployeeSalaryHistory;
import com.utils.Views;

public class EmployeeSalaryHistoryMapper implements RowMapper<EmployeeSalaryHistory> {
	@Override
	public EmployeeSalaryHistory mapRow(ResultSet rs, int rowNum) throws SQLException {
		EmployeeSalaryHistory history = new EmployeeSalaryHistory();

		history.setSalaryTypeId(rs.getInt(Views.COL_SALARY_HISTORY_TYPE_ID));
		history.setId(rs.getObject(Views.COL_SALARY_HISTORY_ID) != null ? rs.getInt(Views.COL_SALARY_HISTORY_ID) : 0);
		history.setEmployeeId(rs.getObject(Views.COL_SALARY_HISTORY_EMPLOYEE_ID) != null
				? rs.getInt(Views.COL_SALARY_HISTORY_EMPLOYEE_ID)
				: 0);
		history.setAmount(
				rs.getObject(Views.COL_SALARY_HISTORY_AMOUNT) != null ? rs.getDouble(Views.COL_SALARY_HISTORY_AMOUNT)
						: 0.0);
		Date startDate = rs.getDate(Views.COL_SALARY_HISTORY_START_DATE);
		if (startDate != null) {
			history.setStartDate(startDate.toLocalDate());
		}
		Date endDate = rs.getDate(Views.COL_SALARY_HISTORY_END_DATE);
		if (endDate != null) {
			history.setEndDate(endDate.toLocalDate());
		}
		history.setNote(rs.getString(Views.COL_SALARY_HISTORY_NOTE));
		history.setCreatedBy(rs.getObject(Views.COL_SALARY_HISTORY_CREATED_BY) != null
				? rs.getInt(Views.COL_SALARY_HISTORY_CREATED_BY)
				: 0);
		Timestamp createdAt = rs.getTimestamp(Views.COL_SALARY_HISTORY_CREATED_AT);
		if (createdAt != null) {
			history.setCreatedAt(createdAt.toLocalDateTime());
		}
		return history;
	}
}