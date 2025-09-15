package com.example.Chronicle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Chronicle.Models.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    
}
