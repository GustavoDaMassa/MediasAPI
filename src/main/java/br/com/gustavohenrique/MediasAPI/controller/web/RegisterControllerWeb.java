package br.com.gustavohenrique.MediasAPI.controller.web;

import br.com.gustavohenrique.MediasAPI.dtos.LogOnDto;
import br.com.gustavohenrique.MediasAPI.service.Impl.UserServiceImpl;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final UserServiceImpl userService;
    private final ModelMapper modelMapper;

    @Autowired
    public RegisterControllerWeb(UserServiceImpl userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public String registerForm(Model model) {
        model.addAttribute("logOnDto", new LogOnDto("", "", ""));
        return "register";
    }

    @PostMapping
    public String registerUser(@ModelAttribute @Valid LogOnDto logOnDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        try {
            userService.create(logOnDto);
            return "redirect:/web/login?registration_success";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erro ao registrar: " + e.getMessage());
            model.addAttribute("logOnDto", logOnDto);
            return "register";
        }
    }
}