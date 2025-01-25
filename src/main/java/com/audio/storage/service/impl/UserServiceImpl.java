package com.audio.storage.service.impl;


import com.audio.storage.dto.UserRequestDto;
import com.audio.storage.entity.User;
import com.audio.storage.repository.UserRepository;
import com.audio.storage.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String create(UserRequestDto requestDto) {
        User user = new User(requestDto.getName(), requestDto.getEmail());
        userRepository.save(user);
        return "User created successfully";
    }
}
