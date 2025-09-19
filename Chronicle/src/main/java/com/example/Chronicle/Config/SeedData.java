package com.example.Chronicle.Config;

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
      for(Authorities auth: Authorities.values()){
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
        account02.setEmail("user2@example.com");
        account02.setPassword("password2");
        account02.setUsername("user2");
        account02.setRole(Roles.EDITOR.getRole());
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
            post1.setTitle("Welcome to Chronicle");
            post1.setContent("This is your first post. Edit or delete it, then start writing!");
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
