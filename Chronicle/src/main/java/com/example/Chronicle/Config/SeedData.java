package com.example.Chronicle.Config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.Chronicle.Models.Account;
import com.example.Chronicle.Models.Post;
import com.example.Chronicle.Service.AccountService;
import com.example.Chronicle.Service.PostService;

//Seed initial data into the database
//Will only run if the database is empty
@Component
public class SeedData implements CommandLineRunner {
    // command line runner runs when the application starts

    @Autowired
    private PostService postService;

    @Autowired
    private AccountService accountService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Seeding data...");

        Account account01 = new Account();
        Account account02 = new Account();

        account01.setEmail("user1@example.com");
        account01.setPassword("password1");
        account01.setUsername("user1");
        account02.setEmail("user2@example.com");
        account02.setPassword("password2");
        account02.setUsername("user2");

        accountService.saveAccount(account01);
        accountService.saveAccount(account02);
        // String ... args means it can take multiple string arguments
        // Seed data logic here
        List<Post> posts = postService.getAll();
        if (posts.size() == 0) {
            Post post1 = new Post();
            post1.setTitle("Welcome to Chronicle");
            post1.setContent("This is your first post. Edit or delete it, then start writing!");
            postService.save(post1);

            Post post2 = new Post();
            post2.setTitle("Getting Started");
            post2.setContent("Chronicle is a simple blogging platform. Start by creating new posts.");
            postService.save(post2);

            Post post3 = new Post();
            post3.setTitle("Features");
            post3.setContent("Chronicle supports creating, editing, and deleting posts. Enjoy blogging!");
            postService.save(post3);

        }

    }

}
