package com.octa.report.bcm.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.stereotype.Service;

@Service
public class ZipUtility {
	
	public byte[] downloadMultipleFiles(String directoryPath) {
    	try {
    		
    		String zipFilePath = directoryPath+"Consolidated_Report.zip";
    		File directory = new File(directoryPath);
    		File[] files = directory.listFiles();
    		List<File> filesList =  List.of(files != null ? files : new File[0]);
    		return createZipFile(filesList,zipFilePath);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	return null;
    }
	
	private byte[] createZipFile(List<File> files, String zipFilePath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(zipFilePath);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            for (File file : files) {
                addToZip(file, zos);
            }

            return readZipFile(zipFilePath);
        }
    }

    private void addToZip(File file, ZipOutputStream zos) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            ZipEntry zipEntry = new ZipEntry(file.getName());
            zos.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zos.write(bytes, 0, length);
            }

            zos.closeEntry();
        }
    }

    private byte[] readZipFile(String zipFilePath) throws IOException {
        try (FileInputStream fis = new FileInputStream(zipFilePath);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }

            return bos.toByteArray();
        }
    }
    
}
