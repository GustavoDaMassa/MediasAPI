package br.com.gustavohenrique.MediasAPI.controller.rest;

import br.com.gustavohenrique.MediasAPI.dtos.EmailUpdateDTO;
import br.com.gustavohenrique.MediasAPI.dtos.LogOnDto;
import br.com.gustavohenrique.MediasAPI.dtos.StringRequestDTO;
import br.com.gustavohenrique.MediasAPI.model.Assessment;
import br.com.gustavohenrique.MediasAPI.model.Course;
import br.com.gustavohenrique.MediasAPI.model.Projection;
import br.com.gustavohenrique.MediasAPI.model.Users;
import br.com.gustavohenrique.MediasAPI.dtos.UserDTO;
import br.com.gustavohenrique.MediasAPI.service.Impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.build.Plugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private ModelMapper modelMapper;

    private final UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }


    @GetMapping
    @Operation(summary = "Listar usuários", description = "Retorna uma lista com todos os usuarios da aplicação e " +
            "seus id's. Perfil Admim necessário.")
    public ResponseEntity<List<UserDTO>> showUsers(){
        return ResponseEntity.ok(userService.listUsers().stream().
                map(users -> modelMapper.map(users,UserDTO.class)).collect(Collectors.toList()));
    }

    @PostMapping
    @Operation(summary = "Cadastrar usuário", description = "Cria um novo perfil de usuário, não é necessário autenticação.")
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid LogOnDto users){
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(userService.create(users), UserDTO.class));
    }

    @PatchMapping("/{id}/name")
    @Operation(summary = "Atualizar nome de usuário")
    public ResponseEntity<UserDTO> updateName(@PathVariable Long id, @RequestBody @Valid StringRequestDTO nameDto) {
           return ResponseEntity.status(HttpStatus.OK).body(modelMapper
                   .map(userService.updateName(id, nameDto), UserDTO.class));
    }

    @PatchMapping("/{id}/email")
    @Operation(summary = "Atualizar email de usuário")
    public ResponseEntity<UserDTO> updateEmail(@PathVariable Long id, @RequestBody @Valid EmailUpdateDTO emailDTO){
            return ResponseEntity.ok(modelMapper.map(userService.updateEmail(id, emailDTO), UserDTO.class));
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Deletar o perfil de usuário")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable Long id){
            return ResponseEntity.ok(modelMapper.map(userService.deleteUser(id), UserDTO.class));
    }

    @GetMapping("{email}")
    @Operation(summary = "resgatar o id de um usuário pelo email")
    public ResponseEntity<UserDTO> findUser(@PathVariable String email){
        return  ResponseEntity.ok(modelMapper.map(userService.findusers(email),UserDTO.class));
    }
}
