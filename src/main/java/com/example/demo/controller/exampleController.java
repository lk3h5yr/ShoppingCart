package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.GoodsInfo;
import com.example.demo.request.AddCartReq;
import com.example.demo.request.QueryGoodsReq;
import com.example.demo.request.testReq;
import com.example.demo.response.queryGoodsResponse;
import com.example.demo.response.testResponse;
import com.example.demo.service.ICartInfoService;
import com.example.demo.service.repository.CartInfoRepository;
import com.example.demo.service.repository.CartProductRepository;
import com.example.demo.shareddomain.dto.CartInfoDto;
import com.example.demo.shareddomain.dto.CartProductInfoDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/")
@Api(tags = {"���ձM�פu�㱵�f"})
public class exampleController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ICartInfoService cartInfoService;
	
	@Autowired
	private CartInfoRepository cartInfoRepository;
	
	@Autowired
	private CartProductRepository cartProductRepository;
	
	
	/*@ResponseBody
	@PostMapping(value = "/getTest")
    @ApiOperation(value = "���ձM��", notes = "���ձM��")
	public testResponse testCartInfoDto(@ApiParam(required = true, value = "���ձM��") @RequestBody testReq testReq) {
		return testResponse.success(null);
	}*/
	
	@ResponseBody
	@PostMapping(value = "/queryGoods")
    @ApiOperation(value = "���o�Ҧ��ӫ~", notes = "���o�Ҧ��ӫ~")
	public queryGoodsResponse queryGoods(@ApiParam(required = true, value = "���o�Ҧ��ӫ~") @RequestBody QueryGoodsReq queryGoodsReq) {
		List<GoodsInfo> queryGoodsInfoList = cartInfoService.queryAllGoodsInfo();
		return queryGoodsResponse.success(queryGoodsInfoList);
	}
	
	@ResponseBody
	@Transactional
	@PostMapping(value = "/addCart")
    @ApiOperation(value = "�[�J�ʪ���", notes = "�[�J�ʪ���")
	public queryGoodsResponse addCart(@ApiParam(required = true, value = "�[�J�ʪ���") @RequestBody AddCartReq addCartReq) {
		// �I�sAPI�d�߰ӫ~��T
		Map goodsMap = cartInfoService.getGoodsBygoodsId(addCartReq.getProductId());
		// �ӫ~�ƶq
		Integer inventory = Integer.valueOf(goodsMap.get("inventory").toString());
		// �ӫ~���B
		Integer amount = Integer.valueOf(goodsMap.get("unitPrice").toString());
		// �ʶR�H
		String customer = addCartReq.getCustomer();
		
		// �I�sAPI�d�L�ӫ~�T��, ��^���~�T��
		if (goodsMap == null) {
			//...
		} 
		// ���ӫ~�T��
		else if (goodsMap != null && inventory != 0) {
			
			// �ʪ����s��
			String cartNumber = addCartReq.getCartNumber();
			CartInfoDto cartInfoDto = cartInfoService.getCartInfo(cartNumber);
			List<CartProductInfoDto> cartProductInfoList = cartInfoService.getCartProductInfo(cartNumber);
			
			// �Y�ʪ�������T, �h�i���ʪ������B��s & �ӫ~�Բӷs�W
			if (cartInfoDto != null) {
				Integer amountTemp = cartInfoDto.getAmount() + amount;
				cartInfoService.saveCartInfoDto(cartNumber, customer, amountTemp, customer, customer);
				cartInfoService.saveCartProductInfoDto(cartNumber, goodsMap.get("goodsId").toString(), goodsMap.get("goodsName").toString(), 
						amount, customer, customer);
				
			}
			// �ʪ����S��T, �����s�W
			else {
				// �s�W��� CART
				cartInfoService.saveCartInfoDto(cartNumber, customer, amount, customer, customer);
				// �s�W��� CART_PRODUCT
				if (CollectionUtils.isEmpty(cartProductInfoList)) {
					cartInfoService.saveCartProductInfoDto(cartNumber, goodsMap.get("goodsId").toString(), goodsMap.get("goodsName").toString(), 
							inventory, customer, customer);
				}
			}
		} else {
			// ...�ӫ~�ƶq��0, ��^���~�T��
		}
		
		return null;
//		return queryGoodsResponse.success(queryGoodsInfoList);
	}

}
