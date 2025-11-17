package br.com.gustavohenrique.MediasAPI.controller.web;

import br.com.gustavohenrique.MediasAPI.dtos.ProjectionDTO;
import br.com.gustavohenrique.MediasAPI.model.Users;
import br.com.gustavohenrique.MediasAPI.repository.UserRepository;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.ProjectionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/web/projections")
public class UserControllerWeb {

    private final ProjectionService projectionService;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserControllerWeb(ProjectionService projectionService, UserRepository userRepository, ModelMapper modelMapper) {
        this.projectionService = projectionService;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public String showUserProjections(Model model, Principal principal) {
        Users user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Long userId = user.getId();

        List<ProjectionDTO> projections = projectionService.listAllProjection(userId).stream()
                .map(projection -> modelMapper.map(projection, ProjectionDTO.class))
                .collect(Collectors.toList());

        model.addAttribute("projections", projections);
        return "userProjections";
    }
}