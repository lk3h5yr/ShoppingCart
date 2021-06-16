package com.example.demo.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    @ApiOperation(value = "���o�~���Ҧ��ӫ~", notes = "���o�~���Ҧ��ӫ~")
	public QueryGoodsResp queryGoods(@ApiParam(required = true, value = "���o�~���Ҧ��ӫ~") @RequestBody QueryGoodsReq queryGoodsReq) {
		List<GoodsInfo> queryGoodsInfoList = cartInfoService.queryAllGoodsInfo();
		return QueryGoodsResp.success(queryGoodsInfoList);
	}
	
	@ResponseBody
	@Transactional
	@PostMapping(value = "/orderSend")
    @ApiOperation(value = "�إ߭q��", notes = "�إ߭q��")
	public OrderResp orderSend(@ApiParam(required = true, value = "�إ߭q��") @RequestBody OrderReq queryGoodsReq) {
//		List<GoodsInfo> queryGoodsInfoList = cartInfoService.queryAllGoodsInfo();
		return OrderResp.success();
	}
	
	@ResponseBody
	@Transactional
	@PostMapping(value = "/deleteGoods")
    @ApiOperation(value = "�R���ӫ~", notes = "�R���ӫ~")
	public DeleteGoodsResp deleteGoods(@ApiParam(required = true, value = "�R���ӫ~") @RequestBody DeleteGoodsReq deleteGoodsReq) {
		List<CartProductInfoDto> queryGoodsInfoList = cartProductRepository.findByCartNumberAndCreatedByAndProductId(deleteGoodsReq.getCartNumber(), 
				deleteGoodsReq.getCustomer(), deleteGoodsReq.getProductId());
		// ���o���R�����ӫ~�ƶq
		int productNo = deleteGoodsReq.getProductNo();
		
		// �ˮֿ�J�R���ƶq�O�_�W�L��ܰӫ~���ʶR�ƶq
		if (productNo > queryGoodsInfoList.size() || CollectionUtils.isEmpty(queryGoodsInfoList)) {
			//.. ��^���~�T��
		}
		// �p�G�R���ƶq�P�ʶR�ƶq�@�P�h���ƧR��
		if (queryGoodsInfoList.size() == productNo) {
			CartInfoDto  cartInfoDto  = cartInfoRepository.getCartInfoDtoBycartNumber(deleteGoodsReq.getCartNumber());
			if (cartInfoDto == null) {
				//..��^���~�T��
			}
			cartInfoRepository.delete(cartInfoDto);
			logger.info("cartInfoRepository.delete: " + cartInfoDto);
			cartProductRepository.deleteAll(queryGoodsInfoList);
			logger.info("cartProductRepository.deleteAll: " + queryGoodsInfoList);
			return DeleteGoodsResp.success();
			
		} else {
			List<CartProductInfoDto> newGoodsInfoList = new ArrayList<CartProductInfoDto>();
			int no = queryGoodsInfoList.size() - productNo;
			for (int i = 0; queryGoodsInfoList.size() < no; i++) {
				newGoodsInfoList.add(queryGoodsInfoList.get(i));
			}
			cartProductRepository.deleteAll(queryGoodsInfoList);
			logger.info("cartProductRepository.deleteAll: " + queryGoodsInfoList);
			cartProductRepository.saveAll(newGoodsInfoList);
			logger.info("cartProductRepository.saveAll: " + newGoodsInfoList);
			return DeleteGoodsResp.success();
		}
	}
	
	@ResponseBody
	@Transactional
	@PostMapping(value = "/modifyGoods")
    @ApiOperation(value = "�ק�ӫ~�ƶq", notes = "�ק�ӫ~�ƶq")
	public ModifyGoodsResp modifyGoods(@ApiParam(required = true, value = "�ק�ӫ~�ƶq") @RequestBody ModifyGoodsReq modifyGoodsReq) {
		List<CartProductInfoDto> queryGoodsInfoList = cartProductRepository.findByCartNumberAndCreatedByAndProductId(modifyGoodsReq.getCartNumber(), 
				modifyGoodsReq.getCustomer(), modifyGoodsReq.getProductId());
		CartInfoDto cartInfoDto = cartInfoService.getCartInfo(modifyGoodsReq.getCartNumber());
		// ���o���ק諸�ӫ~�ƶq
		int productNo = modifyGoodsReq.getProductNo();
		// �ק�᪺���B
		int modifyAmount = new Integer(0);
		// �ˮֿ�J�ק�ƶq�O�_�W�L��ܰӫ~���ʶR�ƶq
		if (productNo > queryGoodsInfoList.size() || CollectionUtils.isEmpty(queryGoodsInfoList)) {
			//.. ��^���~�T��
		}
		// �p�G�ק�ƶq�P�ʶR�ƶq�@�P�h��^���~�T��
		if (queryGoodsInfoList.size() == productNo) {
			//..��^���~�T��
		} else {
			List<CartProductInfoDto> newGoodsInfoList = new ArrayList<CartProductInfoDto>();
			// ��q
			if (productNo < queryGoodsInfoList.size()) {
				int no = queryGoodsInfoList.size() - productNo;
				for (int i = 0; queryGoodsInfoList.size() < no; i++) {
					modifyAmount = modifyAmount + queryGoodsInfoList.get(i).getAmount();
					newGoodsInfoList.add(queryGoodsInfoList.get(i));
				}
				
				cartProductRepository.deleteAll(queryGoodsInfoList);
				logger.info("cartProductRepository.deleteAll: " + queryGoodsInfoList);
				cartProductRepository.saveAll(newGoodsInfoList);
				logger.info("cartProductRepository.saveAll: " + newGoodsInfoList);
			} 
			// �l�[
			else {
				// ���o�ӫ~�s��
				String producId = queryGoodsInfoList.get(0).getProductId();
				Map goodsMap = cartInfoService.getGoodsBygoodsId(producId);
				// �ӫ~�ƶq
				int inventory = Integer.valueOf(goodsMap.get("inventory").toString());
				// �ӫ~���B
				int amount = Integer.valueOf(goodsMap.get("unitPrice").toString());
				// �ӫ~�W��
				String goodsName = goodsMap.get("goodsName").toString();
				// �Y�ӫ~�ƶq�k0
				if (inventory == 0) {
					//..��^���~�T��
				}
				cartInfoService.saveCartProductInfoDto(modifyGoodsReq.getCartNumber(), producId, goodsName, amount, modifyGoodsReq.getCustomer(),
						modifyGoodsReq.getCustomer(), new Date());
				modifyAmount = modifyAmount + amount;
			}
			
			// �ק��ʪ����`���B
			cartInfoDto.setAmount(modifyAmount);
			cartInfoDto.setLastModifiedBy(modifyGoodsReq.getCustomer());
			cartInfoDto.setLastModifiedDate(new Date());
			cartInfoRepository.save(cartInfoDto);
			logger.info("cartInfoRepository.save: " + cartInfoDto);
		}
		return ModifyGoodsResp.success();
	}
	
	@ResponseBody
	@PostMapping(value = "/queryCarts")
    @ApiOperation(value = "���o�ʪ����M��", notes = "���o�ʪ����M��")
	public QueryCartResp queryCarts(@ApiParam(required = true, value = "���o�ʪ����M��") @RequestBody QueryCartReq queryCartReq) {
		List<CartProductInfoDto> queryGoodsInfoList = cartProductRepository.findByCartNumberAndCreatedBy(queryCartReq.getCartNumber(), 
				queryCartReq.getCustomer());
		// �`���B
		Integer allAmount = new Integer(0);
		for (CartProductInfoDto temp : queryGoodsInfoList) {
			allAmount = allAmount + temp.getAmount();
		}
		
		return QueryCartResp.success(queryGoodsInfoList, allAmount);
	}
	
	@ResponseBody
	@PostMapping(value = "/queryCartsNo")
    @ApiOperation(value = "���o�ӫ~�ԲӼƶq", notes = "���o�ӫ~�ԲӼƶq")
	public QueryCartNoResp queryCartsNo(@ApiParam(required = true, value = "���o�ӫ~�ԲӼƶq") @RequestBody QueryCartNoReq queryCartNoReq) {
		List<CartProductInfoDto> queryGoodsInfoList = cartProductRepository.findByCartNumberAndCreatedByAndProductId(queryCartNoReq.getCartNumber(), 
				queryCartNoReq.getCustomer(), queryCartNoReq.getProductId());
		// �`���B
		Integer allAmount = new Integer(0);
		for (CartProductInfoDto temp : queryGoodsInfoList) {
			allAmount = allAmount + temp.getAmount();
		}
		
		return QueryCartNoResp.success(queryGoodsInfoList, queryGoodsInfoList.size(), allAmount);
	}
	
	@ResponseBody
	@Transactional
	@PostMapping(value = "/addCart")
    @ApiOperation(value = "�s�W�ӫ~", notes = "�s�W�ӫ~")
	public AddCartResp addCart(@ApiParam(required = true, value = "�s�W�ӫ~") @RequestBody AddCartReq addCartReq) {
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
			return AddCartResp.success(null);
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
		
		return AddCartResp.success(goodsInfo);
	}

}
