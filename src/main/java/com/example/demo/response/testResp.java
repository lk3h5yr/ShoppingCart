package com.example.demo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "���զ^��")
public class testResp extends apiResp {
	
    @ApiModelProperty(value = "���G", example = "true", position = 3)
    private Boolean data;

    public static testResp success(Boolean data) {
    	testResp apiSuccess = new testResp();
        apiSuccess.setRtnCode(0);
        apiSuccess.setMsg(SUCCESS);
        apiSuccess.setData(data);
        return apiSuccess;
    }

    @Override
    public Boolean getData() {
        return data;
    }

    public void setData(Boolean data) {
        this.data = data;
    }

}
