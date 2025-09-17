package com.example.Chronicle.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true) // <-- This is the main fix
public class WebSecurityConfig {

        private static final String[] WHITELIST = {
                        "/",
                        "/login",
                        "/register",
                        "/h2-console/**",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/fonts/**"
        };

        @Bean
        public PasswordEncoder passwordEncoder() { // Removed 'static'
                return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                // 1. Authorize Requests
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(WHITELIST).permitAll()
                                                .anyRequest().authenticated())
                                // 2. Form Login Configuration
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .loginProcessingUrl("/login")
                                                .usernameParameter("email")
                                                .passwordParameter("password")
                                                .defaultSuccessUrl("/", true)
                                                .failureUrl("/login?error")
                                                .permitAll())
                                // 3. Logout Configuration
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/logout?success"))
                                // 4. CSRF and Headers for H2 Console
                                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
                                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));

                return http.build();
        }
}