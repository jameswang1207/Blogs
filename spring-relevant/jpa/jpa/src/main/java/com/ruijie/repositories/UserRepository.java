package com.ruijie.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ruijie.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findByLastname(String lastname);
}
