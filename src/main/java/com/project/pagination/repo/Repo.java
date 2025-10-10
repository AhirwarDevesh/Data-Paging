package com.project.pagination.repo;

import com.project.pagination.model.Student;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Repo extends JpaRepository<Student, Long> {
    Page<Student> findAll(Pageable pageable);

    Page<Student> findByNameContainingIgnoreCaseOrCityContainingIgnoreCase(String name, String city, Pageable pageable);
}
