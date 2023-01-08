package com.jnunes.user.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserService implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String name = authentication.getName();
        final String password = String.valueOf(authentication.getCredentials());
        if (StringUtils.equals("admin", password) && StringUtils.equals("admin", password)) {
            final List<GrantedAuthority> grantedAuth = new ArrayList<>();
            grantedAuth.add(new SimpleGrantedAuthority("ROLE_USER"));
            final UserDetails userDetails = new User(name, password, grantedAuth);
            return new UsernamePasswordAuthenticationToken(userDetails, password, grantedAuth);
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
