package com.sparta.springcore;

import java.sql.SQLException;
import java.util.List;

public class ProductService {
    public List<Product> getProducts() throws SQLException {
        ProductRepository productRepository = new ProductRepository();
        return productRepository.getProducts();
    }

    public Product createProduct(ProductRequestDto requestDto) throws SQLException {
        ProductRepository productRepository = new ProductRepository();
        // DTO DB
        Product product = new Product(requestDto);
        productRepository.createProduct(product);
        return product;
    }

    public Product updateProduct(Long id, ProductMypriceRequestDto requestDto) throws SQLException {
        ProductRepository productRepository = new ProductRepository();
        Product product = productRepository.getProduct(id);
        if (product == null) {
            throw new NullPointerException(" .");
        }
        int myPrice = requestDto.getMyprice();
        productRepository.updateProductMyPrice(id, myPrice);
        return product;
    }
}
