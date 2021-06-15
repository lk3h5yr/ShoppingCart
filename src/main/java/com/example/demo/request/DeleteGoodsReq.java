package com.example.demo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "刪除商品")
public class DeleteGoodsReq {
	
	@ApiModelProperty(value = "商品編號", required = true, position = 1)
	String productId;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}
}
