package com.sparta.springcore.mockobject;

import com.sparta.springcore.model.Product;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MockProductRepository {
    // 마치 실제 DB에서 상품을 여러개 갖고있는 것 처럼 흉내낸 부분임.
    private List<Product> products = new ArrayList<>();
    private Long id = 1L;

    // 회원 ID 로 등록된 모든 상품 조회
    public List<Product> findAllByUsername(Long userId) {
        List<Product> userProducts = new ArrayList<>();
        for (Product product : products) {
            if (product.getId().equals(userId)) {
                userProducts.add(product);
            }
        }

        return userProducts;
    }

    // 모든 상품 조회 (관리자용)
    public List<Product> findAll() {
        return products;
    }

    // 상품 저장
    public Product save(Product product) {
        product.setId(id); //상품 save 시 id가 DB에서 자동으로 1씩 높아지며 저장되는 것을 i++로 흉내냄.
        ++id;
        products.add(product);
        return product;
    }

    public Optional<Product> findById(Long id) { // Optional = null이 포함되면 false 반환하는 클래스
        for (Product product : products) {
            if (product.getId().equals(id)) { // 파라미터로 가져온 Long id가 product의 id와 같은 경우 == 해당 Product가 존재한다는 뜻이므로 true.
                return Optional.of(product); // true인데 .of 사용하면 of 이하의 값을 반환하고, of 이하의 값이 null이라면 NullPointerException 발생시킴.
            }
        }

        return Optional.empty(); // null을 표현함.
    }
}