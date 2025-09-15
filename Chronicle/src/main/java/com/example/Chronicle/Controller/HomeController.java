package com.example.Chronicle.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


import com.example.Chronicle.Models.Post;
import com.example.Chronicle.Service.PostService;

import org.springframework.ui.Model;


@Controller
public class HomeController {
    
    @Autowired
    private PostService postService;


    @GetMapping("/")
    public String home(Model model) {
        List<Post> posts = postService.getAll();   
         model.addAttribute("posts", posts);
        return "home";
    }

    
}
