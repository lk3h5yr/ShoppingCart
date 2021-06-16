package com.example.demo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "流程回覆")
public class apiResp {

    @ApiModelProperty(value = "回應代碼(0:成功、1:失敗)", required = true, position = 1)
    private int rtnCode;

    @ApiModelProperty(value = "回應訊息", required = true, position = 2)
    private String msg;

    @ApiModelProperty(value = "回應資訊", required = true, position = 3)
    private Object data;

    protected static final String SUCCESS = "success";
    protected static final String FAIL = "fail";

    public static apiResp success() {
        apiResp apiSuccess = new apiResp();
        apiSuccess.setRtnCode(0);
        apiSuccess.setMsg(SUCCESS);
        return apiSuccess;
    }

    public static apiResp success(Object data) {
        apiResp apiSuccess = new apiResp();
        apiSuccess.setRtnCode(0);
        apiSuccess.setMsg(SUCCESS);
        apiSuccess.setData(data);
        return apiSuccess;
    }

    public static apiResp success(String message, Object data) {
        apiResp apiSuccess = new apiResp();
        apiSuccess.setRtnCode(0);
        apiSuccess.setMsg(message);
        apiSuccess.setData(data);
        return apiSuccess;
    }

    public static apiResp fail() {
        apiResp apiFail = new apiResp();
        apiFail.setRtnCode(1);
        apiFail.setMsg(FAIL);
        return apiFail;
    }

    public static apiResp fail(String failMsg) {
        apiResp apiFail = new apiResp();
        apiFail.setRtnCode(1);
        apiFail.setMsg(failMsg);
        return apiFail;
    }
    
    public static apiResp fail(String failMsg, int rtnCode) {
        apiResp apiFail = new apiResp();
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
