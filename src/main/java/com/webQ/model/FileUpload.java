package com.webQ.model;

import org.springframework.web.multipart.MultipartFile;

public class FileUpload {
	public int length;
    public byte[] bytes;
    public String name;
    public String type;
	MultipartFile file;

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

}
