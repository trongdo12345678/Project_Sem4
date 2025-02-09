package com.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.jdbc.core.RowMapper;

import com.customer.repository.FeedbackRepository;
import com.models.Product;
import com.utils.Views;

public class Product_mapper implements RowMapper<Product> {
	public Product mapRow(ResultSet rs, int RowNum) throws SQLException {
		Product item = new Product();
		item.setId(rs.getInt(Views.COL_PRODUCT_ID));
		item.setBrand_id(rs.getInt(Views.COL_PRODUCT_BRAND_ID));
		item.setCate_id(rs.getInt(Views.COL_PRODUCT_CATE_ID));
		item.setProduct_name(rs.getString(Views.COL_PRODUCT_NAME));
		item.setDescription(rs.getString(Views.COL_PRODUCT_DESCIPTION));
		item.setImg(rs.getString(Views.COL_PRODUCT_IMG));
		item.setStatus(rs.getString(Views.COL_PRODUCT_STATUS));
		item.setPrice(rs.getDouble(Views.COL_PRODUCT_PRICE));
		item.setWarranty_period(rs.getInt(Views.COL_PRODUCT_WARRANTY_PERIOD));
		item.setUnit_id(rs.getInt(Views.COL_PRODUCT_UNIT_ID));
		item.setStatus(rs.getString(Views.COL_PRODUCT_STATUS));

		item.setHeight(rs.getInt(Views.COL_PRODUCT_HEIGHT));
		item.setWeight(rs.getInt(Views.COL_PRODUCT_WELGHT));
		item.setLength(rs.getInt(Views.COL_PRODUCT_LENGTH));
		item.setHeight(rs.getInt(Views.COL_PRODUCT_HEIGHT));
		item.setBrandName(rs.getString("brand_name"));
		item.setCategoryName(rs.getString("category_name"));
		item.setUnit_name(rs.getString("unit_name"));
		
		
		 try { FeedbackRepository feedbackRepo = MapperHelper.getFeedbackRepo(); if
		 (feedbackRepo != null) { int productId = item.getId();
		  item.setAverageRating(feedbackRepo.calculateAverageRating(productId));
		 item.setFeedbacks(feedbackRepo.getProductFeedbacks(productId));
		 item.setTotalFeedbacks(item.getFeedbacks().size()); } } catch (Exception e) {
		 System.err.println("Error getting feedback data: " + e.getMessage());
		 item.setAverageRating(0.0); item.setFeedbacks(new ArrayList<>());
		 item.setTotalFeedbacks(0); }
		 
		 

		return item;
	}
}