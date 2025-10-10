package com.project.pagination.controller;

import com.project.pagination.model.Student;
import com.project.pagination.service.CacheService;
import com.project.pagination.service.PageService;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequestMapping("api/student")
public class PageController {

    @Autowired
    private PageService pageService;

    @Autowired
    CacheService cacheService;

    @PostMapping("/addStudent")
    public ResponseEntity<Student> getAllStudents(@RequestBody Student student) {
        return new ResponseEntity<>(pageService.addStudent(student), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable Long id, @RequestBody Student studentUpdate) {
        return pageService.updateStudent(id, studentUpdate);
    }

    @GetMapping("/getCacheData")
    public String getCacheData() {
        cacheService.printCacheContents("getStudents");
        return "Cache data printed in console";
    }

    @GetMapping("/getAllStudents")
    public ResponseEntity<Page<Student>> getAllStudents(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) String search) {

        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);

        Page<Student> pageStudent = pageService.getAllStudents(pageNo, pageSize, sort, search);
        return new ResponseEntity(pageStudent, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable Long id) {
        pageService.deleteStudent(id);
        return "Student has been deleted";
    }
}
