package br.com.gustavohenrique.MediasAPI.controller.web;

import br.com.gustavohenrique.MediasAPI.dtos.AuthDto;
import br.com.gustavohenrique.MediasAPI.dtos.LogOnDto;
import br.com.gustavohenrique.MediasAPI.authentication.AuthenticationService;
import br.com.gustavohenrique.MediasAPI.service.Impl.UserServiceImpl;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web/register")
public class RegisterControllerWeb {

    private final AuthenticationService authenticationService;
    private final UserServiceImpl userService;
    private final ModelMapper modelMapper;

    @Autowired
    public RegisterControllerWeb(AuthenticationService authenticationService, UserServiceImpl userService, ModelMapper modelMapper) {
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public String registerForm(Model model) {
        model.addAttribute("logOnDto", new LogOnDto("", "", ""));
        return "register";
    }

    @PostMapping
    public String registerUser(@ModelAttribute @Valid LogOnDto logOnDto, BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        try {
            var user = userService.create(logOnDto);
            ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(user, LogOnDto.class));
            return "login";
        } catch (Exception e) {
            return "register";
        }
    }
}