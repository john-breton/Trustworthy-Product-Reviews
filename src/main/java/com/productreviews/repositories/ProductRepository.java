package com.productreviews.repositories;
import com.productreviews.models.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "product", path = "product")
public interface ProductRepository extends CrudRepository<Product, Long> {

    List<Product> findByOrderByAverageRatingAsc();
    List<Product> findByOrderByAverageRatingDesc();
    List<Product> findByCategory(String category);
    List<Product> findByCategoryOrderByAverageRatingAsc(String catgeory);
    List<Product> findByCategoryOrderByAverageRatingDesc(String catgeory);
}
