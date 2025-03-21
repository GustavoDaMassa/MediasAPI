package br.com.gustavohenrique.MediasAPI.service;

import br.com.gustavohenrique.MediasAPI.exception.DataIntegrityException;
import br.com.gustavohenrique.MediasAPI.exception.NotFoundArgumentException;
import br.com.gustavohenrique.MediasAPI.model.Users;
import br.com.gustavohenrique.MediasAPI.dtos.EmailUpdateDTO;
import br.com.gustavohenrique.MediasAPI.dtos.StringRequestDTO;
import br.com.gustavohenrique.MediasAPI.repository.UserRepository;
import br.com.gustavohenrique.MediasAPI.service.Impl.CourseServiceImpl;
import br.com.gustavohenrique.MediasAPI.service.Impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("local")
class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private CourseServiceImpl courseService;

    @Mock
    private  UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should return the user created.")
    void createAndReturnUserSuccessfully() {
        var user = new Users("Gustavo Henrique","pereira@discente.ufg.br","aula321");

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(userRepository.save(any(Users.class))).thenReturn(user);

        var createdUser = userService.create(user);
        assertAll(user, createdUser);
        verify( userRepository).save(user);
    }

    @Test
    @DisplayName("Should return exception if the email already exist.")
    void createUserExceptionEmailNotAvailable() {
        var user = new Users("Gustavo Henrique","pereira@discente.ufg.br","aula321");

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        assertThrows(DataIntegrityException.class, () -> userService.create(user));
        verify(userRepository).existsByEmail(user.getEmail());
        verify(userRepository,never()).save(user);
    }



    @Test
    @DisplayName("Should return user updated successfully")
    void updateUsersNameSuccessfully() {
        var nameDto = new StringRequestDTO("Gustavo Pereira");
        var newUser = new Users(nameDto.string(),"pereira@discente.ufg.br","aula321");

        when(userRepository.findById(newUser.getId())).thenReturn(Optional.of(newUser));
        when(userRepository.save(any(Users.class))).thenReturn(newUser);

        var updateUser = userService.updateName(newUser.getId(), nameDto);

        assertAll(newUser, updateUser);
        verify(userRepository).save(any(Users.class));
        verify(userRepository).findById(newUser.getId());
    }

    @Test
    @DisplayName("Should return an Exception when the user id not exist")
    void updateNameUserNotFound() {
        var nameDto = new StringRequestDTO("Gustavo Pereira");
        var newUser = new Users(nameDto.string(),"pereira@discente.ufg.br","aula321");

        when(userRepository.findById(newUser.getId())).thenThrow(NotFoundArgumentException.class);

        assertThrows(NotFoundArgumentException.class, () -> userService.updateName(newUser.getId(), nameDto));

        verify(userRepository,never()).save(any(Users.class));
         verify(userRepository).findById(newUser.getId());
    }

    @Test//___________________--------------------------------------------------------------
    void updateEmail() {
        var emailDTO = new EmailUpdateDTO("gustavo3gb@gmail.com");
        var newUser = new Users( "Gustavo Pereira", emailDTO.email(),"aula321");

        when(userRepository.save(any(Users.class))).thenReturn(newUser);
        var updateUser = userService.updateEmail(newUser.getId(),emailDTO);

        assertAll(newUser,updateUser);
        verify( userRepository, times(1)).save(any(Users.class));
    }

    @Test//---------------------------------------------------------------------
    void deleteUser() {
        var user = new Users("Gustavo Pereira","gustavo3gb@gmail.com","aula321");

        var deletedUser = userService.deleteUser(user.getId());

        assertAll(user, deletedUser);
        verify(userRepository, times(1)).delete(any(Users.class));
    }

    @Test
    @DisplayName("Should return a list of Users")
    void listUsersFindAllSuccessfully() {
        Users users2 = new Users("Henrique","gustavohenrique3gb@gmail.com.br","aula321");
        Users users3 = new Users("Gustavo Henrique","gustavo3gb@gmail.com","aula321");
        Users users1 = new Users("Gustavo","gustavo.pereira@discente.ufg.br","aula321");

        when(userRepository.findAll()).thenReturn(List.of(users1,users2,users3));

        List<Users> users = userService.listUsers();

        assertAll(users1,users.get(0));
        assertAll(users2,users.get(1));
        assertAll(users3,users.get(2));
        assertEquals(3,users.size());
        verify(userRepository).findAll();
        assertEquals(Users.class,users.get(0).getClass());
    }

    @Test
    @DisplayName("Should return an empty list")
    void listUsersEmpty() {
        when(userRepository.findAll()).thenReturn(List.of());
        List<Users> users = userService.listUsers();
        assertNull(users);
        verify(userRepository).findAll();
    }

    private static void assertAll(Users expectedUser, Users actualUser) {
        assertEquals(expectedUser.getId(), actualUser.getId());
        assertEquals(expectedUser.getName(), actualUser.getName());
        assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        assertEquals(expectedUser.getPassword(), actualUser.getPassword());
    }
}