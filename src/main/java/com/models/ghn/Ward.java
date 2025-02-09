package com.models.ghn;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Ward {
	@JsonProperty("WardCode")
	private String wardCode;

	@JsonProperty("WardName")
	private String wardName;

	@JsonProperty("DistrictID")
	private Integer districtId;

	@JsonProperty("NameExtension")
	private List<String> nameExtension;

	public Ward(String wardCode, String wardName, Integer districtId, List<String> nameExtension) {
		super();
		this.wardCode = wardCode;
		this.wardName = wardName;
		this.districtId = districtId;
		this.nameExtension = nameExtension;
	}

	public Ward() {
		super();
	}

	// Getters và Setters
	public String getWardCode() {
		return wardCode;
	}

	public void setWardCode(String wardCode) {
		this.wardCode = wardCode;
	}

	public String getWardName() {
		return wardName;
	}

	public void setWardName(String wardName) {
		this.wardName = wardName;
	}

	public Integer getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Integer districtId) {
		this.districtId = districtId;
	}

	public List<String> getNameExtension() {
		return nameExtension;
	}

	public void setNameExtension(List<String> nameExtension) {
		this.nameExtension = nameExtension;
	}

}