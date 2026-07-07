package com.eddiecwh.NotificationSystem.service;

import com.eddiecwh.NotificationSystem.entity.User;
import com.eddiecwh.NotificationSystem.model.UserRequestBody;
import com.eddiecwh.NotificationSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User createUser(UserRequestBody request) {
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setCreatedDt(LocalDateTime.now());
        user.setUpdatedDt(null);

        return userRepository.save(user);
    }

    public User updateUserById(Long userId, UserRequestBody request) {
        User user = getUserById(userId);

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setUpdatedDt(LocalDateTime.now());

        return userRepository.save(user);
    }
}
