package com.models;

import java.sql.Date;
import java.util.List;

public class Stock {
	private int Id;
	private int Product_id;
	private String product_name;
	private int Wh_rc_dt_id_quantity;
	private int Quantity;
	private String Status;
	private int Wh_rc_dt_id;
	private List<ConversionShow> listconversion;
	private String ProductName;
	private Double WhPrice;
	private String UnitName;
	private Double ShippingFee;
	private Date Date;
	public Stock(int id, int product_id, int quantity, int wh_rc_dt_id, String status) {
		super();
		Id = id;
		Product_id = product_id;
		Quantity = quantity;
		Wh_rc_dt_id = wh_rc_dt_id;
		Status = status;
	}
	public Stock() {
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
	public int getQuantity() {
		return Quantity;
	}
	public void setQuantity(int quantity) {
		Quantity = quantity;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		this.Status = status;
	}
	public int getWh_rc_dt_id() {
		return Wh_rc_dt_id;
	}
	public void setWh_rc_dt_id(int wh_rc_dt_id) {
		Wh_rc_dt_id = wh_rc_dt_id;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public int getWh_rc_dt_id_quantity() {
		return Wh_rc_dt_id_quantity;
	}
	public void setWh_rc_dt_id_quantity(int wh_rc_dt_id_quantity) {
		Wh_rc_dt_id_quantity = wh_rc_dt_id_quantity;
	}
	public List<ConversionShow> getListconversion() {
		return listconversion;
	}
	public void setListconversion(List<ConversionShow> listconversion) {
		this.listconversion = listconversion;
	}
	public String getProductName() {
		return ProductName;
	}
	public void setProductName(String productName) {
		ProductName = productName;
	}
	public Double getWhPrice() {
		return WhPrice;
	}
	public void setWhPrice(Double whPrice) {
		WhPrice = whPrice;
	}
	public String getUnitName() {
		return UnitName;
	}
	public void setUnitName(String unitName) {
		UnitName = unitName;
	}
	public Double getShippingFee() {
		return ShippingFee;
	}
	public void setShippingFee(Double shippingFee) {
		ShippingFee = shippingFee;
	}
	public Date getDate() {
		return Date;
	}
	public void setDate(Date date) {
		Date = date;
	}
	
}
