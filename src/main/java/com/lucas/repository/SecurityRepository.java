package com.lucas.repository;

import com.lucas.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SecurityRepository extends JpaRepository<User, Integer> {

    @Query("select u from User u where u.username=:username")
    User findByUsername(String username);
}
