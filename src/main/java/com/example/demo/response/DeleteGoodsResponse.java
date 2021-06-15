package com.example.demo.response;

import java.util.Map;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "刪除指定商品")
public class DeleteGoodsResponse extends apiResponse {

    public static DeleteGoodsResponse success(Map data) {
    	DeleteGoodsResponse apiSuccess = new DeleteGoodsResponse();
    	String statusCode = data.get("statusCode").toString();
    	if ("204".equals(statusCode)) {
            apiSuccess.setRtnCode(0);
            apiSuccess.setMsg(SUCCESS);
    	} else {
            apiSuccess.setRtnCode(Integer.parseInt(statusCode));
            apiSuccess.setMsg(data.get("errorMsg").toString());
    	}
    	apiSuccess.setData(null);
        return apiSuccess;
    }

}
