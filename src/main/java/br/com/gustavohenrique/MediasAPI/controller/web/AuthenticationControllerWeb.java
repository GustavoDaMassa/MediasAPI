package br.com.gustavohenrique.MediasAPI.controller.web;

import br.com.gustavohenrique.MediasAPI.dtos.AuthDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web")
public class AuthenticationControllerWeb {

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("authDto", new AuthDto());
        return "login";
    }

    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/web/login";
    }
}