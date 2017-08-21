package com.sergialmar.wschat.repository;

import com.sergialmar.wschat.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by mac on 8/15/17.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where u.name = :name")
    User getByName(@Param(value = "name") String name);
}
