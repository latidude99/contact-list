package com.latidude99.model;

public enum ContactProperty {
	
	FIRST_NAME("firstName"), 
	LAST_NAME("lastName"),
	EMAIL("email"),
	PHONE("phone"),
	STREET("street"),
	POSTCODE("postcode"),
	CITY("city"),
	COUNTRY("country"),
	DESCRIPTION("description"),
//	CREATED("created"),
//	UPDATED("updated"),
//	ALL("all")
	;
	
	private String text;
	ContactProperty(String t) {
		text = t;
	}
	
	public String getText() {
		return text;
	}

}
