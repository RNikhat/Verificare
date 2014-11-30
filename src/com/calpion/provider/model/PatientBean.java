package com.calpion.provider.model;

public class PatientBean {

	private String provider;
	private String insurance;
	private String aptdate;
	private String reportstatus;
	private String priority;
	private String patient;
	private boolean selected;


	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getReportStatus() {
		return reportstatus;
	}

	public void setReportStatus(String reportstatus) {
		this.reportstatus = reportstatus;
	}

	public String getPatientName() {
		return patient;
	}

	public void setPatientName(String name) {
		this.patient = name;
	}

	public String getInsurance() {
		return insurance;
	}

	public void setInsurance(String insurance) {
		this.insurance = insurance;
	}

	public String getAptDate() {
		return aptdate;
	}

	public void setAptDate(String aptdate) {
		this.aptdate = aptdate;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}


}