package org.cook.coffee.service.interfaces;

import org.cook.coffee.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface UserService {

    User getById(UUID uuid);
    List<User> getAll();
    User create(User toCreate);
    User update(UUID uuid, User toUpdate);
    void delete(UUID uuid);

}
