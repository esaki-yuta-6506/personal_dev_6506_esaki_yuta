package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Mode;

public interface ModeRepository extends JpaRepository<Mode, Integer>{

	Mode findOneById(Integer id);

	List<Mode> findByOrderById();

}
