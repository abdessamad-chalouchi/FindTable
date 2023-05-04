package com.pfe.service.impl;

import com.pfe.entity.ConfirmationToken;
import com.pfe.entity.Customer;
import com.pfe.entity.Role;
import com.pfe.entity.User;
import com.pfe.repository.*;
import com.pfe.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    private final ConfirmationTokenService confirmationTokenService;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final ManagerRepository managerRepository;
    private final ReviewRepository reviewRepository;

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            log.error("User not found");

            throw new IllegalStateException("User not found");
        } else {
            log.info("User found {}", email);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

    @Override
    public User saveUser(User user) {
        log.info("Saving new user {} to the database", user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    @Override
    public User updateUser(User user) {
        log.info("Saving new user {} to the database", user.getEmail());
        return userRepository.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving new role {} to the database", role.getName());
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String email, String roleName) {
        log.info("Adding role {} to user {}.", roleName, email);
        User user = userRepository.findByEmail(email);
        Role role = roleRepository.findByName(roleName);
        user.getRoles().add(role);
    }
    @Override
    public void addUserToCustomer(String email) {
//        log.info("Adding role {} to user {}.", roleName, email);
        User user = userRepository.findByEmail(email);
        customerRepository.save(new Customer(user));

    }

    @Override
    public User getUser(String email) {
        log.info("Fetching user {}.", email);
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getUsers() {
        log.info("Fetching all users.");
        return userRepository.findAll();
    }

    @Override
    public String signUpUser(User user) {
        if(userRepository.findByEmail(user.getEmail()) != null){
            log.error("Email already taken");
            throw new IllegalStateException("Email already taken");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        String registrationToken = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                registrationToken,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return registrationToken;
    }
    @Override
    public String emailVerification(String email) {
        String registrationToken = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = confirmationTokenService.getVerificationToken(email);
        confirmationToken.setToken(registrationToken);
        confirmationToken.setCreatedAt(LocalDateTime.now());
        confirmationToken.setExpiresAt(LocalDateTime.now().plusMinutes(15));
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return registrationToken;
    }


    @Override
    public void enableAppUser(String email) {
        userRepository.enableUser(email);
    }


}
