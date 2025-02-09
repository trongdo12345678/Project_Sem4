package com.models;

import java.time.LocalDate;

public class Customer {
	private int Id;
	private String Email;
	private LocalDate Creation_time;
	private LocalDate BirthDay;
	private String Password;
	private String Address;
	private String First_name;
	private String Last_name;

	public Customer(int id, String email, LocalDate creation_time, LocalDate birthDay,
			String password, String address, String first_name, String last_name) {
		super();
		Id = id;
		Email = email;
		Creation_time = creation_time;
		BirthDay = birthDay;
		Password = password;
		Address = address;
		First_name = first_name;
		Last_name = last_name;
		
	}
	public Customer() {

	}
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public LocalDate getCreation_time() {
		return Creation_time;
	}
	public void setCreation_time(LocalDate creation_time) {
		Creation_time = creation_time;
	}
	public LocalDate getBirthDay() {
		return BirthDay;
	}
	public void setBirthDay(LocalDate birthDay) {
		BirthDay = birthDay;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public String getFirst_name() {
		return First_name;
	}
	public void setFirst_name(String first_name) {
		First_name = first_name;
	}
	public String getLast_name() {
		return Last_name;
	}
	public void setLast_name(String last_name) {
		Last_name = last_name;
	}
	
}
