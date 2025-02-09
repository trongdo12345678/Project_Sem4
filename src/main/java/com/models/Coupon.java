package com.models;

import java.time.LocalDate;

public class Coupon {
	private int Id;
	private String Code;
	private Double DiscountPercentage;
	private Double DiscountAmount;
	private LocalDate ExpiryDate;
	private Double MinOrderValue;
	private Double MaxDiscountAmount;
	private String Status;
	private int Employee_Id;
	private Employee Employee;
	
	// Constructors
	public Coupon() {
	}

	public Coupon(int id, String code, Double discountPercentage, Double discountAmount, LocalDate expiryDate,
			Double minOrderValue, Double maxDiscountAmount, String status) {
		super();
		Id = id;
		Code = code;
		DiscountPercentage = discountPercentage;
		DiscountAmount = discountAmount;
		ExpiryDate = expiryDate;
		MinOrderValue = minOrderValue;
		MaxDiscountAmount = maxDiscountAmount;
		Status = status;
	}

	public int getEmployee_Id() {
		return Employee_Id;
	}

	public void setEmployee_Id(int employee_Id) {
		Employee_Id = employee_Id;
	}

	public Employee getEmployee() {
		return Employee;
	}

	public void setEmployee(Employee employee) {
		Employee = employee;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getCode() {
		return Code;
	}

	public void setCode(String code) {
		Code = code;
	}

	public Double getDiscountPercentage() {
		return DiscountPercentage;
	}

	public void setDiscountPercentage(Double discountPercentage) {
		DiscountPercentage = discountPercentage;
	}

	public Double getDiscountAmount() {
		return DiscountAmount;
	}

	public void setDiscountAmount(Double discountAmount) {
		DiscountAmount = discountAmount;
	}

	public LocalDate getExpiryDate() {
		return ExpiryDate;
	}

	public void setExpiryDate(LocalDate expiryDate) {
		ExpiryDate = expiryDate;
	}

	public Double getMinOrderValue() {
		return MinOrderValue;
	}

	public void setMinOrderValue(Double minOrderValue) {
		MinOrderValue = minOrderValue;
	}

	public Double getMaxDiscountAmount() {
		return MaxDiscountAmount;
	}

	public void setMaxDiscountAmount(Double maxDiscountAmount) {
		MaxDiscountAmount = maxDiscountAmount;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

}
