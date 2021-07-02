package com.sparta.springcore;

import java.sql.SQLException;
import java.util.List;

public class ProductService {
    final ProductRepository productRepository;

    public ProductService() {
        this.productRepository = new ProductRepository();
    }

    public List<Product> getProducts() throws SQLException {
        return this.productRepository.getProducts();
    }

    public Product createProduct(ProductRequestDto requestDto) throws SQLException {
        // DTO DB
        Product product = new Product(requestDto);
        this.productRepository.createProduct(product);
        return product;
    }

    public Product updateProduct(Long id, ProductMypriceRequestDto requestDto) throws SQLException {
        Product product = this.productRepository.getProduct(id);
        if (product == null) {
            throw new NullPointerException(" .");
        }
        int myPrice = requestDto.getMyprice();
        this.productRepository.updateProductMyPrice(id, myPrice);
        return product;
    }
}
