package com.example.demo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "刪除商品")
public class DeleteGoodsReq {
	
	@ApiModelProperty(value = "商品編號", required = true, position = 1)
	String productId;
	
	@ApiModelProperty(value = "購物車編號", required = true, position = 2)
	String cartNumber;

	@ApiModelProperty(value = "客戶名稱", required = true, position = 3)
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
