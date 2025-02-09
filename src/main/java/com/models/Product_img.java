package com.models;

public class Product_img {
	private int Id;
	private String Img_url;
	private int Product_id;
	private String Product_name;
	public Product_img() {
	}
	public Product_img(int id, String img_url, int product_id) {
		Id = id;
		Img_url = img_url;
		Product_id = product_id;
	}
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getImg_url() {
		return Img_url;
	}
	public void setImg_url(String img_url) {
		Img_url = img_url;
	}
	public int getProduct_id() {
		return Product_id;
	}
	public void setProduct_id(int product_id) {
		Product_id = product_id;
	}
	public String getProduct_name() {
		return Product_name;
	}
	public void setProduct_name(String product_name) {
		Product_name = product_name;
	}
}
