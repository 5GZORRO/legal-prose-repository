package eu._5gzorro.legalproserepository.service.integration.governance;

import java.io.IOException;

public interface GovernanceManagerClient {
    /**
     * Make a request to the governance manager to propose a new Legal Prose Template for use in the marketplace
     * @param legalProseTemplateId
     * @return proposalId (the DID for the created proposal)
     * @throws IOException
     */
    String proposeNewTemplate(String legalProseTemplateId) throws IOException;

    /**
     * Make a request to the governance manager to propose the removal (archiving) of a legal prose template
     * @param legalProseTemplateId
     * @return proposalId (the DID for the created proposal)
     * @throws IOException
     */
    String proposeArchiveTemplate(String legalProseTemplateId) throws IOException;
}
