package com.ad.android.ridesystems.passengercounter.model.vo;

public class SummaryViewVO {

	String trackingLevelName = "";
	
	String criteriaName = "";
	
	int quantity = 0;

	public String getTrackingLevelName() {
		return trackingLevelName;
	}

	public void setTrackingLevelName(String trackingLevelName) {
		this.trackingLevelName = trackingLevelName;
	}

	public String getCriteriaName() {
		return criteriaName;
	}

	public void setCriteriaName(String criteriaName) {
		this.criteriaName = criteriaName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public void addQuantity(int quantity) {
		this.quantity += quantity;
	}

	public String getName() {
		return getTrackingLevelName() + " / " + getCriteriaName();
	}
	
	
}
