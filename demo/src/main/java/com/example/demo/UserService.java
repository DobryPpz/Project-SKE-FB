package com.example.demo;

import java.io.UnsupportedEncodingException;

public interface UserService {

    void saveUser(UserDto userDto);
    User findUserByEmail(String email);
    void register(User user, String siteURL) throws UnsupportedEncodingException;
}
