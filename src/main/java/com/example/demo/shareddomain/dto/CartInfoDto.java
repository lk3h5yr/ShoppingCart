package com.example.demo.shareddomain.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "cart")
public class CartInfoDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="cart_number")
	private String cartNumber;
	
	@Column(name="customer")
	private String customer;
	
	@Column(name="amount")
	private Integer amount;
	
	@Column(name="created_by")
	private String createdBy;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="last_modified_by")
	private String lastModifiedBy;
	
	@Column(name="last_modified_date")
	private Date lastModifiedDate;

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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
