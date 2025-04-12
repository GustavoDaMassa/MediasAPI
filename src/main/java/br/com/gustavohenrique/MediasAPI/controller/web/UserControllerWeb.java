package br.com.gustavohenrique.MediasAPI.controller.web;

import br.com.gustavohenrique.MediasAPI.dtos.ProjectionDTO;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.ProjectionService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/web/users")
public class UserControllerWeb {

    @Autowired
    private ProjectionService projectionService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/{userId}/projections")
    @Operation(summary = "Listar projeções do usuário", description = "Lista todas as projeções de um usuário")
    public String showUserProjections(@PathVariable Long userId, Model model) {
        List<ProjectionDTO> projections = projectionService.listAllProjection(userId).stream()
                .map(projection -> modelMapper.map(projection, ProjectionDTO.class))
                .collect(Collectors.toList());
        model.addAttribute("projections", projections);
        model.addAttribute("userId", userId); // Passa o userId para a view
        return "userProjections";
    }
}