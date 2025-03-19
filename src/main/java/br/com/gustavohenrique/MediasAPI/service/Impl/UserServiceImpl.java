package br.com.gustavohenrique.MediasAPI.service.Impl;

import br.com.gustavohenrique.MediasAPI.exception.DataIntegrityException;
import br.com.gustavohenrique.MediasAPI.exception.NotFoundArgumentException;
import br.com.gustavohenrique.MediasAPI.dtos.EmailUpdateDTO;
import br.com.gustavohenrique.MediasAPI.dtos.StringRequestDTO;
import br.com.gustavohenrique.MediasAPI.model.Users;
import br.com.gustavohenrique.MediasAPI.repository.UserRepository;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.CourseService;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final CourseService courseService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserServiceImpl(CourseService courseService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.courseService = courseService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Users create(Users users) {
        if (userRepository.existsByEmail(users.getEmail()))throw new DataIntegrityException(users.getEmail());
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        return userRepository.save(users);
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
}
