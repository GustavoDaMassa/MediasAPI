package br.com.gustavohenrique.MediasAPI.service.Impl;

import br.com.gustavohenrique.MediasAPI.authentication.ApiKeyService;
import br.com.gustavohenrique.MediasAPI.dtos.ApplicationCreatedDTO;
import br.com.gustavohenrique.MediasAPI.dtos.ApplicationDTO;
import br.com.gustavohenrique.MediasAPI.dtos.CreateApplicationDTO;
import br.com.gustavohenrique.MediasAPI.model.Application;
import br.com.gustavohenrique.MediasAPI.repository.ApplicationRepository;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.ApplicationService;
import org.springframework.stereotype.Service;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ApiKeyService apiKeyService;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository, ApiKeyService apiKeyService) {
        this.applicationRepository = applicationRepository;
        this.apiKeyService = apiKeyService;
    }

    @Override
    public ApplicationCreatedDTO create(CreateApplicationDTO dto) {
        String rawKey = apiKeyService.generate();
        Application app = new Application(
                dto.name(),
                dto.description(),
                apiKeyService.hash(rawKey),
                apiKeyService.extractPrefix(rawKey)
        );
        applicationRepository.save(app);
        return toCreatedDTO(app, rawKey);
    }

    @Override
    public ApplicationDTO toDTO(Application app) {
        return new ApplicationDTO(
                app.getId(), app.getName(), app.getDescription(),
                app.getApiKeyPrefix(), app.isActive(),
                app.getRateLimitPerMinute(), app.getCreatedAt()
        );
    }

    @Override
    public void revoke(Application app) {
        app.setActive(false);
        applicationRepository.save(app);
    }

    @Override
    public ApplicationCreatedDTO rotateKey(Application app) {
        String rawKey = apiKeyService.generate();
        app.rotateKey(apiKeyService.hash(rawKey), apiKeyService.extractPrefix(rawKey));
        applicationRepository.save(app);
        return toCreatedDTO(app, rawKey);
    }

    private ApplicationCreatedDTO toCreatedDTO(Application app, String rawKey) {
        return new ApplicationCreatedDTO(
                app.getId(), app.getName(), app.getDescription(),
                rawKey, app.getApiKeyPrefix(), app.isActive(),
                app.getRateLimitPerMinute(), app.getCreatedAt()
        );
    }
}
