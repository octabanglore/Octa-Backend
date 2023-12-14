package com.octa.report.bcm.service;

public class BCMFileConfiguration {
	
	private static final String defaultEncoding="UTF-8";
	private static final String defaultFont="Helvetica";
	private String encoding;
	private String pdfFont;
	private boolean isOnePLoagePerSheet;
	private boolean isDetectCellType;
	private boolean isRemoveEmptySpaceBetweenRows;
	private String maxPages;

	public static BCMFileConfiguration getDefaultConfiguration() {
		BCMFileConfiguration bcmFileConfiguration = new BCMFileConfiguration();
		bcmFileConfiguration.setEncoding(defaultEncoding);
		bcmFileConfiguration.setPdfFont(defaultFont);
		bcmFileConfiguration.setDetectCellType(Boolean.TRUE);
		bcmFileConfiguration.setOnePLoagePerSheet(Boolean.TRUE);
		bcmFileConfiguration.setRemoveEmptySpaceBetweenRows(Boolean.TRUE);
		bcmFileConfiguration.setMaxPages("300");
		return bcmFileConfiguration;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getPdfFont() {
		return pdfFont;
	}

	public void setPdfFont(String pdfFont) {
		this.pdfFont = pdfFont;
	}

	public boolean isOnePLoagePerSheet() {
		return isOnePLoagePerSheet;
	}

	public void setOnePLoagePerSheet(boolean isOnePLoagePerSheet) {
		this.isOnePLoagePerSheet = isOnePLoagePerSheet;
	}

	public boolean isDetectCellType() {
		return isDetectCellType;
	}

	public void setDetectCellType(boolean isDetectCellType) {
		this.isDetectCellType = isDetectCellType;
	}

	public boolean isRemoveEmptySpaceBetweenRows() {
		return isRemoveEmptySpaceBetweenRows;
	}

	public void setRemoveEmptySpaceBetweenRows(boolean isRemoveEmptySpaceBetweenRows) {
		this.isRemoveEmptySpaceBetweenRows = isRemoveEmptySpaceBetweenRows;
	}

	public String getMaxPages() {
		return maxPages;
	}

	public void setMaxPages(String maxPages) {
		this.maxPages = maxPages;
	}
}
