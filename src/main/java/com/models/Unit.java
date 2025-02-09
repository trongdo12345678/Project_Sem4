package com.models;

public class Unit {
	private int Id;
	private String Name;
	private int relatedCount;
	public Unit(int id, String name) {
		super();
		Id = id;
		Name = name;
	}
	public Unit() {
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
	
	@Override
    public String toString() {
        return "Unit{id=" + Id + ", name='" + Name + "'}";
    }
	
}
