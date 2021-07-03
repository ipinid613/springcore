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

    //Controller의 GET /api/products와 연결 된 부분임. Controller에서 getProducts를 사용하고 있는데, userId를 포함하였기 때문에 여기서도 포함 처리.
    public List<Product> getProducts(Long userId) {
        return this.productRepository.findAllByUserId(userId); // productRepository에 findAllByUserId 추가해야 함.
    }

    @Transactional // 메소드 동작이 SQL 쿼리문임을 선언합니다.
    public Product createProduct(ProductRequestDto requestDto, Long userId ) { //이제는 controller에서 userId도 보내주고 있기 때문에 userId 추가.
        // 요청받은 DTO 로 DB에 저장할 객체 만들기
        Product product = new Product(requestDto, userId); // medel-Product에 userId를 보내줌.
        productRepository.save(product);
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

    // 모든 상품 조회 (관리자용)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
