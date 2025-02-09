package com.models.momo;

public class MomoExecuteResponseModel {
	private String OrderId;
	private String Amount;
	private String OrderInfo;

	public MomoExecuteResponseModel() {
	}

	public MomoExecuteResponseModel(String orderId, String amount, String orderInfo) {
		OrderId = orderId;
		Amount = amount;
		OrderInfo = orderInfo;
	}

	public String getOrderId() {
		return OrderId;
	}

	public void setOrderId(String orderId) {
		OrderId = orderId;
	}

	public String getAmount() {
		return Amount;
	}

	public void setAmount(String amount) {
		Amount = amount;
	}

	public String getOrderInfo() {
		return OrderInfo;
	}

	public void setOrderInfo(String orderInfo) {
		OrderInfo = orderInfo;
	}

}
