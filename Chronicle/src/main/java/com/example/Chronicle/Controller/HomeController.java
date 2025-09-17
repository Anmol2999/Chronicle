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

    //Spring automatically provides this Model object. Think of it as a temporary storage box that you use to pass data from your Java code to your HTML template.

    @GetMapping("/")
    public String home(Model model) {
        List<Post> posts = postService.getAll();   
         model.addAttribute("posts", posts);
        return "home";
        // This is not the text that gets displayed. Instead, it's the logical name of the view (the template) that Spring should render
    }

    
}
