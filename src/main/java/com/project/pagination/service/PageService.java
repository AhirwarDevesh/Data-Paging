package com.project.pagination.service;

import com.project.pagination.model.Student;
import com.project.pagination.repo.Repo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

@Service
public class PageService {

    @Autowired
    private Repo repo;

    public Student addStudent(Student student) {
        return repo.save(student);
    }

    @CachePut(value = "getStudents", key = "#id")
    public Student updateStudent(Long id, Student studentUpdate) {
        repo.findById(id).ifPresent(student -> {
            student.setMarks(studentUpdate.getMarks());
            student.setName(studentUpdate.getName());
            student.setCity(studentUpdate.getCity());
            repo.save(student);
        });
        return studentUpdate;
    }

    @Cacheable(value = "getStudents", key = "#pageNo + '-' + #pageSize + '-' + #sort.toString() + '-' + #search")
    public Page<Student> getAllStudents(int pageNo, int pageSize, Sort sort, String search) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        if (search != null && !search.trim().isEmpty()) {
            return repo.findByNameContainingIgnoreCaseOrCityContainingIgnoreCase(search, search, pageable);
        } else {
            return repo.findAll(pageable);
        }
    }

    @Transactional
    @CacheEvict(value = "getStudents",key = "#id")
    public void deleteStudent(Long id) {
        System.out.println("Removing Student data with id " + id + " from cache");
        repo.deleteById(id);
    }
}