package com.example.demo.service.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.shareddomain.dto.CartProductInfoDto;

@Repository
public interface CartProductRepository extends CrudRepository<CartProductInfoDto, String>, JpaSpecificationExecutor<CartProductInfoDto> {

}
