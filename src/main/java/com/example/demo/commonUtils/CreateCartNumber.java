package com.example.demo.commonUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateCartNumber {
	
	public String createCode() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMhhmmss");
		String uid_pfix = format.format(new Date()); 
		return uid_pfix;
	}

}
