package com.example.demo.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.example.demo.entity.GoodsInfo;
import com.example.demo.entity.OrderInfo;
import com.example.demo.request.OrderReq;
import com.example.demo.response.OrderResp;
import com.example.demo.shareddomain.dto.CartInfoDto;
import com.example.demo.shareddomain.dto.CartProductInfoDto;

@SuppressWarnings({"rawtypes","unused"})
public interface ICartInfoService {
	
	/**
	 * 呼叫 http://18.183.144.77:8080/goodses 取得所有商品
	 * 
	 * @return
	 */
	List<GoodsInfo> queryAllGoodsInfo();
	
	/**
	 * 呼叫 http://18.183.144.77:8080/goods/{goodsId} 取得商品
	 * 
	 * @param goodsId
	 * @return
	 */
	Map getGoodsBygoodsId(String goodsId);
	
	/**
	 * 呼叫 http://18.183.144.77:8080/goods/1297/{0}/inventory 更新商品庫存
	 * @param inventory
	 * @param goodsId
	 */
	void cheangInventory(Map<String, String> inventory, String goodsId);
	
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
	
	/**
	 * 刪除商品
	 * 
	 * @param goodsId 商品編號
	 */
	Map deleteGoods(String goodsId);
	
	/**
	 * 建立訂單
	 * @param queryGoodsReq
	 * @return
	 */
	Map orderSend(Map orderMap);
	
	/**
	 * 儲存資料進購物車
	 * 
	 * @param cartNumber 購物車編號
	 * @param customer 購買人
	 * @param amount 金額
	 * @param createdBy 購買人
	 * @param lastModifiedBy 購買人
	 * @param date 
	 */
	void saveCartInfoDto(String cartNumber, String customer, Integer amount, String createdBy, String lastModifiedBy, Date date);
	
	/**
	 * 儲存資料進商品清單
	 * 
	 * @param cartNumber 購物車編號
	 * @param productId 商品編號
	 * @param productName 商品名稱
	 * @param amount 金額
	 * @param createdBy 購買人
	 * @param lastModifiedBy 購買人
	 * @param date
	 */
	void saveCartProductInfoDto(String cartNumber, String productId, String productName, Integer amount, String createdBy, String lastModifiedBy, Date date);

}
