package com.example.demo.shareddomain.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "cart_product")
public class CartProductInfoDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	// 流水編號
	@Id
	@Column(name="id")
	private String id;
	
	// 購物車編號
	@Column(name="cart_number")
	private String cartNumber;
	
	// 商品編號
	@Column(name="product_id")
	private Integer productId;
	
	// 商品名稱
	@Column(name="product_name")
	private String productName;
	
	// 單價金額
	@Column(name="amount")
	private LocalDateTime amount;
	
	// 創建者
	@Column(name="created_by")
	private String createdBy;
	
	// 創建日期
	@Column(name="created_date")
	private LocalDateTime created_date;

	// 最後修改者
	@Column(name="last_modified_by")
	private LocalDateTime lastModifiedBy;
	
	// 最後修改日期
	@Column(name="last_modified_date")
	private LocalDateTime lastModifiedDate;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCartNumber() {
		return cartNumber;
	}

	public void setCartNumber(String cartNumber) {
		this.cartNumber = cartNumber;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public LocalDateTime getAmount() {
		return amount;
	}

	public void setAmount(LocalDateTime amount) {
		this.amount = amount;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getCreated_date() {
		return created_date;
	}

	public void setCreated_date(LocalDateTime created_date) {
		this.created_date = created_date;
	}

	public LocalDateTime getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(LocalDateTime lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public LocalDateTime getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
