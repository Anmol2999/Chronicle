package com.example.Chronicle.Models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Account {

   
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;


    @Email(message = "Invalid email")
    @NotEmpty(message = "Email is required")
    private String email;

    @NotEmpty(message = "Password is required")
    private String password;

    @NotEmpty(message = "Username is required")
    private String username;
    
    @Min(value = 18)
    @Max(value = 99)
    private int age;

    private String gender;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateofBirth;

    private String photo;

    private String role;


    //tells JPA that the relationship is already managed by the author field in the Post entity
    @OneToMany(mappedBy = "author")
    private List<Post> posts;

    private String password_reset_token;

    private LocalDateTime password_reset_token_expiry;
    @ManyToMany
    //Because it's a many-to-many relationship, a third table is needed to link them. This annotation configures that "join table".
    @JoinTable(
        name ="account_authority",
        joinColumns = {@JoinColumn(name = "account_id",referencedColumnName = "id" )},
        inverseJoinColumns = {@JoinColumn(name = "authority_id",referencedColumnName = "id")}
    )
    private Set<Authority> authorities=new HashSet<>();
}
