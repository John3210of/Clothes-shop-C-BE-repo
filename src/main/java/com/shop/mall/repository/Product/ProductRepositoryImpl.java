package com.shop.mall.repository.Product;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.mall.dto.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.shop.mall.domain.QProduct.product;
import static org.aspectj.util.LangUtil.isEmpty;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<ProductResponseDto.ProductList> searchByRecent(Pageable pageable, String category, String searchKeyword) {
        return getSearchByRecent(pageable, category, searchKeyword);
    }

    @Override
    public Page<ProductResponseDto.ProductList> searchByCost(Pageable pageable, String category, String searchKeyword) {
        return getSearchByCost(pageable, category, searchKeyword);
    }

    @Override
    public Page<ProductResponseDto.ProductList> searchByReviewCnt(Pageable pageable, String category, String searchKeyword) {
        return getSearchByReview(pageable,category,searchKeyword);
    }

    @Override
    public List<ProductResponseDto.ProductList> findAllByOffsetId(Long lastId) {
        return getAllByNoOffsetId(lastId);
    }

    //getAllNoOffset
    private List<ProductResponseDto.ProductList> getAllByNoOffsetId(Long lastId){
        return jpaQueryFactory
                .select(Projections.bean(ProductResponseDto.ProductList.class,product.id,product.title,product.category,product.reviewCnt,product.detail,product.price,product.firstImg))
                .from(product)
                .where(product.id.gt(lastId))
                .orderBy(product.id.asc())
                .limit(20)
                .fetch();

    }

    //searchByRecent
    private Page<ProductResponseDto.ProductList> getSearchByRecent(Pageable pageable, String category, String searchKeyword) {
        List<ProductResponseDto.ProductList> productLists = jpaQueryFactory
                .select(Projections.bean(ProductResponseDto.ProductList.class, product.id, product.title, product.category, product.reviewCnt, product.detail, product.price, product.firstImg))
                .from(product)
                .where(
                        booleanSearchKeyword(searchKeyword),
                        booleanCategory(category)
                )
                .orderBy(product.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        Long count = jpaQueryFactory
                .select(product.count())
                .from(product)
                .where(
                        booleanSearchKeyword(searchKeyword),
                        booleanCategory(category)
                )
                .fetchOne();

        return new PageImpl<>(productLists,pageable,count);
    }

    //searchByCost
    private Page<ProductResponseDto.ProductList> getSearchByCost(Pageable pageable, String category, String searchKeyword) {
        List<ProductResponseDto.ProductList> productLists = jpaQueryFactory
                .select(Projections.bean(ProductResponseDto.ProductList.class, product.id, product.title, product.category, product.reviewCnt, product.detail, product.price, product.firstImg))
                .from(product)
                .where(
                        booleanSearchKeyword(searchKeyword),
                        booleanCategory(category)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(product.price.desc())
                .fetch();

        Long count = jpaQueryFactory
                .select(product.count())
                .from(product)
                .where(
                        booleanSearchKeyword(searchKeyword),
                        booleanCategory(category)
                )
                .fetchOne();
        return new PageImpl<>(productLists,pageable,count);
    }

    //searchByReviewCnt
    private Page<ProductResponseDto.ProductList> getSearchByReview(Pageable pageable, String category, String searchKeyword) {
        List<ProductResponseDto.ProductList> productLists = jpaQueryFactory
                .select(Projections.bean(ProductResponseDto.ProductList.class, product.id, product.title, product.category, product.reviewCnt, product.detail, product.price, product.firstImg))
                .from(product)
                .where(
                        booleanSearchKeyword(searchKeyword),
                        booleanCategory(category)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(product.reviewCnt.desc())
                .fetch();

        Long count = jpaQueryFactory
                .select(product.count())
                .from(product)
                .where(
                        booleanSearchKeyword(searchKeyword),
                        booleanCategory(category)
                )
                .fetchOne();


        return new PageImpl<>(productLists,pageable,count);
    }

    //동적 쿼리를 위한 문들
    private BooleanExpression booleanSearchKeyword(String searchTitle) {
        return isEmpty(searchTitle) ? null : product.title.contains(searchTitle);
    }

    private BooleanExpression booleanCategory(String category) {
        return isEmpty(category) ? null : product.category.eq(category);
    }
}
