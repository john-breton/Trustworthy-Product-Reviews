package com.productreviews.Repositories;

import com.productreviews.Models.mockProduct;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "products", path = "products")
public interface mockProductRepository extends CrudRepository<mockProduct, Long> {

    mockProduct findById(long id);

}
