package com.adminuser.service.imp;



import com.adminuser.entity.User;
import com.adminuser.repository.UserRepository;
import com.adminuser.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User addUserDetails(User user) {
        return userRepository.save(user);
    }

    @Override
    public ArrayList<User> getAllUserDetails() {
        return (ArrayList<User>) userRepository.findAll();
    }

    @Override
    public User getUserDetailsById(String userId) {
        Optional<User> user = userRepository.findById(Integer.parseInt(userId));
        return user.orElse(null);
    }

    @Override
    public User deleteUserDetailsById(String userId) {
        Optional<User> user = userRepository.findById(Integer.parseInt(userId));
        if (user.isPresent()) {
            userRepository.delete(user.get());
            return user.get();
        }
        return null;
    }

    @Override
    public User updateUserDetails(User user) {
        Optional<User> existingUser = userRepository.findById(user.getId());
        if (existingUser.isPresent()) {
            return userRepository.save(user);
        }
        return null;
    }
}
