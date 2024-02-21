package com.kamilglazer.coursesusers.repository;

import com.kamilglazer.coursesusers.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course,Long>  {
}
