package com.fusionorder.controller;

import com.fusionorder.dto.AuthRequest;
import com.fusionorder.dto.AuthResponse;
import com.fusionorder.dto.UserDTO;
import com.fusionorder.entity.User;
import com.fusionorder.service.AuthService;
import com.fusionorder.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody User user) {
        UserDTO userDTO = userService.register(user);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}

