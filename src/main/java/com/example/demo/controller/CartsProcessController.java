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
@Api(tags = {"購物車服務介面API"})
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
    @ApiOperation(value = "測試專案", notes = "測試專案")
	public testResponse testCartInfoDto(@ApiParam(required = true, value = "測試專案") @RequestBody testReq testReq) {
		return testResponse.success(null);
	}*/
	
	@ResponseBody
	@GetMapping(value = "/queryGoods")
    @ApiOperation(value = "取得外部所有商品", notes = "取得外部所有商品")
	public QueryGoodsResp queryGoods(@ApiParam(required = true, value = "取得外部所有商品") @RequestBody QueryGoodsReq queryGoodsReq) {
		List<GoodsInfo> queryGoodsInfoList = cartInfoService.queryAllGoodsInfo();
		return QueryGoodsResp.success(queryGoodsInfoList);
	}
	
	@ResponseBody
	@Transactional
	@PostMapping(value = "/orderSend")
    @ApiOperation(value = "建立訂單", notes = "建立訂單")
	public OrderResp orderSend(@ApiParam(required = true, value = "建立訂單") @RequestBody OrderReq orderReq) {
		// 於訂單建立前透過 /goods/{goodsId}/inventory 變更庫存
		List<CartProductInfoDto> queryGoodsInfoList = cartProductRepository.findByCartNumberAndCreatedBy(orderReq.getCartNumber(), 
				orderReq.getCustomer());
		List<String> productIdList = new ArrayList<String>();
		Map orderResp = new HashMap();
		// 取所有購物車的種類商品代號
		queryGoodsInfoList.forEach( tempGppdsInfo -> {
			String productId = tempGppdsInfo.getProductId();
			if (!productIdList.contains(productId)) {
				productIdList.add(tempGppdsInfo.getProductId());
			}
		});
		
		// Map<商品代號, 總數量>
		Map<String, Integer> productIdMap = new HashMap();
		// 呼叫外部API 查該商品現在有多少數量
		Map<String, Integer> goodsMap = new HashMap();
		
		try {
			// ■ 更新庫存
			if (!CollectionUtils.isEmpty(productIdList)) {
				productIdList.forEach( tempProductId -> {
					// 查詢資料庫內各商品各有幾筆
					int cartProductCount = cartProductRepository.countByProductId(tempProductId);
					
					// 查詢更改庫存前 系統有多少庫存
					Map tempGoodsMap = cartInfoService.getGoodsBygoodsId(tempProductId);
					int inventory = Integer.valueOf(tempGoodsMap.get("inventory").toString());
					
					if (cartProductCount > inventory) {
						throw new CartsProcessException("商品庫存不足!", ExceptionStatus.INPUT_PARAMETER_ERROR, HttpStatus.BAD_REQUEST.value());
					}
					// 外部庫存筆數
					goodsMap.put(tempProductId, inventory);
					// 資料庫內各商品筆數
					productIdMap.put(tempProductId, cartProductCount);
					// 外部庫存 扣 此系統資料庫數量 = 更新後的資料筆數
					int finallyInventory = inventory - cartProductCount;
					// 更新庫存
					this.cheangInventory(finallyInventory, tempProductId);
				});
			}
			// ■ 送出訂單
			Map orderMap = new HashMap();
			// 總金額
			Integer allAmount = 0;
			if (!CollectionUtils.isEmpty(productIdList)) {
				List<OrderInfo> orderInfoList = new ArrayList<OrderInfo>();
				// 組送訂單資料
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
			// 回復更動到的庫存數量
			for (String key : goodsMap.keySet()) {
				this.cheangInventory(goodsMap.get(key), key);
			}
			throw new CartsProcessException(e.getMessage(), ExceptionStatus.INPUT_PARAMETER_ERROR, HttpStatus.BAD_REQUEST.value());
		}
		// 訂單送出成功後, 刪除table資料
		cartProductRepository.deleteByCartNumberAndCreatedBy(orderReq.getCartNumber(), orderReq.getCustomer());
		cartInfoRepository.deleteBycartNumber(orderReq.getCartNumber());
		
		return OrderResp.success(orderResp);
	}
	
	@ResponseBody
	@Transactional
	@DeleteMapping(value = "/deleteGoods")
    @ApiOperation(value = "刪除商品", notes = "刪除商品")
	public DeleteGoodsResp deleteGoods(@ApiParam(required = true, value = "刪除商品") @RequestBody DeleteGoodsReq deleteGoodsReq) {
		 List<CartProductInfoDto> queryGoodsInfoList = cartProductRepository.findByCartNumberAndCreatedByAndProductId(deleteGoodsReq.getCartNumber(), 
				deleteGoodsReq.getCustomer(), deleteGoodsReq.getProductId());
		
		// 檢核輸入刪除數量是否超過選擇商品的購買數量
		if (CollectionUtils.isEmpty(queryGoodsInfoList)) {
			throw new CartsProcessException("刪除數量超過購買數量!", ExceptionStatus.INPUT_PARAMETER_ERROR, HttpStatus.BAD_REQUEST.value());
		}
		// 如果刪除數量與購買數量一致則全數刪除
		CartInfoDto  cartInfoDto  = cartInfoRepository.getCartInfoDtoBycartNumber(deleteGoodsReq.getCartNumber());
		if (cartInfoDto == null) {
			throw new CartsProcessException("請重新查詢後, 再次執行刪除!", ExceptionStatus.INPUT_PARAMETER_ERROR, HttpStatus.BAD_REQUEST.value());
		}
		
		// 修改後的金額
		int modifyAmount = 0;
				
		List<CartProductInfoDto> cartProductInfoDto = cartProductRepository.findByCartNumberAndCreatedBy(deleteGoodsReq.getCartNumber(),
				deleteGoodsReq.getCustomer());
		
		// 刪除該商品清單
		cartProductRepository.deleteAll(queryGoodsInfoList);
		logger.info("cartProductRepository.deleteAll: " + queryGoodsInfoList);
		
		// 如果商品清單內無商品, 則購物車清空
		if (queryGoodsInfoList.size() == cartProductInfoDto.size()) {
			cartInfoRepository.delete(cartInfoDto);
			logger.info("cartInfoRepository.delete: " + cartInfoDto);
		} 
		// 若購物車內刪除該商品還有其餘商品, 則更新購物車金額
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
    @ApiOperation(value = "修改商品數量", notes = "修改商品數量")
	public ModifyGoodsResp modifyGoods(@ApiParam(required = true, value = "修改商品數量") @RequestBody ModifyGoodsReq modifyGoodsReq) {
		List<CartProductInfoDto> queryGoodsInfoList = cartProductRepository.findByCartNumberAndCreatedByAndProductId(modifyGoodsReq.getCartNumber(), 
				modifyGoodsReq.getCustomer(), modifyGoodsReq.getProductId());
		CartInfoDto cartInfoDto = cartInfoService.getCartInfo(modifyGoodsReq.getCartNumber());
		
		// 取得欲修改的商品數量
		int productNo = modifyGoodsReq.getProductNo();
		// 修改後的金額
		int modifyAmount = 0;
		// 檢核有無該商品放入購物車中
		if (CollectionUtils.isEmpty(queryGoodsInfoList)) {
			throw new CartsProcessException("輸入修改數量有誤!", ExceptionStatus.INPUT_PARAMETER_ERROR, HttpStatus.BAD_REQUEST.value());
		}
		// 如果修改數量與購買數量一致則返回錯誤訊息
		if (queryGoodsInfoList.size() == productNo) {
			throw new CartsProcessException("輸入修改數量與購買數量一致!", ExceptionStatus.INPUT_PARAMETER_ERROR, HttpStatus.BAD_REQUEST.value());
		} else {
			List<CartProductInfoDto> newGoodsInfoList = new ArrayList<CartProductInfoDto>();
			// 減量
			if (productNo < queryGoodsInfoList.size()) {
				int no = queryGoodsInfoList.size() - productNo;
				for (int i = 0; i <  no; i++) {
					modifyAmount = modifyAmount + queryGoodsInfoList.get(i).getAmount();
					newGoodsInfoList.add(queryGoodsInfoList.get(i));
				}
				cartProductRepository.deleteAll(newGoodsInfoList);
				logger.info("cartProductRepository.deleteAll: " + queryGoodsInfoList.size());
			} 
			// 追加
			else {
				// 取得商品編號
				String producId = queryGoodsInfoList.get(0).getProductId();
				Map goodsMap = new HashMap();
				try {
					goodsMap = cartInfoService.getGoodsBygoodsId(producId);
				} catch (Exception e) {
					logger.error(e.getMessage());
					goodsMap = cartInfoService.getGoodsBygoodsId(producId);
				}
				// 商品數量
				int inventory = Integer.valueOf(goodsMap.get("inventory").toString());
				// 商品金額
				int amount = Integer.valueOf(goodsMap.get("unitPrice").toString());
				// 商品名稱
				String goodsName = goodsMap.get("goodsName").toString();
				int tempAmount = 0;
				// 若商品數量歸0
				if (inventory == 0) {
					throw new CartsProcessException("目前已無該商品!", ExceptionStatus.INPUT_PARAMETER_ERROR, HttpStatus.BAD_REQUEST.value());
				}
				for (int i = 0; i < productNo - queryGoodsInfoList.size(); i++) {
					cartInfoService.saveCartProductInfoDto(modifyGoodsReq.getCartNumber(), producId, goodsName, amount, modifyGoodsReq.getCustomer(),
							modifyGoodsReq.getCustomer(), new Date());
					tempAmount = tempAmount + amount;
				}
				modifyAmount = cartInfoDto.getAmount() + tempAmount;
			}
			
			// 更新所有購物車該商品詳細時間
			queryGoodsInfoList = cartProductRepository.findByCartNumberAndCreatedByAndProductId(modifyGoodsReq.getCartNumber(), 
					modifyGoodsReq.getCustomer(), modifyGoodsReq.getProductId());
			queryGoodsInfoList.forEach( tempQueryGoodsInfo -> {
				tempQueryGoodsInfo.setLastModifiedDate(new Date());
				cartProductRepository.save(tempQueryGoodsInfo);
			});
			
			// 修改購物車總金額
			cartInfoDto.setAmount(modifyAmount);
			cartInfoDto.setLastModifiedBy(modifyGoodsReq.getCustomer());
			cartInfoDto.setLastModifiedDate(new Date());
			cartInfoRepository.save(cartInfoDto);
			logger.info("cartInfoRepository.save: " + cartInfoDto);
		}
		
		// 更新完後, 取最新更新資料回傳
		List<CartProductInfoDto> finallyQueryGoodsInfoList = cartProductRepository.findByCartNumberAndCreatedByAndProductId(modifyGoodsReq.getCartNumber(), 
				modifyGoodsReq.getCustomer(), modifyGoodsReq.getProductId());
		return ModifyGoodsResp.success(finallyQueryGoodsInfoList);
	}
	
	@ResponseBody
	@GetMapping(value = "/queryCarts")
    @ApiOperation(value = "取得購物車清單", notes = "取得購物車清單")
	public QueryCartResp queryCarts(@ApiParam(required = true, value = "取得購物車清單") @RequestBody QueryCartReq queryCartReq) {
		List<CartProductInfoDto> queryGoodsInfoList = cartProductRepository.findByCartNumberAndCreatedBy(queryCartReq.getCartNumber(), 
				queryCartReq.getCustomer());
		// 總金額
		Integer allAmount = 0;
		for (CartProductInfoDto temp : queryGoodsInfoList) {
			allAmount = allAmount + temp.getAmount();
		}
		
		return QueryCartResp.success(queryGoodsInfoList, allAmount);
	}
	
	@ResponseBody
	@GetMapping(value = "/queryCartsNo")
    @ApiOperation(value = "取得商品詳細數量", notes = "取得商品詳細數量")
	public QueryCartNoResp queryCartsNo(@ApiParam(required = true, value = "取得商品詳細數量") @RequestBody QueryCartNoReq queryCartNoReq) {
		List<CartProductInfoDto> queryGoodsInfoList = cartProductRepository.findByCartNumberAndCreatedByAndProductId(queryCartNoReq.getCartNumber(), 
				queryCartNoReq.getCustomer(), queryCartNoReq.getProductId());
		// 總金額
		Integer allAmount = 0;
		for (CartProductInfoDto temp : queryGoodsInfoList) {
			allAmount = allAmount + temp.getAmount();
		}
		
		return QueryCartNoResp.success(queryGoodsInfoList, queryGoodsInfoList.size(), allAmount);
	}
	
	@ResponseBody
	@Transactional
	@PutMapping(value = "/addCart")
    @ApiOperation(value = "新增商品", notes = "新增商品")
	public AddCartResp addCart(@ApiParam(required = true, value = "新增商品") @RequestBody AddCartReq addCartReq) {
		// 呼叫API查詢商品資訊
		Map goodsMap = cartInfoService.getGoodsBygoodsId(addCartReq.getProductId());
		// 商品數量
		Integer inventory = Integer.valueOf(goodsMap.get("inventory").toString());
		// 商品金額
		Integer amount = Integer.valueOf(goodsMap.get("unitPrice").toString());
		// 購買人
		String customer = addCartReq.getCustomer();
		// 商品代號
		String goodsId = goodsMap.get("goodsId").toString();
		// 商品名稱
		String goodsName = goodsMap.get("goodsName").toString();
		// 新增 與 更改 時間
		Date date = new Date();
		GoodsInfo goodsInfo = new GoodsInfo();
		
		// 呼叫API查無商品訊息, 返回錯誤訊息
		if (goodsMap == null || inventory == 0) {
			throw new CartsProcessException("目前已無該商品!", ExceptionStatus.INPUT_PARAMETER_ERROR, HttpStatus.BAD_REQUEST.value());
		} 
		// 有商品訊息
		else {
			
			// 購物車編號
			String cartNumber = addCartReq.getCartNumber();
			CartInfoDto cartInfoDto = cartInfoService.getCartInfo(cartNumber);
			List<CartProductInfoDto> cartProductInfoList = cartInfoService.getCartProductInfo(cartNumber);
			
			// 若購物車有資訊, 則進行購物車金額更新 & 商品詳細新增
			if (cartInfoDto != null) {
				Integer amountTemp = cartInfoDto.getAmount() + amount;
				cartInfoService.saveCartInfoDto(cartNumber, customer, amountTemp, customer, customer, date);
				cartInfoService.saveCartProductInfoDto(cartNumber, goodsId, goodsName, amount, customer, customer, date);
				
			}
			// 購物車沒資訊, 全部新增
			else {
				// 新增資料 CART
				cartInfoService.saveCartInfoDto(cartNumber, customer, amount, customer, customer, date);
				// 新增資料 CART_PRODUCT
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
