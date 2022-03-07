package com.productreviews.Repositories;

import com.productreviews.Models.mockProduct;
import com.productreviews.Models.mockUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "users", path = "users")
public interface mockUserRepository extends CrudRepository<mockUser, Long> {
    mockUser findById(long id);

}
