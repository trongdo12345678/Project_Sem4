package com.models;

public class Order_detail {
	private int Id;
	private int Stock_id;
	private int Order_id;
	private String Status;
	private Double Price;
	private int Product_Id;
	private int Quantity;
	private String Product_name;
	private String Img;
	private String Unit_name;	
	public Order_detail(int id, int stock_id, int order_id, String status, Double price, int product_Id, int quantity,
			String product_name, String img) {
		super();
		Id = id;
		Stock_id = stock_id;
		Order_id = order_id;
		Status = status;
		Price = price;
		Product_Id = product_Id;
		Quantity = quantity;
		Product_name = product_name;
		Img = img;
	}

	public Order_detail() {
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public int getStock_id() {
		return Stock_id;
	}

	public void setStock_id(int stock_id) {
		Stock_id = stock_id;
	}

	public int getOrder_id() {
		return Order_id;
	}

	public void setOrder_id(int order_id) {
		Order_id = order_id;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public Double getPrice() {
		return Price;
	}

	public void setPrice(Double price) {
		Price = price;
	}

	public int getProduct_Id() {
		return Product_Id;
	}

	public void setProduct_Id(int product_Id) {
		Product_Id = product_Id;
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

	public String getImg() {
		return Img;
	}

	public void setImg(String img) {
		Img = img;
	}
	
	public String getUnit_name() {
		return Unit_name;
	}
	public void setUnit_name(String unit_name) {
		this.Unit_name = unit_name;
	}
	
}

