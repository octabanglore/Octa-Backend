package com.octa.modules.dto;

import lombok.Data;

@Data
public class ModulesData {
	
	private Long moduleId;
	private String title;
    private String description;
    private String buttonText;
    private String imagePath = "";
    
	public ModulesData(Long moduleId, String title, String description, String buttonText) {
		super();
		this.moduleId = moduleId;
		this.title = title;
		this.description = description;
		this.buttonText = buttonText;
	}
	
	public ModulesData(Long moduleId, String title, String description, String buttonText, String imagePath) {
		super();
		this.moduleId = moduleId;
		this.title = title;
		this.description = description;
		this.buttonText = buttonText;
		this.imagePath = imagePath;
	}
}
