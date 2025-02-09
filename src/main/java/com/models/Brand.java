package com.models;

public class Brand {
	private int Id;
	private String Name;
	private int relatedCount;
	public Brand(int id, String name) {
		Id = id;
		Name = name;
	}
	public Brand() {}
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
