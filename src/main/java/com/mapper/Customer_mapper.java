package com.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

import com.models.Customer;
import com.utils.Views;

public class Customer_mapper implements RowMapper<Customer> {
	public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
		Customer item = new Customer();

		// Mapping string fields
		item.setAddress(rs.getString(Views.COL_CUSTOMER_ADDRESS));
		item.setFirst_name(rs.getString(Views.COL_CUSTOMER_FIRST_NAME));
		item.setLast_name(rs.getString(Views.COL_CUSTOMER_LAST_NAME));
		item.setPassword(rs.getString(Views.COL_CUSTOMER_PASSWORD));
		item.setEmail(rs.getString(Views.COL_CUSTOMER_EMAIL));

		// Mapping Timestamp fields to LocalDate
		Timestamp timestampBirthDay = rs.getTimestamp(Views.COL_CUSTOMER_BIRTHOFDATE);
		if (timestampBirthDay != null) {
			item.setBirthDay(timestampBirthDay.toLocalDateTime().toLocalDate());
		}

		Timestamp timestampCreationTime = rs.getTimestamp(Views.COL_CUSTOMER_CREATION_TIME);
		if (timestampCreationTime != null) {
			item.setCreation_time(timestampCreationTime.toLocalDateTime().toLocalDate());
		}

		// Mapping integer fields
		item.setId(rs.getInt(Views.COL_CUSTOMER_ID));

		return item;
	}

}
