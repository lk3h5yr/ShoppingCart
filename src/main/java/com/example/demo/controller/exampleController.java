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
	public queryGoodsResponse queryGoods(@ApiParam(required = true, value = "取得所有商品") @RequestBody QueryGoodsReq queryGoodsReq) {
		List<GoodsInfo> queryGoodsInfoList = cartInfoService.queryAllGoodsInfo();
		return queryGoodsResponse.success(queryGoodsInfoList);
	}
	
	@ResponseBody
	@Transactional
	@PostMapping(value = "/addCart")
    @ApiOperation(value = "加入購物車", notes = "加入購物車")
	public queryGoodsResponse addCart(@ApiParam(required = true, value = "加入購物車") @RequestBody AddCartReq addCartReq) {
		// 呼叫API查詢商品資訊
		Map goodsMap = cartInfoService.getGoodsBygoodsId(addCartReq.getProductId());
		// 商品數量
		Integer inventory = Integer.valueOf(goodsMap.get("inventory").toString());
		// 商品金額
		Integer amount = Integer.valueOf(goodsMap.get("unitPrice").toString());
		// 購買人
		String customer = addCartReq.getCustomer();
		
		// 呼叫API查無商品訊息, 返回錯誤訊息
		if (goodsMap == null) {
			//...
		} 
		// 有商品訊息
		else if (goodsMap != null && inventory != 0) {
			
			// 購物車編號
			String cartNumber = addCartReq.getCartNumber();
			CartInfoDto cartInfoDto = cartInfoService.getCartInfo(cartNumber);
			List<CartProductInfoDto> cartProductInfoList = cartInfoService.getCartProductInfo(cartNumber);
			
			// 若購物車有資訊, 則進行購物車金額更新 & 商品詳細新增
			if (cartInfoDto != null) {
				Integer amountTemp = cartInfoDto.getAmount() + amount;
				cartInfoService.saveCartInfoDto(cartNumber, customer, amountTemp, customer, customer);
				cartInfoService.saveCartProductInfoDto(cartNumber, goodsMap.get("goodsId").toString(), goodsMap.get("goodsName").toString(), 
						amount, customer, customer);
				
			}
			// 購物車沒資訊, 全部新增
			else {
				// 新增資料 CART
				cartInfoService.saveCartInfoDto(cartNumber, customer, amount, customer, customer);
				// 新增資料 CART_PRODUCT
				if (CollectionUtils.isEmpty(cartProductInfoList)) {
					cartInfoService.saveCartProductInfoDto(cartNumber, goodsMap.get("goodsId").toString(), goodsMap.get("goodsName").toString(), 
							inventory, customer, customer);
				}
			}
		} else {
			// ...商品數量為0, 返回錯誤訊息
		}
		
		return null;
//		return queryGoodsResponse.success(queryGoodsInfoList);
	}

}
