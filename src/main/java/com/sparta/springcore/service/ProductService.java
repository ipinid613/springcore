package com.sparta.springcore.service;

import com.sparta.springcore.dto.ProductMypriceRequestDto;
import com.sparta.springcore.dto.ProductRequestDto;
import com.sparta.springcore.model.Product;
import com.sparta.springcore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private static final int MIN_PRICE = 100;

    @Autowired
    public ProductService(ProductRepository re) {
        this.productRepository = re;
    }

    public List<Product> getProducts() {
        return this.productRepository.findAll();
    }

    @Transactional
    public Product createProduct(ProductRequestDto requestDto)  {
        // DTO DB
        Product product = new Product(requestDto);
        this.productRepository.save(product);
        return product;
    }

    @Transactional
    public Product updateProduct(Long id, ProductMypriceRequestDto requestDto) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 아이디가 존재하지 않습니다.")
        );

        int myPrice = requestDto.getMyprice();
        if (myPrice < MIN_PRICE) {
            throw new IllegalArgumentException("유효하지 않은 관심 가격입니다. 최소 " + MIN_PRICE + " 원 이상으로 설정해 주세요.");
        }

        product.setMyprice(myPrice);
        product.updateMyPrice(myPrice);

        return product;
    }
}
