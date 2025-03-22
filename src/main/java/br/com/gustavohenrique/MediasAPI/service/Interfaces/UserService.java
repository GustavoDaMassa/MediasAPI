package br.com.gustavohenrique.MediasAPI.service.Interfaces;

import br.com.gustavohenrique.MediasAPI.dtos.LogOnDto;
import br.com.gustavohenrique.MediasAPI.model.Users;
import br.com.gustavohenrique.MediasAPI.dtos.EmailUpdateDTO;
import br.com.gustavohenrique.MediasAPI.dtos.StringRequestDTO;
import jakarta.validation.Valid;

import java.util.List;

public interface UserService {
    Users create(LogOnDto users);

    Users updateName(Long id, @Valid StringRequestDTO nameDto);

    Users updateEmail(Long id, @Valid EmailUpdateDTO emailDTO);

    Users deleteUser(Long id);

    List<Users> listUsers();
}
