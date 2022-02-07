package com.poscoict.jblog.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadService {
	
	private static String SAVE_PATH = "/upload-images";
	private static String URL_BASE = "/assets/images";
	
	public String restore(MultipartFile multipartFile){
		String url=null;
		
		try {
		if(multipartFile.isEmpty()) {
			return url;
		}
		String originalFilename = multipartFile.getOriginalFilename();
		String endName = originalFilename.substring(originalFilename.indexOf('.')+1);
		String saveFileName = generateSaveFileName(endName);
		long fileSize = multipartFile.getSize();
//		System.out.println("##################" + originalFilename);
//		System.out.println("##################" + fileSize);
//		System.out.println("##################" + saveFileName);
//		System.out.println("##################" + endName);
		byte[] data = multipartFile.getBytes();
		OutputStream os = new FileOutputStream(SAVE_PATH + "/" + saveFileName);
		os.write(data);
		os.close();
		url = URL_BASE + "/" + saveFileName;
;		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("file upload error : " + e);
		}
//		System.out.println(url);
		return url;
		
	}
	
	public String generateSaveFileName(String endName) {
		String filename = "";
		Calendar calendar = Calendar.getInstance();
		filename+=calendar.get(Calendar.YEAR);
		filename+=(calendar.get(Calendar.MONTH)+1);
		filename+=calendar.get(Calendar.DATE);
		filename+=calendar.get(Calendar.MINUTE);
		filename+=calendar.get(Calendar.SECOND);
		filename+=calendar.get(Calendar.MILLISECOND);
		filename+=("." + endName);
		
		return filename;
		
	}
}
