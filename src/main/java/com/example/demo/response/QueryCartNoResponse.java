package com.example.demo.response;

import java.util.List;

import com.example.demo.shareddomain.dto.CartProductInfoDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "���o�ӫ~�ԲӼƶq")
public class QueryCartNoResponse extends apiResponse {
	
    @ApiModelProperty(value = "���G", example = "true", position = 5)
    private List<CartProductInfoDto> data;
    
    @ApiModelProperty(value = "�ƶq", example = "true", position = 4)
    private Integer cartNo;
    
    @ApiModelProperty(value = "�`���B", example = "true", position = 3)
    private Integer allAmount;

	public static QueryCartNoResponse success(List<CartProductInfoDto> data, Integer cartNo, Integer allAmount) {
    	QueryCartNoResponse apiSuccess = new QueryCartNoResponse();
        apiSuccess.setRtnCode(0);
        apiSuccess.setMsg(SUCCESS);
        apiSuccess.setData(data);
        apiSuccess.setCartNo(cartNo);
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
    
    public Integer getCartNo() {
		return cartNo;
	}

	public void setCartNo(Integer cartNo) {
		this.cartNo = cartNo;
	}

	public Integer getAllAmount() {
		return allAmount;
	}

	public void setAllAmount(Integer allAmount) {
		this.allAmount = allAmount;
	}
	
}
