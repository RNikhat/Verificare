package com.calpion.provider.model;

public class PatientBean {

	private String fname;
	private String lname;
	private String dob;
	private String age;
	private String phone;
	private String email;
	private String id;
	private String gender;
	private String addressline1;
	private String addressline2;
	private String city;
	private String state;
	private String insurance;
	private String provider;
	private String ssn;
	private String network;
	private String physician;
	private String payerid;
	private String policyid;
	private String policyeffective;
	private String policyending;
	private String policyno;
	private String insurancerep;
	private String repphone;
	private Boolean ispproved;
	private String fileuploadid;
	private String appointmentdt;
	private String stattime;
	private String endtime;
	private String description;
	private String effectivedate;
	
	String getFirstName() {
		return fname;
	}

	public void setFirstName(String fname) {
		this.fname = fname;
	}

	public String getLastName() {
		return lname;
	}

	public void setLastName(String lname) {
		this.lname = lname;
	}

	public String getDOB() {
		return dob;
	}

	public void setDOB(String dob) {
		this.dob = dob;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getPhoneNumber() {
		return phone;
	}

	public void setPhoneNumber(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public String getEffectiveDate() {
		return effectivedate;
	}

	public void setEffectiveDate(String effectivedate) {
		this.effectivedate = effectivedate;
	}

	
}