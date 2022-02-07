package eu._5gzorro.legalproserepository.controller.v1.api;

import eu._5gzorro.legalproserepository.controller.v1.request.ProposeTemplateRequest;
import eu._5gzorro.legalproserepository.controller.v1.response.PagedTemplateResponse;
import eu._5gzorro.legalproserepository.dto.LegalProseTemplateDetailDto;
import eu._5gzorro.legalproserepository.dto.LegalProseTemplateDto;
import eu._5gzorro.legalproserepository.dto.identityPermissions.*;
import eu._5gzorro.legalproserepository.model.AuthData;
import eu._5gzorro.legalproserepository.model.enumureration.TemplateCategory;
import eu._5gzorro.legalproserepository.service.LegalProseTemplateService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
public class LegalProseTemplatesControllerImpl implements LegalProseTemplatesController {

    private static final Logger log = LogManager.getLogger(LegalProseTemplatesControllerImpl.class);

    @Autowired
    LegalProseTemplateService templateService;

    @Autowired
    AuthData authData;


    public ResponseEntity<PagedTemplateResponse> getLegalProseTemplates(final Pageable pageable, final List<TemplateCategory> categoryFilter, final String filterText) {

        Page<LegalProseTemplateDto> page = templateService.getLegalProseTemplates(pageable, categoryFilter, filterText);
        PagedTemplateResponse responseBody = new PagedTemplateResponse(page);

        return ResponseEntity
                .ok()
                .body(responseBody);
    }

    public ResponseEntity<LegalProseTemplateDetailDto> getLegalProseTemplate(final String identifier) {

        LegalProseTemplateDetailDto template;

        try {
            UUID id = UUID.fromString(identifier);
            template = templateService.getLegalProseTemplateById(id);
        }
        catch(IllegalArgumentException e) {
            template = templateService.getLegalProseTemplateByDid(identifier);
        }

        return ResponseEntity
                .ok()
                .body(template);
    }

    public ResponseEntity<String> proposeNewLegalProseTemplate(final ProposeTemplateRequest proposeTemplateRequest, final MultipartFile templateFile) {

        final String requestingStakeholderId = authData.getUserId();

        UUID templateId = templateService.createLegalProseTemplate(requestingStakeholderId, proposeTemplateRequest, templateFile);

        return ResponseEntity.ok().body(templateId.toString());
    }

    public ResponseEntity<Void> updateTemplateIdentity(final UUID id, final DIDStateCSDto state) {

        CredentialSubjectDto credentialSubjectDto = state.getCredentialSubjectDto();
        if(credentialSubjectDto == null)
            return ResponseEntity.badRequest().build();

        String did = credentialSubjectDto.getId();

        if(did == null)
            return ResponseEntity.badRequest().build();

        templateService.completeTemplateCreation(id, did);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> setLegalStatementTemplateApprovalStatus(final String did, final boolean accept) {

        templateService.setLegalProseTemplateStatus(did, accept);

        return ResponseEntity
                .ok()
                .build();
    }

    public ResponseEntity<Void> removeLegalProseTemplate(String id) {

        final String requestingStakeholderId = authData.getUserId();

        templateService.archiveLegalProseTemplate(requestingStakeholderId, id);

        return ResponseEntity.ok().build();
    }
}