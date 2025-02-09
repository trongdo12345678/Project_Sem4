package com.models.momo;

public class MomoCreatePaymentResponseModel {
	private String RequestId;
	private int ErrorCode;
	private String OrderId;
	private String Message;
	private String LocalMessage;
	private String RequestType;
	private String PayUrl;
	private String Signature;
	private String QrCodeUrl;
	private String Deeplink;
	private String DeeplinkWebInApp;

	public MomoCreatePaymentResponseModel() {

	}

	public MomoCreatePaymentResponseModel(String requestId, int errorCode, String orderId, String message,
			String localMessage, String requestType, String payUrl, String signature, String qrCodeUrl, String deeplink,
			String deeplinkWebInApp) {
		RequestId = requestId;
		ErrorCode = errorCode;
		OrderId = orderId;
		Message = message;
		LocalMessage = localMessage;
		RequestType = requestType;
		PayUrl = payUrl;
		Signature = signature;
		QrCodeUrl = qrCodeUrl;
		Deeplink = deeplink;
		DeeplinkWebInApp = deeplinkWebInApp;
	}

	public String getRequestId() {
		return RequestId;
	}

	public void setRequestId(String requestId) {
		RequestId = requestId;
	}

	public int getErrorCode() {
		return ErrorCode;
	}

	public void setErrorCode(int errorCode) {
		ErrorCode = errorCode;
	}

	public String getOrderId() {
		return OrderId;
	}

	public void setOrderId(String orderId) {
		OrderId = orderId;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	public String getLocalMessage() {
		return LocalMessage;
	}

	public void setLocalMessage(String localMessage) {
		LocalMessage = localMessage;
	}

	public String getRequestType() {
		return RequestType;
	}

	public void setRequestType(String requestType) {
		RequestType = requestType;
	}

	public String getPayUrl() {
		return PayUrl;
	}

	public void setPayUrl(String payUrl) {
		PayUrl = payUrl;
	}

	public String getSignature() {
		return Signature;
	}

	public void setSignature(String signature) {
		Signature = signature;
	}

	public String getQrCodeUrl() {
		return QrCodeUrl;
	}

	public void setQrCodeUrl(String qrCodeUrl) {
		QrCodeUrl = qrCodeUrl;
	}

	public String getDeeplink() {
		return Deeplink;
	}

	public void setDeeplink(String deeplink) {
		Deeplink = deeplink;
	}

	public String getDeeplinkWebInApp() {
		return DeeplinkWebInApp;
	}

	public void setDeeplinkWebInApp(String deeplinkWebInApp) {
		DeeplinkWebInApp = deeplinkWebInApp;
	}

}
