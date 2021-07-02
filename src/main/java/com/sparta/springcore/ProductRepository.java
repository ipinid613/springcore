package com.sparta.springcore;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//@Component

// Inversion of Control (IoC) 컨테이너
// bean 이라고 부름
//@Component

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
