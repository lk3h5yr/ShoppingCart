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
	
	// �y���s��
	@Id
	@Column(name="id")
	private String id;
	
	// �ʪ����s��
	@Column(name="cart_number")
	private String cartNumber;
	
	// �ӫ~�s��
	@Column(name="product_id")
	private Integer productId;
	
	// �ӫ~�W��
	@Column(name="product_name")
	private String productName;
	
	// ������B
	@Column(name="amount")
	private LocalDateTime amount;
	
	// �Ыت�
	@Column(name="created_by")
	private String createdBy;
	
	// �Ыؤ��
	@Column(name="created_date")
	private LocalDateTime created_date;

	// �̫�ק��
	@Column(name="last_modified_by")
	private LocalDateTime lastModifiedBy;
	
	// �̫�ק���
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
