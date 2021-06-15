package com.example.demo.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.shareddomain.dto.CartProductInfoDto;

import feign.Param;

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
}
