package com.example.demo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "�d�߰ӫ~�ƶq")
public class QueryCartNoReq {

	@ApiModelProperty(value = "�ʪ����s��", required = true, position = 1)
	String cartNumber;

	@ApiModelProperty(value = "�Ȥ�W��", required = true, position = 2)
	String customer;

	@ApiModelProperty(value = "�ӫ~�s��", required = true, position = 3)
	String productId;
	
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

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}
}
