package com.example.demo.commonUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateCartNumber {

	/*public static void main(String[] args) {
		createCartNumber t = new createCartNumber();
		String g = t.createCode();
		System.out.println(g);
	}*/
	
	/**
	 * 自動生成編號格式：yyyyMMhhmmss
	 */
	public String createCode() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMhhmmss"); // 時間字串產生方式
		String uid_pfix = format.format(new Date()); 
		return uid_pfix;
	}

}
