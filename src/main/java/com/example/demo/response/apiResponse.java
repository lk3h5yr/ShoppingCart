package com.example.demo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "流程回覆")
public class apiResponse {

    @ApiModelProperty(value = "回應代碼(0:成功、1:失敗)", required = true, position = 1)
    private int rtnCode;

    @ApiModelProperty(value = "回應訊息", required = true, position = 2)
    private String msg;

    @ApiModelProperty(value = "回應資訊", required = true, position = 3)
    private Object data;

    protected static final String SUCCESS = "success";
    protected static final String FAIL = "fail";

    public static apiResponse success() {
        apiResponse apiSuccess = new apiResponse();
        apiSuccess.setRtnCode(0);
        apiSuccess.setMsg(SUCCESS);
        return apiSuccess;
    }

    public static apiResponse success(Object data) {
        apiResponse apiSuccess = new apiResponse();
        apiSuccess.setRtnCode(0);
        apiSuccess.setMsg(SUCCESS);
        apiSuccess.setData(data);
        return apiSuccess;
    }

    public static apiResponse success(String message, Object data) {
        apiResponse apiSuccess = new apiResponse();
        apiSuccess.setRtnCode(0);
        apiSuccess.setMsg(message);
        apiSuccess.setData(data);
        return apiSuccess;
    }

    public static apiResponse fail() {
        apiResponse apiFail = new apiResponse();
        apiFail.setRtnCode(1);
        apiFail.setMsg(FAIL);
        return apiFail;
    }

    public static apiResponse fail(String failMsg) {
        apiResponse apiFail = new apiResponse();
        apiFail.setRtnCode(1);
        apiFail.setMsg(failMsg);
        return apiFail;
    }
    
    public static apiResponse fail(String failMsg, int rtnCode) {
        apiResponse apiFail = new apiResponse();
        apiFail.setMsg(failMsg);
        apiFail.setRtnCode(rtnCode);
        return apiFail;
    }

    public int getRtnCode() {
        return rtnCode;
    }

    public void setRtnCode(int rtnCode) {
        this.rtnCode = rtnCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
