package br.com.gustavohenrique.MediasAPI.controller.web;

import br.com.gustavohenrique.MediasAPI.dtos.CourseDTO;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.CourseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/web/{userId}/courses") // Recebe o userId na rota
public class CourseControllerWeb {

    private final CourseService courseService;
    private final ModelMapper modelMapper;

    @Autowired
    public CourseControllerWeb(CourseService courseService, ModelMapper modelMapper) {
        this.courseService = courseService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public String showCourses(@PathVariable Long userId, Model model) { // Recupera o userId do path
        List<CourseDTO> courses = courseService.listCourses(userId).stream()
                .map(course -> modelMapper.map(course, CourseDTO.class))
                .collect(Collectors.toList());

        model.addAttribute("courses", courses);
        return "courses";
    }

    @GetMapping("{id}/delete")
    public String deleteCourse(@PathVariable Long userId, @PathVariable Long id) {

        courseService.deleteCourse(userId, id);
        return "redirect:/web/{userId}/courses";
    }
}