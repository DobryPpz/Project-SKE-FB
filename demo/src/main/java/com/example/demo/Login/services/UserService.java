package com.example.demo.Login.services;

import com.example.demo.Login.models.User;
import com.example.demo.Login.dto.UserDto;

import java.io.UnsupportedEncodingException;

public interface UserService {

    void saveUser(UserDto userDto);
    User findUserByEmail(String email);
    void register(User user, String siteURL) throws UnsupportedEncodingException;
}
