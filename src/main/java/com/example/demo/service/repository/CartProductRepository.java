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
	
	/**
	 * �d�ߦU���ӫ~�ƶq
	 * @param productId �ӫ~�N��
	 * @return
	 */
	int countByProductId(String productId);
	
	/**
	 * �R����Ʈw���
	 * @param cartNumber �ʪ����s��
	 * @param createdBy �ʶR�H
	 */
	void deleteByCartNumberAndCreatedBy(String cartNumber, String createdBy);
	
}
