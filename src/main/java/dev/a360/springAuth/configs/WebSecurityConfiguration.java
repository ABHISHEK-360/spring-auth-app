package dev.a360.springAuth.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.a360.springAuth.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    JWTUtil jwtUtil;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
            .antMatchers("/", "/users/login", "/users/register").permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilterAfter(new JWTAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.exceptionHandling().authenticationEntryPoint((request, res, e) -> {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> response = new HashMap<>();
            response.put("timestamp", LocalDateTime.now().toString());
            response.put("message", "Invalid/Missing/Expired AuthToken");
            res.setContentType("application/json;charset=UTF-8");
            res.setStatus(403);
            res.getWriter().write(mapper.writeValueAsString(response));;
        });
    }
}
