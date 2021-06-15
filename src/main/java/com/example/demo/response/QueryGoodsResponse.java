package com.example.demo.response;

import java.util.List;

import com.example.demo.entity.GoodsInfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "查詢所有商品")
public class QueryGoodsResponse extends apiResponse {
	
    @ApiModelProperty(value = "結果", example = "true", position = 3)
    private List<GoodsInfo> data;

    public static QueryGoodsResponse success(List<GoodsInfo> data) {
    	QueryGoodsResponse apiSuccess = new QueryGoodsResponse();
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
