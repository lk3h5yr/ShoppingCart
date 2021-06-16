package com.example.demo.response;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "刪除指定商品")
public class DeleteGoodsResp extends apiResp {

    public static DeleteGoodsResp success() {
    	DeleteGoodsResp apiSuccess = new DeleteGoodsResp();
        apiSuccess.setRtnCode(0);
        apiSuccess.setMsg(SUCCESS);
        return apiSuccess;
    }

}
