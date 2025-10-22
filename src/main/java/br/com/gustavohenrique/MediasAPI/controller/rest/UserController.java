package br.com.gustavohenrique.MediasAPI.controller.rest;

import br.com.gustavohenrique.MediasAPI.dtos.EmailUpdateDTO;
import br.com.gustavohenrique.MediasAPI.dtos.LogOnDto;
import br.com.gustavohenrique.MediasAPI.dtos.StringRequestDTO;
import br.com.gustavohenrique.MediasAPI.dtos.UserDTO;
import br.com.gustavohenrique.MediasAPI.service.Impl.UserServiceImpl;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private ModelMapper modelMapper;

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar usuários", description = "Retorna uma lista com todos os usuarios da aplicação e " +
            "seus id's. Perfil Admim necessário.")
    public ResponseEntity<List<UserDTO>> showUsers(){
        return ResponseEntity.ok(userService.listUsers().stream().
                map(users -> modelMapper.map(users,UserDTO.class)).collect(Collectors.toList()));
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Cadastrar usuário administrador", description = "Cria um novo perfil de usuário com role ADMIN. Perfil Admim necessário.")
    public ResponseEntity<UserDTO> createAdminUser(@RequestBody @Valid LogOnDto users){
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(userService.createAdminUser(users), UserDTO.class));
    }

    @PostMapping
    @Operation(summary = "Cadastrar usuário", description = "Cria um novo perfil de usuário, não é necessário autenticação.")
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid LogOnDto users){
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(userService.create(users), UserDTO.class));
    }

    @PatchMapping("/{id}/name")
    @Operation(summary = "Atualizar nome de usuário")
    public ResponseEntity<UserDTO> updateName(@PathVariable Long id, @RequestBody @Valid StringRequestDTO nameDto) {
           var user = userService.getAuthenticatedUser();
           if (!user.getId().equals(id)) {
                throw new org.springframework.security.access.AccessDeniedException("You are not authorized to update this user.");
           }
           return ResponseEntity.status(HttpStatus.OK).body(modelMapper
                   .map(userService.updateName(id, nameDto), UserDTO.class));
    }

    @PatchMapping("/{id}/email")
    @Operation(summary = "Atualizar email de usuário")
    public ResponseEntity<UserDTO> updateEmail(@PathVariable Long id, @RequestBody @Valid EmailUpdateDTO emailDTO){
            var user = userService.getAuthenticatedUser();
            if (!user.getId().equals(id)) {
                throw new org.springframework.security.access.AccessDeniedException("You are not authorized to update this user.");
            }
            return ResponseEntity.ok(modelMapper.map(userService.updateEmail(id, emailDTO), UserDTO.class));
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Deletar o perfil de usuário")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable Long id){
            var user = userService.getAuthenticatedUser();
            if (!user.getId().equals(id)) {
                throw new org.springframework.security.access.AccessDeniedException("You are not authorized to delete this user.");
            }
            return ResponseEntity.ok(modelMapper.map(userService.deleteUser(id), UserDTO.class));
    }

    @GetMapping("{email}")
    @Operation(summary = "resgatar o id de um usuário pelo email")
    public ResponseEntity<UserDTO> findUser(@PathVariable String email){
        return  ResponseEntity.ok(modelMapper.map(userService.findusers(email),UserDTO.class));
    }
}
