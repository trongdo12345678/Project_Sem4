package com.customer.repository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.models.Order;
import com.models.momo.MomoConfig;
import com.models.momo.MomoCreatePaymentResponseModel;
import com.models.momo.MomoExecuteResponseModel;
import com.models.momo.OrderInfoModel;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class MomoService {

	private final RestTemplate restTemplate = new RestTemplate();
	@Autowired
	private MomoConfig momoConfig;
	@Autowired
	private Environment env;
	private static final Logger logger = LoggerFactory.getLogger(MomoService.class);

	public MomoCreatePaymentResponseModel createPayment(OrderInfoModel model, Order or) {
		try {
			String requestId = String.valueOf(System.currentTimeMillis());
			String OrderId = String.valueOf(System.currentTimeMillis());
			// Tạo chuỗi raw data
			String rawData = "accessKey=" + momoConfig.getAccessKey() + "&amount=" + model.getAmount() + "&extraData="
					+ "&ipnUrl=" + momoConfig.getNotifyUrl() + "&orderId=" + OrderId + "&orderInfo="
					+ model.getOrderInfo() + "&partnerCode=" + momoConfig.getPartnerCode() + "&redirectUrl="
					+ momoConfig.getReturnUrl() + "&requestId=" + requestId + "&requestType=captureWallet"; 
																											

			String signature = generateHMAC(rawData, momoConfig.getSecretKey());

			// Log để debug
			System.out.println("Raw Data: " + rawData);
			System.out.println("Generated Signature: " + signature);

			// Tạo request body
			Map<String, Object> requestData = new HashMap<>();
			requestData.put("partnerCode", momoConfig.getPartnerCode());
			requestData.put("accessKey", momoConfig.getAccessKey());
			requestData.put("requestId", requestId);
			requestData.put("amount", model.getAmount());
			requestData.put("orderId", OrderId);
			requestData.put("orderInfo", model.getOrderInfo());
			requestData.put("redirectUrl", momoConfig.getReturnUrl());
			requestData.put("ipnUrl", momoConfig.getNotifyUrl());
			requestData.put("extraData", "");
			requestData.put("requestType", "captureWallet");
			requestData.put("signature", signature);
			requestData.put("lang", "vi");

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestData, headers);

			// Log request body
			System.out.println("Request Body: " + new ObjectMapper().writeValueAsString(requestData));

			ResponseEntity<MomoCreatePaymentResponseModel> response = restTemplate
					.postForEntity(momoConfig.getMomoApiUrl(), request, MomoCreatePaymentResponseModel.class);
			return response.getBody();

		} catch (Exception e) {
			System.err.println("Error creating payment: " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("Failed to create MoMo payment: " + e.getMessage(), e);
		}
	}

	private String generateHMAC(String data, String secretKey) {
		try {
			Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
			SecretKeySpec secret_key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
			sha256_HMAC.init(secret_key);
			byte[] bytes = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));

			StringBuilder sb = new StringBuilder();
			for (byte b : bytes) {
				sb.append(String.format("%02x", b));
			}
			return sb.toString();
		} catch (Exception e) {
			throw new RuntimeException("Failed to generate HMAC", e);
		}
	}

	public MomoExecuteResponseModel paymentExecute(Map<String, String> params) {
		return new MomoExecuteResponseModel(params.get("amount"), params.get("orderId"), params.get("orderInfo"));
	}

	public void processPayment(Order or, HttpServletResponse response) {
		try {
			System.out.println(or.getTotalAmount());
			double usdToVndRate = Double.parseDouble(env.getProperty("exchange.rate.usd-to-vnd"));
			if ((int) Math.round(or.getTotalAmount() * usdToVndRate) < 1000 || (int) Math.round(or.getTotalAmount() * usdToVndRate) > 50000000) {
				sendErrorResponse(response, "Số tiền phải từ 1,000đ đến 50,000,000đ");
				return;
			}

			// Tạo order info model
			OrderInfoModel orderInfoModel = new OrderInfoModel();
			orderInfoModel.setOrderInfo(or.getOrderID());
			orderInfoModel.setAmount((int) Math.round(or.getTotalAmount() * usdToVndRate));

			// Gọi API MoMo
			MomoCreatePaymentResponseModel momoResponse = createPayment(orderInfoModel, or);
			orderInfoModel.setAmount(15000);
			// Kiểm tra response
			if (momoResponse == null) {
				sendErrorResponse(response, "Không nhận được phản hồi từ MoMo");
				return;
			}

			// Kiểm tra error code
			Integer errorCode = momoResponse.getErrorCode();
			if (errorCode != null && errorCode != 0) {
				sendErrorResponse(response, "Lỗi từ MoMo: " + momoResponse.getLocalMessage());
				return;
			}

			// Redirect nếu có payUrl
			String payUrl = momoResponse.getPayUrl();
			if (payUrl != null && !payUrl.isEmpty()) {
				response.sendRedirect(payUrl);
			} else {
				sendErrorResponse(response, "Không nhận được URL thanh toán");
			}

		} catch (Exception e) {
			logger.error("Error processing MoMo payment", e);
			sendErrorResponse(response, "Có lỗi xảy ra: " + e.getMessage());
		}
	}

	private void sendErrorResponse(HttpServletResponse response, String message) {
		try {
			response.setContentType("text/plain;charset=UTF-8");
			response.getWriter().write(message);
		} catch (IOException e) {
			logger.error("Error sending error response", e);
		}
	}

	public boolean refundPayment(Order order, String description) {
		try {
			String requestId = String.valueOf(System.currentTimeMillis());
			String orderId = String.valueOf(System.currentTimeMillis());
			double usdToVndRate = Double.parseDouble(env.getProperty("exchange.rate.usd-to-vnd"));
			
			long amount = Math.round(order.getTotalAmount() * usdToVndRate);

			amount = 15000;
		
			StringBuilder rawSignature = new StringBuilder();
			rawSignature.append("accessKey=").append(momoConfig.getAccessKey()).append("&amount=").append(amount)
					.append("&description=").append(description).append("&orderId=").append(orderId)
					.append("&partnerCode=").append(momoConfig.getPartnerCode()).append("&requestId=").append(requestId)
					.append("&transId=").append(order.getTransactionId());

		

			String signature = generateHMAC(rawSignature.toString(), momoConfig.getSecretKey());

			// Tạo request body
			Map<String, Object> requestBody = new HashMap<>();
			requestBody.put("partnerCode", momoConfig.getPartnerCode());
			requestBody.put("orderId", orderId);
			requestBody.put("requestId", requestId);
			requestBody.put("amount", amount);
			requestBody.put("transId", order.getTransactionId());
			requestBody.put("description", description);
			requestBody.put("signature", signature);
			requestBody.put("lang", "vi");

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

			ResponseEntity<Map> response = restTemplate.postForEntity(momoConfig.getRefundUrl(), request, Map.class);

			Map<String, Object> responseBody = response.getBody();


			if (responseBody != null) {
				String resultCode = String.valueOf(responseBody.get("resultCode"));


				switch (resultCode) {
				case "0":
				case "21":
				case "37":
				case "42":
					return true;

				default:
					logger.error("Refund failed with code: " + resultCode);
					return false;
				}
			}
			return false;

		} catch (Exception e) {
			logger.error("Error processing refund: " + e.getMessage(), e);
			return false;
		}
	}
}
