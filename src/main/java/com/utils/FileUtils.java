package com.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public class FileUtils {
	public static String uploadFileImage(MultipartFile file, String parentFolder, String subFolder) {
	    try {
	        Path uploadPath = subFolder != null ? Paths.get(parentFolder, subFolder) : Paths.get(parentFolder);
	        if (!Files.exists(uploadPath)) {
	            Files.createDirectories(uploadPath);
	        }
	        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
	        Path filePath = uploadPath.resolve(fileName);
	        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
	        return subFolder != null ? subFolder + "/" + fileName : fileName;
	    } catch (IOException e) {
	        throw new RuntimeException("Lỗi khi upload file: " + file.getOriginalFilename(), e);
	    }
	}

	    public static String getBase64EncodedImage(String folderName, String imageName) {
	        String folderUpload = System.getProperty("user.dir") + "/" + folderName;
	        String filePath = folderUpload + "/" + imageName;

	        try (FileInputStream fin = new FileInputStream(filePath)) {
	            byte[] data = fin.readAllBytes();
	            return Base64.getEncoder().encodeToString(data);
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        return "";
	    }
	    public static String deleteFile(String folderName, String fileName) {
	        try {
	            Path pathFile = Path.of(System.getProperty("user.dir"), folderName, fileName);
	            System.out.println("Attempting to delete file: " + pathFile.toString());
	            if (Files.exists(pathFile)) {
	                Files.delete(pathFile);
	                return "file deleted";
	            } else {
	                return "file not found at path: " + pathFile.toString();
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	            return "file delete failed: " + e.getMessage();
	        }
	    }





}
