package com.models;

public class ReturnOrderDetail {
    private int id;
    private int returnOrderId;
    private int orderDetailId;
    private int quantity;
    private String reason;
    private double amount;
    private int productId;
    // Thêm fields để hiển thị
    private String productName;
    private String productImage;
    private double productPrice;
    private int originalQuantity;
	public ReturnOrderDetail(int id, int returnOrderId, int orderDetailId, int quantity, String reason, double amount,
			String productName, String productImage, double productPrice, int originalQuantity) {
		this.id = id;
		this.returnOrderId = returnOrderId;
		this.orderDetailId = orderDetailId;
		this.quantity = quantity;
		this.reason = reason;
		this.amount = amount;
		this.productName = productName;
		this.productImage = productImage;
		this.productPrice = productPrice;
		this.originalQuantity = originalQuantity;
	}
	
	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public ReturnOrderDetail() {
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getReturnOrderId() {
		return returnOrderId;
	}
	public void setReturnOrderId(int returnOrderId) {
		this.returnOrderId = returnOrderId;
	}
	public int getOrderDetailId() {
		return orderDetailId;
	}
	public void setOrderDetailId(int orderDetailId) {
		this.orderDetailId = orderDetailId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductImage() {
		return productImage;
	}
	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}
	public double getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}
	public int getOriginalQuantity() {
		return originalQuantity;
	}
	public void setOriginalQuantity(int originalQuantity) {
		this.originalQuantity = originalQuantity;
	}
    
    
}