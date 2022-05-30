package com.shop.mall.service;

import com.querydsl.jpa.impl.JPAQuery;
import com.shop.mall.domain.Product;
import com.shop.mall.dto.ProductResponseDto;
import com.shop.mall.repository.Product.ProductRepository;
import com.shop.mall.validator.ProductValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.shop.mall.dto.ProductResponseDto.ProductList.productListFrom;


@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductValidator productValidator;

    // 8번 API
    public List<ProductResponseDto.ProductList> productList(Long lastId) {
        List<Product> listProducts = productRepository.findAllByLastId(lastId);
        List<ProductResponseDto.ProductList> productsList = new ArrayList<>();
        for (Product listProduct : listProducts) {
            productsList.add(productListFrom(listProduct));
        }
        List<ProductResponseDto.ProductList> lists = productRepository.findAllByOffsetId(lastId);
        return lists;
    }


    //9번 API
    public ProductResponseDto.ProductsDetail productsDetail(Long productId) {
        Product product = productValidator.authorization(productId);
        return ProductResponseDto.ProductsDetail.productsDetailFrom(product);
    }


    //10번 API
    public Page<ProductResponseDto.ProductList> conditionProductList(Pageable pageable, String sort, String category, String searchKeyword){
        Page<ProductResponseDto.ProductList> productLists = null;
        switch (sort){
            case "cost":
                productLists = productRepository.searchByCost(pageable,category,searchKeyword);
                break;

            case "review":
                productLists = productRepository.searchByReviewCnt(pageable,category,searchKeyword);
                break;

            default:
                productLists = productRepository.searchByRecent(pageable,category,searchKeyword);
                break;
        }

        return productLists;
    }





}
