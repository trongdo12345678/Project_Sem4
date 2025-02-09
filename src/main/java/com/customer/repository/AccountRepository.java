package com.customer.repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mapper.Customer_mapper;
import com.models.Customer;
import com.utils.Views;
import org.mindrot.jbcrypt.BCrypt;

@Repository
public class AccountRepository {
	@Autowired
	JdbcTemplate db;

	public boolean createAccount(Customer cus) {
		try {
			// Hash the password using BCrypt
			String hashedPassword = BCrypt.hashpw(cus.getPassword(), BCrypt.gensalt());

			// Define the SQL insert statement for the Customer table
			String sql = String.format("INSERT INTO %s (%s, %s,%s) VALUES (?, ?, ?)", Views.TBL_CUSTOMER,
					Views.COL_CUSTOMER_EMAIL, Views.COL_CUSTOMER_PASSWORD, Views.COL_CUSTOMER_CREATION_TIME);

			LocalDate creationDate = LocalDate.now(); // Get the current date

			// Convert LocalDate to LocalDateTime at the start of the day
			LocalDateTime creationDateTime = creationDate.atStartOfDay();

			// Convert to Timestamp
			Timestamp creationTimestamp = Timestamp.valueOf(creationDateTime);
			// Execute the insert statement and get the number of rows affected
			int rowsAffected = db.update(sql, cus.getEmail(), hashedPassword, creationTimestamp);

			// Return true if the insert was successful (i.e., at least one row was
			// affected)
			return rowsAffected > 0;
		} catch (DataAccessException e) {
			// Print the error to the console for debugging
			System.err.println("Error inserting customer: " + e.getMessage());
			return false; // Return false if there was an error
		}
	}

	public boolean isEmailRegistered(String email) {
		try {
			// Define the SQL select statement to check for existing email
			String sql = String.format("SELECT %s FROM %s WHERE %s = ?", Views.COL_CUSTOMER_EMAIL, Views.TBL_CUSTOMER,
					Views.COL_CUSTOMER_EMAIL);

			// Execute the query and retrieve the result
			List<String> existingEmails = db.queryForList(sql, String.class, email);

			// If the list is not empty, it means the email is already registered
			return !existingEmails.isEmpty(); // Return true if email exists, false otherwise
		} catch (DataAccessException e) {
			// Print the error to the console for debugging
			System.err.println("Error checking email registration: " + e.getMessage());
			return false; // Return false in case of an error
		}
	}

	public Customer login(String email, String password) {
		try {
			// Define the SQL query to retrieve the customer by email
			String sql = String.format("SELECT * FROM %s WHERE %s = ?", Views.TBL_CUSTOMER, Views.COL_CUSTOMER_EMAIL);

			// Fetch the customer details
			@SuppressWarnings("deprecation")
			Customer customer = db.queryForObject(sql, new Object[] { email }, new Customer_mapper());

			// Check if the customer exists and verify the password
			if (customer != null && BCrypt.checkpw(password, customer.getPassword())) {
				return customer; // Return the customer if the password matches
			}
		} catch (DataAccessException e) {

		}
		return null; // Return null if login fails
	}

	public Customer finbyid(int id) {
		try {
			// Define the SQL query to retrieve the customer by email
			String sql = String.format("SELECT * FROM %s WHERE %s = ?", Views.TBL_CUSTOMER, Views.COL_CUSTOMER_ID);

			// Fetch the customer details
			@SuppressWarnings("deprecation")
			Customer customer = db.queryForObject(sql, new Object[] { id }, new Customer_mapper());

			return customer;

		} catch (DataAccessException e) {

		}
		return null; // Return null if login fails
	}

	public boolean updateAccount(Customer cus) {
		try {
			// Define the SQL update statement for the Customer table, excluding email and
			// password
			String sql = String.format("UPDATE %s SET %s = ?, %s = ?, %s = ?, %s = ? WHERE %s = ?", Views.TBL_CUSTOMER,
					Views.COL_CUSTOMER_FIRST_NAME, Views.COL_CUSTOMER_LAST_NAME, Views.COL_CUSTOMER_ADDRESS,
					Views.COL_CUSTOMER_BIRTHOFDATE, Views.COL_CUSTOMER_ID);

			// Execute the update statement and get the number of rows affected
			int rowsAffected = db.update(sql, cus.getFirst_name(), cus.getLast_name(), cus.getAddress(),
					cus.getBirthDay(), cus.getId());

			// Return true if the update was successful (i.e., at least one row was
			// affected)
			return rowsAffected > 0;
		} catch (DataAccessException e) {
			// Print the error to the console for debugging
			System.err.println("Error updating customer: " + e.getMessage());
			return false; // Return false if there was an error
		}
	}

	public boolean verifyPassword(int customerId, String passwordToVerify) {
		try {
			// Define the SQL select statement to get the customer by ID
			String sql = String.format("SELECT %s FROM %s WHERE %s = ?", Views.COL_CUSTOMER_PASSWORD,
					Views.TBL_CUSTOMER, Views.COL_CUSTOMER_ID); // Assuming this is the ID column

			// Execute the query and retrieve the hashed password
			String storedHashedPassword = db.queryForObject(sql, String.class, customerId);

			// Check if the hashed password exists
			if (storedHashedPassword != null) {
				// Compare the stored hashed password with the provided password
				return BCrypt.checkpw(passwordToVerify, storedHashedPassword);
			}
		} catch (DataAccessException e) {
			// Print the error to the console for debugging
			System.err.println("Error verifying password: " + e.getMessage());
		}

		// Return false if there was an error or the user was not found
		return false;
	}

	public boolean updatePassword(int customerId, String newPassword) {
		try {
			// Hash the new password using BCrypt
			String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

			// Define the SQL update statement for the Customer table
			String sql = String.format("UPDATE %s SET %s = ? WHERE id = ?", Views.TBL_CUSTOMER,
					Views.COL_CUSTOMER_PASSWORD);

			// Execute the update statement and get the number of rows affected
			int rowsAffected = db.update(sql, hashedPassword, customerId);

			// Return true if the update was successful (i.e., at least one row was
			// affected)
			return rowsAffected > 0;
		} catch (DataAccessException e) {
			// Print the error to the console for debugging
			System.err.println("Error updating password: " + e.getMessage());
			return false; // Return false if there was an error
		}
	}
}
