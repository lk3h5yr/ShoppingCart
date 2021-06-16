package com.example.demo.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.GoodsInfo;
import com.example.demo.entity.OrderInfo;
import com.example.demo.exceptions.CartsProcessException;
import com.example.demo.exceptions.ExceptionStatus;
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
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/")
@Api(tags = {"�ʪ����A�Ȥ���API"})
@ApiResponses({@ApiResponse(code = 200, message = "Successfully."),
	@ApiResponse(code = 400, message = "Bad Request."),
	@ApiResponse(code = 404, message = "Not found Error."),
	@ApiResponse(code = 500, message = "Internal Server Error.")}) 
@SuppressWarnings({"rawtypes","unchecked","unused"})
public class CartsProcessController {
	
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
	@GetMapping(value = "/queryGoods")
    @ApiOperation(value = "���o�~���Ҧ��ӫ~", notes = "���o�~���Ҧ��ӫ~")
	public QueryGoodsResp queryGoods(@ApiParam(required = true, value = "���o�~���Ҧ��ӫ~") @RequestBody QueryGoodsReq queryGoodsReq) {
		List<GoodsInfo> queryGoodsInfoList = cartInfoService.queryAllGoodsInfo();
		return QueryGoodsResp.success(queryGoodsInfoList);
	}
	
	@ResponseBody
	@Transactional
	@PostMapping(value = "/orderSend")
    @ApiOperation(value = "�إ߭q��", notes = "�إ߭q��")
	public OrderResp orderSend(@ApiParam(required = true, value = "�إ߭q��") @RequestBody OrderReq orderReq) {
		// ��q��إ߫e�z�L /goods/{goodsId}/inventory �ܧ�w�s
		List<CartProductInfoDto> queryGoodsInfoList = cartProductRepository.findByCartNumberAndCreatedBy(orderReq.getCartNumber(), 
				orderReq.getCustomer());
		List<String> productIdList = new ArrayList<String>();
		Map orderResp = new HashMap();
		// ���Ҧ��ʪ����������ӫ~�N��
		queryGoodsInfoList.forEach( tempGppdsInfo -> {
			String productId = tempGppdsInfo.getProductId();
			if (!productIdList.contains(productId)) {
				productIdList.add(tempGppdsInfo.getProductId());
			}
		});
		
		// Map<�ӫ~�N��, �`�ƶq>
		Map<String, Integer> productIdMap = new HashMap();
		// �I�s�~��API �d�Ӱӫ~�{�b���h�ּƶq
		Map<String, Integer> goodsMap = new HashMap();
		
		try {
			// �� ��s�w�s
			if (!CollectionUtils.isEmpty(productIdList)) {
				productIdList.forEach( tempProductId -> {
					// �d�߸�Ʈw���U�ӫ~�U���X��
					int cartProductCount = cartProductRepository.countByProductId(tempProductId);
					
					// �d�ߧ��w�s�e �t�Φ��h�֮w�s
					Map tempGoodsMap = cartInfoService.getGoodsBygoodsId(tempProductId);
					int inventory = Integer.valueOf(tempGoodsMap.get("inventory").toString());
					
					if (cartProductCount > inventory) {
						throw new CartsProcessException("�ӫ~�w�s����!", ExceptionStatus.INPUT_PARAMETER_ERROR, HttpStatus.BAD_REQUEST.value());
					}
					// �~���w�s����
					goodsMap.put(tempProductId, inventory);
					// ��Ʈw���U�ӫ~����
					productIdMap.put(tempProductId, cartProductCount);
					// �~���w�s �� ���t�θ�Ʈw�ƶq = ��s�᪺��Ƶ���
					int finallyInventory = inventory - cartProductCount;
					// ��s�w�s
					this.cheangInventory(finallyInventory, tempProductId);
				});
			}
			// �� �e�X�q��
			Map orderMap = new HashMap();
			// �`���B
			Integer allAmount = 0;
			if (!CollectionUtils.isEmpty(productIdList)) {
				List<OrderInfo> orderInfoList = new ArrayList<OrderInfo>();
				// �հe�q����
				for (int i = 0; i < productIdList.size(); i++) {
					List<CartProductInfoDto> cartProductInfoDtoList = cartProductRepository.findByCartNumberAndCreatedByAndProductId(orderReq.getCartNumber(), 
							orderReq.getCustomer(), productIdList.get(i));
					int count = cartProductInfoDtoList.size();
					int amount = cartProductInfoDtoList.get(i).getAmount();
					OrderInfo orderInfo = new OrderInfo();
					orderInfo.setCount(count);
					orderInfo.setGoodsId(productIdList.get(i));
					orderInfo.setGoodsName(cartProductInfoDtoList.get(0).getProductName());
					orderInfo.setUnitPrice(amount);
					orderInfoList.add(orderInfo);
					allAmount = allAmount + (amount * count);
				}
				List<CartProductInfoDto> cartProductInfo = cartProductRepository.findByCartNumberAndCreatedBy(orderReq.getCartNumber(), orderReq.getCustomer());
				List<Map> tempListMap = new ArrayList<Map>();
				orderInfoList.forEach( temp -> {
					Map tempMap = new HashMap();
					tempMap.put("goodsId", temp.getGoodsId());
					tempMap.put("goodsName", temp.getGoodsName());
					tempMap.put("unitPrice", temp.getUnitPrice());
					tempMap.put("count", temp.getCount());
					tempListMap.add(tempMap);
				});
				orderMap.put("goods", tempListMap);
				orderMap.put("totalAmount", allAmount);
				orderMap.put("customerName", orderReq.getCustomer());
				orderMap.put("cartId", orderReq.getCartNumber());
				orderResp = cartInfoService.orderSend(orderMap);
			}
		} catch (Exception e) {
			// �^�_��ʨ쪺�w�s�ƶq
			for (String key : goodsMap.keySet()) {
				this.cheangInventory(goodsMap.get(key), key);
			}
			throw new CartsProcessException(e.getMessage(), ExceptionStatus.INPUT_PARAMETER_ERROR, HttpStatus.BAD_REQUEST.value());
		}
		// �q��e�X���\��, �R��table���
		cartProductRepository.deleteByCartNumberAndCreatedBy(orderReq.getCartNumber(), orderReq.getCustomer());
		cartInfoRepository.deleteBycartNumber(orderReq.getCartNumber());
		
		return OrderResp.success(orderResp);
	}
	
	@ResponseBody
	@Transactional
	@DeleteMapping(value = "/deleteGoods")
    @ApiOperation(value = "�R���ӫ~", notes = "�R���ӫ~")
	public DeleteGoodsResp deleteGoods(@ApiParam(required = true, value = "�R���ӫ~") @RequestBody DeleteGoodsReq deleteGoodsReq) {
		 List<CartProductInfoDto> queryGoodsInfoList = cartProductRepository.findByCartNumberAndCreatedByAndProductId(deleteGoodsReq.getCartNumber(), 
				deleteGoodsReq.getCustomer(), deleteGoodsReq.getProductId());
		
		// �ˮֿ�J�R���ƶq�O�_�W�L��ܰӫ~���ʶR�ƶq
		if (CollectionUtils.isEmpty(queryGoodsInfoList)) {
			throw new CartsProcessException("�R���ƶq�W�L�ʶR�ƶq!", ExceptionStatus.INPUT_PARAMETER_ERROR, HttpStatus.BAD_REQUEST.value());
		}
		// �p�G�R���ƶq�P�ʶR�ƶq�@�P�h���ƧR��
		CartInfoDto  cartInfoDto  = cartInfoRepository.getCartInfoDtoBycartNumber(deleteGoodsReq.getCartNumber());
		if (cartInfoDto == null) {
			throw new CartsProcessException("�Э��s�d�߫�, �A������R��!", ExceptionStatus.INPUT_PARAMETER_ERROR, HttpStatus.BAD_REQUEST.value());
		}
		
		// �ק�᪺���B
		int modifyAmount = 0;
				
		List<CartProductInfoDto> cartProductInfoDto = cartProductRepository.findByCartNumberAndCreatedBy(deleteGoodsReq.getCartNumber(),
				deleteGoodsReq.getCustomer());
		
		// �R���Ӱӫ~�M��
		cartProductRepository.deleteAll(queryGoodsInfoList);
		logger.info("cartProductRepository.deleteAll: " + queryGoodsInfoList);
		
		// �p�G�ӫ~�M�椺�L�ӫ~, �h�ʪ����M��
		if (queryGoodsInfoList.size() == cartProductInfoDto.size()) {
			cartInfoRepository.delete(cartInfoDto);
			logger.info("cartInfoRepository.delete: " + cartInfoDto);
		} 
		// �Y�ʪ������R���Ӱӫ~�٦���l�ӫ~, �h��s�ʪ������B
		else {
			for (CartProductInfoDto temp : queryGoodsInfoList) {
				modifyAmount = modifyAmount + temp.getAmount();
			}
			cartInfoDto.setAmount(modifyAmount);
			cartInfoDto.setLastModifiedBy(deleteGoodsReq.getCustomer());
			cartInfoDto.setLastModifiedDate(new Date());
			cartInfoRepository.save(cartInfoDto);
			logger.info("cartInfoRepository.save: " + cartInfoDto);
		}
		
		return DeleteGoodsResp.success(queryGoodsInfoList);
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
		int modifyAmount = 0;
		// �ˮ֦��L�Ӱӫ~��J�ʪ�����
		if (CollectionUtils.isEmpty(queryGoodsInfoList)) {
			throw new CartsProcessException("��J�ק�ƶq���~!", ExceptionStatus.INPUT_PARAMETER_ERROR, HttpStatus.BAD_REQUEST.value());
		}
		// �p�G�ק�ƶq�P�ʶR�ƶq�@�P�h��^���~�T��
		if (queryGoodsInfoList.size() == productNo) {
			throw new CartsProcessException("��J�ק�ƶq�P�ʶR�ƶq�@�P!", ExceptionStatus.INPUT_PARAMETER_ERROR, HttpStatus.BAD_REQUEST.value());
		} else {
			List<CartProductInfoDto> newGoodsInfoList = new ArrayList<CartProductInfoDto>();
			// ��q
			if (productNo < queryGoodsInfoList.size()) {
				int no = queryGoodsInfoList.size() - productNo;
				for (int i = 0; i <  no; i++) {
					modifyAmount = modifyAmount + queryGoodsInfoList.get(i).getAmount();
					newGoodsInfoList.add(queryGoodsInfoList.get(i));
				}
				cartProductRepository.deleteAll(newGoodsInfoList);
				logger.info("cartProductRepository.deleteAll: " + queryGoodsInfoList.size());
			} 
			// �l�[
			else {
				// ���o�ӫ~�s��
				String producId = queryGoodsInfoList.get(0).getProductId();
				Map goodsMap = new HashMap();
				try {
					goodsMap = cartInfoService.getGoodsBygoodsId(producId);
				} catch (Exception e) {
					logger.error(e.getMessage());
					goodsMap = cartInfoService.getGoodsBygoodsId(producId);
				}
				// �ӫ~�ƶq
				int inventory = Integer.valueOf(goodsMap.get("inventory").toString());
				// �ӫ~���B
				int amount = Integer.valueOf(goodsMap.get("unitPrice").toString());
				// �ӫ~�W��
				String goodsName = goodsMap.get("goodsName").toString();
				int tempAmount = 0;
				// �Y�ӫ~�ƶq�k0
				if (inventory == 0) {
					throw new CartsProcessException("�ثe�w�L�Ӱӫ~!", ExceptionStatus.INPUT_PARAMETER_ERROR, HttpStatus.BAD_REQUEST.value());
				}
				for (int i = 0; i < productNo - queryGoodsInfoList.size(); i++) {
					cartInfoService.saveCartProductInfoDto(modifyGoodsReq.getCartNumber(), producId, goodsName, amount, modifyGoodsReq.getCustomer(),
							modifyGoodsReq.getCustomer(), new Date());
					tempAmount = tempAmount + amount;
				}
				modifyAmount = cartInfoDto.getAmount() + tempAmount;
			}
			
			// ��s�Ҧ��ʪ����Ӱӫ~�ԲӮɶ�
			queryGoodsInfoList = cartProductRepository.findByCartNumberAndCreatedByAndProductId(modifyGoodsReq.getCartNumber(), 
					modifyGoodsReq.getCustomer(), modifyGoodsReq.getProductId());
			queryGoodsInfoList.forEach( tempQueryGoodsInfo -> {
				tempQueryGoodsInfo.setLastModifiedDate(new Date());
				cartProductRepository.save(tempQueryGoodsInfo);
			});
			
			// �ק��ʪ����`���B
			cartInfoDto.setAmount(modifyAmount);
			cartInfoDto.setLastModifiedBy(modifyGoodsReq.getCustomer());
			cartInfoDto.setLastModifiedDate(new Date());
			cartInfoRepository.save(cartInfoDto);
			logger.info("cartInfoRepository.save: " + cartInfoDto);
		}
		
		// ��s����, ���̷s��s��Ʀ^��
		List<CartProductInfoDto> finallyQueryGoodsInfoList = cartProductRepository.findByCartNumberAndCreatedByAndProductId(modifyGoodsReq.getCartNumber(), 
				modifyGoodsReq.getCustomer(), modifyGoodsReq.getProductId());
		return ModifyGoodsResp.success(finallyQueryGoodsInfoList);
	}
	
	@ResponseBody
	@GetMapping(value = "/queryCarts")
    @ApiOperation(value = "���o�ʪ����M��", notes = "���o�ʪ����M��")
	public QueryCartResp queryCarts(@ApiParam(required = true, value = "���o�ʪ����M��") @RequestBody QueryCartReq queryCartReq) {
		List<CartProductInfoDto> queryGoodsInfoList = cartProductRepository.findByCartNumberAndCreatedBy(queryCartReq.getCartNumber(), 
				queryCartReq.getCustomer());
		// �`���B
		Integer allAmount = 0;
		for (CartProductInfoDto temp : queryGoodsInfoList) {
			allAmount = allAmount + temp.getAmount();
		}
		
		return QueryCartResp.success(queryGoodsInfoList, allAmount);
	}
	
	@ResponseBody
	@GetMapping(value = "/queryCartsNo")
    @ApiOperation(value = "���o�ӫ~�ԲӼƶq", notes = "���o�ӫ~�ԲӼƶq")
	public QueryCartNoResp queryCartsNo(@ApiParam(required = true, value = "���o�ӫ~�ԲӼƶq") @RequestBody QueryCartNoReq queryCartNoReq) {
		List<CartProductInfoDto> queryGoodsInfoList = cartProductRepository.findByCartNumberAndCreatedByAndProductId(queryCartNoReq.getCartNumber(), 
				queryCartNoReq.getCustomer(), queryCartNoReq.getProductId());
		// �`���B
		Integer allAmount = 0;
		for (CartProductInfoDto temp : queryGoodsInfoList) {
			allAmount = allAmount + temp.getAmount();
		}
		
		return QueryCartNoResp.success(queryGoodsInfoList, queryGoodsInfoList.size(), allAmount);
	}
	
	@ResponseBody
	@Transactional
	@PutMapping(value = "/addCart")
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
			throw new CartsProcessException("�ثe�w�L�Ӱӫ~!", ExceptionStatus.INPUT_PARAMETER_ERROR, HttpStatus.BAD_REQUEST.value());
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
	
	private void cheangInventory(int goodNo, String goodsId) {
		Map updateMap = new HashMap();
		updateMap.put("inventory", goodNo);
		cartInfoService.cheangInventory(updateMap, goodsId);
	}

}
