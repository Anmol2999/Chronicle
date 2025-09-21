package com.example.Chronicle.Controller;



import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.Chronicle.Models.Account;
import com.example.Chronicle.Models.Post;
import com.example.Chronicle.Service.AccountService;
import com.example.Chronicle.Service.PostService;

import jakarta.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;







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

    @PostMapping("/posts/add")
    @PreAuthorize("isAuthenticated()")
    public String addPostHandler(@Valid @ModelAttribute Post post,BindingResult bindingResult,Principal principal){
        String authUser="email";
        if (bindingResult.hasErrors()) {
            return "post_views/add_post";
        }
        if (principal!=null) {
            authUser=principal.getName();
        }
        if (post.getAuthor().getEmail().equals(authUser)){

            postService.save(post);
        return "redirect:/posts/"+post.getId();
            
        }
       return "redirect:/";
    }

    @GetMapping("/posts/{id}/edit")
    @PreAuthorize("isAuthenticated()")
    public String geteditPost(@PathVariable Long id,Model model){


         
       Optional<Post> optionalPost=postService.getById(id);
       if (optionalPost.isPresent()) {
         Post post=optionalPost.get();
         model.addAttribute("post", post);
         return "post_views/edit_post";
       }else{
        return "error/404";
       }
       
    }

    @PostMapping("/posts/{id}/edit")
    @PreAuthorize("isAuthenticated()")
    public String UpdatePost(@PathVariable Long id,@Valid @ModelAttribute Post post, BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            return "post_views/edit_post";
        }
       Optional<Post> optionalPost=postService.getById(id);
       if (optionalPost.isPresent()) {
         Post existingPost=optionalPost.get();

        existingPost.setTitle(post.getTitle());
        existingPost.setContent(post.getContent());
        postService.save(existingPost);
        return "redirect:/posts/"+existingPost.getId();
       }

       return "error/404";
    }

    @GetMapping("/posts/{id}/delete")
    @PreAuthorize("isAuthenticated()")
    public String deletePost(@PathVariable Long id){
       Optional<Post> optionalPost=postService.getById(id);
        if (optionalPost.isPresent()) {
            Post post=optionalPost.get();
            postService.delete(post);
            return "redirect:/";
        }
       return "error/404";
    }
}
