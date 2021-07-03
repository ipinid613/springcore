package com.sparta.springcore.repository;

import com.sparta.springcore.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Component

// Inversion of Control (IoC) 컨테이너
// bean 이라고 부름
@Component
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByUserId(Long userId); // ProductService의 getProducts와 연결된 부분임.
    // userId가 같은 모든 테이블(상품들)을 조회하라는 뜻임.
}
