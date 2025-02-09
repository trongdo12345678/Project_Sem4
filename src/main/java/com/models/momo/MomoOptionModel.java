package com.models.momo;

public class MomoOptionModel {
	private String MomoApiUrl;
	private String SecretKey;
	private String AccessKey;
	private String ReturnUrl;
	private String NotifyUrl;
	private String PartnerCode;
	private String RequestType;

	public MomoOptionModel() {
	}

	public MomoOptionModel(String momoApiUrl, String secretKey, String accessKey, String returnUrl, String notifyUrl,
			String partnerCode, String requestType) {
		MomoApiUrl = momoApiUrl;
		SecretKey = secretKey;
		AccessKey = accessKey;
		ReturnUrl = returnUrl;
		NotifyUrl = notifyUrl;
		PartnerCode = partnerCode;
		RequestType = requestType;
	}

	public String getMomoApiUrl() {
		return MomoApiUrl;
	}

	public void setMomoApiUrl(String momoApiUrl) {
		MomoApiUrl = momoApiUrl;
	}

	public String getSecretKey() {
		return SecretKey;
	}

	public void setSecretKey(String secretKey) {
		SecretKey = secretKey;
	}

	public String getAccessKey() {
		return AccessKey;
	}

	public void setAccessKey(String accessKey) {
		AccessKey = accessKey;
	}

	public String getReturnUrl() {
		return ReturnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		ReturnUrl = returnUrl;
	}

	public String getNotifyUrl() {
		return NotifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		NotifyUrl = notifyUrl;
	}

	public String getPartnerCode() {
		return PartnerCode;
	}

	public void setPartnerCode(String partnerCode) {
		PartnerCode = partnerCode;
	}

	public String getRequestType() {
		return RequestType;
	}

	public void setRequestType(String requestType) {
		RequestType = requestType;
	}

}
