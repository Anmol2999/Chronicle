package com.example.Chronicle.Controller;



import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.Chronicle.Models.Account;
import com.example.Chronicle.Models.Post;
import com.example.Chronicle.Service.AccountService;
import com.example.Chronicle.Service.PostService;

import org.springframework.ui.Model;



@Controller
public class PostController {
    
    @Autowired
    private PostService postService;

    @Autowired
    private AccountService accountService;

   @GetMapping("/posts/{id}")
   public String getPost(@PathVariable Long id,Model model, Principal principal){ {
      Optional<Post> optionalPost=postService.getById(id);
      String authUser="email";


      if (optionalPost.isEmpty()) {
        return "error/404"; 
        
      }else{
        Post post= optionalPost.get();
      model.addAttribute("post", post);

      //get authenticated user who is logged in
      //String authUsername=SecurityContextHolder.getContext().getAuthentication().getName();
       
       if (principal!=null) {
        authUser=principal.getName();
        
       }
       if (authUser.equals(post.getAuthor().getEmail())) {
        model.addAttribute("isOwner", true);
        
       }else{
        model.addAttribute("isOwner", false);
       }
   }
       return "post_views/post";
      
}

}

    @GetMapping("/add_post")
    public String addPost(Model model,Principal principal){
        String authUser="email";
        if (principal!=null) {
            authUser=principal.getName();
        }
        Optional<Account> optionalAccount=accountService.findOnebyEmail(authUser);

        if (optionalAccount.isPresent()) {
           Post post=new Post();
           post.setAuthor(
            optionalAccount.get() 
           );
           model.addAttribute("post", post);
        return "post_views/add_post";
            
        }else{
            return "redirect:/";
        }
    }

}
