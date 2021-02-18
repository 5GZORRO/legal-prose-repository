package eu._5gzorro.legalproserepository.service;

import eu._5gzorro.legalproserepository.controller.v1.request.ProposeTemplateRequest;
import eu._5gzorro.legalproserepository.dto.LegalProseTemplateDetailDto;
import eu._5gzorro.legalproserepository.dto.LegalProseTemplateDto;
import eu._5gzorro.legalproserepository.model.entity.LegalProseTemplate;
import eu._5gzorro.legalproserepository.model.entity.LegalProseTemplateFile;
import eu._5gzorro.legalproserepository.model.enumureration.TemplateStatus;
import eu._5gzorro.legalproserepository.model.exception.LegalProseTemplateFileNotFoundException;
import eu._5gzorro.legalproserepository.model.exception.LegalProseTemplateIOException;
import eu._5gzorro.legalproserepository.model.exception.LegalProseTemplateNotFoundException;
import eu._5gzorro.legalproserepository.model.exception.LegalProseTemplateStatusException;
import eu._5gzorro.legalproserepository.model.mapper.LegalProseTemplateMapper;
import eu._5gzorro.legalproserepository.repository.LegalProseTemplateFileRepository;
import eu._5gzorro.legalproserepository.repository.LegalProseTemplateRepository;
import eu._5gzorro.legalproserepository.repository.specification.LegalProseTemplateSpecs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class LegalProseTemplateServiceImpl implements LegalProseTemplateService {

    private static final Logger log = LogManager.getLogger(LegalProseTemplateServiceImpl.class);

    @Autowired
    LegalProseTemplateRepository templateRepository;

    @Autowired
    LegalProseTemplateFileRepository templateFileRepository;

    @Autowired
    ModelMapper mapper;

    @Override
    public Page<LegalProseTemplateDto> getLegalProseTemplates(Pageable pageable, String filterText) {

        Specification<LegalProseTemplate> spec = Specification
                .where(LegalProseTemplateSpecs.nameContains(filterText))
                .or(LegalProseTemplateSpecs.descriptionContains(filterText));

        return templateRepository.findAll(spec, pageable)
            .map(template -> mapper.map(template, LegalProseTemplateDto.class));

    }

    @Override
    public LegalProseTemplateDetailDto getLegalProseTemplate(String templateId) {
        LegalProseTemplate template = templateRepository.findById(templateId)
                .orElseThrow(() -> new LegalProseTemplateNotFoundException(templateId));

        return LegalProseTemplateMapper.toLegalProseTemplateDetailDto(template);
    }

    @Override
    @Transactional
    public String createLegalProseTemplate(String requestingStakeholderId, ProposeTemplateRequest request, MultipartFile file) {

        // TODO: Generate a DID for the template
        String templateId = UUID.randomUUID().toString();

        LegalProseTemplate template = new LegalProseTemplate()
                .id(templateId)
                .name(request.getName())
                .description(request.getDescription())
                .status(TemplateStatus.PROPOSED);

        try {
            LegalProseTemplateFile templateFile = new LegalProseTemplateFile();
            templateFile.setData(file.getBytes());
            template.addFile(templateFile);
        }
        catch (IOException e) {
            log.error("Error creating prose template", e);
            throw new LegalProseTemplateIOException(e);
        }

        templateRepository.save(template);

        //TODO: Create a governance proposal

        String proposalId = "NewProposalId";
        return proposalId;
    }

    @Override
    @Transactional
    public void setLegalProseTemplateStatus(String templateId, boolean approved) {

        //TODO: Verify Governance decision claim

        LegalProseTemplate template = templateRepository.findById(templateId)
                .orElseThrow(() -> new LegalProseTemplateNotFoundException(templateId));

        switch (template.getStatus()) {
            case PROPOSED:
                template.status(approved ? TemplateStatus.ACTIVE : TemplateStatus.REJECTED);
                break;
            case ARCHIVE_PROPOSED:
                template.status(approved ? TemplateStatus.ARCHIVED : TemplateStatus.ACTIVE);
                break;
            default:
                log.error(String.format("Attempted to approve/reject a template not in PROPOSED/ARCHIVE_PROPOSED state with id %s.  Actual state: %s", template.getId(), template.getStatus()));

                throw new LegalProseTemplateStatusException(
                  List.of(TemplateStatus.PROPOSED, TemplateStatus.ARCHIVE_PROPOSED),
                  template.getStatus());
          }

        templateRepository.save(template);
    }

    @Override
    @Transactional
    public String archiveLegalProseTemplate(String requestingStakeholderId, String templateId) {

        LegalProseTemplate template = templateRepository.findById(templateId)
                .orElseThrow(() -> new LegalProseTemplateNotFoundException(templateId));

        if (template.getStatus() != TemplateStatus.ACTIVE) {
            log.error(
                String.format(
                  "Attempted to archive a template not in ACTIVE state with id %s.  Actual state: %s",
                  template.getId(), template.getStatus()));
            throw new LegalProseTemplateStatusException(TemplateStatus.ACTIVE, template.getStatus());
        }

        template.status(TemplateStatus.ARCHIVE_PROPOSED);
        templateRepository.save(template);

        //TODO: Create a governance proposal

        String proposalId = "NewProposalId";
        return proposalId;
    }
}
