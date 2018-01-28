package io.ermdev.projectx.data.repository;

import io.ermdev.projectx.data.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
