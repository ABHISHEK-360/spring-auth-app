package dev.a360.springAuth.services;

import dev.a360.springAuth.dtos.UserDTO;
import dev.a360.springAuth.exceptions.InvalidCredentialException;
import dev.a360.springAuth.models.User;
import dev.a360.springAuth.repositories.UserRepository;
import dev.a360.springAuth.utils.JWTUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
class AuthServiceImpl implements AuthService {
    @Autowired
    UserRepository userRepository;
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    JWTUtil jwtUtil;

    @Override
    public long createUser(String username, String password) {
        User user = new User();
        String encodedPass = passwordEncoder.encode(password);

        user.setUsername(username.toLowerCase());
        user.setPassword(encodedPass);
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        
        return userRepository.save(user).getUserId();
    }

    @Override
    public Map<String, Object> login(String username, String password) {
        Map<String, Object> res = new HashMap<>();
        List<User> users = userRepository.findByUsername(username.toLowerCase());

        if(users.size()>0){
            User user = users.get(0);

            if(passwordEncoder.matches(password, user.getPassword())){
                user.setLastLogin(new Timestamp(System.currentTimeMillis()));
                userRepository.save(user);
                res.put("jwt", jwtUtil.generateToken(""+user.getUserId()));
                return res;
            }
            else {
                throw new InvalidCredentialException("Invalid Password");
            }
        }
        else {
            throw new InvalidCredentialException("Invalid Username");
        }
    }

    @Override
    public UserDTO details(long id) {
        UserDTO res = new UserDTO();
        Optional<User> op = userRepository.findById(id);
        BeanUtils.copyProperties(op.orElseThrow(), res, "password");
        return res;
    }

    @Override
    public boolean changePassword(long id, String oldPassword, String newPassword) {
        Optional<User> op = userRepository.findById(id);
        User user = op.orElseThrow();

        if(passwordEncoder.matches(oldPassword, user.getPassword())){
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return true;
        }
        else {
            throw new InvalidCredentialException("Incorrect Password");
        }
    }
}
