package com.models;

public class Role {
	private int Id;
	private String Name;
	public Role(int id, String name) {
		super();
		Id = id;
		Name = name;
	}
	public Role() {
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
	
}
