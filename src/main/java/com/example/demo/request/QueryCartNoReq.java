package com.example.demo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "查詢商品數量")
public class QueryCartNoReq {

	@ApiModelProperty(value = "購物車編號", required = true, position = 1)
	String cartNumber;

	@ApiModelProperty(value = "客戶名稱", required = true, position = 2)
	String customer;

	@ApiModelProperty(value = "商品編號", required = true, position = 3)
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
