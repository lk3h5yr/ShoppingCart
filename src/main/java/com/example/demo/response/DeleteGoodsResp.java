package com.example.demo.response;

import java.util.List;

import com.example.demo.shareddomain.dto.CartProductInfoDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "�R�����w�ӫ~")
public class DeleteGoodsResp extends apiResp {
	
	@ApiModelProperty(value = "���G", example = "true", position = 1)
	private List<CartProductInfoDto> data;

	public static DeleteGoodsResp success(List<CartProductInfoDto> data) {
    	DeleteGoodsResp apiSuccess = new DeleteGoodsResp();
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
