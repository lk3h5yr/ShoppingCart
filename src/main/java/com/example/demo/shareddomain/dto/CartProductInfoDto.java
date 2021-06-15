package com.example.demo.shareddomain.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private String id;
	
	// �ʪ����s��
	@Column(name="cart_number")
	private String cartNumber;
	
	// �ӫ~�s��
	@Column(name="product_id")
	private String productId;
	
	// �ӫ~�W��
	@Column(name="product_name")
	private String productName;
	
	// ������B
	@Column(name="amount")
	private Integer amount;
	
	// �Ыت�
	@Column(name="created_by")
	private String createdBy;
	
	// �Ыؤ��
	@Column(name="created_date")
	private Date createdDate;

	// �̫�ק��
	@Column(name="last_modified_by")
	private String lastModifiedBy;
	
	// �̫�ק���
	@Column(name="last_modified_date")
	private Date lastModifiedDate;
	
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

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
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

	public Date getCreated_date() {
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
