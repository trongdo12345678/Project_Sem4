package com.models;

import java.util.List;

public class Product {
	private int Id;
	private String Product_name;
	private int Cate_id;
	private String Category_name;
    private int Brand_id;
    private int Unit_id;
    private String Unit_name;
    private String Brand_name;    
	private double Price;
	private String Img;
	private String Status;
	private String Description;
	private int Warranty_period;
	private int Length;
	private int Height;
	private int Width;
	private int Weight;
	private Double averageRating;
    private Integer totalFeedbacks;
    private List<Feedback> feedbacks;
	private String formattedPrice;
	private List<Product_img> listimg;
	private List<Product_spe> listspe;
	private List<Comment> listcomment;
	
	
	public List<Comment> getListcomment() {
		return listcomment;
	}

	public void setListcomment(List<Comment> listcomment) {
		this.listcomment = listcomment;
	}

	public List<Product_spe> getListspe() {
		return listspe;
	}

	public void setListspe(List<Product_spe> listspe) {
		this.listspe = listspe;
	}

	public String getFormattedPrice() {
		return formattedPrice;
	}

	public void setFormattedPrice(String formattedPrice) {
		this.formattedPrice = formattedPrice;
	}

	public List<Product_img> getListimg() {
		return listimg;
	}

	public void setListimg(List<Product_img> listimg) {
		this.listimg = listimg;
	}

	public Product(int id, String product_name, int cate_id, String category_name, int brand_id, int unit_id,
			String unit_name, String brand_name, double price, String img, String status, String description,
			int warranty_period, int length, int height, int width, int weight, Double averageRating,
			Integer totalFeedbacks, List<Feedback> feedbacks) {
		Id = id;
		Product_name = product_name;
		Cate_id = cate_id;
		Brand_id = brand_id;
		Unit_id = unit_id;
		Unit_name = unit_name;
		Brand_name = brand_name;
		Price = price;
		Img = img;
		Status = status;
		Description = description;
		Warranty_period = warranty_period;
		Length = length;
		Height = height;
		Width = width;
		Weight = weight;
		this.averageRating = averageRating;
		this.totalFeedbacks = totalFeedbacks;
		this.feedbacks = feedbacks;
	}
	
	public Product() {
	}
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getProduct_name() {
		return Product_name;
	}
	public void setProduct_name(String product_name) {
		Product_name = product_name;
	}
	public int getCate_id() {
		return Cate_id;
	}
	public void setCate_id(int cate_id) {
		Cate_id = cate_id;
	}
	public int getBrand_id() {
		return Brand_id;
	}
	public void setBrand_id(int brand_id) {
		Brand_id = brand_id;
	}
	public double getPrice() {
		return Price;
	}
	public void setPrice(double price) {
		Price = price;
	}
	public String getImg() {
		return Img;
	}
	public void setImg(String img) {
		Img = img;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public int getWarranty_period() {
		return Warranty_period;
	}
	public void setWarranty_period(int warranty_period) {
		Warranty_period = warranty_period;
	}
	public String getCategoryName() {
		return Category_name;
	}
	public void setCategoryName(String category_name) {
		Category_name = category_name;
	}
	public String getBrandName() {
		return Brand_name;
	}
	public void setBrandName(String brand_name) {
		Brand_name = brand_name;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}

	public int getUnit_id() {
		return Unit_id;
	}
	public void setUnit_id(int unit_id) {
		Unit_id = unit_id;
	}
	public String getUnit_name() {
		return Unit_name;
	}
	public void setUnit_name(String unit_name) {
		Unit_name = unit_name;
	}

	public int getLength() {
		return Length;
	}
	public void setLength(int length) {
		Length = length;
	}
	public int getHeight() {
		return Height;
	}
	public void setHeight(int height) {
		Height = height;
	}
	public int getWidth() {
		return Width;
	}
	public void setWidth(int width) {
		Width = width;
	}
	public int getWeight() {
		return Weight;
	}
	public void setWeight(int weight) {
		Weight = weight;
	}

	public String getCategory_name() {
		return Category_name;
	}

	public void setCategory_name(String category_name) {
		Category_name = category_name;
	}

	public String getBrand_name() {
		return Brand_name;
	}

	public void setBrand_name(String brand_name) {
		Brand_name = brand_name;
	}

	public Double getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(Double averageRating) {
		this.averageRating = averageRating;
	}

	public Integer getTotalFeedbacks() {
		return totalFeedbacks;
	}

	public void setTotalFeedbacks(Integer totalFeedbacks) {
		this.totalFeedbacks = totalFeedbacks;
	}

	public List<Feedback> getFeedbacks() {
		return feedbacks;
	}

	public void setFeedbacks(List<Feedback> feedbacks) {
		this.feedbacks = feedbacks;
	}
}
