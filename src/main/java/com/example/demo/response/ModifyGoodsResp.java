package com.example.demo.response;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "�ק���w�ӫ~�ƶq")
public class ModifyGoodsResp extends apiResp {

    public static ModifyGoodsResp success() {
    	ModifyGoodsResp apiSuccess = new ModifyGoodsResp();
        apiSuccess.setRtnCode(0);
        apiSuccess.setMsg(SUCCESS);
        return apiSuccess;
    }

}
