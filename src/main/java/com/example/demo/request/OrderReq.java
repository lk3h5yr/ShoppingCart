package com.example.demo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "�إ߭q��")
public class OrderReq {

	@ApiModelProperty(value = "�ʪ����s��", required = true, position = 1)
	String cartNumber;

	@ApiModelProperty(value = "�Ȥ�W��", required = true, position = 2)
	String customer;

	public String getCartNumber() {
		return cartNumber;
	}

	public void setCartNumber(String cartNumber) {
		this.cartNumber = cartNumber;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}
}
