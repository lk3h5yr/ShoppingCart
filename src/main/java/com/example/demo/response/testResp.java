package com.example.demo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "測試回覆")
public class testResp extends apiResp {
	
    @ApiModelProperty(value = "結果", example = "true", position = 3)
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
