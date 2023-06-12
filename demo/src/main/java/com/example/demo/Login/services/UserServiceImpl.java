package com.example.demo.Login.services;

import com.example.demo.Login.dto.UserDto;
import com.example.demo.Login.models.Role;
import com.example.demo.Login.models.RoleConstants;
import com.example.demo.Login.models.User;
import com.example.demo.Login.repository.RoleRepository;
import com.example.demo.Login.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public void register(User user, String siteURL)
            throws UnsupportedEncodingException {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setEnabled(true);
        userRepository.save(user);

    }


    @Override
    public void saveUser(UserDto userDto) {
        Role role = roleRepository.findByName(RoleConstants.Roles.USER);

        if (role == null)
            role = roleRepository.save(new Role(RoleConstants.Roles.USER));

        User user = new User(userDto.getUsername(), userDto.getEmail(), passwordEncoder.encode(userDto.getPassword()),
                Arrays.asList(role));
        userRepository.save(user);


    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
