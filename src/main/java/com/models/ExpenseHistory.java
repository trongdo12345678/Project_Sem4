package com.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ExpenseHistory {
    private int id;
    private int expenseTypeId;
    private Double amount;
    private ExpenseType type; 
    private LocalDate startDate;
    private LocalDate endDate;
    private String note;
    private int createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ExpenseType expenseType;     
    private Employee createdByEmployee;  
    
    
    
    
    
	public ExpenseType getType() {
		return type;
	}
	public void setType(ExpenseType type) {
		this.type = type;
	}
	public ExpenseHistory() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getExpenseTypeId() {
		return expenseTypeId;
	}
	public void setExpenseTypeId(int expenseTypeId) {
		this.expenseTypeId = expenseTypeId;
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
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
	public ExpenseType getExpenseType() {
		return expenseType;
	}
	public void setExpenseType(ExpenseType expenseType) {
		this.expenseType = expenseType;
	}
	public Employee getCreatedByEmployee() {
		return createdByEmployee;
	}
	public void setCreatedByEmployee(Employee createdByEmployee) {
		this.createdByEmployee = createdByEmployee;
	}
    
    
    
}