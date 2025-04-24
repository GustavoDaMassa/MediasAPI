package br.com.gustavohenrique.MediasAPI.controller.web;

import br.com.gustavohenrique.MediasAPI.dtos.ProjectionDTO;
import br.com.gustavohenrique.MediasAPI.dtos.StringRequestDTO;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.ProjectionService;
import br.com.gustavohenrique.MediasAPI.controller.rest.mapper.MapDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/web/{courseId}/projections/create/{userId}")
public class ProjectionCreateControllerWeb {

    private final ProjectionService projectionService;
    private final MapDTO mapDTO;

    @Autowired
    public ProjectionCreateControllerWeb(ProjectionService projectionService, MapDTO mapDTO) {
        this.projectionService = projectionService;
        this.mapDTO = mapDTO;
    }

    @GetMapping
    public String createProjectionForm(@PathVariable Long courseId,@PathVariable Long userId, Model model) {
        model.addAttribute("stringRequestDTO", new StringRequestDTO(""));
        model.addAttribute("courseId", courseId);
        model.addAttribute("userId", userId);
        return "projectionCreate";
    }

    @PostMapping
    public String createProjection(@PathVariable Long userId,@PathVariable Long courseId, @ModelAttribute @Valid StringRequestDTO stringRequestDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "projectionCreate";
        }
        System.out.println(userId);
        projectionService.createProjection(courseId, stringRequestDTO);
        redirectAttributes.addFlashAttribute("message", "Projeção criada com sucesso!");
        return "redirect:/web/{courseId}/projections/{userId}";
    }
}