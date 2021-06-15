package com.example.demo.service.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.commonUtils.CreateCartNumber;
import com.example.demo.entity.GoodsInfo;
import com.example.demo.request.AddCartReq;
import com.example.demo.service.ICartInfoService;
import com.example.demo.service.repository.CartInfoRepository;
import com.example.demo.service.repository.CartProductRepository;
import com.example.demo.shareddomain.dto.CartInfoDto;
import com.example.demo.shareddomain.dto.CartProductInfoDto;

@Service
public class CartInfoServiceImpl implements ICartInfoService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private CartInfoRepository cartInfoRepository;
	
	@Autowired
	private CartProductRepository cartProductRepository;
	
	// 取得所有商品
    @Value("${goodsMgr.getAllGoodsesUrl}")
    private String allGoodsesUrl;
    
	// 取得指定商品API
    @Value("${goodsMgr.getGoodsByGoodsId}")
    private String goodsByGoodsIdUrl;
    
    private RestTemplate restTemplate = new RestTemplate();
	
	/**
	 * 加入購物車
	 */
	public void addCartInfo(AddCartReq addCartReq) {
		CreateCartNumber createCartNumber = new CreateCartNumber();

		CartInfoDto cartInfoDto = new CartInfoDto();
		cartInfoDto.setCartNumber(createCartNumber.createCode());

		try {
			cartInfoRepository.save(cartInfoDto);
		} catch (Exception e) {
			// 返回errorCode ...未定
			logger.error(e.getMessage());
		}
	}
    
	/**
	 * 呼叫http://18.183.144.77:8080/goodses 取得所有商品
	 */
	public List<GoodsInfo> queryAllGoodsInfo() {
		List<GoodsInfo> queryGoodsInfoList = new ArrayList<GoodsInfo>();

		ResponseEntity<Object> responseEntity = null;
		try {
			responseEntity = restTemplate.exchange(allGoodsesUrl, HttpMethod.GET, null, Object.class);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		Map bodyMap = (Map) responseEntity.getBody();
		Map goodsesMap = (Map) bodyMap.get("_embedded");
		List<Map> goodsesList = (List<Map>) goodsesMap.get("goodses");

		goodsesList.forEach(goods -> {
			GoodsInfo queryGoodsInfo = new GoodsInfo();
			queryGoodsInfo.setGoodsId(goods.get("goodsId").toString());
			queryGoodsInfo.setGoodsName(goods.get("goodsName").toString());
			queryGoodsInfo.setUnitPrice(Integer.valueOf(goods.get("unitPrice").toString()));
			queryGoodsInfo.setInventory(Integer.valueOf(goods.get("inventory").toString()));
			queryGoodsInfo.setCreatedBy(goods.get("createdBy").toString());
			queryGoodsInfo.setCreatedDate(goods.get("createdDate").toString());
			queryGoodsInfo.setLastModifiedBy(goods.get("lastModifiedBy").toString());
			queryGoodsInfo.setLastModifiedDate(goods.get("lastModifiedDate").toString());
			queryGoodsInfoList.add(queryGoodsInfo);
		});

		return queryGoodsInfoList;
	}
	
	public Map getGoodsBygoodsId(String goodsId) {
		ResponseEntity<Map> responseEntity = null;
		try {
			String url = MessageFormat.format(goodsByGoodsIdUrl, goodsId);
			responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, Map.class);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return (Map) responseEntity.getBody();
	}
	
	public CartInfoDto getCartInfo(String cartNumber) {
		CartInfoDto cartInfoDto = cartInfoRepository.getCartInfoDtoBycartNumber(cartNumber);
//    	return cartInfoDtoList;
		return null;
	}
	
	public List<CartProductInfoDto> getCartProductInfo(String cartNumber) {
		return null;
	}
	
	public void saveCartInfoDto(String cartNumber, String customer, Integer amount, String createdBy, String lastModifiedBy) {
		CartInfoDto cartInfo = new CartInfoDto();
		cartInfo.setCartNumber(cartNumber);
		cartInfo.setCustomer(customer);
		cartInfo.setAmount(amount);
		cartInfo.setCreatedBy(createdBy);
		cartInfo.setCreatedDate(new Date());
		cartInfo.setLastModifiedBy(lastModifiedBy);
		cartInfo.setLastModifiedDate(new Date());
		cartInfoRepository.save(cartInfo);
	}

	public void saveCartProductInfoDto(String cartNumber, String productId, String productName, Integer amount, String createdBy, String lastModifiedBy) {
		CartProductInfoDto cartProductInfo = new CartProductInfoDto();
		cartProductInfo.setCartNumber(cartNumber);
		cartProductInfo.setProductId(productId);
		cartProductInfo.setProductName(productName);
		cartProductInfo.setAmount(amount);
		cartProductInfo.setCreatedDate(new Date());
		cartProductInfo.setCreatedBy(createdBy);
		cartProductInfo.setLastModifiedDate(new Date());
		cartProductInfo.setLastModifiedBy(lastModifiedBy);
		cartProductRepository.save(cartProductInfo);
	}
}
