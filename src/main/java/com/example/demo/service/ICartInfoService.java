package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.GoodsInfo;
import com.example.demo.request.addCartReq;

public interface ICartInfoService {
	
	List<GoodsInfo> queryAllGoodsInfo();
	
	void addCartInfo(addCartReq addCartReq);
	
}
