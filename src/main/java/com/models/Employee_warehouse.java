package com.models;

public class Employee_warehouse {
	private int Id;
	private int Employee_id;
	private String Employee_name;
	private int Warehouse_id;
	private String Warehouse_name;
	private Warehouse warehouse;
    private Employee_warehouse empwh;
	public Employee_warehouse(int id, int employee_id, int warehouse_id) {
		super();
		Id = id;
		Employee_id = employee_id;
		Warehouse_id = warehouse_id;
	}
	public Employee_warehouse() {
	}
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public int getEmployee_id() {
		return Employee_id;
	}
	public void setEmployee_Id(int employee_id) {
		Employee_id = employee_id;
	}
	public int getWarehouse_id() {
		return Warehouse_id;
	}
	public void setWarehouse_Id(int warehouse_id) {
		Warehouse_id = warehouse_id;
	}
	public String getEmployee_name() {
		return Employee_name;
	}
	public void setEmployee_name(String employee_name) {
		Employee_name = employee_name;
	}
	public String getWarehouse_name() {
		return Warehouse_name;
	}
	public void setWarehouse_name(String warehouse_name) {
		Warehouse_name = warehouse_name;
	}
	public Warehouse getWarehouse() {
		return warehouse;
	}
	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}
	public Employee_warehouse getEmpwh() {
		return empwh;
	}
	public void setEmpwh(Employee_warehouse empwh) {
		this.empwh = empwh;
	}
	
}
