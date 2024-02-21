package com.kamilglazer.coursesusers.controller;

import com.kamilglazer.coursesusers.dto.CourseRequest;
import com.kamilglazer.coursesusers.dto.UserRequest;
import com.kamilglazer.coursesusers.model.Course;
import com.kamilglazer.coursesusers.service.ApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ApiController {

    private final ApiService apiService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> register(@RequestBody UserRequest userRequest){
        return apiService.save(userRequest);
    }

    @GetMapping("/courses")
    @ResponseStatus(HttpStatus.OK)
    public List<CourseRequest> getAllCourses(){
        return apiService.getAllCourses();
    }

    @PatchMapping("/courseToFavorites/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> courseToFavorites(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails){
        return apiService.courseToFavorites(id,userDetails);
    }

    @GetMapping("/favorites")
    @ResponseStatus(HttpStatus.OK)
    public List<CourseRequest> getFavorites(@AuthenticationPrincipal UserDetails userDetails){
        return apiService.getFavorites(userDetails);
    }

    @DeleteMapping("/courseFromFavorites/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> courseFromFavorites(@PathVariable Long id,@AuthenticationPrincipal UserDetails userDetails){
        return apiService.courseFromFavorites(id,userDetails);
    }

    @PostMapping("/addCourse")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Course> addCourse(@RequestBody CourseRequest courseRequest){
        return apiService.addCourse(courseRequest);
    }

    @PutMapping("/course/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Course> putCourse(@PathVariable Long id, @RequestBody CourseRequest courseRequest){
        return apiService.putCourse(id,courseRequest);
    }

    @PatchMapping("/course/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Course> patchCourse(@PathVariable Long id, @RequestBody CourseRequest courseRequest){
        return apiService.patchCourse(id,courseRequest);
    }

    @DeleteMapping("/deleteCourse/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> deleteCourse(@PathVariable Long id){
       return apiService.deleteCourse(id);
    }

}
