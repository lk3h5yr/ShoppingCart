package com.example.demo.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.example.demo.entity.GoodsInfo;
import com.example.demo.request.AddCartReq;
import com.example.demo.shareddomain.dto.CartInfoDto;
import com.example.demo.shareddomain.dto.CartProductInfoDto;

public interface ICartInfoService {
	
	/**
	 * �I�s http://18.183.144.77:8080/goodses ���o�Ҧ��ӫ~
	 * 
	 * @return
	 */
	List<GoodsInfo> queryAllGoodsInfo();
	
	/**
	 * �I�s http://18.183.144.77:8080/goods/{goodsId} ���o�ӫ~
	 * 
	 * @param goodsId
	 * @return
	 */
	Map getGoodsBygoodsId(String goodsId);
	
	/**
	 * ���o�ʪ���T
	 * 
	 * @param cartNumber �ʪ����s��
	 * @return List<CartInfoDto>
	 */
	CartInfoDto getCartInfo(String cartNumber);
	
	/**
	 * ���o�ӫ~��T
	 * 
	 * @param cartNumber �ʪ����s��
	 * @return
	 */
	List<CartProductInfoDto> getCartProductInfo(String cartNumber);
	
	/**
	 * �R���ӫ~
	 * 
	 * @param goodsId �ӫ~�s��
	 */
	Map deleteGoods(String goodsId);
	
	/**
	 * �x�s��ƶi�ʪ���
	 * 
	 * @param cartNumber �ʪ����s��
	 * @param customer �ʶR�H
	 * @param amount ���B
	 * @param createdBy �ʶR�H
	 * @param lastModifiedBy �ʶR�H
	 * @param date 
	 */
	void saveCartInfoDto(String cartNumber, String customer, Integer amount, String createdBy, String lastModifiedBy, Date date);
	
	/**
	 * �x�s��ƶi�ӫ~�M��
	 * 
	 * @param cartNumber �ʪ����s��
	 * @param productId �ӫ~�s��
	 * @param productName �ӫ~�W��
	 * @param amount ���B
	 * @param createdBy �ʶR�H
	 * @param lastModifiedBy �ʶR�H
	 * @param date
	 */
	void saveCartProductInfoDto(String cartNumber, String productId, String productName, Integer amount, String createdBy, String lastModifiedBy, Date date);
	
}
