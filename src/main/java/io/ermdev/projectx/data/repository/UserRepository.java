package io.ermdev.projectx.data.repository;

import io.ermdev.projectx.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("from User where username=:username")
    User findByUsername(@Param("username") String username);
}
