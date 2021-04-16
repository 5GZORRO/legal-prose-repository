package eu._5gzorro.legalproserepository.service;

import eu._5gzorro.legalproserepository.controller.v1.request.ProposeTemplateRequest;
import eu._5gzorro.legalproserepository.dto.LegalProseTemplateDetailDto;
import eu._5gzorro.legalproserepository.dto.LegalProseTemplateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface LegalProseTemplateService {
    Page<LegalProseTemplateDto> getLegalProseTemplates(Pageable pageable, String filterText);
    LegalProseTemplateDetailDto getLegalProseTemplateById(UUID id);
    LegalProseTemplateDetailDto getLegalProseTemplateByDid(String did);

    /**
     * Creates a legal prose template in CREATING state and issues request for a DID
     * @param requestingStakeholderId
     * @param request
     * @param file
     * @return the local id for the template
     */
    UUID createLegalProseTemplate(String requestingStakeholderId, ProposeTemplateRequest request, MultipartFile file);

  /**
   * Complete the process of creating the template by assigning the provided DID to the template.
   * Generates a governance proposal for admins to vote on, the result of the subsequent
   * proposal voting will determine the availability of the template in the marketplace
   * @param id
   * @param did
   */
  void completeTemplateCreation(UUID id, String did);

    /**
     * Update the status of the prose template based on the outcome of the governance process
     * The claim will be used to validate the action prior to any update being performed
     * @param did
     * @param approved
     */
    void setLegalProseTemplateStatus(String did, boolean approved);

    /**
     * Sets the status of a template to ARCHIVE_PROPOSED and generates a governance proposal for admins to vote on
     * The result of the subsequent proposal voting will determine whether the template is archived or reverted to
     * ACTIVE state (i.e. this action is rejected)
     * @param requestingStakeholderId
     * @param did
     * @return the the PROPOSAL Identifier
     */
    String archiveLegalProseTemplate(String requestingStakeholderId, String did);
}
