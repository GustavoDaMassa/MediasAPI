package br.com.gustavohenrique.MediasAPI.controller.web;

import br.com.gustavohenrique.MediasAPI.dtos.StringRequestDTO;
import br.com.gustavohenrique.MediasAPI.model.Course;
import br.com.gustavohenrique.MediasAPI.model.Users;
import br.com.gustavohenrique.MediasAPI.repository.CourseRepository;
import br.com.gustavohenrique.MediasAPI.repository.UserRepository;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.ProjectionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/web/courses/{courseId}/projections/create")
public class ProjectionCreateControllerWeb {

    private final ProjectionService projectionService;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public ProjectionCreateControllerWeb(ProjectionService projectionService, UserRepository userRepository, CourseRepository courseRepository) {
        this.projectionService = projectionService;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    @GetMapping
    public String createProjectionForm(@PathVariable Long courseId, Model model, Principal principal) {
        validateCourseOwner(principal, courseId);
        model.addAttribute("stringRequestDTO", new StringRequestDTO(""));
        model.addAttribute("courseId", courseId);
        return "projectionCreate";
    }

    @PostMapping
    public String createProjection(@PathVariable Long courseId, @ModelAttribute @Valid StringRequestDTO stringRequestDTO, BindingResult bindingResult, Principal principal, RedirectAttributes redirectAttributes, Model model) {
        try {
            validateCourseOwner(principal, courseId);
            if (bindingResult.hasErrors()) {
                model.addAttribute("courseId", courseId);
                return "projectionCreate";
            }
            projectionService.createProjection(courseId, stringRequestDTO);
            redirectAttributes.addFlashAttribute("message", "Projeção criada com sucesso!");
            return "redirect:/web/courses/{courseId}/projections";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erro ao criar a projeção: " + e.getMessage());
            model.addAttribute("stringRequestDTO", stringRequestDTO);
            model.addAttribute("courseId", courseId);
            return "projectionCreate";
        }
    }

    private void validateCourseOwner(Principal principal, Long courseId) {
        Users user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        if (!course.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("User does not own this course");
        }
    }
}