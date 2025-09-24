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
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig {

        private static final String[] WHITELIST = {
                        "/",
                        "/login",
                        "/register",
                        "/h2-console/**",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/fonts/**",
                        "/posts/**",
                        "images/**", "/uploads/**",
                        "/forgot-password",
                        "/reset-password",
                        "/change-password"
        };

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

            http
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/test").permitAll()
                            .requestMatchers(WHITELIST).permitAll()
                            .requestMatchers("/profile/**").authenticated()
                            .requestMatchers("/admin/**").hasRole("ADMIN")
                            .requestMatchers("/editor/**").hasAnyRole("EDITOR", "ADMIN")
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
                            .logoutSuccessUrl("/"))
                    // 4. Remember Me Configuration
                    .rememberMe(me -> me.rememberMeParameter("remember-me"))
                    // 5. CSRF and Headers for H2 Console
                    .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
                    .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));

                return http.build();
        }
}