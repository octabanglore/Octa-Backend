package com.octa.modules.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.octa.modules.bean.Module;

@Service
public class ModuleService {
	
	public Map<String, Module> getModuleData() {
		
		Map<String, Module> moduleData = new LinkedHashMap<String, Module>();
		
		Module vendorsDetails = new Module("Vendors Details", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod", "Inspect");
        Module productCatalogue = new Module("Product Catalogue", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod", "Inspect");
        Module purchaseOrder = new Module("Purchase Order", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod", "Inspect");
        Module insights = new Module("Insights", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod", "Inspect");
        Module inventoryManage = new Module("Inventory Manage", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod", "Inspect");
        Module approvals = new Module("Approvals", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod", "Inspect");
        
        moduleData.put("vendorsDetails", vendorsDetails);
        moduleData.put("productCatalogue", productCatalogue);
        moduleData.put("purchaseOrder", purchaseOrder);
        moduleData.put("insights", insights);
        moduleData.put("inventoryManage", inventoryManage);
        moduleData.put("approvals", approvals);
		
        return moduleData;
	}

	public Map<String, String> getTopbarData() {
		Map<String, String> topBarData = new HashMap<String, String>();

		topBarData.put("bulletins", "Bulletins");
		topBarData.put("help", "Help");
		topBarData.put("modules", "Modules");
		topBarData.put("tasks", "Tasks");
		topBarData.put("dashboard", "Dashboards");

		return topBarData;
	}
	
	

}
