package com.models;

public class Request_detail {
	private int Id;
	private String status;
	private int Id_product;
	private int Quantity_requested;
	private int Quantity_exported;
	private int Request_id;
	private String ProductName;
	private String Unit_name;
	
	public Request_detail(int id, int idProduct, String status, int quantity_requested, int quantity_exported, int request_id) {
		super();
		Id = id;
		this.status = status;
		this.Id_product = idProduct;
		Quantity_requested = quantity_requested;
		Quantity_exported = quantity_exported;
		Request_id = request_id;
	}

	public Request_detail() {}
	
	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getId_product() {
		return Id_product;
	}

	public void setId_product(int id_product) {
		Id_product = id_product;
	}

	public int getQuantity_requested() {
		return Quantity_requested;
	}

	public void setQuantity_requested(int quantity_requested) {
		Quantity_requested = quantity_requested;
	}

	public int getQuantity_exported() {
		return Quantity_exported;
	}

	public void setQuantity_exported(int quantity_exported) {
		Quantity_exported = quantity_exported;
	}

	public int getRequest_id() {
		return Request_id;
	}

	public void setRequest_id(int request_id) {
		Request_id = request_id;
	}
	
	public String getProductName() {
		return ProductName;
	}
	
	public void setProductName(String ProductName) {
		this.ProductName = ProductName;
	}
	
	public String getUnit_name() {
		return Unit_name;
	}
	
	public void setUnit_name(String unit_name) {
		this.Unit_name = unit_name;
	}

}

