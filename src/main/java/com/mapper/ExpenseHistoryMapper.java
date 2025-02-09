package com.mapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

import com.models.ExpenseHistory;
import com.utils.Views;

public class ExpenseHistoryMapper implements RowMapper<ExpenseHistory> {
    @Override
    public ExpenseHistory mapRow(ResultSet rs, int rowNum) throws SQLException {
        ExpenseHistory history = new ExpenseHistory();
        history.setId(rs.getInt(Views.COL_EXPENSE_HISTORY_ID));
        history.setExpenseTypeId(rs.getInt(Views.COL_EXPENSE_HISTORY_TYPE_ID));
        history.setAmount(rs.getDouble(Views.COL_EXPENSE_HISTORY_AMOUNT));
        history.setStartDate(rs.getDate(Views.COL_EXPENSE_HISTORY_START_DATE).toLocalDate());
        
        Date endDate = rs.getDate(Views.COL_EXPENSE_HISTORY_END_DATE);
        if (endDate != null) {
            history.setEndDate(endDate.toLocalDate());
        }
        
        history.setNote(rs.getString(Views.COL_EXPENSE_HISTORY_NOTE));
        history.setCreatedBy(rs.getInt(Views.COL_EXPENSE_HISTORY_CREATED_BY));
        history.setCreatedAt(rs.getTimestamp(Views.COL_EXPENSE_HISTORY_CREATED_AT).toLocalDateTime());
        
        Timestamp updatedAt = rs.getTimestamp(Views.COL_EXPENSE_HISTORY_UPDATED_AT);
        if (updatedAt != null) {
            history.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return history;
    }
}