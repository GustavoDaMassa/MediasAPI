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

    @PatchMapping("/{id}/name")
    public ResponseEntity<?> updateName(@PathVariable Long id, @RequestBody @Valid StringRequestDTO nameDto) {
       try {
           return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(userService.updateName(id, nameDto), UserDTO.class));
       }catch (IllegalArgumentException e){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
       }
    }

    @PatchMapping("/{id}/email")
    public ResponseEntity<?> updateEmail(@PathVariable Long id, @RequestBody @Valid EmailUpdateDTO emailDTO){
        try {
            return ResponseEntity.ok(modelMapper.map(userService.updateEmail(id, emailDTO), UserDTO.class));
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        try {
            return ResponseEntity.ok(modelMapper.map(userService.deleteUser(id), UserDTO.class));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
