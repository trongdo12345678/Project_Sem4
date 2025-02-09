package com.models;

import java.time.LocalDate;

public class Feedback {
	private int Id;
	private int Product_Id;
	private int OrderDetail_Id;
	private int Rating;
	private String Comment;
	private LocalDate Created_Date;
	private String Status;
	private String customerName;     
	
	public Feedback(int id, int product_Id, int orderDetail_Id, int rating, String comment, LocalDate created_Date,
			String status, String customerName) {
		super();
		Id = id;
		Product_Id = product_Id;
		OrderDetail_Id = orderDetail_Id;
		Rating = rating;
		Comment = comment;
		Created_Date = created_Date;
		Status = status;
		this.customerName = customerName;
	}
	public Feedback() {}
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public int getProduct_Id() {
		return Product_Id;
	}
	public void setProduct_Id(int product_Id) {
		Product_Id = product_Id;
	}
	public int getOrderDetail_Id() {
		return OrderDetail_Id;
	}
	public void setOrderDetail_Id(int orderDetail_Id) {
		OrderDetail_Id = orderDetail_Id;
	}
	public int getRating() {
		return Rating;
	}
	public void setRating(int rating) {
		Rating = rating;
	}
	public String getComment() {
		return Comment;
	}
	public void setComment(String comment) {
		Comment = comment;
	}
	public LocalDate getCreated_Date() {
		return Created_Date;
	}
	public void setCreated_Date(LocalDate created_Date) {
		Created_Date = created_Date;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	
}
