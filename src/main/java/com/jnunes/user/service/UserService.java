package com.jnunes.user.service;

import com.jnunes.user.model.User;
import lombok.NonNull;

public interface UserService {

    User findByEmail(String email);

    void registerNewUser(@NonNull User user);
    void saveUser(@NonNull User user);
}
