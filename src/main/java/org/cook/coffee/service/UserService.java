package org.cook.coffee.service;

import org.cook.coffee.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface UserService {

    User getUserById(UUID uuid);
    List<User> getAllUsers();
    User createUser(User toCreate);
    User updateUser(UUID uuid, User toUpdate);
    void deleteUser(UUID uuid);

}
