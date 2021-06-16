package com.example.demo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "建立訂單")
public class OrderReq {

	@ApiModelProperty(value = "購物車編號", required = true, position = 1)
	String cartNumber;

	@ApiModelProperty(value = "客戶名稱", required = true, position = 2)
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
