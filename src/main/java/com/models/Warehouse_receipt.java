package com.models;

import java.time.LocalDateTime;

public class Warehouse_receipt {
	private int Id;
	private String Name;
	private int Wh_id;
	private String Wh_name;
	private String Status;
	private int Employee_id;
	private String Employee_name;
    private LocalDateTime Date;
    private Double Shipping_fee;
	private Double Other_fee;
    private Double Total_fee;
    
	public Warehouse_receipt(int id, String name, int wh_id, String wh_name, LocalDateTime date, Double shipping_fee,
			Double other_fee, Double total_fee, String status,int employee_id) {
		Id = id;
		Name = name;
		Wh_id = wh_id;
		Wh_name = wh_name;
		Date = date;
		Shipping_fee = shipping_fee;
		Other_fee = other_fee;
		Total_fee = total_fee;
		Status = status;
		Employee_id = employee_id;
	}
	public Warehouse_receipt() {
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
	public int getWh_id() {
		return Wh_id;
	}
	public void setWh_id(int wh_id) {
		Wh_id = wh_id;
	}
	public LocalDateTime getDate() {
		return Date;
	}
	public void setDate(LocalDateTime date) {
		Date = date;
	}
	public String getWh_name() {
		return Wh_name;
	}
	public void setWh_name(String wh_name) {
		Wh_name = wh_name;
	}
	public Double getShipping_fee() {
		return Shipping_fee;
	}
	public void setShipping_fee(Double shipping_fee) {
		Shipping_fee = shipping_fee;
	}
	public Double getOther_fee() {
		return Other_fee;
	}
	public void setOther_fee(Double otder_fee) {
		Other_fee = otder_fee;
	}
	public Double getTotal_fee() {
		return Total_fee;
	}
	public void setTotal_fee(Double total_fee) {
		Total_fee = total_fee;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {

		Status = status;
	}
	public int getEmployee_id() {
		return Employee_id;
	}
	public void setEmployee_id(int employee_id) {
		Employee_id = employee_id;
	}
	public String getEmployee_name() {
		return Employee_name;
	}
	public void setEmployee_name(String employee_name) {
		Employee_name = employee_name;
	}
	
	
}



