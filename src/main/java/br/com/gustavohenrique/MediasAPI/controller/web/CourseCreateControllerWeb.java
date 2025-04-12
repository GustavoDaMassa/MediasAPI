package br.com.gustavohenrique.MediasAPI.controller.web;

import br.com.gustavohenrique.MediasAPI.dtos.CourseDTO;
import br.com.gustavohenrique.MediasAPI.dtos.RequestCourseDto;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.CourseService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/web/{userId}/courses/create")
public class CourseCreateControllerWeb {

    private final CourseService courseService;
    private final ModelMapper modelMapper;

    @Autowired
    public CourseCreateControllerWeb(CourseService courseService, ModelMapper modelMapper) {
        this.courseService = courseService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public String createCourseForm(@PathVariable Long userId, Model model) {
        model.addAttribute("requestCourseDto", new RequestCourseDto("", "", 6.0));
        model.addAttribute("userId", userId);
        return "courseCreate";
    }

    @PostMapping
    public String createCourse(@PathVariable Long userId, @ModelAttribute @Valid RequestCourseDto requestCourseDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "courseCreate";
        }
        try {
            CourseDTO courseDTO = modelMapper.map(courseService.createCourse(userId, requestCourseDto), CourseDTO.class);
            return "redirect:/web/" +userId+ "/courses";
        } catch (Exception e) {
            return "courseCreate";
        }
    }
}