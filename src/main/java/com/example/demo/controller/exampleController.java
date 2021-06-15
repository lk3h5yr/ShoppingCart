package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.GoodsInfo;
import com.example.demo.request.addCartReq;
import com.example.demo.request.queryGoodsReq;
import com.example.demo.request.testReq;
import com.example.demo.response.queryGoodsResponse;
import com.example.demo.response.testResponse;
import com.example.demo.service.ICartInfoService;
import com.example.demo.service.repository.CartInfoRepository;
import com.example.demo.shareddomain.dto.CartInfoDto;

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
	
	
	/*@ResponseBody
	@PostMapping(value = "/getTest")
    @ApiOperation(value = "測試專案", notes = "測試專案")
	public testResponse testCartInfoDto(@ApiParam(required = true, value = "測試專案") @RequestBody testReq testReq) {
		return testResponse.success(null);
	}*/
	
	@ResponseBody
	@PostMapping(value = "/queryGoods")
    @ApiOperation(value = "取得所有商品", notes = "取得所有商品")
	public queryGoodsResponse queryGoods(@ApiParam(required = true, value = "取得所有商品") @RequestBody queryGoodsReq queryGoodsReq) {
		List<GoodsInfo> queryGoodsInfoList = cartInfoService.queryAllGoodsInfo();
		return queryGoodsResponse.success(queryGoodsInfoList);
	}
	
	@ResponseBody
	@PostMapping(value = "/addCart")
    @ApiOperation(value = "加入購物車", notes = "加入購物車")
	public queryGoodsResponse addCart(@ApiParam(required = true, value = "加入購物車") @RequestBody addCartReq addCartReq) {
		cartInfoService.addCartInfo(null)
//		List<goodsInfo> queryGoodsInfoList = cartInfoService.queryAllGoodsInfo();
		return null;
//		return queryGoodsResponse.success(queryGoodsInfoList);
	}


}
