package com.octa.report.bcm.service;

import lombok.Data;

@Data
public class Person {
	
	
	private String name;
    private int age;
    private String city;
    
	public Person(String name, int age, String city) {
		super();
		this.name = name;
		this.age = age;
		this.city = city;
	}
    
    
}
