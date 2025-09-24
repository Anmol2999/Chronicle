package com.example.Chronicle.Controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Chronicle.Models.Account;
import com.example.Chronicle.Service.AccountService;
import com.example.Chronicle.Service.EmailService;
import com.example.Chronicle.util.AppUtil;
import com.example.Chronicle.util.email.emailDetails;

import jakarta.validation.Valid;

@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private EmailService emailService;

    // This method handles GET requests to the /register URL. When a user clicks a
    // link to the registration page, this code runs.
    @GetMapping("/register")
    public String register(Model model) {
        Account account = new Account();
        model.addAttribute("account", account);
        // This tells Spring to render the register.html view template and show it to
        // the user.
        return "account_views/register";
    }

    // BindingResult is used to hold the result of a validation and binding and
    // contains errors that may have occurred.
    // The @Valid annotation is used to indicate that the Account object should be
    // validated before the method is invoked.
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
    @PreAuthorize("isAuthenticated()")
    public String profile(Model model, Principal principal) {
        String authUser = "email";
        if (principal != null) {
            authUser = principal.getName();
        }
        Optional<Account> accountOptional = accountService.findOnebyEmail(authUser);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            model.addAttribute("account", account);
            model.addAttribute("photo", account.getPhoto());
            return "account_views/profile";
        } else {
            return "redirect:/?error ";
        }
    }

    @PostMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String updateProfile(@Valid @ModelAttribute Account account, BindingResult bindingResult,
            Principal principal) {
        if (bindingResult.hasErrors()) {
            return "account_views/profile";
        }
        String authUser = "email";
        if (principal != null) {
            authUser = principal.getName();
        }
        Optional<Account> optionalAccount = accountService.findOnebyEmail(authUser);

        if (optionalAccount.isPresent()) {
            Account account_by_id = accountService.findOnebyId(account.getId()).get();

            account_by_id.setEmail(account.getEmail());
            account_by_id.setPassword(account.getPassword());
            account_by_id.setUsername(account.getUsername());
            account_by_id.setAge(account.getAge());
            account_by_id.setDateofBirth(account.getDateofBirth());
            account_by_id.setGender(account.getGender());

            accountService.saveAccount(account_by_id);
            SecurityContextHolder.clearContext();
            return "redirect:/login";
        } else {
            return "redirect:/?error ";
        }

    }

     @PostMapping("/update_photo")
    @PreAuthorize("isAuthenticated()")
    public String updatePhoto(@RequestParam("file") MultipartFile file,
                              RedirectAttributes redirectAttributes,
                              Principal principal) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Please select a file to upload.");
            return "redirect:/profile";
        }

        try {
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String randomPrefix = RandomStringUtils.randomAlphanumeric(10);
            String finalFilename = randomPrefix + "_" + originalFilename;

            // Get the external directory path from AppUtil
            Path uploadDir = Paths.get(AppUtil.getUploadDir());
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir); // Ensure the directory exists
            }
            
            // Resolve the final filename against the directory and save the file
            Path filePath = uploadDir.resolve(finalFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            Optional<Account> optionalAccount = accountService.findOnebyEmail(principal.getName());
            if (optionalAccount.isPresent()) {
                Account accountToUpdate = optionalAccount.get();
                // Create the web-accessible URL path
                String webPath = "/uploads/" + finalFilename;
                // Save this URL path to the database
                accountToUpdate.setPhoto(webPath);
                accountService.saveAccount(accountToUpdate);
                redirectAttributes.addFlashAttribute("message", "Photo updated successfully!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Could not upload the file.");
        }
        return "redirect:/profile";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "account_views/forgot-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("email") String _email,RedirectAttributes redirectAttributes,Model model) {
        Optional<Account> optionalAccount = accountService.findOnebyEmail(_email);
        if (optionalAccount.isPresent()) {
            Account account = accountService.findOnebyId(optionalAccount.get().getId()).get();
            String reset_token=UUID.randomUUID().toString();
            account.setPasswordResetToken(reset_token);
            account.setPasswordResetTokenExpiry(LocalDateTime.now().plusMinutes(600));
            accountService.saveAccount(account);
            String resetLink = "This is the reset password link : http://localhost:8080/change-password?token=" + reset_token;
            emailDetails details=new emailDetails(account.getEmail(), "Password Reset Request", resetLink);
            if (emailService.sendEmail(details)==false) {
                redirectAttributes.addFlashAttribute("error", "Error while sending email.");
        return "redirect:/forgot-password";
            }
            
            redirectAttributes.addFlashAttribute("message", "Password reset link has been sent to your email (simulated).");
            return "redirect:/login";
        }
        redirectAttributes.addFlashAttribute("error", "Email not found.");
        return "redirect:/forgot-password";
    }

    @GetMapping("/change-password")
    public String changePassword(Model model,@RequestParam("token") String token,RedirectAttributes redirectAttributes) {
        Optional<Account> optionalAccount = accountService.findByToken(token);
        if (optionalAccount.isPresent()) {
           Account account=accountService.findOnebyId(optionalAccount.get().getId()).get();
           LocalDateTime now=LocalDateTime.now();
           if (now.isAfter(optionalAccount.get().getPasswordResetTokenExpiry())) {
               model.addAttribute("error", "Password reset token has expired.");
               return "redirect:/forgot-password";
           }
           model.addAttribute("account", account);
           return "account_views/change-password";
        }
        redirectAttributes.addFlashAttribute("error", "Invalid password reset token.");
        return "redirect:/forgot-password";
    }
    @PostMapping("/change-password")
    public String post_changePassword(@ModelAttribute Account account, RedirectAttributes redirectAttributes) {
        Account account_by_id=accountService.findOnebyId(account.getId()).get();
        account_by_id.setPassword(account.getPassword());
        account_by_id.setPasswordResetToken(null);
        accountService.saveAccount(account_by_id);


        redirectAttributes.addFlashAttribute("message", "Password changed successfully. Please log in with your new password.");
        return "redirect:/login";
    }
}
