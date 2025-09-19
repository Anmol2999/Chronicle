package com.example.Chronicle.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.Chronicle.Models.Account;
import com.example.Chronicle.Models.Authority;
import com.example.Chronicle.repository.AccountRepository;
import com.example.Chronicle.util.constants.Roles;

@Service
public class AccountService implements UserDetailsService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Account saveAccount(Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        if (account.getRole() == null) {
            account.setRole(Roles.USER.getRole());
        }
        return accountRepository.save(account);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Account> account = accountRepository.findOneByEmailIgnoreCase(email);
        if (!account.isPresent()) {
            throw new UsernameNotFoundException("User with email " + email + " not found");
        } else {
            Account user = account.get();

            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(user.getRole()));


            for(Authority auth: user.getAuthorities()){
                authorities.add(new SimpleGrantedAuthority(auth.getName()));
            }

            return new User(user.getEmail(), user.getPassword(), authorities);
        }
    }

    public Optional<Account> findOnebyEmail(String email) {
        return accountRepository.findOneByEmailIgnoreCase(email);
    }
}
