package br.com.gustavohenrique.MediasAPI.service.Impl;

import br.com.gustavohenrique.MediasAPI.dtos.EmailUpdateDTO;
import br.com.gustavohenrique.MediasAPI.dtos.LogOnDto;
import br.com.gustavohenrique.MediasAPI.dtos.StringRequestDTO;
import br.com.gustavohenrique.MediasAPI.exception.DataIntegrityException;
import br.com.gustavohenrique.MediasAPI.exception.NotFoundArgumentException;
import br.com.gustavohenrique.MediasAPI.model.Role;
import br.com.gustavohenrique.MediasAPI.model.Users;
import br.com.gustavohenrique.MediasAPI.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Autowired
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private CourseServiceImpl courseService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should create an user successfully")
    void createUserSuccessfully() {

        var userDto = new LogOnDto("Gustavo", "gustavo.pereira@discente.ufg.br","aula321");
        var user = new Users(null,userDto.name(),userDto.email(),new ArrayList<>(),"Senha criptografada", Role.USER);

        when(userRepository.existsByEmail(userDto.email())).thenReturn(false);
        when(passwordEncoder.encode(userDto.password())).thenReturn("Senha criptografada");
        when(userRepository.save(any(Users.class))).thenReturn(user);

        var response = userService.create(userDto);

        AssertUser(user, response);
        assertEquals(user.getRole(), response.getRole());
        verify(userRepository).save(any(Users.class));
    }

    @Test
    @DisplayName("Should create an admin user successfully")
    void createAdminUserSuccessfully() {
        var userDto = new LogOnDto("Admin User", "admin@example.com", "adminpass");
        var user = new Users(null, userDto.name(), userDto.email(), new ArrayList<>(), "EncryptedAdminPass", Role.ADMIN);

        when(userRepository.existsByEmail(userDto.email())).thenReturn(false);
        when(passwordEncoder.encode(userDto.password())).thenReturn("EncryptedAdminPass");
        when(userRepository.save(any(Users.class))).thenReturn(user);

        var response = userService.createAdminUser(userDto);

        AssertUser(user, response);
        assertEquals(user.getRole(), response.getRole());
        verify(userRepository).save(any(Users.class));
    }

    @Test
    @DisplayName("Should return exception if the email already exist for admin user.")
    void createAdminUserExceptionEmailNotAvailable() {
        var userDto = new LogOnDto("Admin User", "admin@example.com", "adminpass");

        when(userRepository.existsByEmail(userDto.email())).thenReturn(true);

        assertThrows(DataIntegrityException.class, () -> userService.createAdminUser(userDto));
        verify(userRepository).existsByEmail(userDto.email());
        verify(userRepository,never()).save(any());
        verify(passwordEncoder,never()).encode(userDto.password());
    }

    @Test
    @DisplayName("Should return exception if the email already exist.")
    void createUserExceptionEmailNotAvailable() {

        var userDto = new LogOnDto("Gustavo", "gustavo.pereira@discente.ufg.br","aula321");

        when(userRepository.existsByEmail(userDto.email())).thenReturn(true);

        assertThrows(DataIntegrityException.class, () -> userService.create(userDto));
        verify(userRepository).existsByEmail(userDto.email());
        verify(userRepository,never()).save(any());
        verify(passwordEncoder,never()).encode(userDto.password());
    }

    @Test
    @DisplayName("Should return user with the name updated successfully")
    void updateUsersNameSuccessfully() {
        var nameDto = new StringRequestDTO("Gustavo Henrique");
        var newUser = new Users(null,nameDto.string(),"gustavo.pereira@discente.ufg.br",new ArrayList<>(),
                "Senha criptografada", Role.USER);

        when(userRepository.findById(newUser.getId())).thenReturn(Optional.of(newUser));
        when(userRepository.save(any(Users.class))).thenReturn(newUser);

        var updateUser = userService.updateName(newUser.getId(), nameDto);

        AssertUser(newUser, updateUser);
        assertEquals(newUser.getRole(), updateUser.getRole());
        verify(userRepository).save(any(Users.class));
        verify(userRepository).findById(newUser.getId());
    }

    @Test
    @DisplayName("Should return an Exception when the user id not exist, in the updateName")
    void updateNameUserNotFound() {
        var nameDto = new StringRequestDTO("Gustavo Pereira");
        var newUser = new Users(null,nameDto.string(),"gustavo.pereira@discente.ufg.br",new ArrayList<>(),
                "Senha criptografada", Role.USER);

        when(userRepository.findById(newUser.getId())).thenThrow(NotFoundArgumentException.class);

        assertThrows(NotFoundArgumentException.class, () -> userService.updateName(newUser.getId(), nameDto));

        verify(userRepository,never()).save(any(Users.class));
        verify(userRepository).findById(newUser.getId());
    }

    @Test
    @DisplayName("Should return the user with the email updated successfully")
    void updateUsersEmailSuccessfully() {
        var emailDTO = new EmailUpdateDTO("gustavohenrique3gb@gmail.com");
        var newUser = new Users(null, "Gustavo Pereira", emailDTO.email(), new ArrayList<>(),
                "Senha criptografada", Role.USER);

        when(userRepository.findById(newUser.getId())).thenReturn(Optional.of(newUser));
        when(userRepository.save(any(Users.class))).thenReturn(newUser);

        var response = userService.updateEmail(newUser.getId(),emailDTO);

        AssertUser(newUser,response);
        assertEquals(newUser.getRole(), response.getRole());
        verify(userRepository).save(any(Users.class));
    }

    @Test
    @DisplayName("Should return an Exception when the user id not exist, in the updateEmail")
    void updateEmailUserNotFound() {
        var emailDTO = new EmailUpdateDTO("gustavohenrique3gb@gmail.com");
        var newUser = new Users(null, "Gustavo Pereira", emailDTO.email(), new ArrayList<>(),
                "Senha criptografada", Role.USER);

        when(userRepository.findById(newUser.getId())).thenThrow(NotFoundArgumentException.class);

        assertThrows(NotFoundArgumentException.class, () -> userService.updateEmail(newUser.getId(), emailDTO));

        verify(userRepository,never()).save(any(Users.class));
        verify(userRepository).findById(newUser.getId());
    }

    @Test
    @DisplayName("Should delete an user successfully")
    void deleteUserSuccessfully() {
        var user = new Users(null, "Gustavo Pereira","gustavohenrique3gb@gmail.com", new ArrayList<>(),
                "Senha criptografada", Role.USER);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(any(Users.class));


        var deletedUser = userService.deleteUser(user.getId());

        AssertUser(user, deletedUser);
        assertEquals(user.getRole(), deletedUser.getRole());
        verify(userRepository).delete(any(Users.class));
    }

    @Test
    @DisplayName("Should throw an exception when de userId not exist")
    void deleteUserNotFound() {
        var user = new Users(null, "Gustavo Pereira","gustavohenrique3gb@gmail.com", new ArrayList<>(),
                "Senha criptografada", Role.USER);

        when(userRepository.findById(user.getId())).thenThrow(NotFoundArgumentException.class);

        assertThrows(NotFoundArgumentException.class, () -> userService.deleteUser(user.getId()));

        verify(userRepository).findById(user.getId());
        verify(userRepository,never()).delete(any(Users.class));
    }

    @Test
    @DisplayName("Should return a list of Users")
    void listUsersFindAllSuccessfully() {
        Users users2 = new Users(null, "Henrique","gustavohenrique3gb@gmail.com.br", new ArrayList<>(),
                "aula321", Role.USER);
        Users users3 = new Users(null, "Gustavo Henrique","gustavo3gb@gmail.com", new ArrayList<>(),
                "aula321", Role.USER);
        Users users1 = new Users(null, "Gustavo","gustavo.pereira@discente.ufg.br", new ArrayList<>(),
                "aula321", Role.USER);

        when(userRepository.findAll()).thenReturn(List.of(users1,users2,users3));

        List<Users> users = userService.listUsers();

        AssertUser(users2,users.get(1));
        AssertUser(users1,users.get(0));
        AssertUser(users3,users.get(2));
        assertEquals(3,users.size());
        verify(userRepository).findAll();
        assertEquals(Users.class,users.get(0).getClass());
    }



    private static void AssertUser(Users user, Users response) {
        assertEquals(user.getId(), response.getId());
        assertEquals(user.getName(), response.getName());
        assertEquals(user.getEmail(), response.getEmail());
        assertEquals(user.getCourse(), response.getCourse());
        assertEquals(user.getPassword(), response.getPassword());
    }
}