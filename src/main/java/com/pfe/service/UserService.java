package com.pfe.service;

import com.pfe.entity.Role;
import com.pfe.entity.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);

    User updateUser(User user);

    Role saveRole(Role role);
    void addRoleToUser(String email, String roleName);
    User getUser(String email);
    void addUserToCustomer(String email);
    List<User> getUsers();
    String signUpUser(User user);

    String emailVerification(String email);

    void enableAppUser(String email);

}
