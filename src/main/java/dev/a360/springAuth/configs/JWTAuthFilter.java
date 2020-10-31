package dev.a360.springAuth.configs;

import dev.a360.springAuth.utils.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {
    private final String HEADER_STRING = "UserToken";

    JWTUtil jwtUtil;

    public JWTAuthFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    private Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);

        try {
            if (token != null && jwtUtil.validateToken(token)) {
                return new UsernamePasswordAuthenticationToken(
                        jwtUtil.getUserFromToken(token), null, Collections.emptyList());
            }
        } catch (ExpiredJwtException exception) {
            System.out.println("Request to parse expired JWT: Failed: "+exception.getMessage());
        } catch (UnsupportedJwtException exception) {
            System.out.println("Request to parse unsupported JWT: Failed: "+exception.getMessage());
        } catch (MalformedJwtException exception) {
            System.out.println("Request to parse invalid JWT: Failed: "+exception.getMessage());
        } catch (SignatureException exception) {
            System.out.println("Request to parse JWT with invalid signature: Failed: "+exception.getMessage());
        } catch (IllegalArgumentException exception) {
            System.out.println("Request to parse empty or null JWT: Failed: "+exception.getMessage());
        }

        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = getAuthentication(request);
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        if (authentication == null) {
            filterChain.doFilter(request, response);
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request,response);
    }
}