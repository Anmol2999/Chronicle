package com.example.Chronicle.Config;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.Chronicle.Models.Account;
import com.example.Chronicle.Models.Authority;
import com.example.Chronicle.Models.Post;
import com.example.Chronicle.Service.AccountService;
import com.example.Chronicle.Service.AuthorityService;
import com.example.Chronicle.Service.PostService;
import com.example.Chronicle.util.constants.Authorities;
import com.example.Chronicle.util.constants.Roles;

//Seed initial data into the database
//Will only run if the database is empty
@Component
public class SeedData implements CommandLineRunner {
  // command line runner runs when the application starts

  @Autowired
  private PostService postService;

  @Autowired
  private AccountService accountService;
  @Autowired
  private AuthorityService authorityService;

  @Override
  public void run(String... args) throws Exception {
    for (Authorities auth : Authorities.values()) {
      Authority authority = new Authority();
      authority.setId(auth.getAuthorityId());
      authority.setName(auth.getAuthorityString());
      authorityService.save(authority);
    }

    Account account01 = new Account();
    Account account02 = new Account();

    account01.setEmail("user1@example.com");
    account01.setPassword("password1");
    account01.setUsername("user1");
    account01.setRole(Roles.ADMIN.getRole());
    account01.setAge(30);
    account01.setDateofBirth(LocalDate.parse("1993-05-15"));
    account01.setGender("male");

    account02.setEmail("anu436657@gmail.com");
    account02.setPassword("password2");
    account02.setUsername("user2");
    account02.setRole(Roles.EDITOR.getRole());
    account02.setAge(28);
    account02.setDateofBirth(LocalDate.parse("1995-08-22"));
    account02.setGender("female");

    Set<Authority> authorities = new HashSet<>();
    authorityService.findById(Authorities.RESET_ANY_USER_PASSWORD.getAuthorityId()).ifPresent(authorities::add);
    authorityService.findById(Authorities.ACCESS_ADMIN_DASHBOARD.getAuthorityId()).ifPresent(authorities::add);

    account01.setAuthorities(authorities);
    account02.setAuthorities(authorities);

    accountService.saveAccount(account01);
    accountService.saveAccount(account02);
    // String ... args means it can take multiple string arguments
    // Seed data logic here
    List<Post> posts = postService.getAll();
    if (posts.size() == 0) {
      Post post1 = new Post();
      post1.setTitle("Spring Boot");
      post1.setContent("""
         
      <div >
          <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRNou7-DlVcN5nOVq73_RDi6OAYZAzOknfzQw&s">

          
      </div>





       

              Spring Boot is an open-source Java framework used for programming standalone, production-grade Spring-based applications with a bundle of libraries that make project startup and management easier.[3] Spring Boot is a convention-over-configuration extension for the Spring Java platform intended to help minimize configuration concerns while creating Spring-based applications.[4][5] The application can still be adjusted for specific needs, but the initial Spring Boot project provides a preconfigured \"opinionated view\" of the best configuration to use with the Spring platform and selected third-party libraries.[6][7]
              "Spring Boot can be used to build microservices, web applications, and console applications.[3][8]
              
              "Features
             
              "Embedded Tomcat, Jetty or Undertow web application server.[9]
              "Provides opinionated 'starter' Project Object Models (POMs) for the build tool. The only build tools supported are Maven and Gradle.[10][11]
              "Automatic configuration of the Spring Application.[12]
              
              "Provides production-ready[4] functionality such as metrics,[13] health checks,[13] and externalized configuration.[14]
              "No code generation is required.[9]
              
              "No XML configuration is required.[10]
             
              "Optional support for Kotlin and Apache Groovy in addition to Java.[3][15]"
              """
                  
      );
      post1.setAuthor(account01);
      postService.save(post1);

      Post post2 = new Post();
      post2.setTitle("Getting Started");
      post2.setContent("Chronicle is a simple blogging platform. Start by creating new posts.");
      post2.setAuthor(account02);
      postService.save(post2);

      Post post3 = new Post();
      post3.setTitle("Features");
      post3.setContent("Chronicle supports creating, editing, and deleting posts. Enjoy blogging!");
      postService.save(post3);

    }

  }

}
