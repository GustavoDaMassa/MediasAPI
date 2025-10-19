package br.com.gustavohenrique.MediasAPI.service.Impl;

import br.com.gustavohenrique.MediasAPI.dtos.LogOnDto;
import br.com.gustavohenrique.MediasAPI.exception.DataIntegrityException;
import br.com.gustavohenrique.MediasAPI.exception.NotFoundArgumentException;
import br.com.gustavohenrique.MediasAPI.dtos.EmailUpdateDTO;
import br.com.gustavohenrique.MediasAPI.dtos.StringRequestDTO;
import br.com.gustavohenrique.MediasAPI.model.Users;
import br.com.gustavohenrique.MediasAPI.repository.UserRepository;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.CourseService;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.UserService;
import jakarta.validation.Valid;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final CourseService courseService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserServiceImpl(@Lazy CourseService courseService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.courseService = courseService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Users create(LogOnDto users) {
        if (userRepository.existsByEmail(users.email()))throw new DataIntegrityException(users.email());
        Users newUser = new Users(null,users.name(), users.email(),new ArrayList<>(), users.password());
        newUser.setPassword(passwordEncoder.encode(users.password()));
        return userRepository.save(newUser);
    }
    @Transactional
    public Users updateName(Long id, @Valid StringRequestDTO nameDto) {
        Users newUsers = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundArgumentException("User Id "+id+" not found"));
        newUsers.setName(nameDto.string());
        return userRepository.save(newUsers);
    }
    @Transactional
    public Users updateEmail(Long id, @Valid EmailUpdateDTO emailDTO) {
        Users newUsers = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundArgumentException("User Id "+id+" not found"));
        if (userRepository.existsByEmail(emailDTO.email()))throw new DataIntegrityException(emailDTO.email());
        newUsers.setEmail(emailDTO.email());
        return userRepository.save(newUsers);
    }

    @Transactional
    public Users deleteUser(Long id) {
            var user  = userRepository.findById(id)
                    .orElseThrow(() -> new NotFoundArgumentException("User Id "+id+" not found"));
            userRepository.delete(user);
            return user;
    }

    public List<Users> listUsers() {
        return userRepository.findAll();
    }

    @Override
    public Users findusers(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->new NotFoundArgumentException("User email "+email+" not found"));
    }

    @Override
    public Users getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User not authenticated");
        }
        Object principal = authentication.getPrincipal();
        String email;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else if (principal instanceof org.springframework.security.oauth2.jwt.Jwt) {
            email = ((org.springframework.security.oauth2.jwt.Jwt) principal).getSubject();
        } else {
            throw new IllegalStateException("Unexpected principal type: " + principal.getClass());
        }
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundArgumentException("User email " + email + " not found"));
    }
}
