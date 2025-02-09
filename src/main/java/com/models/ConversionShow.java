package com.models;

public class ConversionShow {

	private String FromUnitName;
	private int ConverSionQuantity;
	
	
	
	public ConversionShow(String fromUnitName, int converSionQuantity) {
		super();
		FromUnitName = fromUnitName;
		ConverSionQuantity = converSionQuantity;
	}
	
	public ConversionShow() {
		super();
	}

	public String getFromUnitName() {
		return FromUnitName;
	}
	public void setFromUnitName(String fromUnitName) {
		FromUnitName = fromUnitName;
	}
	public int getConverSionQuantity() {
		return ConverSionQuantity;
	}
	public void setConverSionQuantity(int converSionQuantity) {
		ConverSionQuantity = converSionQuantity;
	}	
}
