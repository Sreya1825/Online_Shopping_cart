package com.adminuser.service;

import java.util.ArrayList;

import com.adminuser.entity.User;

public interface UserService {
    User addUserDetails(User user);
    ArrayList<User> getAllUserDetails();
    User getUserDetailsById(String userId);
    User deleteUserDetailsById(String userId);
    User updateUserDetails(User user);
}
