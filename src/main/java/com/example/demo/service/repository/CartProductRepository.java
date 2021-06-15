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
	 * �d���ʪ����M��
	 * @param cartNumber �ʪ����s��
	 * @param createdBy
	 * @return
	 */
	List<CartProductInfoDto> findByCartNumberAndCreatedBy(String cartNumber, String createdBy);
	
	/**
	 * �d�߰ӫ~�ƶq
	 * @param cartNumber �ʪ����s��
	 * @param createdBy
	 * @param productId
	 * @return
	 */
	List<CartProductInfoDto> findByCartNumberAndCreatedByAndProductId(String cartNumber, String createdBy, String productId);
}
