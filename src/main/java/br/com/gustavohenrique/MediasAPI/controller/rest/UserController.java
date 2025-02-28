package br.com.gustavohenrique.MediasAPI.controller.rest;

import br.com.gustavohenrique.MediasAPI.model.dtos.EmailUpdateDTO;
import br.com.gustavohenrique.MediasAPI.model.dtos.StringRequestDTO;
import br.com.gustavohenrique.MediasAPI.model.Users;
import br.com.gustavohenrique.MediasAPI.model.dtos.UserDTO;
import br.com.gustavohenrique.MediasAPI.service.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
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

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping
public ResponseEntity<UserDTO> createUser(@RequestBody @Valid Users users){
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(userService.create(users), UserDTO.class));
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> showUsers(){
        return ResponseEntity.ok(userService.listUsers().stream().
                map(users -> modelMapper.map(users,UserDTO.class)).collect(Collectors.toList()));
    }

    @PatchMapping("/{id}/name")
    public ResponseEntity<UserDTO> updateName(@PathVariable Long id, @RequestBody @Valid StringRequestDTO nameDto) {
           return ResponseEntity.status(HttpStatus.OK).body(modelMapper
                   .map(userService.updateName(id, nameDto), UserDTO.class));
    }

    @PatchMapping("/{id}/email")
    public ResponseEntity<UserDTO> updateEmail(@PathVariable Long id, @RequestBody @Valid EmailUpdateDTO emailDTO){
            return ResponseEntity.ok(modelMapper.map(userService.updateEmail(id, emailDTO), UserDTO.class));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable Long id){
            return ResponseEntity.ok(modelMapper.map(userService.deleteUser(id), UserDTO.class));
    }

}
