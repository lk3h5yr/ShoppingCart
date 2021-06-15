package com.example.demo.request;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "測試請求")
public class testReq {
    
    @ApiModelProperty(value = "購物車編號", required = true, position = 1)
    private String cartNumber;

	@ApiModelProperty(value = "客戶名稱", required = true, position = 2)
    private String customer;
    
    @ApiModelProperty(value = "金額", required = true, position = 3)
    private Integer amount;
    
    private String createdBy;
    
    private LocalDateTime createdDdate;
    
    private String lastModifiedBy;
    
    private LocalDateTime lastModifiedDate;
    

    public String getCartNumber() {
		return cartNumber;
	}

	public void setCartNumber(String cartNumber) {
		this.cartNumber = cartNumber;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getCreatedDdate() {
		return createdDdate;
	}

	public void setCreatedDdate(LocalDateTime createdDdate) {
		this.createdDdate = createdDdate;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public LocalDateTime getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
}
