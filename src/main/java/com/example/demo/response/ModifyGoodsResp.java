package com.example.demo.response;

import java.util.List;

import com.example.demo.shareddomain.dto.CartProductInfoDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "修改指定商品數量")
public class ModifyGoodsResp extends apiResp {

	@ApiModelProperty(value = "結果", example = "true", position = 1)
	private List<CartProductInfoDto> data;
	  
	public static ModifyGoodsResp success(List<CartProductInfoDto> data) {
    	ModifyGoodsResp apiSuccess = new ModifyGoodsResp();
        apiSuccess.setRtnCode(0);
        apiSuccess.setMsg(SUCCESS);
        apiSuccess.setData(data);
        return apiSuccess;
    }
	
    public List<CartProductInfoDto> getData() {
		return data;
	}

	public void setData(List<CartProductInfoDto> data) {
		this.data = data;
	}
}
