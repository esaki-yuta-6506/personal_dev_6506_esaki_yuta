package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Shop;

public interface ShopRepository extends JpaRepository<Shop, Integer> {

	List<Shop> findByCustomerIdOrderById(Integer customerId);

	Shop findOneById(Integer id);

	List<Shop> findByOrderById();

}
