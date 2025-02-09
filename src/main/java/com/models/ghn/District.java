package com.models.ghn;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class District {
	@JsonProperty("DistrictID")
	private Integer districtId;

	@JsonProperty("DistrictName")
	private String districtName;

	@JsonProperty("ProvinceID")
	private Integer provinceId;

	@JsonProperty("Code")
	private String code;

	@JsonProperty("NameExtension")
	private List<String> nameExtension;

	public District(Integer districtId, String districtName, Integer provinceId, String code,
			List<String> nameExtension) {
		super();
		this.districtId = districtId;
		this.districtName = districtName;
		this.provinceId = provinceId;
		this.code = code;
		this.nameExtension = nameExtension;
	}

	public District() {
		super();
	}

	// Getters và Setters
	public Integer getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Integer districtId) {
		this.districtId = districtId;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public Integer getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<String> getNameExtension() {
		return nameExtension;
	}

	public void setNameExtension(List<String> nameExtension) {
		this.nameExtension = nameExtension;
	}

}