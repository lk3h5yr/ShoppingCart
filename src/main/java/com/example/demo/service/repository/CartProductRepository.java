package com.example.demo.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.shareddomain.dto.CartProductInfoDto;

@Repository
public interface CartProductRepository extends CrudRepository<CartProductInfoDto, String>, JpaSpecificationExecutor<CartProductInfoDto> {

	List<CartProductInfoDto> getCartProductByCartNumber(String cartNumber);
	
	/**
	 * 查詢購物車清單
	 * @param cartNumber 購物車編號
	 * @param createdBy
	 * @return
	 */
	List<CartProductInfoDto> findByCartNumberAndCreatedBy(String cartNumber, String createdBy);
	
	/**
	 * 查詢商品數量
	 * @param cartNumber 購物車編號
	 * @param createdBy
	 * @param productId
	 * @return
	 */
	List<CartProductInfoDto> findByCartNumberAndCreatedByAndProductId(String cartNumber, String createdBy, String productId);
	
	/**
	 * 查詢各類商品數量
	 * @param productId 商品代號
	 * @return
	 */
	int countByProductId(String productId);
	
	/**
	 * 刪除資料庫資料
	 * @param cartNumber 購物車編號
	 * @param createdBy 購買人
	 */
	void deleteByCartNumberAndCreatedBy(String cartNumber, String createdBy);
	
}
