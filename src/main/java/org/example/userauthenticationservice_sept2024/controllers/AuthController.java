package org.example.userauthenticationservice_sept2024.controllers;

import jakarta.validation.Valid;
import org.example.userauthenticationservice_sept2024.dtos.*;
import org.example.userauthenticationservice_sept2024.exceptions.UserAlreadyExistsException;
import org.example.userauthenticationservice_sept2024.exceptions.UserNotFoundException;
import org.example.userauthenticationservice_sept2024.exceptions.WrongPasswordException;
import org.example.userauthenticationservice_sept2024.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/sign_up")
    public ResponseEntity<SignUpResponseDto> signUp(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        SignUpResponseDto responseDto = new SignUpResponseDto();
        boolean response = this.authService.signUp(signUpRequestDto.getEmail(), signUpRequestDto.getPassword());
        if(response) {
            responseDto.setRequestStatus(RequestStatus.SUCCESS);
            responseDto.setMessage("Sign Up Successful");
        }
        else {
            responseDto.setRequestStatus(RequestStatus.FAILURE);
            responseDto.setMessage("Sign Up Failed");
        }
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/login_user")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto responseDto = new LoginResponseDto();
        String response = this.authService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());
        if(response != null){
            responseDto.setRequestStatus(RequestStatus.SUCCESS);
            responseDto.setMessage("Login successful");
            responseDto.setToken(response);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } else throw new RuntimeException("Invalid username or password");
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<SignUpResponseDto> handleUserAlreadyExistsException(UserAlreadyExistsException exception) {
        SignUpResponseDto responseDto = new SignUpResponseDto();
        responseDto.setRequestStatus(RequestStatus.FAILURE);
        responseDto.setMessage(exception.getMessage());
        return new ResponseEntity<>(responseDto, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({UserNotFoundException.class, WrongPasswordException.class, RuntimeException.class})
    public ResponseEntity<LoginResponseDto> handleUserNotFoundException(LoginResponseDto exception) {
        LoginResponseDto responseDto = new LoginResponseDto();
        responseDto.setRequestStatus(RequestStatus.FAILURE);
        responseDto.setMessage(exception.getMessage());
        return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
    }
}
