package com.customer.repository;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

@Service
public class EmailService {
	@Autowired
	JavaMailSender mailSender;
	
	

	public void sendConfirmationEmail(String toEmail, String confirmationLink) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setTo(toEmail);
		helper.setSubject("Email Confirmation");

		
		helper.setFrom("Electro Sphere <anhbon148@gmail.com>");

		helper.setText("<html>" + "<body style=\"font-family: Arial, sans-serif;\">"
				+ "<div style=\"max-width: 600px; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 5px; background-color: #f9f9f9;\">"
				+ "<h2 style=\"color: #333; text-align: center;\">Registration Confirmation</h2>"
				+ "<p style=\"font-size: 16px; color: #555; text-align: center;\">Thank you for registering! Please click the button below to confirm your registration:</p>"
				+ "<div style=\"text-align: center;\">" + "<a href=\"" + confirmationLink
				+ "\" style=\"display: inline-block; margin: 20px 0; padding: 10px 20px; background-color: #007BFF; color: white; text-decoration: none; border-radius: 5px; font-size: 16px;\">"
				+ "Confirm Registration" + "</a>" + "</div>"
				+ "<p style=\"font-size: 14px; color: #777; text-align: center;\">If you did not request this, please ignore this email.</p>"
				+ "<footer style=\"text-align: center; margin-top: 20px; font-size: 12px; color: #aaa;\">"
				+ "<p>Electro Sphere<br>Your trusted partner for electronic components</p>" + "</footer>" + "</div>"
				+ "</body>" + "</html>", true);

		mailSender.send(message);
	}

	public static String generateToken() {
		return UUID.randomUUID().toString();
	}

	public void sendcontact(String name, String email, String Message) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setTo("anhbon1488@gmail.com");
		helper.setSubject("Contact from ES");

		helper.setFrom(email);

		String htmlContent = "<html><body>" + "<h2 style='color: #4CAF50;'>Contact Information</h2>"
				+ "<p><strong>Name:</strong> " + name + "</p>" + "<p><strong>Email:</strong> " + email + "</p>"
				+ "<p><strong>Message:</strong></p>"
				+ "<p style='border: 1px solid #ddd; padding: 10px; background-color: #f9f9f9;'>" + Message + "</p>"
				+ "</body></html>";

		helper.setText(htmlContent, true);

		mailSender.send(message);
	}
	
	
	

    private final Map<String, RegistrationData> registrationCache = new ConcurrentHashMap<>();
    @Data
    @AllArgsConstructor
    private static class RegistrationData {
        private String password;
        private String otp;
        private LocalDateTime expiryTime;
        
		public RegistrationData(String password, String otp, LocalDateTime expiryTime) {
			super();
			this.password = password;
			this.otp = otp;
			this.expiryTime = expiryTime;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getOtp() {
			return otp;
		}
		public void setOtp(String otp) {
			this.otp = otp;
		}
		public LocalDateTime getExpiryTime() {
			return expiryTime;
		}
		public void setExpiryTime(LocalDateTime expiryTime) {
			this.expiryTime = expiryTime;
		}
        
    }
    public void sendOTPEmail(String toEmail, String password) throws MessagingException {
        
        String otp = String.format("%06d", new Random().nextInt(999999));
        
       
        registrationCache.put(toEmail, new RegistrationData(
            password,
            otp,
            LocalDateTime.now().plusMinutes(5)
        ));

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(toEmail);
        helper.setSubject("OTP Verification - Electro Sphere");
        helper.setFrom("Electro Sphere <anhbon148@gmail.com>");

        String htmlContent = "<html>" 
            + "<body style=\"font-family: Arial, sans-serif;\">"
            + "<div style=\"max-width: 600px; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 5px; background-color: #f9f9f9;\">"
            + "<h2 style=\"color: #333; text-align: center;\">OTP Verification</h2>"
            + "<p style=\"font-size: 16px; color: #555; text-align: center;\">Your OTP code for registration is:</p>"
            + "<div style=\"text-align: center;\">"
            + "<h1 style=\"color: #007BFF; font-size: 32px; letter-spacing: 5px; margin: 20px 0;\">" 
            + otp 
            + "</h1>"
            + "</div>"
            + "<p style=\"font-size: 14px; color: #777; text-align: center;\">This code will expire in 5 minutes.</p>"
            + "<p style=\"font-size: 14px; color: #777; text-align: center;\">If you did not request this, please ignore this email.</p>"
            + "<footer style=\"text-align: center; margin-top: 20px; font-size: 12px; color: #aaa;\">"
            + "<p>Electro Sphere<br>Your trusted partner for electronic components</p>"
            + "</footer>"
            + "</div>"
            + "</body>"
            + "</html>";

        helper.setText(htmlContent, true);
        mailSender.send(message);
        
    }

   
    public boolean verifyOTP(String email, String otp) {
        RegistrationData data = registrationCache.get(email);
        if (data == null) {
            return false;
        }

        
        if (LocalDateTime.now().isAfter(data.getExpiryTime())) {
            registrationCache.remove(email);
            return false;
        }

        
        boolean isValid = data.getOtp().equals(otp);
        return isValid;
    }
    public String getStoredPassword(String email) {
        RegistrationData data = registrationCache.get(email);
        if (data != null && LocalDateTime.now().isBefore(data.getExpiryTime())) {
            return data.getPassword();
        }
        return null;
    }

    public void clearRegistrationData(String email) {
        registrationCache.remove(email);
    }

    public boolean resendOTP(String email) throws MessagingException {
        RegistrationData data = registrationCache.get(email);
        if (data == null) {
            return false;
        }

        // Kiểm tra xem đã đủ thời gian để gửi lại OTP chưa (ví dụ: 1 phút)
        if (data.getExpiryTime() != null && 
            LocalDateTime.now().isBefore(data.getExpiryTime().minusMinutes(4))) {
            return false;
        }

        // Tạo OTP mới
        String newOTP = String.format("%06d", new Random().nextInt(999999));
        
        // Cập nhật OTP mới và thời gian hết hạn
        data.setOtp(newOTP);
        data.setExpiryTime(LocalDateTime.now().plusMinutes(5));
        registrationCache.put(email, data);

        // Gửi email OTP mới
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(email);
        helper.setSubject("OTP Verification - Electro Sphere");
        helper.setFrom("Electro Sphere <anhbon148@gmail.com>");

        String htmlContent = "<html>" 
            + "<body style=\"font-family: Arial, sans-serif;\">"
            + "<div style=\"max-width: 600px; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 5px; background-color: #f9f9f9;\">"
            + "<h2 style=\"color: #333; text-align: center;\">New OTP Verification</h2>"
            + "<p style=\"font-size: 16px; color: #555; text-align: center;\">Your new OTP code for registration is:</p>"
            + "<div style=\"text-align: center;\">"
            + "<h1 style=\"color: #007BFF; font-size: 32px; letter-spacing: 5px; margin: 20px 0;\">" 
            + newOTP 
            + "</h1>"
            + "</div>"
            + "<p style=\"font-size: 14px; color: #777; text-align: center;\">This code will expire in 5 minutes.</p>"
            + "<p style=\"font-size: 14px; color: #777; text-align: center;\">If you did not request this, please ignore this email.</p>"
            + "<footer style=\"text-align: center; margin-top: 20px; font-size: 12px; color: #aaa;\">"
            + "<p>Electro Sphere<br>Your trusted partner for electronic components</p>"
            + "</footer>"
            + "</div>"
            + "</body>"
            + "</html>";

        helper.setText(htmlContent, true);
        mailSender.send(message);

        return true;
    }

    // Thêm phương thức để kiểm tra thời gian còn lại
    public long getRemainingTimeForResend(String email) {
        RegistrationData data = registrationCache.get(email);
        if (data == null || data.getExpiryTime() == null) {
            return 0;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime resendTime = data.getExpiryTime().minusMinutes(4);
        
        if (now.isBefore(resendTime)) {
            return Duration.between(now, resendTime).getSeconds();
        }
        return 0;
    }
}
