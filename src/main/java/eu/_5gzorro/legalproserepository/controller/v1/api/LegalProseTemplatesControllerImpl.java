package eu._5gzorro.legalproserepository.controller.v1.api;

import eu._5gzorro.legalproserepository.controller.v1.request.ProposeTemplateRequest;
import eu._5gzorro.legalproserepository.controller.v1.response.PagedTemplateResponse;
import eu._5gzorro.legalproserepository.controller.v1.response.ProposalResponse;
import eu._5gzorro.legalproserepository.dto.LegalProseTemplateDetailDto;
import eu._5gzorro.legalproserepository.dto.LegalProseTemplateDto;
import eu._5gzorro.legalproserepository.dto.identityPermissions.DIDStateDto;
import eu._5gzorro.legalproserepository.model.AuthData;
import eu._5gzorro.legalproserepository.service.LegalProseTemplateService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.UUID;

@RestController
public class LegalProseTemplatesControllerImpl implements LegalProseTemplatesController {

    private static final Logger log = LogManager.getLogger(LegalProseTemplatesControllerImpl.class);

    @Autowired
    LegalProseTemplateService templateService;

    @Autowired
    AuthData authData;


    public ResponseEntity<PagedTemplateResponse> getLegalProseTemplates(final Pageable pageable, final String filterText) {

        Page<LegalProseTemplateDto> page = templateService.getLegalProseTemplates(pageable, filterText);
        PagedTemplateResponse responseBody = new PagedTemplateResponse(page);

        return ResponseEntity
                .ok()
                .body(responseBody);
    }

    public ResponseEntity<LegalProseTemplateDetailDto> getLegalProseTemplate(final String id) {

        LegalProseTemplateDetailDto template = templateService.getLegalProseTemplate(id);

        return ResponseEntity
                .ok()
                .body(template);
    }

    public ResponseEntity<UUID> proposeNewLegalProseTemplate(final ProposeTemplateRequest proposeTemplateRequest, final MultipartFile templateFile) {

        final String requestingStakeholderId = authData.getUserId();

        UUID templateHandle = templateService.createLegalProseTemplate(requestingStakeholderId, proposeTemplateRequest, templateFile);

        return ResponseEntity
                .ok()
                .body(templateHandle);
    }

    public ResponseEntity<Void> updateTemplateIdentity(final UUID templateHandle, final DIDStateDto state) {
        templateService.completeTemplateCreation(templateHandle, state.getDid());

        return ResponseEntity
                .ok()
                .build();
    }

    public ResponseEntity<Void> setLegalStatementTemplateApprovalStatus(final String id, final boolean accept) {

        templateService.setLegalProseTemplateStatus(id, accept);

        return ResponseEntity
                .ok()
                .build();
    }

    public ResponseEntity<String> removeLegalProseTemplate(String id) {

        final String requestingStakeholderId = authData.getUserId();

        String proposalId = templateService.archiveLegalProseTemplate(requestingStakeholderId, id);

        return ResponseEntity
                .ok()
                .body(proposalId);
    }
}