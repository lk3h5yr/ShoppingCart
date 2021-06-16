package com.example.demo.response;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "«Ø¥ß­q³æ")
public class OrderResp extends apiResp {

    public static OrderResp success() {
    	OrderResp apiSuccess = new OrderResp();
        apiSuccess.setRtnCode(0);
        apiSuccess.setMsg(SUCCESS);
        return apiSuccess;
    }

}
