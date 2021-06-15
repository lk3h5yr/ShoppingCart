package com.example.demo.service;

import java.util.List;
import java.util.Map;

import com.example.demo.entity.GoodsInfo;
import com.example.demo.request.AddCartReq;
import com.example.demo.shareddomain.dto.CartInfoDto;
import com.example.demo.shareddomain.dto.CartProductInfoDto;

public interface ICartInfoService {
	
	/**
	 * 呼叫 http://18.183.144.77:8080/goodses 取得所有商品
	 * 
	 * @return
	 */
	List<GoodsInfo> queryAllGoodsInfo();
	
	void addCartInfo(AddCartReq addCartReq);
	
	/**
	 * 呼叫 http://18.183.144.77:8080/goods/{goodsId} 取得商品
	 * 
	 * @param goodsId
	 * @return
	 */
	Map getGoodsBygoodsId(String goodsId);
	
	/**
	 * 取得購物資訊
	 * 
	 * @param cartNumber 購物車編號
	 * @return List<CartInfoDto>
	 */
	CartInfoDto getCartInfo(String cartNumber);
	
	/**
	 * 取得商品資訊
	 * 
	 * @param cartNumber 購物車編號
	 * @return
	 */
	List<CartProductInfoDto> getCartProductInfo(String cartNumber);
	
	void saveCartInfoDto(String cartNumber, String customer, Integer amount, String createdBy, String lastModifiedBy);
	
	void saveCartProductInfoDto(String cartNumber, String productId, String productName, Integer amount, String createdBy, String lastModifiedBy);
	
}
