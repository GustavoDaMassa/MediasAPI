package br.com.gustavohenrique.MediasAPI.service;

import br.com.gustavohenrique.MediasAPI.service.Interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
public class OwnershipValidationService {

    private final UserService userService;

    @Autowired
    public OwnershipValidationService(UserService userService) {
        this.userService = userService;
    }

    public void validate(Long resourceOwnerId) {
        var user = userService.getAuthenticatedUser();
        if (!resourceOwnerId.equals(user.getId())) {
            throw new AccessDeniedException("You are not authorized to access this resource.");
        }
    }
}
