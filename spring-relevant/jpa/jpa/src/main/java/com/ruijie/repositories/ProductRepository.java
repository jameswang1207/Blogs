package com.ruijie.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ruijie.entity.Product;
import com.ruijie.entity.User;

public interface ProductRepository extends CrudRepository<Product, Long>{
    List<User> findByName(String name);
}
