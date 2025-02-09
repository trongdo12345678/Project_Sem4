package com.models;

import java.time.LocalDate;
import java.util.List;

public class Order {
	private int Id;
	private int Customer_id;
	private String Cus_Name;
	private String Phone;
	private String Status;
	private String Address;
	private String Pay_status;
	private int Employee_id;
	private int Payment_id;
	private LocalDate Date;
	private int Coupon_id;
	private Double Discount;
	private Double TotalAmount;
	private Double ShippingFee;
	private String PaymentMethod;
	private String Notes;
	private String OrderID;
	private String TransactionId;
	private int WareHouse_Id;
	private int Province_Id;
	private int District_Id;
	private String Ward_Id;
	private String Ghn_order_code;
	private String Tracking_code;
	private LocalDate expected_delivery_time;
	private String shipping_status;

	private List<Order_detail> orderDetails;
    private ReturnOrder returnOrder;
    private ChatRoom chatRoom;
    private List<ChatMessage> chatMessages;

	public List<Order_detail> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(List<Order_detail> orderDetails) {
		this.orderDetails = orderDetails;
	}

	public ReturnOrder getReturnOrder() {
		return returnOrder;
	}

	public void setReturnOrder(ReturnOrder returnOrder) {
		this.returnOrder = returnOrder;
	}

	public ChatRoom getChatRoom() {
		return chatRoom;
	}

	public void setChatRoom(ChatRoom chatRoom) {
		this.chatRoom = chatRoom;
	}

	public List<ChatMessage> getChatMessages() {
		return chatMessages;
	}

	public void setChatMessages(List<ChatMessage> chatMessages) {
		this.chatMessages = chatMessages;
	}


	public Order(int id, int customer_id, String cus_Name, String phone, String status, String address,
			String pay_status, int employee_id, int payment_id, LocalDate date, int coupon_id, Double discount,
			Double totalAmount, Double shippingFee, String paymentMethod, String notes, String orderID,
			String transactionId, int province_Id, int district_Id, String ward_Id, String ghn_order_code,
			String tracking_code, LocalDate expected_delivery_time, String shipping_status, int wareHouse_Id) {

		super();
		Id = id;
		Customer_id = customer_id;
		Cus_Name = cus_Name;
		Phone = phone;
		Status = status;
		Address = address;
		Pay_status = pay_status;
		Employee_id = employee_id;
		Payment_id = payment_id;
		Date = date;
		Coupon_id = coupon_id;
		Discount = discount;
		TotalAmount = totalAmount;
		ShippingFee = shippingFee;
		PaymentMethod = paymentMethod;
		Notes = notes;
		OrderID = orderID;
		TransactionId = transactionId;
		Province_Id = province_Id;
		District_Id = district_Id;
		Ward_Id = ward_Id;
		Ghn_order_code = ghn_order_code;
		Tracking_code = tracking_code;
		WareHouse_Id = wareHouse_Id;
		this.expected_delivery_time = expected_delivery_time;
		this.shipping_status = shipping_status;
	}

	public Order() {
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public int getCustomer_id() {
		return Customer_id;
	}

	public void setCustomer_id(int customer_id) {
		Customer_id = customer_id;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public int getEmployee_id() {
		return Employee_id;
	}

	public void setEmployee_id(int employee_id) {
		Employee_id = employee_id;
	}

	public int getPayment_id() {
		return Payment_id;
	}

	public void setPayment_id(int payment_id) {
		Payment_id = payment_id;
	}

	public String getCus_Name() {
		return Cus_Name;
	}

	public void setCus_Name(String cus_Name) {
		Cus_Name = cus_Name;
	}

	public String getPhone() {
		return Phone;
	}

	public void setPhone(String phone) {
		Phone = phone;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getPay_status() {
		return Pay_status;
	}

	public void setPay_status(String pay_status) {
		Pay_status = pay_status;
	}

	public LocalDate getDate() {
		return Date;
	}

	public void setDate(LocalDate date) {
		Date = date;
	}

	public int getCoupon_id() {
		return Coupon_id;
	}

	public void setCoupon_id(int coupon_id) {
		Coupon_id = coupon_id;
	}

	public Double getDiscount() {
		return Discount;
	}

	public void setDiscount(Double discount) {
		Discount = discount;
	}

	public Double getTotalAmount() {
		return TotalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		TotalAmount = totalAmount;
	}

	public Double getShippingFee() {
		return ShippingFee;
	}

	public void setShippingFee(Double shippingFee) {
		ShippingFee = shippingFee;
	}

	public String getPaymentMethod() {
		return PaymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		PaymentMethod = paymentMethod;
	}

	public String getNotes() {
		return Notes;
	}

	public void setNotes(String notes) {
		Notes = notes;
	}

	public String getOrderID() {
		return OrderID;
	}

	public void setOrderID(String orderID) {
		OrderID = orderID;
	}

	public String getTransactionId() {
		return TransactionId;
	}

	public void setTransactionId(String transactionId) {
		TransactionId = transactionId;
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

	public String getGhn_order_code() {
		return Ghn_order_code;
	}

	public void setGhn_order_code(String ghn_order_code) {
		Ghn_order_code = ghn_order_code;
	}

	public String getTracking_code() {
		return Tracking_code;
	}

	public void setTracking_code(String tracking_code) {
		Tracking_code = tracking_code;
	}

	public LocalDate getExpected_delivery_time() {
		return expected_delivery_time;
	}

	public void setExpected_delivery_time(LocalDate expected_delivery_time) {
		this.expected_delivery_time = expected_delivery_time;
	}

	public String getShipping_status() {
		return shipping_status;
	}

	public void setShipping_status(String shipping_status) {
		this.shipping_status = shipping_status;
	}

	public int getWareHouse_Id() {
		return WareHouse_Id;
	}

	public void setWareHouse_Id(int wareHouse_Id) {
		WareHouse_Id = wareHouse_Id;
	}
	
}