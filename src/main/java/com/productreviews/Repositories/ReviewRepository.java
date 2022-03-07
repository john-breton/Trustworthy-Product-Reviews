package com.productreviews.Repositories;

import com.productreviews.Models.Review;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "reviews", path = "reviews")
public interface ReviewRepository extends CrudRepository<Review, Long> {
    Review findById(long id);


}
