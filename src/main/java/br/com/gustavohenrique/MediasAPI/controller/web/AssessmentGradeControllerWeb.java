package br.com.gustavohenrique.MediasAPI.controller.web;

import br.com.gustavohenrique.MediasAPI.dtos.AssessmentDTO;
import br.com.gustavohenrique.MediasAPI.dtos.DoubleRequestDTO;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.AssessmentService;
import br.com.gustavohenrique.MediasAPI.controller.rest.v1.mapper.MapDTO;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/web/{courseId}/projections/{projectionId}/assessments/{id}/grade")
public class AssessmentGradeControllerWeb {

    private final AssessmentService assessmentService;
    private final MapDTO mapDTO;
    private final ModelMapper modelMapper;

    @Autowired
    public AssessmentGradeControllerWeb(AssessmentService assessmentService, MapDTO mapDTO, ModelMapper modelMapper) {
        this.assessmentService = assessmentService;
        this.mapDTO = mapDTO;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{userId}")
    public String insertGradeForm(@PathVariable Long userId ,@PathVariable Long courseId, @PathVariable Long projectionId, @PathVariable Long id, Model model) {
        model.addAttribute("doubleRequestDTO", new DoubleRequestDTO(0.0));
        model.addAttribute("projectionId", projectionId);
        model.addAttribute("courseId", courseId);
        model.addAttribute("userId", userId);
        model.addAttribute("assessmentId", id);
        return "assessmentGrade";
    }

    @PostMapping("/{userId}")
    public String insertGrade(@PathVariable Long userId ,@PathVariable Long projectionId, @PathVariable Long id, @ModelAttribute @Valid DoubleRequestDTO doubleRequestDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "assessmentGrade";
        }

        assessmentService.insertGrade(projectionId, id, doubleRequestDTO);
        redirectAttributes.addFlashAttribute("message", "Nota inserida com sucesso!");
        return "redirect:/web/{courseId}/projections/{userId}";
    }
}