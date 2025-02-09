package com.models;

public class Conversion {
	private int Id;
	private int Product_id;
	private int From_unit_id;
	private int To_unit_id;
	private int Conversion_rate;
	private String FromUnitName;
	private String ToUnitName;
	private String ProductName;
	public Conversion(int id, int product_id, int from_unit_id, int to_unit_id, int conversion_rate) {
		super();
		Id = id;
		Product_id = product_id;
		From_unit_id = from_unit_id;
		To_unit_id = to_unit_id;
		Conversion_rate = conversion_rate;
		Product_id = product_id;
	}
	public Conversion() {
	}
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public int getFrom_unit_id() {
		return From_unit_id;
	}
	public void setFrom_unit_id(int from_unit_id) {
		From_unit_id = from_unit_id;
	}
	public int getTo_unit_id() {
		return To_unit_id;
	}
	public void setTo_unit_id(int to_unit_id) {
		To_unit_id = to_unit_id;
	}
	public int getConversion_rate() {
		return Conversion_rate;
	}
	public void setConversion_rate(int conversion_rate) {
		Conversion_rate = conversion_rate;
	}
	public String getFromUnitName() {
		return FromUnitName;
	}
	public void setFromUnitName(String fromUnitName) {
		this.FromUnitName = fromUnitName;
	}
	public String getToUnitName() {
		return ToUnitName;
	}
	public void setToUnitName(String toUnitName) {
		this.ToUnitName = toUnitName;
	}
	public String getProductName() {
		return ProductName;
	}
	public void setProductName(String productName) {
		this.ProductName = productName;
	}
	public int getProduct_id() {
		return Product_id;
	}
	public void setProduct_id(int product_id) {
		Product_id = product_id;
	}

}

