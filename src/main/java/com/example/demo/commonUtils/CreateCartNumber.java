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
	 * �۰ʥͦ��s���榡�GyyyyMMhhmmss
	 */
	public String createCode() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMhhmmss"); // �ɶ��r�겣�ͤ覡
		String uid_pfix = format.format(new Date()); 
		return uid_pfix;
	}

}
