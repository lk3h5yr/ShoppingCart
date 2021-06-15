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
    
	// 刪除指定商品API
    @Value("${goodsMgr.deleteGoodsIdUrl}")
    private String deleteGoodsIdUrl;
    
    private RestTemplate restTemplate = new RestTemplate();
    
    public Map deleteGoods(String goodsId) {
    	Map deleteMap = new HashMap();
		ResponseEntity<Object> responseEntity = null;
		try {
			String url = MessageFormat.format(deleteGoodsIdUrl, goodsId);
			responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, null, Object.class);
			if (responseEntity.getBody() == null) {
				deleteMap.put("statusCode", responseEntity.getStatusCode());
			}
		} catch (Exception e) {
			String localizedMessage = e.getLocalizedMessage();
    		String[] errorArr = localizedMessage.split(":");
			deleteMap.put("statusCode", errorArr[0].trim());
			deleteMap.put("errorMsg", errorArr[1]);
		}
		
		return deleteMap;
    }
    
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
		return cartInfoDto;
	}
	
	public List<CartProductInfoDto> getCartProductInfo(String cartNumber) {
		List<CartProductInfoDto> cartInfoDtoList = cartProductRepository.getCartProductByCartNumber(cartNumber);
		return cartInfoDtoList;
	}
	
	public void saveCartInfoDto(String cartNumber, String customer, Integer amount, String createdBy, String lastModifiedBy, Date date) {
		CartInfoDto cartInfo = new CartInfoDto();
		cartInfo.setCartNumber(cartNumber);
		cartInfo.setCustomer(customer);
		cartInfo.setAmount(amount);
		cartInfo.setCreatedBy(createdBy);
		cartInfo.setCreatedDate(date);
		cartInfo.setLastModifiedBy(lastModifiedBy);
		cartInfo.setLastModifiedDate(date);
		cartInfoRepository.save(cartInfo);
	}

	public void saveCartProductInfoDto(String cartNumber, String productId, String productName, Integer amount, String createdBy, String lastModifiedBy, Date date) {
		CartProductInfoDto cartProductInfo = new CartProductInfoDto();
		cartProductInfo.setCartNumber(cartNumber);
		cartProductInfo.setProductId(productId);
		cartProductInfo.setProductName(productName);
		cartProductInfo.setAmount(amount);
		cartProductInfo.setCreatedDate(date);
		cartProductInfo.setCreatedBy(createdBy);
		cartProductInfo.setLastModifiedDate(date);
		cartProductInfo.setLastModifiedBy(lastModifiedBy);
		cartProductRepository.save(cartProductInfo);
	}
}
