package com.webQ.Validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.webQ.model.FileUpload;

public class FileUploadValidator implements Validator{
	 
	@Override
	public boolean supports(Class clazz) {
		//just validate the FileUpload instances
		return FileUpload.class.isAssignableFrom(clazz);
	}
 
	@Override
	public void validate(Object target, Errors errors) {
 
		FileUpload file = (FileUpload)target;
 
		if(file.getFile().getSize()==0){
			errors.rejectValue("file", "Please select a file");
		}
	}
}