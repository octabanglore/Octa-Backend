package com.octa.modules.bean;

import lombok.Data;

@Data
public class Module {
	
	private String title;
    private String description;
    private String buttonText;
    
	public Module(String title, String description, String buttonText) {
		super();
		this.title = title;
		this.description = description;
		this.buttonText = buttonText;
	}

    

}
