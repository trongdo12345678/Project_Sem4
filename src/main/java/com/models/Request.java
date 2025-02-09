package com.models;

import java.time.LocalDateTime;

public class Request {
	private int Id;
	private String Name;
	private LocalDateTime Date;
	private String StatusRequest;
	private int Employee_Id;
	private int Warehouse_Id;
	private String Type;
	private int Order_id;
	

	public Request(int id, String name, LocalDateTime date, String statusRequest, int employee_Id, int warehouse_Id,
			String type, int order_id) {
		super();
		Id = id;
		Name = name;
		Date = date;
		StatusRequest = statusRequest;
		Employee_Id = employee_Id;
		Warehouse_Id = warehouse_Id;
		Type = type;
		Order_id = order_id;
	}
	public Request() {}
	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public LocalDateTime getDate() {
		return Date;
	}

	public void setDate(LocalDateTime date) {
		Date = date;
	}

	public String getStatusRequest() {
		return StatusRequest;
	}

	public void setStatusRequest(String status) {
		StatusRequest = status;
	}

	public int getEmployee_Id() {
		return Employee_Id;
	}

	public void setEmployee_Id(int employee_Id) {
		Employee_Id = employee_Id;
	}

	public int getWarehouse_Id() {
		return Warehouse_Id;
	}

	public void setWarehouse_Id(int warehoise_Id) {
		Warehouse_Id = warehoise_Id;
	}

	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}
	public int getOrder_id() {
		return Order_id;
	}
	public void setOrder_id(int order_id) {
		Order_id = order_id;
	}
	
}




