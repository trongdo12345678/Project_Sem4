package com.models.ghn;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Province {
	@JsonProperty("ProvinceID")
	private Integer provinceId;

	@JsonProperty("ProvinceName")
	private String provinceName;

	@JsonProperty("CountryID")
	private Integer countryId;

	@JsonProperty("Code")
	private String code;

	@JsonProperty("NameExtension")
	private List<String> nameExtension;

	public Province(Integer provinceId, String provinceName, Integer countryId, String code,
			List<String> nameExtension) {
		super();
		this.provinceId = provinceId;
		this.provinceName = provinceName;
		this.countryId = countryId;
		this.code = code;
		this.nameExtension = nameExtension;
	}

	public Province() {
		super();
	}

	// Getters và Setters
	public Integer getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
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
