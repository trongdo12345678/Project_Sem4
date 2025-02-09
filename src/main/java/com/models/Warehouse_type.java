package com.models;

public class Warehouse_type {
	private int Id;
	private String Name;
	private int relatedCount;
	public Warehouse_type(int id, String name) {
		super();
		Id = id;
		Name = name;
	}
	public Warehouse_type() {
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
	public int getRelatedCount() {
		return relatedCount;
	}
	public void setRelatedCount(int relatedCount) {
		this.relatedCount = relatedCount;
	}
}
