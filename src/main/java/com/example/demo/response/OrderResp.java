package com.example.demo.response;

import java.util.List;
import java.util.Map;

import com.example.demo.entity.OrderInfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "�إ߭q��")
public class OrderResp extends apiResp {
	
	@ApiModelProperty(value = "�q��^�ǽs��", example = "true", position = 1)
	private String orderId;
	
	@ApiModelProperty(value = "�q�ʤH", example = "true", position = 2)
	private String customerName;
	
	@ApiModelProperty(value = "�q�ʪ��B", example = "true", position = 3)
	private Integer totalAmount;
	
	@ApiModelProperty(value = "���G", example = "true", position = 4)
	private List<OrderInfo> data;
	
	public static OrderResp success(Map data) {
    	OrderResp apiSuccess = new OrderResp();
        apiSuccess.setRtnCode(0);
        apiSuccess.setMsg(SUCCESS);
        List<OrderInfo> goodsList = (List<OrderInfo>) data.get("goods");
        apiSuccess.setData(goodsList);
        apiSuccess.setOrderId(data.get("orderId").toString());
        apiSuccess.setCustomerName(data.get("customerName").toString());
        int totalAmount = Integer.parseInt(data.get("totalAmount").toString());
        apiSuccess.setTotalAmount(totalAmount);
        return apiSuccess;
    }

    public List<OrderInfo> getData() {
		return data;
	}

	public void setData(List<OrderInfo> data) {
		this.data = data;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Integer getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Integer totalAmount) {
		this.totalAmount = totalAmount;
	}
	
}
