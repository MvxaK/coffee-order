package org.cook.coffee.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.cook.coffee.entity.UserEntity;
import org.cook.coffee.mapper.UserMapper;
import org.cook.coffee.model.User;
import org.cook.coffee.repository.UserRepository;
import org.cook.coffee.service.interfaces.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public User getById(UUID id) {
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no user with id -> " + id));

        return userMapper.toModel(entity);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toModel)
                .toList();
    }

    @Override
    @Transactional
    public User create(User user) {
        UserEntity entity = userMapper.toEntity(user);

        return userMapper.toModel(userRepository.save(entity));
    }

    @Override
    @Transactional
    public User update(UUID id, User user) {
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no user with id -> " + id));

        entity.setEmail(user.getEmail());
        entity.setRole(entity.getRole());

        return userMapper.toModel(userRepository.save(entity));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("There is no user with id -> " + id);
        }

        userRepository.deleteById(id);
    }

}