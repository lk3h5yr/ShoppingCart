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
@Api(tags = {"���ձM�פu�㱵�f"})
public class exampleController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ICartInfoService cartInfoService;
	
	@Autowired
	private CartInfoRepository cartInfoRepository;
	
	
	/*@ResponseBody
	@PostMapping(value = "/getTest")
    @ApiOperation(value = "���ձM��", notes = "���ձM��")
	public testResponse testCartInfoDto(@ApiParam(required = true, value = "���ձM��") @RequestBody testReq testReq) {
		return testResponse.success(null);
	}*/
	
	@ResponseBody
	@PostMapping(value = "/queryGoods")
    @ApiOperation(value = "���o�Ҧ��ӫ~", notes = "���o�Ҧ��ӫ~")
	public queryGoodsResponse queryGoods(@ApiParam(required = true, value = "���o�Ҧ��ӫ~") @RequestBody queryGoodsReq queryGoodsReq) {
		List<GoodsInfo> queryGoodsInfoList = cartInfoService.queryAllGoodsInfo();
		return queryGoodsResponse.success(queryGoodsInfoList);
	}
	
	@ResponseBody
	@PostMapping(value = "/addCart")
    @ApiOperation(value = "�[�J�ʪ���", notes = "�[�J�ʪ���")
	public queryGoodsResponse addCart(@ApiParam(required = true, value = "�[�J�ʪ���") @RequestBody addCartReq addCartReq) {
		cartInfoService.addCartInfo(null)
//		List<goodsInfo> queryGoodsInfoList = cartInfoService.queryAllGoodsInfo();
		return null;
//		return queryGoodsResponse.success(queryGoodsInfoList);
	}


}
