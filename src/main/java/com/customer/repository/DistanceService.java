package com.customer.repository;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.models.Warehouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class DistanceService {
	private final RestTemplate restTemplate = new RestTemplate();

	@Autowired
	private CheckoutRepository warehouseRepository;

	public Warehouse findNearestWarehouse(String customerAddress) {
		try {
			String HERE_API = "https://geocode.search.hereapi.com/v1/geocode";
			String API_KEY = "1A8jGJNNhmP0o-YIcYCJNYF07307tUBWlhquzYRqgds";

			// Lấy tọa độ khách hàng từ API
			String customerUrl = String.format("%s?q=%s&apiKey=%s", HERE_API,
					URLEncoder.encode(customerAddress, StandardCharsets.UTF_8), API_KEY);

			ResponseEntity<String> response = restTemplate.getForEntity(customerUrl, String.class);
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(response.getBody());

			if (root.has("items") && root.get("items").size() > 0) {
				JsonNode position = root.get("items").get(0).get("position");
				double customerLat = position.get("lat").asDouble();
				double customerLng = position.get("lng").asDouble();

				// Tìm kho gần nhất
				List<Warehouse> warehouses = warehouseRepository.findAll();
				Warehouse nearestWarehouse = null;
				double minDistance = Double.MAX_VALUE;

				for (Warehouse wh : warehouses) {
					double distance = calculateDistance(customerLat, customerLng, wh.getLat().doubleValue(),
							wh.getLng().doubleValue());

					if (distance < minDistance) {
						minDistance = distance;
						nearestWarehouse = wh;
					}
				}

				return nearestWarehouse;
			}

			return null;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
		final int R = 6371; // Radius of the earth in km

		double latDistance = Math.toRadians(lat2 - lat1);
		double lonDistance = Math.toRadians(lon2 - lon1);

		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return R * c; // Distance in km
	}
}