package com.example.demo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "�R���ӫ~")
public class DeleteGoodsReq {
	
	@ApiModelProperty(value = "�ӫ~�s��", required = true, position = 1)
	String productId;
	
	@ApiModelProperty(value = "�ʪ����s��", required = true, position = 2)
	String cartNumber;

	@ApiModelProperty(value = "�Ȥ�W��", required = true, position = 3)
	String customer;

	public String getProductId() {
		return productId;
	}

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

	public void setProductId(String productId) {
		this.productId = productId;
	}
	
}
