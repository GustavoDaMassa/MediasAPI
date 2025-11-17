package br.com.gustavohenrique.MediasAPI.controller.web;

import br.com.gustavohenrique.MediasAPI.dtos.DoubleRequestDTO;
import br.com.gustavohenrique.MediasAPI.model.Course;
import br.com.gustavohenrique.MediasAPI.model.Users;
import br.com.gustavohenrique.MediasAPI.repository.CourseRepository;
import br.com.gustavohenrique.MediasAPI.repository.UserRepository;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.AssessmentService;
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
@RequestMapping("/web/courses/{courseId}/projections/{projectionId}/assessments/{assessmentId}/grade")
public class AssessmentGradeControllerWeb {

    private final AssessmentService assessmentService;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public AssessmentGradeControllerWeb(AssessmentService assessmentService, UserRepository userRepository, CourseRepository courseRepository) {
        this.assessmentService = assessmentService;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    @GetMapping
    public String insertGradeForm(@PathVariable Long courseId, @PathVariable Long projectionId, @PathVariable Long assessmentId, Model model, Principal principal) {
        validateCourseOwner(principal, courseId);
        model.addAttribute("doubleRequestDTO", new DoubleRequestDTO(0.0));
        model.addAttribute("projectionId", projectionId);
        model.addAttribute("courseId", courseId);
        model.addAttribute("assessmentId", assessmentId);
        return "assessmentGrade";
    }

    @PostMapping
    public String insertGrade(@PathVariable Long courseId, @PathVariable Long projectionId, @PathVariable Long assessmentId, @ModelAttribute @Valid DoubleRequestDTO doubleRequestDTO, BindingResult bindingResult, Principal principal, RedirectAttributes redirectAttributes, Model model) {
        try {
            validateCourseOwner(principal, courseId);
            if (bindingResult.hasErrors()) {
                model.addAttribute("projectionId", projectionId);
                model.addAttribute("courseId", courseId);
                model.addAttribute("assessmentId", assessmentId);
                return "assessmentGrade";
            }

            assessmentService.insertGrade(projectionId, assessmentId, doubleRequestDTO);
            redirectAttributes.addFlashAttribute("message", "Nota inserida com sucesso!");
            return "redirect:/web/courses/{courseId}/projections";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erro ao inserir a nota: " + e.getMessage());
            model.addAttribute("doubleRequestDTO", doubleRequestDTO);
            model.addAttribute("projectionId", projectionId);
            model.addAttribute("courseId", courseId);
            model.addAttribute("assessmentId", assessmentId);
            return "assessmentGrade";
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