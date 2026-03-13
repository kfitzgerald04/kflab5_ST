// modeled off of StudentRepository not from repo

package com.fitzgerald.studentRegDemo.repository;

// import the model that defines the course data type.
import com.fitzgerald.studentRegDemo.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

// uses inheritance to acquire the typical CRUD operations.
public interface CourseRepository extends JpaRepository<Course, Long> {
}
