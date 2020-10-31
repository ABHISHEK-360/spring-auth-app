package dev.a360.springAuth.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.a360.springAuth.enums.UserTypes;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    @NotBlank(message = "Username cannot be null")
    @Size(min = 4, max = 10, message = "Username must be b/w 4 to 10 in length")
    @Pattern(regexp="^[a-zA-Z0-9]*$", message="Username must contain numbers and alphabets only")
    private String username;
    @NotBlank(message = "Password cannot be null")
    @Size(min = 8, max = 16, message = "Password must be b/w 8 to 16 in length")
    private String password;
    private String phone;
    private String email;
    private UserTypes userType = UserTypes.CONSUMER;
    private Timestamp lastLogin;

    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserTypes getUserType() {
        return userType;
    }

    public void setUserType(UserTypes userType) {
        this.userType = userType;
    }
}
