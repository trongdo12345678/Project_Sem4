package com.models;

public class Warehouse_receipt_detail {
	private int Id;
	private int Wh_receipt_id;
	private double Wh_price;
	private int Product_id;
	private int Conversion_id;
	private int Quantity;
	private String Status;
	private String Product_name;
	private String formattedPrice;
	private int Conversion_rate;
	private String toUnitName;
	private String fromUnitName;

	public Warehouse_receipt_detail(int id, int wh_receipt_id, double wh_price, int quantity,String status,int product_id) {
		Id = id;
		Wh_receipt_id = wh_receipt_id;
		Product_id = product_id;
		Status = status;
		Wh_price = wh_price;
		Product_id = product_id;
		Quantity = quantity;
	}
	public Warehouse_receipt_detail() {
	}
	
	public int getConversion_id() {
		return Conversion_id;
	}
	public void setConversion_id(int conversion_id) {
		Conversion_id = conversion_id;
	}
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public int getWh_receipt_id() {
		return Wh_receipt_id;
	}
	public void setWh_receipt_id(int wh_receipt_id) {
		Wh_receipt_id = wh_receipt_id;
	}
	public double getWh_price() {
		return Wh_price;
	}
	public void setWh_price(double wh_price) {
		Wh_price = wh_price;
	}
	public int getQuantity() {
		return Quantity;
	}
	public void setQuantity(int quantity) {
		Quantity = quantity;
	}
	public String getFormattedPrice() {
		return formattedPrice;
	}
	public void setFormattedPrice(String formattedPrice) {
		this.formattedPrice = formattedPrice;
	}
	public int getProduct_id() { 
		return Product_id;
	}
	public void setProduct_id(int product_id) {
		Product_id = product_id;
	}

	public String getProduct_name() {
		return Product_name;
	}
	public void setProduct_name(String product_name) {
		Product_name = product_name;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public int getConversion_rate() {
		return Conversion_rate;
	}
	public void setConversion_rate(int conversion_rate) {
		Conversion_rate = conversion_rate;
	}
	public String getToUnitName() {
		return toUnitName;
	}
	public void setToUnitName(String toUnitName) {
		this.toUnitName = toUnitName;
	}
	public String getFromUnitName() {
		return fromUnitName;
	}
	public void setFromUnitName(String fromUnitName) {
		this.fromUnitName = fromUnitName;
	}

}