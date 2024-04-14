package com.kamilglazer.coursesusers.service;


import com.kamilglazer.coursesusers.dto.CourseRequest;
import com.kamilglazer.coursesusers.dto.UserRequest;
import com.kamilglazer.coursesusers.model.Course;
import com.kamilglazer.coursesusers.model.UserEntity;
import com.kamilglazer.coursesusers.model.UserRole;
import com.kamilglazer.coursesusers.repository.CourseRepository;
import com.kamilglazer.coursesusers.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApiService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CourseRepository courseRepository;

    @Transactional
    public String save(UserRequest userRequest) {

        if(userRepository.existsByUsername(userRequest.getUsername())){
            throw new RuntimeException("User with this username already exists!");
        }

        UserEntity user = UserEntity.builder()
                .username(userRequest.getUsername())
                .name(userRequest.getName())
                .surname(userRequest.getSurname())
                .age(userRequest.getAge())
                .active(true)
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .build();

        UserRole userRole = new UserRole(user, "ROLE_USER");
        user.setUserRole(userRole);

        userRepository.save(user);
        return "Registration successful.";
    }

    public List<CourseRequest> getAllCourses() {
        List<Course> courseList = courseRepository.findAll();
        return courseList.stream().map(this::mapToCourseRequest).toList();
    }

    public List<CourseRequest> getFavorites(UserDetails userDetails) {
        String username = userDetails.getUsername();
        UserEntity user = userRepository.findByUsername(username);
        List<Course> courseList = user.getCourseList();
        return courseList.stream().map(this::mapToCourseRequest).toList();
    }

    private CourseRequest mapToCourseRequest(Course course) {
        return CourseRequest.builder()
                .author(course.getAuthor())
                .content(course.getContent())
                .title(course.getTitle())
                .build();
    }

    @Transactional
    public String courseToFavorites(Long id,UserDetails userDetails){

        Optional<Course> optionalCourse = courseRepository.findById(id);

        if(optionalCourse.isPresent()){
            String username = userDetails.getUsername();
            UserEntity user = userRepository.findByUsername(username);
            Course course = optionalCourse.get();

            if(user.getCourseList().contains(course)){
                throw new RuntimeException("This course is already in your favorites!");
            }

            user.addCourse(course);
            userRepository.save(user);
            return "Course successfully added to your favorites.";
        }else{
            throw new RuntimeException("Failed to add course to favorites: Course not found.");
        }
    }


    @Transactional
    public CourseRequest addCourse(CourseRequest courseRequest) {
        Course course = Course.builder()
                .title(courseRequest.getTitle())
                .author(courseRequest.getAuthor())
                .content(courseRequest.getContent())
                .build();
        courseRepository.save(course);
        return courseRequest;
    }

    @Transactional
    public void putCourse(Long id, CourseRequest courseRequest) {

        Optional<Course> optionalCourse = courseRepository.findById(id);

        if(optionalCourse.isPresent()){
            Course course = optionalCourse.get();
            course.setAuthor(courseRequest.getAuthor());
            course.setContent(courseRequest.getContent());
            course.setTitle(courseRequest.getTitle());
            courseRepository.save(course);
        }else{
            throw new RuntimeException("Bad request!");
        }
    }

    @Transactional
    public void patchCourse(Long id, CourseRequest courseRequest) {

        Optional<Course> optionalCourse = courseRepository.findById(id);

        if(optionalCourse.isPresent()){
            Course course = optionalCourse.get();

            if(courseRequest.getTitle() != null){
                course.setTitle(courseRequest.getTitle());
            }

            if(courseRequest.getContent() != null){
                course.setContent(courseRequest.getContent());
            }

            if(courseRequest.getAuthor() != null){
                course.setAuthor(courseRequest.getAuthor());
            }

            courseRepository.save(course);
        }else{
            throw new RuntimeException("Bad request!");
        }
    }

    @Transactional
    public void deleteCourse(Long id) {

        Optional<Course> optionalCourse = courseRepository.findById(id);

        if(optionalCourse.isPresent()){
            courseRepository.delete(optionalCourse.get());
        }else{
            throw new RuntimeException("Bad request! Course not found.");
        }
    }

    @Transactional
    public String courseFromFavorites(Long id, UserDetails userDetails) {

        Optional<Course> optionalCourse = courseRepository.findById(id);

        if(optionalCourse.isPresent()){
            String username = userDetails.getUsername();
            UserEntity user = userRepository.findByUsername(username);

            if(!user.getCourseList().contains(optionalCourse.get())){
                throw new RuntimeException("Bad request! Course not found in your favorites.");
            }

            user.removeCourseFromFavorites(optionalCourse.get());
            userRepository.save(user);

            return "Course successfully removed from favorites";
        }else{
            throw new RuntimeException("Bad request! Course ID not found.");
        }
    }
}
