package com.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

import com.models.ExpenseType;
import com.utils.Views;

public class ExpenseTypeMapper implements RowMapper<ExpenseType> {
	@Override
	public ExpenseType mapRow(ResultSet rs, int rowNum) throws SQLException {
		ExpenseType type = new ExpenseType();
		type.setId(rs.getInt(Views.COL_EXPENSE_TYPE_ID));
		type.setName(rs.getString(Views.COL_EXPENSE_TYPE_NAME));
		type.setDescription(rs.getString(Views.COL_EXPENSE_TYPE_DESC));
		type.setIsFixed(rs.getString(Views.COL_EXPENSE_TYPE_IS_FIXED));
		type.setIsActive(rs.getString(Views.COL_EXPENSE_TYPE_IS_ACTIVE));
		type.setCreatedAt(rs.getTimestamp(Views.COL_EXPENSE_TYPE_CREATED_AT).toLocalDateTime());

		Timestamp updatedAt = rs.getTimestamp(Views.COL_EXPENSE_TYPE_UPDATED_AT);
		if (updatedAt != null) {
			type.setUpdatedAt(updatedAt.toLocalDateTime());
		}

		return type;
	}
}