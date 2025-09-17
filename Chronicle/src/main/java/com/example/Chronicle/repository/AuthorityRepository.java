package com.example.Chronicle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Chronicle.Models.Authority;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    
}
