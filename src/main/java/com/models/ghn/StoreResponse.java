package com.models.ghn;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StoreResponse {
	@JsonProperty("shop_id")
	private Integer shopId;

	@JsonProperty("name")
	private String name;

	@JsonProperty("phone")
	private String phone;

	@JsonProperty("address")
	private String address;

	@JsonProperty("ward_code")
	private String wardCode;

	@JsonProperty("district_id")
	private Integer districtId;

	@JsonProperty("client_id")
	private String clientId;

	@JsonProperty("status")
	private Integer status;

	public StoreResponse() {
		super();
	}

	public StoreResponse(Integer shopId, String name, String phone, String address, String wardCode, Integer districtId,
			String clientId, Integer status) {
		super();
		this.shopId = shopId;
		this.name = name;
		this.phone = phone;
		this.address = address;
		this.wardCode = wardCode;
		this.districtId = districtId;
		this.clientId = clientId;
		this.status = status;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getWardCode() {
		return wardCode;
	}

	public void setWardCode(String wardCode) {
		this.wardCode = wardCode;
	}

	public Integer getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Integer districtId) {
		this.districtId = districtId;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}