	package com.models;
	
	import java.time.LocalDate;
	import java.time.LocalDateTime;
	
	public class EmployeeSalaryHistory {
	    private int id;
	    private int employeeId;
	    private int salaryTypeId;
	    private Double amount;
	    private LocalDate startDate;
	    private LocalDate endDate;
	    private String note;
	    private int createdBy;
	    private LocalDateTime createdAt;
	    private SalaryType salaryType;        
	    private Employee employee;            
	    private Employee createdByEmployee;    
		public EmployeeSalaryHistory() {
			super();
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public int getEmployeeId() {
			return employeeId;
		}
		public void setEmployeeId(int employeeId) {
			this.employeeId = employeeId;
		}
		public int getSalaryTypeId() {
			return salaryTypeId;
		}
		public void setSalaryTypeId(int salaryTypeId) {
			this.salaryTypeId = salaryTypeId;
		}
		public Double getAmount() {
			return amount;
		}
		public void setAmount(Double amount) {
			this.amount = amount;
		}
		public LocalDate getStartDate() {
			return startDate;
		}
		public void setStartDate(LocalDate startDate) {
			this.startDate = startDate;
		}
		public LocalDate getEndDate() {
			return endDate;
		}
		public void setEndDate(LocalDate endDate) {
			this.endDate = endDate;
		}
		public String getNote() {
			return note;
		}
		public void setNote(String note) {
			this.note = note;
		}
		public int getCreatedBy() {
			return createdBy;
		}
		public void setCreatedBy(int createdBy) {
			this.createdBy = createdBy;
		}
		public LocalDateTime getCreatedAt() {
			return createdAt;
		}
		public void setCreatedAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
		}
		public SalaryType getSalaryType() {
			return salaryType;
		}
		public void setSalaryType(SalaryType salaryType) {
			this.salaryType = salaryType;
		}
		public Employee getEmployee() {
			return employee;
		}
		public void setEmployee(Employee employee) {
			this.employee = employee;
		}
		public Employee getCreatedByEmployee() {
			return createdByEmployee;
		}
		public void setCreatedByEmployee(Employee createdByEmployee) {
			this.createdByEmployee = createdByEmployee;
		}
	    
	    
	    
	    
	    
	}