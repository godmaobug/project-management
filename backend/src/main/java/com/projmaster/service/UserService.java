package com.projmaster.service;

import com.projmaster.dto.request.RegisterRequest;
import com.projmaster.dto.response.UserResponse;
import com.projmaster.entity.User;

import java.util.List;

public interface UserService {

    UserResponse getCurrentUser();

    UserResponse getUserById(Long id);

    List<UserResponse> getAllUsers();

    UserResponse createUser(RegisterRequest request);

    UserResponse updateUser(Long id, RegisterRequest request);

    void deleteUser(Long id);

    User getCurrentUserEntity();
}
