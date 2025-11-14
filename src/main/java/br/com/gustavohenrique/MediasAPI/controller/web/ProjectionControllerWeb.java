package br.com.gustavohenrique.MediasAPI.controller.web;

import br.com.gustavohenrique.MediasAPI.dtos.ProjectionDTO;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.ProjectionService;
import br.com.gustavohenrique.MediasAPI.controller.rest.v1.mapper.MapDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/web/{courseId}/projections")
public class ProjectionControllerWeb {

    private final ProjectionService projectionService;
    private final ModelMapper modelMapper;
    private final MapDTO mapDTO;

    @Autowired
    public ProjectionControllerWeb(ProjectionService projectionService, ModelMapper modelMapper, MapDTO mapDTO) {
        this.projectionService = projectionService;
        this.modelMapper = modelMapper;
        this.mapDTO = mapDTO;
    }

    @GetMapping("/{userId}")
    public String showProjections(@PathVariable Long courseId,@PathVariable Long userId, Model model) {
        List<ProjectionDTO> projections = projectionService.listProjection(courseId).stream()
                .map(mapDTO::projectionDTO)
                .collect(Collectors.toList());

        model.addAttribute("projections", projections);
        model.addAttribute("userId", userId);
        model.addAttribute("courseId", courseId);
        return "projections";
    }

    @GetMapping("/{id}/delete/{userId}")
    public String deleteProjection(@PathVariable Long userId, @PathVariable Long courseId, @PathVariable Long id, RedirectAttributes redirectAttributes) {
        projectionService.deleteProjection(courseId, id);
        redirectAttributes.addFlashAttribute("message", "Projeção deletada com sucesso!");
        return "redirect:/web/{courseId}/projections/{userId}";
    }
}