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
    @ApiOperation(value = "取得所有商品", notes = "取得所有商品")
	public QueryGoodsResponse queryGoods(@ApiParam(required = true, value = "取得所有商品") @RequestBody QueryGoodsReq queryGoodsReq) {
		List<GoodsInfo> queryGoodsInfoList = cartInfoService.queryAllGoodsInfo();
		return QueryGoodsResponse.success(queryGoodsInfoList);
	}
	
	@ResponseBody
	@PostMapping(value = "/deleteGoods")
    @ApiOperation(value = "刪除商品", notes = "刪除商品")
	public DeleteGoodsResponse deleteGoods(@ApiParam(required = true, value = "刪除商品") @RequestBody DeleteGoodsReq deleteGoodsReq) {
		Map deleteMap = cartInfoService.deleteGoods(deleteGoodsReq.getProductId());
		return DeleteGoodsResponse.success(deleteMap);
	}
	
	@ResponseBody
	@PostMapping(value = "/queryCarts")
    @ApiOperation(value = "取得購物車清單", notes = "取得購物車清單")
	public QueryCartResponse queryCarts(@ApiParam(required = true, value = "取得購物車清單") @RequestBody QueryCartReq queryCartReq) {
		List<CartProductInfoDto> queryGoodsInfoList = cartProductRepository.findByCartNumberAndCreatedBy(queryCartReq.getCartNumber(), 
				queryCartReq.getCustomer());
		// 總金額
		Integer allAmount = new Integer(0);
		for (CartProductInfoDto temp : queryGoodsInfoList) {
			allAmount = allAmount + temp.getAmount();
		}
		
		return QueryCartResponse.success(queryGoodsInfoList, allAmount);
	}
	
	@ResponseBody
	@PostMapping(value = "/queryCartsNo")
    @ApiOperation(value = "取得商品詳細數量", notes = "取得商品詳細數量")
	public QueryCartNoResponse queryCartsNo(@ApiParam(required = true, value = "取得商品詳細數量") @RequestBody QueryCartNoReq queryCartNoReq) {
		List<CartProductInfoDto> queryGoodsInfoList = cartProductRepository.findByCartNumberAndCreatedByAndProductId(queryCartNoReq.getCartNumber(), 
				queryCartNoReq.getCustomer(), queryCartNoReq.getProductId());
		// 總金額
		Integer allAmount = new Integer(0);
		for (CartProductInfoDto temp : queryGoodsInfoList) {
			allAmount = allAmount + temp.getAmount();
		}
		
		return QueryCartNoResponse.success(queryGoodsInfoList, queryGoodsInfoList.size(), allAmount);
	}
	
	@ResponseBody
	@Transactional
	@PostMapping(value = "/addCart")
    @ApiOperation(value = "加入購物車", notes = "加入購物車")
	public AddCartResponse addCart(@ApiParam(required = true, value = "加入購物車") @RequestBody AddCartReq addCartReq) {
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
			return AddCartResponse.success(null);
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
		
		return AddCartResponse.success(goodsInfo);
	}

}
