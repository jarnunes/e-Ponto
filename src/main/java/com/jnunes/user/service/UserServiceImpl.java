package com.jnunes.user.service;

import com.jnunes.springjsf.support.utils.JSFUtils;
import com.jnunes.user.model.User;
import com.jnunes.user.repository.UserRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    ApplicationEventPublisher eventPublisher;


    @Override
    public User findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public void registerNewUser(@NonNull User user) {
        if (findByEmail(user.getEmail()) != null) {
            JSFUtils.addErrorMessage("user.register.email.already.exists");
        } else {
            user.setPassword(getEncoder().encode(user.getPassword()));
            saveUser(user);
            JSFUtils.addInfoMessage("user.register.success");
        }
    }

    @Override
    public void saveUser(@NonNull User user) {
        repository.save(user);
    }

    private PasswordEncoder getEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


}
