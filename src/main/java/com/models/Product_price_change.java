package com.models;

import java.time.LocalDateTime;

public class Product_price_change {
	private int Id;
	private int Product_id;
	private double Price;
	private LocalDateTime Date_start;
	private LocalDateTime Date_end;
	public Product_price_change() {
	}
	public Product_price_change(int id, int product_id, double price, LocalDateTime date_start,
			LocalDateTime date_end) {
		super();
		Id = id;
		Product_id = product_id;
		Price = price;
		Date_start = date_start;
		Date_end = date_end;
	}
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public int getProduct_id() {
		return Product_id;
	}
	public void setProduct_id(int product_id) {
		Product_id = product_id;
	}
	public double getPrice() {
		return Price;
	}
	public void setPrice(double price) {
		Price = price;
	}
	public LocalDateTime getDate_start() {
		return Date_start;
	}
	public void setDate_start(LocalDateTime date_start) {
		Date_start = date_start;
	}
	public LocalDateTime getDate_end() {
		return Date_end;
	}
	public void setDate_end(LocalDateTime date_end) {
		Date_end = date_end;
	}
	
}
