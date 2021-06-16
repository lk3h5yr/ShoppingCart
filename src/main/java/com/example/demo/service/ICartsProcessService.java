package com.example.demo.service;

import com.example.demo.request.AddCartReq;
import com.example.demo.request.DeleteGoodsReq;
import com.example.demo.request.ModifyGoodsReq;
import com.example.demo.request.OrderReq;
import com.example.demo.request.QueryCartNoReq;
import com.example.demo.request.QueryCartReq;
import com.example.demo.request.QueryGoodsReq;
import com.example.demo.response.AddCartResp;
import com.example.demo.response.DeleteGoodsResp;
import com.example.demo.response.ModifyGoodsResp;
import com.example.demo.response.OrderResp;
import com.example.demo.response.QueryCartNoResp;
import com.example.demo.response.QueryCartResp;
import com.example.demo.response.QueryGoodsResp;

/**
 * CartsProcess 服務介面
 */
public interface ICartsProcessService {
	
	/**
	 * 取得外部所有商品
	 * @param queryGoodsReq
	 * @return
	 */
	QueryGoodsResp queryGoods(QueryGoodsReq queryGoodsReq);
	
	/**
	 * 刪除商品
	 * @param deleteGoodsReq
	 * @return
	 */
	DeleteGoodsResp deleteGoods(DeleteGoodsReq deleteGoodsReq);
	
	/**
	 * 修改商品數量
	 * @param modifyGoodsReq
	 * @return
	 */
	ModifyGoodsResp modifyGoods(ModifyGoodsReq modifyGoodsReq);
	
	/**
	 * 取得購物車清單
	 * @param queryCartReq
	 * @return
	 */
	QueryCartResp queryCarts(QueryCartReq queryCartReq);
	
	/**
	 * 取得商品詳細數量
	 * @param queryCartNoReq
	 * @return
	 */
	QueryCartNoResp queryCartsNo(QueryCartNoReq queryCartNoReq);
	
	/**
	 * 新增商品
	 * @param addCartReq
	 * @return
	 */
	AddCartResp addCart(AddCartReq addCartReq);
	
}
