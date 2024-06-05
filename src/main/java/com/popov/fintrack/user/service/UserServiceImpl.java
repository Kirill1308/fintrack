package com.popov.fintrack.user.service;

import com.popov.fintrack.email.MailService;
import com.popov.fintrack.email.props.MailProperties;
import com.popov.fintrack.email.utils.MailType;
import com.popov.fintrack.exception.ResourceNotFoundException;
import com.popov.fintrack.user.ImageService;
import com.popov.fintrack.user.UserRepository;
import com.popov.fintrack.user.UserService;
import com.popov.fintrack.user.model.ProfileImage;
import com.popov.fintrack.user.model.Role;
import com.popov.fintrack.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Properties;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;
    private final MailService mailService;
    private final MailProperties mailProperties;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "UserService::getByUsername", key = "#username")
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found."));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "UserService::getUserById", key = "#id")
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }

    @Override
    @Transactional
    @Caching(cacheable = {
            @Cacheable(
                    value = "UserService::getUserById",
                    condition = "#user.id!=null",
                    key = "#user.id"
            ),
            @Cacheable(
                    value = "UserService::getByUsername",
                    condition = "#user.username!=null",
                    key = "#user.username"
            )
    })
    public User create(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalStateException("User already exists.");
        }
        if (!user.getPassword().equals(user.getPasswordConfirm())) {
            throw new IllegalStateException(
                    "Password and password confirmation do not match."
            );
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> roles = Set.of(Role.ROLE_USER);
        user.setRoles(roles);
        userRepository.save(user);
        mailService.sendEmail(user, MailType.REGISTRATION, new Properties());
        return user;
    }

    @Override
    @Transactional
    @Caching(put = {
            @CachePut(value = "UserService::getUserById", key = "#user.id"),
            @CachePut(value = "UserService::getByUsername", key = "#user.username")
    })
    public User update(User user) {
        User existing = getUserById(user.getId());
        existing.setName(user.getName());
        user.setUsername(user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    @Override
    @Transactional
    @CacheEvict(value = "UserService::getUserById", key = "#userId")
    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    @Transactional
    @CacheEvict(value = "UserService::getUserById", key = "#userId")
    public void uploadProfileImage(Long userId, ProfileImage profileImage) {
        User user = getUserById(userId);
        String fileName = imageService.upload(profileImage);
        user.setFile(fileName);
        userRepository.save(user);
    }

    @Override
    @Transactional
    @CacheEvict(value = "UserService::getUserById", key = "#userId")
    public void deleteProfileImage(Long userId) {
        User user = getUserById(userId);
        if (user.getFile() != null) {
            imageService.delete(user.getFile());
            user.setFile(null);
            userRepository.save(user);
        }
    }
}
