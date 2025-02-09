package com.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;


import com.models.Feedback;
import com.utils.Views;

public class FeedbackWithCustomerMapper implements RowMapper<Feedback> {
    @Override
    public Feedback mapRow(ResultSet rs, int rowNum) throws SQLException {
        Feedback feedback = new Feedback();
        
        feedback.setId(rs.getInt(Views.COL_FEEDBACK_ID));
        feedback.setProduct_Id(rs.getInt(Views.COL_FEEDBACK_PROID));
        feedback.setOrderDetail_Id(rs.getInt(Views.COL_FEEDBACK_ORDID));
        feedback.setRating(rs.getInt(Views.COL_FEEDBACK_RATE));
        feedback.setComment(rs.getString(Views.COL_FEEDBACK_COMMENT));
        
        Timestamp timestamp = rs.getTimestamp(Views.COL_FEEDBACK_CREATEDATE);
        if (timestamp != null) {
            feedback.setCreated_Date(timestamp.toLocalDateTime().toLocalDate());
        }
        
        feedback.setStatus(rs.getString(Views.COL_FEEDBACK_STATUS));
        
        feedback.setCustomerName(rs.getString("CustomerName"));
        
        return feedback;
    }
}
