package com.productreviews.repositories;

import com.productreviews.models.Review;
import com.productreviews.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "review", path = "review")
public interface ReviewRepository extends CrudRepository<Review, Long> {
    /* associatedProduct.id == productId && user in users && score >= min && score <= max */
    List<Review> findAllByAssociatedProductIdAndUserInAndScoreGreaterThanEqualAndScoreLessThanEqual(long productId, List<User> users, int min, int max);

    /* associatedProduct.id == productId && score >= min && score <= max */
    List<Review> findAllByAssociatedProductIdAndScoreGreaterThanEqualAndScoreLessThanEqual(long productId, int min, int max);
}
