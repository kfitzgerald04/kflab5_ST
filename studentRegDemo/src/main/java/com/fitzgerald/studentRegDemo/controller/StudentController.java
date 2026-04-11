package com.fitzgerald.studentRegDemo.controller;
import com.fitzgerald.studentRegDemo.model.Student;
import com.fitzgerald.studentRegDemo.repository.StudentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;


import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/students")
public class StudentController {

    private final StudentRepository repository;

    public StudentController(StudentRepository repository) {
        this.repository = repository;
    }

    // validation (business rules)
    private void validate(Student student) {
        if(student.getGpa() < 0.0 || student.getGpa() > 4.0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "GPA must be between 0 and 4.0");
        }

        if(student.getName() == null || student.getName().length() > 255) {
            throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "Name must be 1-255 characters");
        }
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

        // enforcing business rules
        validate(student);

        return repository.save(student);
    }

    // UPDATE a student
    @PutMapping("/{id}")
    public ResponseEntity<Student> update(@PathVariable Long id, @RequestBody Student updatedStudent) {
        
        // enforcing business rules
        validate(updatedStudent);

        return repository.findById(id).map(student -> {
            student.setName((updatedStudent.getName()));
            student.setMajor((updatedStudent.getMajor()));
            student.setGpa(updatedStudent.getGpa());
            if(student.getGpa() < 0 || student.getGpa() > 4.0) {
            throw new RuntimeException("GPA must be between 0 and 4.0");
        }
            Student saved = repository.save(student);
            return ResponseEntity.ok(saved);
        })
        .orElseGet(() -> ResponseEntity.notFound().build());
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
