package com.jnunes.user.service;

import com.jnunes.user.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AuthenticationService implements AuthenticationProvider {
    @Autowired
    private UserService service;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (StringUtils.isAnyEmpty(authentication.getName(), String.valueOf(authentication.getCredentials())))
            throw new BadCredentialsException("invalid login details");
        User user = service.findByEmail(authentication.getName());

        if (successAuthentitcate(user, authentication)) {
            return new UsernamePasswordAuthenticationToken(user, authentication.getCredentials(), user.getAuthorities());
        } else {
            return null;
        }
    }

    private boolean successAuthentitcate(User user, final Authentication authentication) {
        final String password = String.valueOf(authentication.getCredentials());
        return Objects.nonNull(user)
                && PasswordEncoderFactories.createDelegatingPasswordEncoder()
                .matches(password, user.getPassword());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
