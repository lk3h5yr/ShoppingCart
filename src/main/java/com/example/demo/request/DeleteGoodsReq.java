package com.example.demo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "�R���ӫ~")
public class DeleteGoodsReq {
	
	@ApiModelProperty(value = "�ӫ~�s��", required = true, position = 1)
	String productId;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}
}
