// modeled off of StudentController not from repo

package com.fitzgerald.studentRegDemo.controller;

import com.fitzgerald.studentRegDemo.model.Course;
import com.fitzgerald.studentRegDemo.model.Student;
import com.fitzgerald.studentRegDemo.repository.CourseRepository;
import com.fitzgerald.studentRegDemo.repository.StudentRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseRepository courseRepo;
    private final StudentRepository studentRepo;

    public CourseController(CourseRepository courseRepo, StudentRepository studentRepo) 
    {
        this.courseRepo = courseRepo;
        this.studentRepo = studentRepo;
    }

    // GET all courses
    @GetMapping
    public List<Course> getCourses() 
    {
        return courseRepo.findAll();
    }

    // POST a new course (The target for Postman testing)
    @PostMapping
    public Course createCourse(@RequestBody Course course)
     {
        return courseRepo.save(course);
    }

    // PUT updating a course
    @PutMapping("/{courseId}")
    public Course updateCourse(@PathVariable Long courseId, @RequestBody Course updatedCourse) {

    Course course = courseRepo.findById(courseId).orElseThrow();

    course.setName(updatedCourse.getName());
    course.setSize(updatedCourse.getSize());
    course.setRoom(updatedCourse.getRoom());
    course.setInstructor(updatedCourse.getInstructor());

    return courseRepo.save(course);
    }

    // ADD a student to the COURSE if there is space
    @PostMapping("/{courseId}/students/{studentId}")
    public Course addStudent(@PathVariable Long courseId, @PathVariable Long studentId) 
    {

        Course course = courseRepo.findById(courseId).orElseThrow();
        Student student = studentRepo.findById(studentId).orElseThrow();

        if(course.getRoster().size() >= course.getSize()) 
            {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Course full");
        }

        course.getRoster().add(student);
        return courseRepo.save(course);
    }

    // DELETE a student from a course course
    @DeleteMapping("/{courseId}/students/{studentId}")
    public Course removeStudent(@PathVariable Long courseId, @PathVariable Long studentId) 
    {

        Course course = courseRepo.findById(courseId).orElseThrow();

        course.getRoster().removeIf(s -> s.getId().equals(studentId));

        return courseRepo.save(course);
    }

    // DELETE a course
    @DeleteMapping("/{courseId}")
    public void deleteCourse(@PathVariable Long courseId) {
        courseRepo.deleteById(courseId);
    }
}