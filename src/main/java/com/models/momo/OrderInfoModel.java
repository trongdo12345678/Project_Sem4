package com.models.momo;

public class OrderInfoModel {
	private String FullName;
	private String OrderId;
	private String OrderInfo;
	private int Amount;
	private String Idproject;
	private String Memberid;

	public OrderInfoModel() {
	}

	public OrderInfoModel(String fullName, String orderId, String orderInfo, int amount, String idproject,
			String memberid) {
		FullName = fullName;
		OrderId = orderId;
		OrderInfo = orderInfo;
		Amount = amount;
		Idproject = idproject;
		Memberid = memberid;
	}

	public String getFullName() {
		return FullName;
	}

	public void setFullName(String fullName) {
		FullName = fullName;
	}

	public String getOrderId() {
		return OrderId;
	}

	public void setOrderId(String orderId) {
		OrderId = orderId;
	}

	public String getOrderInfo() {
		return OrderInfo;
	}

	public void setOrderInfo(String orderInfo) {
		OrderInfo = orderInfo;
	}

	public int getAmount() {
		return Amount;
	}

	public void setAmount(int amount) {
		Amount = amount;
	}

	public String getIdproject() {
		return Idproject;
	}

	public void setIdproject(String idproject) {
		Idproject = idproject;
	}

	public String getMemberid() {
		return Memberid;
	}

	public void setMemberid(String memberid) {
		Memberid = memberid;
	}

}
