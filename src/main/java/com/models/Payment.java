package com.models;

public class Payment {
	private int Id;
	private String Name;
	public Payment(int id, String name) {
		super();
		Id = id;
		Name = name;
	}
	public Payment() {
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
