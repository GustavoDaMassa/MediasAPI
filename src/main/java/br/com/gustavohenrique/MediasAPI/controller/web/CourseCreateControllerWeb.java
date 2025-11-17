package br.com.gustavohenrique.MediasAPI.controller.web;

import br.com.gustavohenrique.MediasAPI.dtos.RequestCourseDto;
import br.com.gustavohenrique.MediasAPI.model.Users;
import br.com.gustavohenrique.MediasAPI.repository.UserRepository;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.CourseService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/web/courses/create")
public class CourseCreateControllerWeb {

    private final CourseService courseService;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CourseCreateControllerWeb(CourseService courseService, UserRepository userRepository, ModelMapper modelMapper) {
        this.courseService = courseService;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public String createCourseForm(Model model) {
        model.addAttribute("requestCourseDto", new RequestCourseDto("", "", 6.0));
        return "courseCreate";
    }

    @PostMapping
    public String createCourse(Principal principal, @ModelAttribute @Valid RequestCourseDto requestCourseDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "courseCreate";
        }
        try {
            Users user = userRepository.findByEmail(principal.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            courseService.createCourse(user.getId(), requestCourseDto);
            return "redirect:/web/courses";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erro ao criar o curso: " + e.getMessage());
            model.addAttribute("requestCourseDto", requestCourseDto);
            return "courseCreate";
        }
    }
}