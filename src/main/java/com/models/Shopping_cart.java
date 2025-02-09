package com.models;

public class Shopping_cart {
	private int Id;
	private int Customer_id;
	private int Product_id;
	private int Quantity;
	
	private String Product_name;
	private Double Price;
	private String Img;
	private String Pro_Status;

	private int Length;
	private int Height;
	private int Width;
	private int Weight;
	
	public Shopping_cart(int id, int customer_id, int product_id, int quantity, String product_name, Double price,
			String img, String pro_Status) {
		super();
		Id = id;
		Customer_id = customer_id;
		Product_id = product_id;
		Quantity = quantity;
		Product_name = product_name;
		Price = price;
		Img = img;
		Pro_Status = pro_Status;
	}
	public Shopping_cart() {
	}
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public int getCustomer_id() {
		return Customer_id;
	}
	public void setCustomer_id(int customer_id) {
		Customer_id = customer_id;
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
	public String getProduct_name() {
		return Product_name;
	}
	public void setProduct_name(String product_name) {
		Product_name = product_name;
	}
	public Double getPrice() {
		return Price;
	}
	public void setPrice(Double price) {
		Price = price;
	}
	public String getImg() {
		return Img;
	}
	public void setImg(String img) {
		Img = img;
	}
	public String getPro_Status() {
		return Pro_Status;
	}
	public void setPro_Status(String pro_Status) {
		Pro_Status = pro_Status;
	}
	public int getLength() {
		return Length;
	}
	public void setLength(int length) {
		Length = length;
	}
	public int getHeight() {
		return Height;
	}
	public void setHeight(int height) {
		Height = height;
	}
	public int getWidth() {
		return Width;
	}
	public void setWidth(int width) {
		Width = width;
	}
	public int getWeight() {
		return Weight;
	}
	public void setWeight(int weight) {
		Weight = weight;
	}
	
}
