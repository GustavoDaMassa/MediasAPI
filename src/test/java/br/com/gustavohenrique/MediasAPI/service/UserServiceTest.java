package br.com.gustavohenrique.MediasAPI.service;

import br.com.gustavohenrique.MediasAPI.exception.DataIntegrityException;
import br.com.gustavohenrique.MediasAPI.exception.NotFoundArgumentException;
import br.com.gustavohenrique.MediasAPI.model.Users;
import br.com.gustavohenrique.MediasAPI.model.dtos.EmailUpdateDTO;
import br.com.gustavohenrique.MediasAPI.model.dtos.StringRequestDTO;
import br.com.gustavohenrique.MediasAPI.model.dtos.UserDTO;
import br.com.gustavohenrique.MediasAPI.repository.UserRepository;
import org.h2.engine.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("local")
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private CourseService courseService;

    @Mock
    private  UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should return the user created.")
    void createAndReturnUserSuccessfully() {
        var user = new Users(null,"Gustavo Henrique","pereira@discente.ufg.br","aula321");

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(userRepository.save(any(Users.class))).thenReturn(user);

        var createdUser = userService.create(user);
        assertEquals(user.getId(),createdUser.getId());
        assertEquals(user.getName(), createdUser.getName());
        assertEquals(user.getEmail(), createdUser.getEmail());
        assertEquals(user.getPassword(), createdUser.getPassword());
        verify( userRepository).save(user);
    }

    @Test
    @DisplayName("Should return exception if the email already exist.")
    void createUserExceptionEmailNotAvailable() {
        var user = new Users(null,"Gustavo Henrique","pereira@discente.ufg.br","aula321");

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        assertThrows(DataIntegrityException.class, () -> userService.create(user));
        verify(userRepository).existsByEmail(user.getEmail());
        verify(userRepository,never()).save(user);
    }



    @Test
    @DisplayName("Should return user updated successfully")
    void updateUsersNameSuccessfully() {
        var nameDto = new StringRequestDTO("Gustavo Pereira");
        var newUser = new Users(1L,nameDto.string(),"pereira@discente.ufg.br","aula321");

        when(userRepository.findById(newUser.getId())).thenReturn(Optional.of(newUser));
        when(userRepository.save(any(Users.class))).thenReturn(newUser);

        var updateUser = userService.updateName(newUser.getId(), nameDto);

        assertEquals(newUser.getId(),updateUser.getId());
        assertEquals(newUser.getName(), updateUser.getName());
        assertEquals(newUser.getEmail(), updateUser.getEmail());
        assertEquals(newUser.getPassword(), updateUser.getPassword());
        verify(userRepository).save(any(Users.class));
        verify(userRepository).findById(newUser.getId());
    }

    @Test
    @DisplayName("Should return an Exception when the user id not exist")
    void updateNameUserNotFound() {
        var nameDto = new StringRequestDTO("Gustavo Pereira");
        var newUser = new Users(1L,nameDto.string(),"pereira@discente.ufg.br","aula321");

        when(userRepository.findById(newUser.getId())).thenThrow(NotFoundArgumentException.class);

        assertThrows(NotFoundArgumentException.class, () -> userService.updateName(newUser.getId(), nameDto));

        verify(userRepository,never()).save(any(Users.class));
        verify(userRepository).findById(newUser.getId());
    }

    @Test//___________________--------------------------------------------------------------
    void updateEmail() {
        var emailDTO = new EmailUpdateDTO("gustavo3gb@gmail.com");
        var newUser = new Users(5L, "Gustavo Pereira", emailDTO.email(),"aula321");

        when(userRepository.save(any(Users.class))).thenReturn(newUser);
        var updateUser = userService.updateEmail(newUser.getId(),emailDTO);

        assertEquals(1,updateUser.getId());
        assertEquals(newUser.getName(), updateUser.getName());
        assertEquals(newUser.getEmail(), updateUser.getEmail());
        assertEquals(newUser.getPassword(), updateUser.getPassword());
        verify( userRepository, times(1)).save(any(Users.class));
    }

    @Test//---------------------------------------------------------------------
    void deleteUser() {
        var user = new Users(3L,"Gustavo Pereira","gustavo3gb@gmail.com","aula321");

        var deletedUser = userService.deleteUser(user.getId());

        assertEquals(user.getId(),deletedUser.getId());
        assertEquals(user.getName(), deletedUser.getName());
        assertEquals(user.getEmail(), deletedUser.getEmail());
        assertEquals(user.getPassword(), deletedUser.getPassword());
        verify(userRepository, times(1)).delete(any(Users.class));
    }

    @Test
    void listUsers() {
        Users users1 = new Users(1L,"Gustavo","gustavo.pereira@discente.ufg.br","aula321");
        Users users2 = new Users(2L,"Henrique","gustavohenrique3gb@gmail.com.br","aula321");
        Users users3 = new Users(3L,"Gustavo Henrique","gustavo3gb@gmail.com","aula321");


    }

}