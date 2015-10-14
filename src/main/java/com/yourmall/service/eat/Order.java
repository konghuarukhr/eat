package com.yourmall.service.eat;

public class Order {
	private String eatterName;
	private String eattingDate;
	
	public Order(String eatterName, String eattingDate) {
		this.eatterName = eatterName;
		this.eattingDate = eattingDate;
	}
	
	@Override
	public String toString() {
		return "eatterName: " + eatterName + "; eattingDate: " + eattingDate;
	}
	
	public String getName() {
		return eatterName;
	}
	
	public String getDate() {
		return eattingDate;
	}
}
