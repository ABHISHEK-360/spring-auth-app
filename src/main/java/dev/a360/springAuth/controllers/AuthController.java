package dev.a360.springAuth.controllers;

import dev.a360.springAuth.dtos.UserDTO;
import dev.a360.springAuth.exceptions.InvalidCredentialException;
import dev.a360.springAuth.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class AuthController {
    @Autowired
    AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody UserDTO body){
        String username = body.getUsername();
        String password = body.getPassword();

        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("userId", authService.createUser(username, password));

        return ResponseEntity.ok(res);
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> body){
        String username = body.get("username");
        String password = body.get("password");

        if(username==null||username.length()<4||username.length()>10)
            throw new InvalidCredentialException("Invalid Username");

        if(password==null||password.length()<8||password.length()>16)
            throw new InvalidCredentialException("Invalid Password");

        return authService.login(username, password);
    }

    @GetMapping("/details")
    public UserDTO details(){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return authService.details(Integer.parseInt(userId));
    }

    @PostMapping("/change-password")
    public Map<String, Object> changePassword(@RequestBody Map<String, String> body){
        String oldPassword = body.get("oldPassword");
        String newPassword = body.get("newPassword");
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        Map<String, Object> res = new HashMap<>();
        res.put("success", authService.changePassword(Integer.parseInt(userId), oldPassword, newPassword));
        res.put("message", "Password Changed!!!");

        return res;
    }
}
