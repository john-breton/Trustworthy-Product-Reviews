package com.productreviews.repositories;
import com.productreviews.models.Product;
import com.productreviews.models.common.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "product", path = "product")
public interface ProductRepository extends CrudRepository<Product, Long> {

    List<Product> findByOrderByAverageRatingAsc();
    List<Product> findByOrderByAverageRatingDesc();
    List<Product> findByCategory(Category category);
    List<Product> findByCategoryOrderByAverageRatingAsc(Category category);
    List<Product> findByCategoryOrderByAverageRatingDesc(Category category);
}
