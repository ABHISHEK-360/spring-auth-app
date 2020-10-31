package dev.a360.springAuth.services;

import dev.a360.springAuth.dtos.UserDTO;

import java.util.Map;

public interface AuthService {
    long createUser(String username, String password);
    Map<String, Object> login(String username, String password);
    UserDTO details(long id);
    boolean changePassword(long id, String oldPassword, String newPassword);
}
