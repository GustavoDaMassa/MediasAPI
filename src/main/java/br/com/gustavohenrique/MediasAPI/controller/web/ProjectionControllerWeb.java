package br.com.gustavohenrique.MediasAPI.controller.web;

import br.com.gustavohenrique.MediasAPI.dtos.ProjectionDTO;
import br.com.gustavohenrique.MediasAPI.model.Course;
import br.com.gustavohenrique.MediasAPI.model.Users;
import br.com.gustavohenrique.MediasAPI.repository.CourseRepository;
import br.com.gustavohenrique.MediasAPI.repository.UserRepository;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.ProjectionService;
import br.com.gustavohenrique.MediasAPI.controller.rest.v1.mapper.MapDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/web/courses/{courseId}/projections")
public class ProjectionControllerWeb {

    private final ProjectionService projectionService;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final MapDTO mapDTO;

    @Autowired
    public ProjectionControllerWeb(ProjectionService projectionService, UserRepository userRepository, CourseRepository courseRepository, MapDTO mapDTO) {
        this.projectionService = projectionService;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.mapDTO = mapDTO;
    }

    @GetMapping
    public String showProjections(@PathVariable Long courseId, Model model, Principal principal) {
        validateCourseOwner(principal, courseId);
        List<ProjectionDTO> projections = projectionService.listProjection(courseId).stream()
                .map(mapDTO::projectionDTO)
                .collect(Collectors.toList());

        model.addAttribute("projections", projections);
        model.addAttribute("courseId", courseId);
        return "projections";
    }

    @GetMapping("/{projectionId}/delete")
    public String deleteProjection(@PathVariable Long courseId, @PathVariable Long projectionId, Principal principal, RedirectAttributes redirectAttributes) {
        validateCourseOwner(principal, courseId);
        projectionService.deleteProjection(courseId, projectionId);
        redirectAttributes.addFlashAttribute("message", "Projeção deletada com sucesso!");
        return "redirect:/web/courses/{courseId}/projections";
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