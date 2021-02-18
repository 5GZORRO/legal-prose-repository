package eu._5gzorro.legalproserepository.service;

import eu._5gzorro.legalproserepository.controller.v1.request.ProposeTemplateRequest;
import eu._5gzorro.legalproserepository.dto.LegalProseTemplateDetailDto;
import eu._5gzorro.legalproserepository.dto.LegalProseTemplateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface LegalProseTemplateService {
    Page<LegalProseTemplateDto> getLegalProseTemplates(Pageable pageable, String filterText);
    LegalProseTemplateDetailDto getLegalProseTemplate(String templateId);

    /**
     * Creates a legal prose template in PROPOSED state and generates a governance proposal for admins to vote on
     * The result of the subsequent proposal voting will determine the availa``bility of the template in the marketplace
     * @param requestingStakeholderId
     * @param request
     * @param file
     * @return the PROPOSAL Identifier
     */
    String createLegalProseTemplate(String requestingStakeholderId, ProposeTemplateRequest request, MultipartFile file);

    /**
     * Update the status of the prose template based on the outcome of the governance process
     * The claim will be used to validate the action prior to any update being performed
     * @param templateId
     * @param approved
     */
    void setLegalProseTemplateStatus(String templateId, boolean approved);

    /**
     * Sets the status of a template to ARCHIVE_PROPOSED and generates a governance proposal for admins to vote on
     * The result of the subsequent proposal voting will determine whether the template is archived or reverted to
     * ACTIVE state (i.e. this action is rejected)
     * @param requestingStakeholderId
     * @param templateId
     * @return the the PROPOSAL Identifier
     */
    String archiveLegalProseTemplate(String requestingStakeholderId, String templateId);
}
