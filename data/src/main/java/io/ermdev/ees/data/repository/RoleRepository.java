package io.ermdev.ees.data.repository;

import io.ermdev.ees.data.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
