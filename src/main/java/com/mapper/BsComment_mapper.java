package com.mapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.jdbc.core.RowMapper;

import com.models.Brand;
import com.models.Comment;
import com.utils.Views;

public class BsComment_mapper implements RowMapper<Comment> {
	@Override
	public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
		Comment comment = new Comment();
		comment.setId(rs.getInt(Views.COL_COMMENT_ID));
		comment.setContent(rs.getString(Views.COL_COMMENT_CONTENT));
		comment.setProductId(rs.getInt(Views.COL_COMMENT_PRODUCT_ID));
		comment.setCustomerId(rs.getInt(Views.COL_COMMENT_CUSTOMER_ID));
		comment.setEmployeeId(rs.getInt(Views.COL_COMMENT_STAFF_ID));
		comment.setParentId(rs.getInt(Views.COL_COMMENT_PARENT_ID));
		comment.setStatus(rs.getString(Views.COL_COMMENT_STATUS));

		Date createdDate = rs.getDate(Views.COL_COMMENT_CREATED_AT);
		if (createdDate != null) {
			comment.setCreatedAt(createdDate.toLocalDate());
		}

		Date updatedDate = rs.getDate(Views.COL_COMMENT_UPDATED_AT);
		if (updatedDate != null) {
			comment.setUpdatedAt(updatedDate.toLocalDate());
		}

		comment.setCustomerFirstName(rs.getString("customer_firstname"));
		comment.setCustomerLastName(rs.getString("customer_lastname"));
		comment.setEmployeeFirstName(rs.getString("employee_firstname"));
		comment.setEmployeeLastName(rs.getString("employee_lastname"));
		comment.setProductName(rs.getString("product_name"));
		comment.setProductImage(rs.getString("product_image"));
		comment.setReplies(new ArrayList<>());
		return comment;
	}
}