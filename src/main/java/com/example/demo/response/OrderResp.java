package com.example.demo.response;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "�إ߭q��")
public class OrderResp extends apiResp {

    public static OrderResp success() {
    	OrderResp apiSuccess = new OrderResp();
        apiSuccess.setRtnCode(0);
        apiSuccess.setMsg(SUCCESS);
        return apiSuccess;
    }

}
