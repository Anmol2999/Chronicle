package com.example.Chronicle.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.Chronicle.Models.Account;
import com.example.Chronicle.Service.AccountService;

import jakarta.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;

    //This method handles GET requests to the /register URL. When a user clicks a link to the registration page, this code runs.
    @GetMapping("/register")
    public String register(Model model) {
        Account account = new Account();
        model.addAttribute("account", account);
        //This tells Spring to render the register.html view template and show it to the user.
        return "account_views/register";
    }

    //BindingResult is used to hold the result of a validation and binding and contains errors that may have occurred.
    //The @Valid annotation is used to indicate that the Account object should be validated before the method is invoked.
    @PostMapping("/register")
    public String register_user(@Valid @ModelAttribute Account account, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "account_views/register";
        } else {
            accountService.saveAccount(account);
            return "redirect:/";
        }
    }

    @GetMapping("/login")
    public String login() {
        return "account_views/login";
    }

    @GetMapping("/profile")
    public String profile() {
        return "account_views/profile";
    }

   
}
