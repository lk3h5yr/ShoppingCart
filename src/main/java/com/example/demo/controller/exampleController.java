package com.example.demo.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.GoodsInfo;
import com.example.demo.request.AddCartReq;
import com.example.demo.request.DeleteGoodsReq;
import com.example.demo.request.QueryCartNoReq;
import com.example.demo.request.QueryCartReq;
import com.example.demo.request.QueryGoodsReq;
import com.example.demo.response.AddCartResponse;
import com.example.demo.response.DeleteGoodsResponse;
import com.example.demo.response.QueryCartNoResponse;
import com.example.demo.response.QueryCartResponse;
import com.example.demo.response.QueryGoodsResponse;
import com.example.demo.service.ICartInfoService;
import com.example.demo.service.repository.CartInfoRepository;
import com.example.demo.service.repository.CartProductRepository;
import com.example.demo.shareddomain.dto.CartInfoDto;
import com.example.demo.shareddomain.dto.CartProductInfoDto;
import com.mysql.cj.util.StringUtils;

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
	public QueryGoodsResponse queryGoods(@ApiParam(required = true, value = "���o�Ҧ��ӫ~") @RequestBody QueryGoodsReq queryGoodsReq) {
		List<GoodsInfo> queryGoodsInfoList = cartInfoService.queryAllGoodsInfo();
		return QueryGoodsResponse.success(queryGoodsInfoList);
	}
	
	@ResponseBody
	@PostMapping(value = "/deleteGoods")
    @ApiOperation(value = "�R���ӫ~", notes = "�R���ӫ~")
	public DeleteGoodsResponse deleteGoods(@ApiParam(required = true, value = "�R���ӫ~") @RequestBody DeleteGoodsReq deleteGoodsReq) {
		Map deleteMap = cartInfoService.deleteGoods(deleteGoodsReq.getProductId());
		return DeleteGoodsResponse.success(deleteMap);
	}
	
	@ResponseBody
	@PostMapping(value = "/queryCarts")
    @ApiOperation(value = "���o�ʪ����M��", notes = "���o�ʪ����M��")
	public QueryCartResponse queryCarts(@ApiParam(required = true, value = "���o�ʪ����M��") @RequestBody QueryCartReq queryCartReq) {
		List<CartProductInfoDto> queryGoodsInfoList = cartProductRepository.findByCartNumberAndCreatedBy(queryCartReq.getCartNumber(), 
				queryCartReq.getCustomer());
		// �`���B
		Integer allAmount = new Integer(0);
		for (CartProductInfoDto temp : queryGoodsInfoList) {
			allAmount = allAmount + temp.getAmount();
		}
		
		return QueryCartResponse.success(queryGoodsInfoList, allAmount);
	}
	
	@ResponseBody
	@PostMapping(value = "/queryCartsNo")
    @ApiOperation(value = "���o�ӫ~�ԲӼƶq", notes = "���o�ӫ~�ԲӼƶq")
	public QueryCartNoResponse queryCartsNo(@ApiParam(required = true, value = "���o�ӫ~�ԲӼƶq") @RequestBody QueryCartNoReq queryCartNoReq) {
		List<CartProductInfoDto> queryGoodsInfoList = cartProductRepository.findByCartNumberAndCreatedByAndProductId(queryCartNoReq.getCartNumber(), 
				queryCartNoReq.getCustomer(), queryCartNoReq.getProductId());
		// �`���B
		Integer allAmount = new Integer(0);
		for (CartProductInfoDto temp : queryGoodsInfoList) {
			allAmount = allAmount + temp.getAmount();
		}
		
		return QueryCartNoResponse.success(queryGoodsInfoList, queryGoodsInfoList.size(), allAmount);
	}
	
	@ResponseBody
	@Transactional
	@PostMapping(value = "/addCart")
    @ApiOperation(value = "�[�J�ʪ���", notes = "�[�J�ʪ���")
	public AddCartResponse addCart(@ApiParam(required = true, value = "�[�J�ʪ���") @RequestBody AddCartReq addCartReq) {
		// �I�sAPI�d�߰ӫ~��T
		Map goodsMap = cartInfoService.getGoodsBygoodsId(addCartReq.getProductId());
		// �ӫ~�ƶq
		Integer inventory = Integer.valueOf(goodsMap.get("inventory").toString());
		// �ӫ~���B
		Integer amount = Integer.valueOf(goodsMap.get("unitPrice").toString());
		// �ʶR�H
		String customer = addCartReq.getCustomer();
		// �ӫ~�N��
		String goodsId = goodsMap.get("goodsId").toString();
		// �ӫ~�W��
		String goodsName = goodsMap.get("goodsName").toString();
		// �s�W �P ��� �ɶ�
		Date date = new Date();
		GoodsInfo goodsInfo = new GoodsInfo();
		
		// �I�sAPI�d�L�ӫ~�T��, ��^���~�T��
		if (goodsMap == null || inventory == 0) {
			return AddCartResponse.success(null);
		} 
		// ���ӫ~�T��
		else {
			
			// �ʪ����s��
			String cartNumber = addCartReq.getCartNumber();
			CartInfoDto cartInfoDto = cartInfoService.getCartInfo(cartNumber);
			List<CartProductInfoDto> cartProductInfoList = cartInfoService.getCartProductInfo(cartNumber);
			
			// �Y�ʪ�������T, �h�i���ʪ������B��s & �ӫ~�Բӷs�W
			if (cartInfoDto != null) {
				Integer amountTemp = cartInfoDto.getAmount() + amount;
				cartInfoService.saveCartInfoDto(cartNumber, customer, amountTemp, customer, customer, date);
				cartInfoService.saveCartProductInfoDto(cartNumber, goodsId, goodsName, amount, customer, customer, date);
				
			}
			// �ʪ����S��T, �����s�W
			else {
				// �s�W��� CART
				cartInfoService.saveCartInfoDto(cartNumber, customer, amount, customer, customer, date);
				// �s�W��� CART_PRODUCT
				if (CollectionUtils.isEmpty(cartProductInfoList)) {
					cartInfoService.saveCartProductInfoDto(cartNumber, goodsId, goodsName, amount, customer, customer, date);
				}
			}
			
			SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
			String formatDate = sdFormat.format(date);
			goodsInfo.setGoodsId(goodsId);
			goodsInfo.setGoodsName(goodsName);
			goodsInfo.setInventory(inventory);
			goodsInfo.setUnitPrice(amount);
			goodsInfo.setCreatedBy(customer);
			goodsInfo.setCreatedDate(formatDate);
			goodsInfo.setLastModifiedBy(customer);
			goodsInfo.setLastModifiedDate(formatDate);
		}
		
		return AddCartResponse.success(goodsInfo);
	}

}
