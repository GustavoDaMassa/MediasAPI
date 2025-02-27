package br.com.gustavohenrique.MediasAPI.service;

import br.com.gustavohenrique.MediasAPI.model.dtos.EmailUpdateDTO;
import br.com.gustavohenrique.MediasAPI.model.dtos.StringRequestDTO;
import br.com.gustavohenrique.MediasAPI.model.User;
import br.com.gustavohenrique.MediasAPI.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(User user) {
        return userRepository.save(user);
    }

    public User updateName(Long id, @Valid StringRequestDTO nameDto) {
        User newUser = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User Id not found"));
        newUser.setName(nameDto.string());
        return userRepository.save(newUser);
    }
    public User updateEmail(Long id, @Valid EmailUpdateDTO emailDTO) {
        User newUser = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User Id not found"));
        newUser.setEmail(emailDTO.email());
        return userRepository.save(newUser);
    }

    public User deleteUser(Long id) {
            var user  = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User Id not found"));
            userRepository.deleteById(id);
            return user;
    }
}
