package br.com.gustavohenrique.MediasAPI.service;

import br.com.gustavohenrique.MediasAPI.exception.DataIntegrityException;
import br.com.gustavohenrique.MediasAPI.exception.NotFoundArgumentException;
import br.com.gustavohenrique.MediasAPI.model.dtos.EmailUpdateDTO;
import br.com.gustavohenrique.MediasAPI.model.dtos.StringRequestDTO;
import br.com.gustavohenrique.MediasAPI.model.Users;
import br.com.gustavohenrique.MediasAPI.model.dtos.UserDTO;
import br.com.gustavohenrique.MediasAPI.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserRepository userRepository;


    public Users create(Users users) {
        if (userRepository.existsByEmail(users.getEmail()))throw new DataIntegrityException(users.getEmail());
        return userRepository.save(users);
    }

    public Users updateName(Long id, @Valid StringRequestDTO nameDto) {
        Users newUsers = userRepository.findById(id).orElseThrow(() -> new NotFoundArgumentException("User Id "+id+" not found"));
        newUsers.setName(nameDto.string());
        return userRepository.save(newUsers);
    }
    public Users updateEmail(Long id, @Valid EmailUpdateDTO emailDTO) {
        Users newUsers = userRepository.findById(id).orElseThrow(() -> new NotFoundArgumentException("User Id "+id+" not found"));
        if (userRepository.existsByEmail(emailDTO.email()))throw new DataIntegrityException(emailDTO.email());
        newUsers.setEmail(emailDTO.email());
        return userRepository.save(newUsers);
    }

    public Users deleteUser(Long id) {
            var user  = userRepository.findById(id).orElseThrow(() -> new NotFoundArgumentException("User Id "+id+" not found"));
            userRepository.delete(user);
            return user;
    }

    public List<Users> listUsers() {
        return userRepository.findAll();
    }
}
