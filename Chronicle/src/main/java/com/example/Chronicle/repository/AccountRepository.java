package com.example.Chronicle.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Chronicle.Models.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    //
    Optional<Account> findOneByEmailIgnoreCase(String email);

}
