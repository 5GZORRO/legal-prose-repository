package eu._5gzorro.legalproserepository.service;

import eu._5gzorro.legalproserepository.controller.v1.request.ProposeTemplateRequest;
import eu._5gzorro.legalproserepository.dto.LegalProseTemplateDetailDto;
import eu._5gzorro.legalproserepository.dto.LegalProseTemplateDto;
import eu._5gzorro.legalproserepository.httpClient.requests.CreateDidRequest;
import eu._5gzorro.legalproserepository.model.entity.LegalProseTemplate;
import eu._5gzorro.legalproserepository.model.entity.LegalProseTemplateFile;
import eu._5gzorro.legalproserepository.model.enumureration.CredentialRequestType;
import eu._5gzorro.legalproserepository.model.enumureration.TemplateCategory;
import eu._5gzorro.legalproserepository.model.enumureration.TemplateStatus;
import eu._5gzorro.legalproserepository.model.exception.*;
import eu._5gzorro.legalproserepository.model.mapper.LegalProseTemplateMapper;
import eu._5gzorro.legalproserepository.repository.LegalProseTemplateRepository;
import eu._5gzorro.legalproserepository.repository.specification.LegalProseTemplateSpecs;
import eu._5gzorro.legalproserepository.service.integration.identity.IdentityAndPermissionsApiClient;
import eu._5gzorro.legalproserepository.utils.UuidSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class LegalProseTemplateServiceImpl implements LegalProseTemplateService {

    private static final Logger log = LogManager.getLogger(LegalProseTemplateServiceImpl.class);

    @Autowired
    LegalProseTemplateRepository templateRepository;

    @Autowired
    IdentityAndPermissionsApiClient identityClient;

    @Autowired
    UuidSource uuidSource;

    @Value("${callbacks.updateTemplateIdentity}")
    private String updateTemplateIdentityCallbackUrl;

    @Override
    public Page<LegalProseTemplateDto> getLegalProseTemplates(Pageable pageable, List<TemplateCategory> categoryFilter, String filterText) {

        Specification<LegalProseTemplate> spec = Specification
                .where(LegalProseTemplateSpecs.categoryIn(categoryFilter))
                .and(LegalProseTemplateSpecs.nameContains(filterText)
                        .or(LegalProseTemplateSpecs.descriptionContains(filterText))
                );

        return templateRepository.findAll(spec, pageable)
            .map(template -> LegalProseTemplateMapper.toLegalProseTemplateDto(template));

    }

    @Override
    public LegalProseTemplateDetailDto getLegalProseTemplateByDid(String did) {
        LegalProseTemplate template = templateRepository.findByDid(did)
                .orElseThrow(() -> new LegalProseTemplateNotFoundException(did));

        return LegalProseTemplateMapper.toLegalProseTemplateDetailDto(template);
    }

    @Override
    public LegalProseTemplateDetailDto getLegalProseTemplateById(UUID id) {
        LegalProseTemplate template = templateRepository.findById(id)
                .orElseThrow(() -> new LegalProseTemplateNotFoundException(id.toString()));

        return LegalProseTemplateMapper.toLegalProseTemplateDetailDto(template);
    }

    @Override
    public UUID createLegalProseTemplate(String requestingStakeholderId, ProposeTemplateRequest request, MultipartFile file) {

        UUID id = uuidSource.newUUID();

        LegalProseTemplate template = new LegalProseTemplate()
                .id(id)
                .name(request.getName())
                .category(request.getCategory())
                .description(request.getDescription());

        try {
            LegalProseTemplateFile templateFile = new LegalProseTemplateFile();
            templateFile.setData(file.getBytes());
            template.addFile(templateFile);
        } catch (IOException e) {
            log.error("Error creating prose template", e);
            throw new LegalProseTemplateIOException(e);
        }

        templateRepository.save(template);

        try {
            String callbackUrl = String.format(updateTemplateIdentityCallbackUrl, id);
            CreateDidRequest didRequest = new CreateDidRequest()
                    .callbackUrl(callbackUrl)
                    .claims(Collections.emptyList())
                    .type(CredentialRequestType.LegalProseTemplate);

            identityClient.createDID(didRequest);
        }
        catch (Exception ex) {
            throw new DIDCreationException(ex);
        }

        return id;
    }

    @Deprecated
    @Override
    @Transactional
    public void setLegalProseTemplateStatus(String did, boolean approved) {

        //TODO: Verify Governance decision claim

        LegalProseTemplate template = templateRepository.findByDid(did)
                .orElseThrow(() -> new LegalProseTemplateNotFoundException(did));

        switch (template.getStatus()) {
            case PROPOSED:
                template.status(approved ? TemplateStatus.ACTIVE : TemplateStatus.REJECTED);
                break;
            case ARCHIVE_PROPOSED:
                template.status(approved ? TemplateStatus.ARCHIVED : TemplateStatus.ACTIVE);
                break;
            default:
                log.error(String.format("Attempted to approve/reject a template not in PROPOSED/ARCHIVE_PROPOSED state with did %s.  Actual state: %s", template.getDid(), template.getStatus()));

                throw new LegalProseTemplateStatusException(
                  List.of(TemplateStatus.PROPOSED, TemplateStatus.ARCHIVE_PROPOSED),
                  template.getStatus());
          }

        templateRepository.save(template);
    }

    @Override
    @Transactional
    public void archiveLegalProseTemplate(String requestingStakeholderId, String did) {

        LegalProseTemplate template = templateRepository.findByDid(did)
                .orElseThrow(() -> new LegalProseTemplateNotFoundException(did));

        if (template.getStatus() != TemplateStatus.ACTIVE) {
            log.error(
                String.format(
                  "Attempted to archive a template not in ACTIVE state with did %s.  Actual state: %s",
                  template.getDid(), template.getStatus()));
            throw new LegalProseTemplateStatusException(TemplateStatus.ACTIVE, template.getStatus());
        }

        template.status(TemplateStatus.ARCHIVED);
        templateRepository.save(template);
    }

    @Override
    @Transactional
    public void completeTemplateCreation(UUID id, String did) {

        log.info("Updating LP Template {} with DID {}", id, did);

        LegalProseTemplate template = templateRepository.findById(id)
                .orElseThrow(() -> new LegalProseTemplateNotFoundException(id.toString()));

        if(template.getStatus() != TemplateStatus.CREATING)
            throw new LegalProseTemplateStatusException(TemplateStatus.CREATING, template.getStatus());

        template = template.did(did).status(TemplateStatus.ACTIVE);

        templateRepository.save(template);
    }
}
