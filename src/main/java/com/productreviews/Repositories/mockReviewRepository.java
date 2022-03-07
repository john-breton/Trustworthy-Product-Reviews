package com.productreviews.Repositories;

import com.productreviews.Models.mockProduct;
import com.productreviews.Models.mockReview;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "reviews", path = "reviews")
public interface mockReviewRepository extends CrudRepository<mockReview, Long> {
    mockReview findById(long id);


}
