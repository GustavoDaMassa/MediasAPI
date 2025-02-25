package br.com.gustavohenrique.MediasAPI.controller.rest;

import br.com.gustavohenrique.MediasAPI.controller.dtos.EmailUpdateDTO;
import br.com.gustavohenrique.MediasAPI.controller.dtos.StringRequestDTO;
import br.com.gustavohenrique.MediasAPI.model.User;
import br.com.gustavohenrique.MediasAPI.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Valid User user){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(user));
    }

    @PatchMapping("/{id}/name")
    public ResponseEntity<?> updateName(@PathVariable Long id, @RequestBody @Valid StringRequestDTO nameDto) {
       try {
           return ResponseEntity.status(HttpStatus.OK).body(userService.updateName(id, nameDto));
       }catch (IllegalArgumentException e){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
       }
    }

    @PatchMapping("/{id}/email")
    public ResponseEntity<?> updateEmail(@PathVariable Long id, @RequestBody @Valid EmailUpdateDTO emailDTO){
        try {
            return ResponseEntity.ok(userService.updateEmail(id, emailDTO));
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        try {
            return ResponseEntity.ok(userService.deleteUser(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
