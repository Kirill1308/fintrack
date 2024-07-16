package com.popov.fintrack.user.service;

import com.popov.fintrack.email.MailService;
import com.popov.fintrack.email.MailType;
import com.popov.fintrack.exception.ResourceNotFoundException;
import com.popov.fintrack.user.ImageService;
import com.popov.fintrack.user.UserRepository;
import com.popov.fintrack.user.model.ProfileImage;
import com.popov.fintrack.user.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Properties;

import static com.popov.fintrack.user.UserTestData.user;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ImageService imageService;

    @Mock
    private MailService mailService;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getByUsername_success() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        User foundUser = userService.getByUsername(user.getUsername());

        assertNotNull(foundUser);
        assertEquals(user.getUsername(), foundUser.getUsername());
        verify(userRepository, times(1)).findByUsername(user.getUsername());
    }

    @Test
    void getByUsername_notFound() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        Executable executable = () -> userService.getByUsername(user.getUsername());

        assertThrows(ResourceNotFoundException.class, executable);
        verify(userRepository, times(1)).findByUsername(user.getUsername());
    }

    @Test
    void getUserById_success() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        User foundUser = userService.getUserById(user.getId());

        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    void getUserById_notFound() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        Executable executable = () -> userService.getUserById(user.getId());

        assertThrows(ResourceNotFoundException.class, executable);
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    void createUser_success() {
        user.setPassword("password123");
        user.setPasswordConfirm("password123");

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals(user.getUsername(), createdUser.getUsername());
        verify(userRepository, times(1)).findByUsername(user.getUsername());
        verify(passwordEncoder, times(1)).encode("password123");
        verify(userRepository, times(1)).save(any(User.class));
        verify(mailService, times(1)).sendEmail(any(User.class), eq(MailType.REGISTRATION), any(Properties.class));
    }

    @Test
    void createUser_userAlreadyExists() {
        user.setPassword("password123");
        user.setPasswordConfirm("password123");

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        assertThrows(IllegalStateException.class, () -> userService.createUser(user));
        verify(userRepository, times(1)).findByUsername(user.getUsername());
        verify(userRepository, never()).save(any(User.class));
        verify(mailService, never()).sendEmail(any(User.class), eq(MailType.REGISTRATION), any(Properties.class));
    }

    @Test
    void createUser_passwordMismatch() {
        user.setPassword("password123");
        user.setPasswordConfirm("wrongPassword");

        assertThrows(IllegalStateException.class, () -> userService.createUser(user));
        verify(userRepository, never()).findByUsername(user.getUsername());
        verify(userRepository, never()).save(any(User.class));
        verify(mailService, never()).sendEmail(any(User.class), eq(MailType.REGISTRATION), any(Properties.class));
    }

    @Test
    void updateUser_success() {
        user.setPassword("password123");
        user.setPasswordConfirm("password123");

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User updatedUser = userService.updateUser(user);

        assertNotNull(updatedUser);
        assertEquals(user.getId(), updatedUser.getId());
        verify(userRepository, times(1)).findById(user.getId());
        verify(passwordEncoder, times(1)).encode("password123");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void deleteUserById_success() {
        doNothing().when(userRepository).deleteById(user.getId());

        userService.deleteUserById(user.getId());

        verify(userRepository, times(1)).deleteById(user.getId());
    }

    @Test
    void uploadProfileImage_success() {
        ProfileImage profileImage = new ProfileImage();
        String fileName = "profile.jpg";

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(imageService.upload(any(ProfileImage.class))).thenReturn(fileName);
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.uploadProfileImage(user.getId(), profileImage);

        assertEquals(fileName, user.getFile());
        verify(imageService, times(1)).upload(any(ProfileImage.class));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void deleteProfileImage_success() {
        user.setFile("profile.jpg");

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        doNothing().when(imageService).delete("profile.jpg");
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.deleteProfileImage(user.getId());

        assertNull(user.getFile());
        verify(imageService, times(1)).delete("profile.jpg");
        verify(userRepository, times(1)).save(any(User.class));
    }
}
