package io.ermdev.ees.data.repository;

import io.ermdev.ees.data.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long>{
}
