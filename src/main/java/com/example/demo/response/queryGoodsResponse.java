package com.example.demo.response;

import java.util.List;

import com.example.demo.entity.GoodsInfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "���զ^��")
public class queryGoodsResponse extends apiResponse {
	
    @ApiModelProperty(value = "���G", example = "true", position = 3)
    private List<GoodsInfo> data;

    public static queryGoodsResponse success(List<GoodsInfo> data) {
    	queryGoodsResponse apiSuccess = new queryGoodsResponse();
        apiSuccess.setRtnCode(0);
        apiSuccess.setMsg(SUCCESS);
        apiSuccess.setData(data);
        return apiSuccess;
    }

    @Override
    public List<GoodsInfo> getData() {
        return data;
    }

    public void setData(List<GoodsInfo> data) {
        this.data = data;
    }

}
