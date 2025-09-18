package com.example.Chronicle.Models;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
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


    private String email;

    private String password;

    private String username;

    private String role;


    //tells JPA that the relationship is already managed by the author field in the Post entity
    @OneToMany(mappedBy = "author")
    private List<Post> posts;

    @ManyToMany
    //Because it's a many-to-many relationship, a third table is needed to link them. This annotation configures that "join table".
    @JoinTable(
        name ="account_authority",
        joinColumns = {@JoinColumn(name = "account_id",referencedColumnName = "id" )},
        inverseJoinColumns = {@JoinColumn(name = "authority_id",referencedColumnName = "id")}
    )
    private Set<Authority> authorities=new HashSet<>();
}
