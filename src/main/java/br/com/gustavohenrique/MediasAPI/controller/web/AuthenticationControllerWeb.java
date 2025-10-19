package br.com.gustavohenrique.MediasAPI.controller.web;

import br.com.gustavohenrique.MediasAPI.authentication.AuthenticationService;
import br.com.gustavohenrique.MediasAPI.dtos.AuthDto;
import br.com.gustavohenrique.MediasAPI.model.Users;
import br.com.gustavohenrique.MediasAPI.model.Users;
import br.com.gustavohenrique.MediasAPI.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/web")
public class AuthenticationControllerWeb {


    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;

    @Autowired
    public AuthenticationControllerWeb(AuthenticationService authenticationService, UserRepository userRepository) {
        this.authenticationService = authenticationService;
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("authDto", new AuthDto());
        return "login";
    }

    @PostMapping("/login")
    public String authenticate(@ModelAttribute AuthDto user, HttpSession session) {
        try {
            String token = authenticationService.authenticate(user);
            session.setAttribute("token", token);
            var byEmail = userRepository.findByEmail(user.getEmail()).orElseThrow();
            Long userId = byEmail.getId();
            return "redirect:/web/" + userId + "/courses";
        } catch (Exception e) {
            return "login"; // Retorna para a página de login em caso de erro
        }
    }



    @GetMapping("/") // Adiciona esta rota
    public String redirectToLogin() {
        return "redirect:/web/login"; // Redireciona para a página de login
    }
}