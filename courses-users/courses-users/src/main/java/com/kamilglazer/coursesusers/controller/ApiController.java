package com.kamilglazer.coursesusers.controller;

import com.kamilglazer.coursesusers.dto.CourseRequest;
import com.kamilglazer.coursesusers.dto.UserRequest;
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
    public ResponseEntity<String> register(@RequestBody UserRequest userRequest){
        return new ResponseEntity<>(apiService.save(userRequest),HttpStatus.CREATED);
    }

    @GetMapping("/courses")
    public ResponseEntity<List<CourseRequest>> getAllCourses(){
        return ResponseEntity.ok(apiService.getAllCourses());
    }

    @PatchMapping("/courseToFavorites/{id}")
    public ResponseEntity<String> courseToFavorites(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(apiService.courseToFavorites(id,userDetails));
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<CourseRequest>> getFavorites(@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(apiService.getFavorites(userDetails));
    }

    @DeleteMapping("/courseFromFavorites/{id}")
    public ResponseEntity<String> courseFromFavorites(@PathVariable Long id,@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(apiService.courseFromFavorites(id,userDetails));
    }

    @PostMapping("/addCourse")
    public ResponseEntity<CourseRequest> addCourse(@RequestBody CourseRequest courseRequest){
        return new ResponseEntity<>(apiService.addCourse(courseRequest),HttpStatus.CREATED);
    }

    @PutMapping("/course/{id}")
    public ResponseEntity<Void> putCourse(@PathVariable Long id, @RequestBody CourseRequest courseRequest){
        apiService.putCourse(id,courseRequest);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/course/{id}")
    public ResponseEntity<Void> patchCourse(@PathVariable Long id, @RequestBody CourseRequest courseRequest){
        apiService.patchCourse(id,courseRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deleteCourse/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id){
        apiService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

}
