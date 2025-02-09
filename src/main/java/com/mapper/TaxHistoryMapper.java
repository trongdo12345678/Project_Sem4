package com.mapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.models.TaxHistory;
import com.utils.Views;

public class TaxHistoryMapper implements RowMapper<TaxHistory> {
    @Override
    public TaxHistory mapRow(ResultSet rs, int rowNum) throws SQLException {
        TaxHistory tax = new TaxHistory();
        tax.setId(rs.getInt(Views.COL_TAX_HISTORY_ID));
        tax.setTaxType(rs.getString(Views.COL_TAX_HISTORY_TYPE));
        tax.setPeriodStart(rs.getDate(Views.COL_TAX_HISTORY_PERIOD_START).toLocalDate());
        tax.setPeriodEnd(rs.getDate(Views.COL_TAX_HISTORY_PERIOD_END).toLocalDate());
        tax.setAmount(rs.getDouble(Views.COL_TAX_HISTORY_REVENUE));
        tax.setTaxRate(rs.getDouble(Views.COL_TAX_HISTORY_RATE));
        tax.setTaxAmount(rs.getDouble(Views.COL_TAX_HISTORY_AMOUNT));
        tax.setPaymentStatus(rs.getString(Views.COL_TAX_HISTORY_STATUS));
        
        Date paymentDate = rs.getDate(Views.COL_TAX_HISTORY_PAYMENT_DATE);
        if (paymentDate != null) {
            tax.setPaymentDate(paymentDate.toLocalDate());
        }
        
        tax.setNote(rs.getString(Views.COL_TAX_HISTORY_NOTE));
        tax.setCreatedAt(rs.getTimestamp(Views.COL_TAX_HISTORY_CREATED_AT).toLocalDateTime());
        tax.setCreatedBy(rs.getInt(Views.COL_TAX_HISTORY_CREATED_BY));
        
        return tax;
    }
}