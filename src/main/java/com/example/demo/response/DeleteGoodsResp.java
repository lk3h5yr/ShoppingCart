package com.example.demo.response;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "�R�����w�ӫ~")
public class DeleteGoodsResp extends apiResp {

    public static DeleteGoodsResp success() {
    	DeleteGoodsResp apiSuccess = new DeleteGoodsResp();
        apiSuccess.setRtnCode(0);
        apiSuccess.setMsg(SUCCESS);
        return apiSuccess;
    }

}
