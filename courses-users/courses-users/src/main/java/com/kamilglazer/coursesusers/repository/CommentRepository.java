package com.kamilglazer.coursesusers.repository;

import com.kamilglazer.coursesusers.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {
}
