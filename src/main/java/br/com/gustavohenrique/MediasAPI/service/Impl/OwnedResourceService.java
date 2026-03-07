package br.com.gustavohenrique.MediasAPI.service.Impl;

import br.com.gustavohenrique.MediasAPI.service.Interfaces.OwnershipValidator;
import br.com.gustavohenrique.MediasAPI.service.OwnershipValidationService;

public abstract class OwnedResourceService implements OwnershipValidator {

    private final OwnershipValidationService ownershipValidationService;

    protected OwnedResourceService(OwnershipValidationService ownershipValidationService) {
        this.ownershipValidationService = ownershipValidationService;
    }

    protected abstract Long resolveOwnerId(Long resourceId);

    @Override
    public final void validateOwnership(Long resourceId) {
        ownershipValidationService.validate(resolveOwnerId(resourceId));
    }
}