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
@Api(tags = {"測試專案工具接口"})
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
    @ApiOperation(value = "測試專案", notes = "測試專案")
	public testResponse testCartInfoDto(@ApiParam(required = true, value = "測試專案") @RequestBody testReq testReq) {
		return testResponse.success(null);
	}*/
	
	@ResponseBody
	@PostMapping(value = "/queryGoods")
    @ApiOperation(value = "取得外部所有商品", notes = "取得外部所有商品")
	public QueryGoodsResp queryGoods(@ApiParam(required = true, value = "取得外部所有商品") @RequestBody QueryGoodsReq queryGoodsReq) {
		List<GoodsInfo> queryGoodsInfoList = cartInfoService.queryAllGoodsInfo();
		return QueryGoodsResp.success(queryGoodsInfoList);
	}
	
	@ResponseBody
	@Transactional
	@PostMapping(value = "/orderSend")
    @ApiOperation(value = "建立訂單", notes = "建立訂單")
	public OrderResp orderSend(@ApiParam(required = true, value = "建立訂單") @RequestBody OrderReq queryGoodsReq) {
//		List<GoodsInfo> queryGoodsInfoList = cartInfoService.queryAllGoodsInfo();
		return OrderResp.success();
	}
	
	@ResponseBody
	@Transactional
	@PostMapping(value = "/deleteGoods")
    @ApiOperation(value = "刪除商品", notes = "刪除商品")
	public DeleteGoodsResp deleteGoods(@ApiParam(required = true, value = "刪除商品") @RequestBody DeleteGoodsReq deleteGoodsReq) {
		List<CartProductInfoDto> queryGoodsInfoList = cartProductRepository.findByCartNumberAndCreatedByAndProductId(deleteGoodsReq.getCartNumber(), 
				deleteGoodsReq.getCustomer(), deleteGoodsReq.getProductId());
		// 取得欲刪除的商品數量
		int productNo = deleteGoodsReq.getProductNo();
		
		// 檢核輸入刪除數量是否超過選擇商品的購買數量
		if (productNo > queryGoodsInfoList.size() || CollectionUtils.isEmpty(queryGoodsInfoList)) {
			//.. 返回錯誤訊息
		}
		// 如果刪除數量與購買數量一致則全數刪除
		if (queryGoodsInfoList.size() == productNo) {
			CartInfoDto  cartInfoDto  = cartInfoRepository.getCartInfoDtoBycartNumber(deleteGoodsReq.getCartNumber());
			if (cartInfoDto == null) {
				//..返回錯誤訊息
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
    @ApiOperation(value = "修改商品數量", notes = "修改商品數量")
	public ModifyGoodsResp modifyGoods(@ApiParam(required = true, value = "修改商品數量") @RequestBody ModifyGoodsReq modifyGoodsReq) {
		List<CartProductInfoDto> queryGoodsInfoList = cartProductRepository.findByCartNumberAndCreatedByAndProductId(modifyGoodsReq.getCartNumber(), 
				modifyGoodsReq.getCustomer(), modifyGoodsReq.getProductId());
		CartInfoDto cartInfoDto = cartInfoService.getCartInfo(modifyGoodsReq.getCartNumber());
		// 取得欲修改的商品數量
		int productNo = modifyGoodsReq.getProductNo();
		// 修改後的金額
		int modifyAmount = new Integer(0);
		// 檢核輸入修改數量是否超過選擇商品的購買數量
		if (productNo > queryGoodsInfoList.size() || CollectionUtils.isEmpty(queryGoodsInfoList)) {
			//.. 返回錯誤訊息
		}
		// 如果修改數量與購買數量一致則返回錯誤訊息
		if (queryGoodsInfoList.size() == productNo) {
			//..返回錯誤訊息
		} else {
			List<CartProductInfoDto> newGoodsInfoList = new ArrayList<CartProductInfoDto>();
			// 減量
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
			// 追加
			else {
				// 取得商品編號
				String producId = queryGoodsInfoList.get(0).getProductId();
				Map goodsMap = cartInfoService.getGoodsBygoodsId(producId);
				// 商品數量
				int inventory = Integer.valueOf(goodsMap.get("inventory").toString());
				// 商品金額
				int amount = Integer.valueOf(goodsMap.get("unitPrice").toString());
				// 商品名稱
				String goodsName = goodsMap.get("goodsName").toString();
				// 若商品數量歸0
				if (inventory == 0) {
					//..返回錯誤訊息
				}
				cartInfoService.saveCartProductInfoDto(modifyGoodsReq.getCartNumber(), producId, goodsName, amount, modifyGoodsReq.getCustomer(),
						modifyGoodsReq.getCustomer(), new Date());
				modifyAmount = modifyAmount + amount;
			}
			
			// 修改購物車總金額
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
    @ApiOperation(value = "取得購物車清單", notes = "取得購物車清單")
	public QueryCartResp queryCarts(@ApiParam(required = true, value = "取得購物車清單") @RequestBody QueryCartReq queryCartReq) {
		List<CartProductInfoDto> queryGoodsInfoList = cartProductRepository.findByCartNumberAndCreatedBy(queryCartReq.getCartNumber(), 
				queryCartReq.getCustomer());
		// 總金額
		Integer allAmount = new Integer(0);
		for (CartProductInfoDto temp : queryGoodsInfoList) {
			allAmount = allAmount + temp.getAmount();
		}
		
		return QueryCartResp.success(queryGoodsInfoList, allAmount);
	}
	
	@ResponseBody
	@PostMapping(value = "/queryCartsNo")
    @ApiOperation(value = "取得商品詳細數量", notes = "取得商品詳細數量")
	public QueryCartNoResp queryCartsNo(@ApiParam(required = true, value = "取得商品詳細數量") @RequestBody QueryCartNoReq queryCartNoReq) {
		List<CartProductInfoDto> queryGoodsInfoList = cartProductRepository.findByCartNumberAndCreatedByAndProductId(queryCartNoReq.getCartNumber(), 
				queryCartNoReq.getCustomer(), queryCartNoReq.getProductId());
		// 總金額
		Integer allAmount = new Integer(0);
		for (CartProductInfoDto temp : queryGoodsInfoList) {
			allAmount = allAmount + temp.getAmount();
		}
		
		return QueryCartNoResp.success(queryGoodsInfoList, queryGoodsInfoList.size(), allAmount);
	}
	
	@ResponseBody
	@Transactional
	@PostMapping(value = "/addCart")
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
			return AddCartResp.success(null);
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

}
