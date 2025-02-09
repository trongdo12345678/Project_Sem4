package com.models;

public class Product_spe {
	private int Id;
	private String Name_spe;
	private String Des_spe;
	private int Product_id;

	public Product_spe(int id, String name_spe, String des_spe, int product_id) {
		Id = id;
		Name_spe = name_spe;
		Des_spe = des_spe;
		Product_id = product_id;
	}

	public Product_spe() {
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getName_spe() {
		return Name_spe;
	}

	public void setName_spe(String name_spe) {
		this.Name_spe = name_spe;
	}

	public String getDes_spe() {
		return Des_spe;
	}

	public void setDes_spe(String des_spe) {
		Des_spe = des_spe;
	}

	public int getProduct_id() {
		return Product_id;
	}

	public void setProduct_id(int product_id) {
		Product_id = product_id;
	}

}
