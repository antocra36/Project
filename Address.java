package com.cognixia.jump.jdbc.ems;

public class Address {
	private int addId;
	private int streetNum;
	private String streetName;
	private String city;
	private String state;
	private int zipCode;
//	private static int counter;
	public Address(int addId, int streetNum, String streetName, String city, String state, int zipCode) {
		this.addId = addId;
		this.streetNum = streetNum;
		this.streetName = streetName;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
	}
	public int getAddId() {
		return addId;
	}
	public void setAddId(int addId) {
		this.addId = addId;
	}
	public int getStreetNum() {
		return streetNum;
	}
	public void setStreetNum(int streetNum) {
		this.streetNum = streetNum;
	}
	public String getStreetName() {
		return streetName;
	}
	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public int getZipCode() {
		return zipCode;
	}
	public void setZipCode(int zipCode) {
		this.zipCode = zipCode;
	}
	public String toString() {
		return "Address ID: " + addId + "\n" + streetNum + " " + streetName + "\n" + city + ", " + state + " " + zipCode;
	}
}