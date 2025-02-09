package com.models;

import java.util.List;

public class Warehouse {
	private int Id;
	private String Name;
	private String Address;
	private int Wh_type_id;
	private String Type_name;
	private String ManagerFirstName;
	private String ManagerLastName;
	private List<Employee> managers;
	private int relatedCount;
	private int relatedCounte;
	private int ewCuont;
	private int Province_Id;
	private int District_Id;
	private String Ward_Id;
	private String Ghn_store_code;
	private Double lat;
	private Double lng;
	
	
	public Warehouse(int id, String name, String address, int wh_type_id, String type_name, int province_Id,
			int district_Id, String ward_Id, String ghn_store_code) {
		super();
		Id = id;
		Name = name;
		Address = address;
		Wh_type_id = wh_type_id;
		Type_name = type_name;
		Province_Id = province_Id;
		District_Id = district_Id;
		Ward_Id = ward_Id;
		Ghn_store_code = ghn_store_code;
	}
	public Warehouse() {
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
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public int getWh_type_id() {
		return Wh_type_id;
	}
	public void setWh_type_id(int wh_type_id) {
		Wh_type_id = wh_type_id;
	}
	public String getTypeName() {
		return Type_name;
	}
	public void setTypeName(String type_name) {
		Type_name = type_name;
	}
	public String getManagerFirstName() {
		return ManagerFirstName;
	}
	public void setManagerFirstName(String managerFirstName) {
		ManagerFirstName = managerFirstName;
	}
	public String getManagerLastName() {
		return ManagerLastName;
	}
	public void setManagerLastName(String managerLastName) {
		ManagerLastName = managerLastName;
	}
	public List<Employee> getManagers() {
		return managers;
	}
	public void setManagers(List<Employee> managers) {
		this.managers = managers;
	}
	public int getRelatedCount() {
		return relatedCount;
	}
	public void setRelatedCount(int relatedCount) {
		this.relatedCount = relatedCount;
	}

	public String getType_name() {
		return Type_name;
	}
	public void setType_name(String type_name) {
		Type_name = type_name;
	}
	public int getProvince_Id() {
		return Province_Id;
	}
	public void setProvince_Id(int province_Id) {
		Province_Id = province_Id;
	}
	public int getDistrict_Id() {
		return District_Id;
	}
	public void setDistrict_Id(int district_Id) {
		District_Id = district_Id;
	}
	public String getWard_Id() {
		return Ward_Id;
	}
	public void setWard_Id(String ward_Id) {
		Ward_Id = ward_Id;
	}
	public String getGhn_store_code() {
		return Ghn_store_code;
	}
	public void setGhn_store_code(String ghn_store_code) {
		Ghn_store_code = ghn_store_code;
	}
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public Double getLng() {
		return lng;
	}
	public void setLng(Double lng) {
		this.lng = lng;
	}
	public int getEwCuont() {
		return ewCuont;
	}
	public int getRelatedCounte() {
		return relatedCounte;
	}
	public void setRelatedCounte(int relatedCounte) {
		this.relatedCounte = relatedCounte;
	}
	public void setEwCuont(int ewCuont) {
		this.ewCuont = ewCuont;
	}
	
	
	
}
