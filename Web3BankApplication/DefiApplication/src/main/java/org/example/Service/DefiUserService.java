package org.example.Service;

import org.example.Entity.Response;
import org.example.dto.DefiUserRequest;
import org.example.dto.UserRequest;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface DefiUserService {
    Response registerUser(DefiUserRequest userRequest);
    Response updateUser(DefiUserRequest userRequest);
    List<Response> allUsers();
    Response fetchUser(Long id);
}
