package com.example.demo.request;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "加入購物車")
public class addCartReq {
	// 購物車編號
	// String cartNumber;
	// 客戶名稱
	String customer;
	// 商品編號
	String productId;
	// 商品
	String productName;
	// 單價金額
	String unitPrice;
}
