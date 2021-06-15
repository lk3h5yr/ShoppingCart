package com.example.demo.service.impl;

import java.util.ArrayList;
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
import com.example.demo.request.addCartReq;
import com.example.demo.service.ICartInfoService;
import com.example.demo.service.repository.CartInfoRepository;
import com.example.demo.shareddomain.dto.CartInfoDto;

@Service
public class CartInfoServiceImpl implements ICartInfoService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private CartInfoRepository cartInfoRepository;
	
	// 取得所有商品
    @Value("${goodsMgr.getAllGoodsesUrl}")
    private String allGoodsesUrl;
    
    private RestTemplate restTemplate = new RestTemplate();
	
    /**
     * 加入購物車
     */
    public void addCartInfo(addCartReq addCartReq) {
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
	public List<GoodsInfo> queryAllGoodsInfo(){
		List<GoodsInfo> queryGoodsInfoList = new ArrayList<GoodsInfo>();
		
        ResponseEntity<Object> responseEntity = null;
        try {
          responseEntity = 
              restTemplate.exchange(allGoodsesUrl, HttpMethod.GET, null, Object.class);
        } catch (Exception e) {
        	logger.error(e.getMessage());
        }
        
        Map bodyMap = (Map) responseEntity.getBody();
        Map goodsesMap = (Map) bodyMap.get("_embedded");
        List<Map> goodsesList = (List<Map>) goodsesMap.get("goodses");
        
        goodsesList.forEach( goods -> {
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

}
