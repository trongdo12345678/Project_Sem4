package com.customer.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@RestController
@RequestMapping("/api/v2/momo")
public class MomoController {

    @Value("${momo.partner-code}")
    private String partnerCode;

    @Value("${momo.access-key}")
    private String accessKey;

    @Value("${momo.secret-key}")
    private String secretKey;

    // Cache để lưu trữ trạng thái giao dịch
    private final ConcurrentMap<String, TransactionStatus> transactionCache = new ConcurrentHashMap<>();

    @PostMapping("/ipn")
    public ResponseEntity<?> handleMomoIPN(@RequestBody MomoIPNRequest request) {
    	
        try {
            
           
            TransactionStatus status = new TransactionStatus(
                request.getOrderId(),
                request.getTransId(),
                request.getResultCode(),
                request.getMessage(),
                request.getAmount()
            );
            transactionCache.put(request.getOrderId(), status);

            // Tự động xóa cache sau 30 phút
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    transactionCache.remove(request.getOrderId());
                }
            }, 30 * 60 * 1000);

            // Kiểm tra resultCode
            if (request.getResultCode() == 0) {
                // Thanh toán thành công
                return ResponseEntity.ok().body(new MomoIPNResponse("success"));
            } else {
                // Thanh toán thất bại
                return ResponseEntity.ok().body(new MomoIPNResponse("failed"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MomoIPNResponse("Error: " + e.getMessage()));
        }
    }

	    private static final String PARTNER_CODE = "MOMO";
	    private static final String ACCESS_KEY = "F8BBA842ECF85";
	    private static final String SECRET_KEY = "K951B6PE1waDMi640xX08PD3vg6EkVlz";
	    
	    @GetMapping("/check-status")
	    public ResponseEntity<?> checkPaymentStatus(
	            @RequestParam String orderId,
	            @RequestParam(required = false) String transId
	    ) {
	    	
	        try {
	            String requestId = String.valueOf(System.currentTimeMillis());
	            
	            
	            String rawSignature = String.format("accessKey=%s&orderId=%s&partnerCode=%s&requestId=%s",
	                    ACCESS_KEY, orderId, PARTNER_CODE, requestId);
	            
	          
	            
	            Mac hmacSha256 = Mac.getInstance("HmacSHA256");
	            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "HmacSHA256");
	            hmacSha256.init(secretKeySpec);
	            String signature = Hex.encodeHexString(hmacSha256.doFinal(rawSignature.getBytes()));
	
	            // Tạo request body
	            Map<String, String> requestBody = new HashMap<>();
	            requestBody.put("partnerCode", PARTNER_CODE);
	            requestBody.put("requestId", requestId);
	            requestBody.put("orderId", orderId);
	            requestBody.put("lang", "vi");
	            requestBody.put("signature", signature);
	
	            
	
	            
	            RestTemplate restTemplate = new RestTemplate();
	            HttpHeaders headers = new HttpHeaders();
	            headers.setContentType(MediaType.APPLICATION_JSON);
	
	            HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);
	            
	            ResponseEntity<Map> response = restTemplate.exchange(
	                "https://test-payment.momo.vn/v2/gateway/api/query",
	                HttpMethod.POST,
	                entity,
	                Map.class
	            );
	
	          
	
	            Map<String, Object> responseBody = response.getBody();
	            
	            if (responseBody != null) {
	                int resultCode = ((Number) responseBody.get("resultCode")).intValue();
	                return ResponseEntity.ok(Map.of(
	                    "success", resultCode == 0,
	                    "status", resultCode,
	                    "message", responseBody.get("message"),
	                    "data", responseBody
	                ));
	            }
	
	            return ResponseEntity.ok(Map.of(
	                "success", false,
	                "message", "Không thể kết nối đến MoMo"
	            ));
	
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.ok(Map.of(
	                "success", false,
	                "message", "Lỗi kiểm tra trạng thái: " + e.getMessage()
	            ));
	        }
	    }

    private String generateHmacSHA256(String data, String secretKey) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        return Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class TransactionStatus {
    private String orderId;
    private Long transId;
    private Integer resultCode;
    private String message;
    private Long amount;
    
    
	public TransactionStatus(String orderId, Long transId, Integer resultCode, String message, Long amount) {
		super();
		this.orderId = orderId;
		this.transId = transId;
		this.resultCode = resultCode;
		this.message = message;
		this.amount = amount;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public Long getTransId() {
		return transId;
	}
	public void setTransId(Long transId) {
		this.transId = transId;
	}
	public Integer getResultCode() {
		return resultCode;
	}
	public void setResultCode(Integer resultCode) {
		this.resultCode = resultCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Long getAmount() {
		return amount;
	}
	public void setAmount(Long amount) {
		this.amount = amount;
	}
    
    
}

@Data
class MomoIPNRequest {
    private String partnerCode;
    private String orderId;
    private String requestId;
    private Long amount;
    private String orderInfo;
    private String orderType;
    private Long transId;
    private Integer resultCode;
    private String message;
    private String payType;
    private Long responseTime;
    private String extraData;
    private String signature;
	public String getPartnerCode() {
		return partnerCode;
	}
	public void setPartnerCode(String partnerCode) {
		this.partnerCode = partnerCode;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public Long getAmount() {
		return amount;
	}
	public void setAmount(Long amount) {
		this.amount = amount;
	}
	public String getOrderInfo() {
		return orderInfo;
	}
	public void setOrderInfo(String orderInfo) {
		this.orderInfo = orderInfo;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public Long getTransId() {
		return transId;
	}
	public void setTransId(Long transId) {
		this.transId = transId;
	}
	public Integer getResultCode() {
		return resultCode;
	}
	public void setResultCode(Integer resultCode) {
		this.resultCode = resultCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public Long getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(Long responseTime) {
		this.responseTime = responseTime;
	}
	public String getExtraData() {
		return extraData;
	}
	public void setExtraData(String extraData) {
		this.extraData = extraData;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
    
    
}

// Model cho IPN response
@Data
class MomoIPNResponse {
    private String message;
    
    public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public MomoIPNResponse(String message) {
        this.message = message;
    }
}