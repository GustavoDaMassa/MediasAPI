package br.com.gustavohenrique.MediasAPI.service;

import br.com.gustavohenrique.MediasAPI.controller.dtos.EmailUpdateDTO;
import br.com.gustavohenrique.MediasAPI.controller.dtos.NameUpdateDTO;
import br.com.gustavohenrique.MediasAPI.model.User;
import br.com.gustavohenrique.MediasAPI.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(User user) {
        return userRepository.save(user);
    }

    public List<User> list() {
        return userRepository.findAll();
    }


    public User updateName(Long id, @Valid NameUpdateDTO nameDto) {
        User newUser = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Id não encontrado"));
        newUser.setName(nameDto.name());
        return userRepository.save(newUser);
    }
    public User updateEmail(Long id, @Valid EmailUpdateDTO emailDTO) {
        User newUser = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Id não encontrado"));
        newUser.setName(emailDTO.email());
        return userRepository.save(newUser);
    }

    public User deleteUser(Long id) {
            var user  = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Id não encontrado"));
            userRepository.deleteById(id);
            return user;
    }
}
