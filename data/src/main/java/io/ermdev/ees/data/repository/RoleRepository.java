package io.ermdev.ees.data.repository;

import io.ermdev.ees.data.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("from Role where id=:roleId")
    Role findById(@Param("roleId") Long id);
}
