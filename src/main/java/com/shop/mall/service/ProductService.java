package com.shop.mall.service;

import com.shop.mall.domain.Product;
import com.shop.mall.dto.ProductResponseDto;
import com.shop.mall.repository.Product.ProductRepository;
import com.shop.mall.validator.ProductValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductValidator productValidator;

    // 8번 API
    public Page<ProductResponseDto.ProductList> productList(Pageable pageable) {
        return productRepository.findAll(pageable).map(ProductResponseDto.ProductList::productListFrom);
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
