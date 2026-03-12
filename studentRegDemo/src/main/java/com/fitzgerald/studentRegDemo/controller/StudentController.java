package com.fitzgerald.studentRegDemo.controller;
import com.fitzgerald.studentRegDemo.model.Student;
import com.fitzgerald.studentRegDemo.repository.StudentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentRepository repository;

    public StudentController(StudentRepository repository) {
        this.repository = repository;
    }

    // GET all students
    @GetMapping
    public List<Student> getAll() {
        return repository.findAll();
    }

    // POST a new student (The target for Postman testing)
    @PostMapping
    public Student create(@RequestBody Student student) {
        // Lab Logic: You could add validation here to throw an error
        // if GPA > 4.0, giving students a negative test case.
        return repository.save(student);
    }

    // DELETE a student
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
