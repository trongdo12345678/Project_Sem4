package com.models;

import java.time.LocalDateTime;


public class Warehouse_releasenote {
	private int Id;
	private String Name;
	private LocalDateTime Date;
	private String Status;
	private int Order_id;
	private int Employee_Id;
	private int Warehouse_Id;
	private int Request_id;
	
	public Warehouse_releasenote(int id, int warehouse_id,  int employeeId, String name, String status, LocalDateTime date, int order_id, int request_id) {
		super();
		Id = id;
		Name = name;
		Employee_Id = employeeId;
		Warehouse_Id = warehouse_id;
		Date = date;
		Request_id = request_id;
		Status = status;
		Order_id = order_id;
	}
	
	public Warehouse_releasenote() {
	}
	
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
	public String getStatusWr() {
		return Status;
	}
	public void setStatusWr(String status) {
		this.Status = status;
	}

	public int getOrder_id() {
		return Order_id;
	}
	public void setOrder_id(int order_id) {
		Order_id = order_id;
	}
	
	public int getEmployee_Id() {
		return Employee_Id;
	}
	public void setEmployee_Id(int employeeid) {
		Employee_Id = employeeid;
	}
	
	public int getWarehouse_id() {
		return Warehouse_Id;
	}
	
	public void setWarehouse_id(int warehoise_id) {
		this.Warehouse_Id = warehoise_id;
	}
	
	public int getRequest_id() {
		return Request_id;
	}
	
	public void setRequest_id(int request_id) {
		this.Request_id = request_id;
	}
}
