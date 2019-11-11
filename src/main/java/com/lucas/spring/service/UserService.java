package com.lucas.spring.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.lucas.spring.shared.dto.UserDto;

public interface UserService extends UserDetailsService {

	UserDto createUser(UserDto user) throws Exception;

	UserDto getUser(String email);

	UserDto getUserByUserId(String userId);

	UserDto updateUser(String userId, UserDto user);

	void deleteUser(String userId);

	List<UserDto> getAllUsers(int page, int limit);

	boolean verifyEmailToken(String token);

	boolean requestPasswordReset(String email);

	boolean resetPassword(String token, String password);
}
