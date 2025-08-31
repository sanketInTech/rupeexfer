package com.sanket.rupeexfer.service;

import com.sanket.rupeexfer.model.User;

public interface UserService {
    User getCurrentUser();
    User getUserByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
