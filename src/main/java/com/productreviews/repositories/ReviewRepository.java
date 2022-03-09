package com.productreviews.repositories;

import com.productreviews.models.Review;
import com.productreviews.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "review", path = "review")
public interface ReviewRepository extends CrudRepository<Review, Long> {
    List<Review> findByUser(User user);
}
