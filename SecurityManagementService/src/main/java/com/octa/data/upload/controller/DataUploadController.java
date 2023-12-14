package com.octa.data.upload.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.octa.data.upload.service.DataUploadService;

@RestController
@RequestMapping("/api/v1/uploadData")
public class DataUploadController {
	
	@Autowired
	DataUploadService dataUploadService;
	
	@PostMapping("/upload")
    public String validateandUploadData(@RequestParam("file") MultipartFile file) {
		System.out.println("Test data Upload");
        if (file.isEmpty()) {
            return "Please select a file to upload.";
        }
        try {
        	dataUploadService.readDataFromXLS(file);
            return "File uploaded and data saved successfully!";
        } catch (Exception e) {
            return "Error uploading file: " + e.getMessage();
        }
    }

}
