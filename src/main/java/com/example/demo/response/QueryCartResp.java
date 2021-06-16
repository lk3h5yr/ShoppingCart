package com.example.demo.response;

import java.util.List;

import com.example.demo.entity.GoodsInfo;
import com.example.demo.shareddomain.dto.CartProductInfoDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "���o�ʪ��M��")
public class QueryCartResp extends apiResp {
	
    @ApiModelProperty(value = "���G", example = "true", position = 4)
    private List<CartProductInfoDto> data;
    
    @ApiModelProperty(value = "�`���B", example = "true", position = 3)
    private Integer allAmount;

	public static QueryCartResp success(List<CartProductInfoDto> data, Integer allAmount) {
    	QueryCartResp apiSuccess = new QueryCartResp();
        apiSuccess.setRtnCode(0);
        apiSuccess.setMsg(SUCCESS);
        apiSuccess.setData(data);
        apiSuccess.setAllAmount(allAmount);
        return apiSuccess;
    }

    @Override
    public List<CartProductInfoDto> getData() {
        return data;
    }

    public void setData(List<CartProductInfoDto> data) {
        this.data = data;
    }
    
    public Integer getAllAmount() {
		return allAmount;
	}

	public void setAllAmount(Integer allAmount) {
		this.allAmount = allAmount;
	}

}
