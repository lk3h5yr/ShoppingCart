package com.example.demo.entity;

public class OrderInfo {
	
	// �ӫ~�s��
	private String goodsId;
	
	// �ӫ~�W��
	private String goodsName;
	
	// �ӫ~���
	private Integer unitPrice;
	
	// �ӫ~�ƶq
	private Integer count;
	
	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public Integer getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Integer unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

}
