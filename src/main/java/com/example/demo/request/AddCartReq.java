package com.example.demo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "加入購物車")
public class AddCartReq {

	@ApiModelProperty(value = "購物車編號", required = true, position = 1)
	String cartNumber;

	@ApiModelProperty(value = "客戶名稱", required = true, position = 2)
	String customer;

	@ApiModelProperty(value = "商品編號", required = true, position = 3)
	String productId;
	
	// 商品
//	String productName;
	// 單價金額
//	String unitPrice;

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
