package br.com.gustavohenrique.MediasAPI.controller.web;

import br.com.gustavohenrique.MediasAPI.dtos.CourseDTO;
import br.com.gustavohenrique.MediasAPI.model.Users;
import br.com.gustavohenrique.MediasAPI.repository.UserRepository;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.CourseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/web/courses") // Rota não depende mais do userId
public class CourseControllerWeb {

    private final CourseService courseService;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CourseControllerWeb(CourseService courseService, UserRepository userRepository, ModelMapper modelMapper) {
        this.courseService = courseService;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public String showCourses(Model model, Principal principal) { // Injeta o Principal
        Users user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Long userId = user.getId();

        List<CourseDTO> courses = courseService.listCourses(userId).stream()
                .map(course -> modelMapper.map(course, CourseDTO.class))
                .collect(Collectors.toList());

        model.addAttribute("courses", courses);
        model.addAttribute("userId", userId); // Adiciona o userId ao modelo se necessário em outro lugar
        return "courses";
    }

    @GetMapping("{id}/delete")
    public String deleteCourse(@PathVariable Long id, Principal principal) { // Injeta o Principal
        Users user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Long userId = user.getId();

        courseService.deleteCourse(userId, id);
        return "redirect:/web/courses"; // Redireciona para a rota corrigida
    }
}