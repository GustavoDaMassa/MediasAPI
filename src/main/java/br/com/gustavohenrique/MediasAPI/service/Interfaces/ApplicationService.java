package br.com.gustavohenrique.MediasAPI.service.Interfaces;

import br.com.gustavohenrique.MediasAPI.dtos.ApplicationCreatedDTO;
import br.com.gustavohenrique.MediasAPI.dtos.ApplicationDTO;
import br.com.gustavohenrique.MediasAPI.dtos.CreateApplicationDTO;
import br.com.gustavohenrique.MediasAPI.model.Application;

public interface ApplicationService {
    ApplicationCreatedDTO create(CreateApplicationDTO dto);
    ApplicationDTO toDTO(Application app);
    void revoke(Application app);
    ApplicationCreatedDTO rotateKey(Application app);
}
