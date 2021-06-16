package com.example.demo.response;

import com.example.demo.entity.GoodsInfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "新增商品")
public class AddCartResp extends apiResp {
	
    @ApiModelProperty(value = "結果", example = "true", position = 3)
    private GoodsInfo data;

    public static AddCartResp success(GoodsInfo data) {
    	AddCartResp apiSuccess = new AddCartResp();
    	if (data == null) {
            apiSuccess.setRtnCode(1);
            apiSuccess.setMsg(FAIL);
    	} else {
            apiSuccess.setRtnCode(0);
            apiSuccess.setMsg(SUCCESS);
            apiSuccess.setData(data);
    	}

        return apiSuccess;
    }

    @Override
    public GoodsInfo getData() {
        return data;
    }

    public void setData(GoodsInfo data) {
        this.data = data;
    }

}
