package br.com.gustavohenrique.MediasAPI.service.Impl;

import br.com.gustavohenrique.MediasAPI.service.Interfaces.UserService;
import br.com.gustavohenrique.MediasAPI.service.OwnershipValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
public class OwnershipValidationServiceImpl implements OwnershipValidationService {

    private final UserService userService;

    @Autowired
    public OwnershipValidationServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void validate(Long resourceOwnerId) {
        var user = userService.getAuthenticatedUser();
        if (!resourceOwnerId.equals(user.getId())) {
            throw new AccessDeniedException("You are not authorized to access this resource.");
        }
    }
}