package eu._5gzorro.legalproserepository;


import eu._5gzorro.legalproserepository.config.Config;
import eu._5gzorro.legalproserepository.controller.v1.request.ProposeTemplateRequest;
import eu._5gzorro.legalproserepository.controller.v1.response.ProposalResponse;
import eu._5gzorro.legalproserepository.dto.LegalProseTemplateDetailDto;
import eu._5gzorro.legalproserepository.dto.LegalProseTemplateDto;
import eu._5gzorro.legalproserepository.model.AuthData;
import eu._5gzorro.legalproserepository.model.entity.LegalProseTemplate;
import eu._5gzorro.legalproserepository.model.entity.LegalProseTemplateFile;
import eu._5gzorro.legalproserepository.model.enumureration.TemplateCategory;
import eu._5gzorro.legalproserepository.model.enumureration.TemplateStatus;
import eu._5gzorro.legalproserepository.model.exception.LegalProseTemplateNotFoundException;
import eu._5gzorro.legalproserepository.model.exception.LegalProseTemplateStatusException;
import eu._5gzorro.legalproserepository.repository.LegalProseTemplateRepository;
import eu._5gzorro.legalproserepository.service.LegalProseTemplateService;
import eu._5gzorro.legalproserepository.service.LegalProseTemplateServiceImpl;
import eu._5gzorro.legalproserepository.service.integration.governance.GovernanceManagerClient;
import eu._5gzorro.legalproserepository.service.integration.governance.GovernanceManagerClientImpl;
import eu._5gzorro.legalproserepository.service.integration.identity.IdentityAndPermissionsApiClient;
import eu._5gzorro.legalproserepository.utils.UuidSource;
import org.apache.commons.lang3.NotImplementedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {LegalProseTemplateServiceImplUnitTests.LegalProseTemplateServiceImplUnitTestContextConfiguration.class })
public class LegalProseTemplateServiceImplUnitTests {

    @TestConfiguration
    static class LegalProseTemplateServiceImplUnitTestContextConfiguration extends Config {
        @Bean
        public LegalProseTemplateService governanceProposalService() {
            return new LegalProseTemplateServiceImpl();
        }
    }

    @Autowired
    private LegalProseTemplateService templateService;

    @MockBean
    private LegalProseTemplateRepository templateRepository;

    @MockBean
    private GovernanceManagerClient governanceManagerClient;

    @MockBean
    private IdentityAndPermissionsApiClient identityClient;

    @MockBean
    private AuthData authData;

    @MockBean
    private UuidSource uuidSource;


    // Get templates
    @Test
    public void getLegalProseTemplates_returnsDtosWithNoFileData() {

        // given
        // Entities in db:
        LegalProseTemplateFile templateFile = new LegalProseTemplateFile();
        templateFile.setData("TEST DATA".getBytes(StandardCharsets.UTF_8));

        UUID id = UUID.randomUUID();

        LegalProseTemplate t1 = new LegalProseTemplate()
                .id(id)
                .name("template 1")
                .description(("description of template 1"))
                .status(TemplateStatus.PROPOSED);

        t1.addFile(templateFile);

        Page<LegalProseTemplate> expectedPage = new PageImpl(List.of(t1));
        Mockito.when(templateRepository.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class))).thenReturn(expectedPage);

        // when
        Page<LegalProseTemplateDto> result = templateService.getLegalProseTemplates(PageRequest.of(0, 10), Collections.emptyList(), "template 1");

        // then
        assertEquals(expectedPage.getSize(), result.getSize());

        for (int i = 0; i < expectedPage.getSize(); i++) {
            assertTrue(expectedPage.getContent().get(i).getId().toString().equals(result.getContent().get(i).getId()));
        }
    }

    // Get template
    @Test
    public void getLegalProseTemplateById_throwsLegalProseTemplateNotFoundException_whenNotFound() {

        // given
        final UUID id = UUID.randomUUID();
        when(templateRepository.findById(id)).thenReturn(Optional.empty());

        // then
        Throwable exception = assertThrows(LegalProseTemplateNotFoundException.class, () -> templateService.getLegalProseTemplateById(id));
    }

    @Test
    public void getLegalProseTemplateById_returnsMatchingTemplateWithFileDataPopulated_whenFound() {

        // given
        final UUID id = UUID.randomUUID();
        byte[] fileBytes = "Test data".getBytes();

        LegalProseTemplateFile fileEntity = new LegalProseTemplateFile();
        fileEntity.setData(fileBytes);

        LegalProseTemplate templateEntity = new LegalProseTemplate()
                .id(id)
                .name("template 2")
                .description(("description of template 2"))
                .status(TemplateStatus.ACTIVE);

        templateEntity.addFile(fileEntity);

        when(templateRepository.findById(id)).thenReturn(Optional.of(templateEntity));

        // then
        LegalProseTemplateDetailDto result = templateService.getLegalProseTemplateById(id);

        assertNotNull(result);
        assertTrue(id.toString().equals(result.getId()));
        assertEquals(fileBytes, result.getTemplateFileData());
    }

    @Test
    public void getLegalProseTemplateByDid_throwsLegalProseTemplateNotFoundException_whenNotFound() {

        // given
        final String did = "did:5gzorro:345nmsdfhbsdjhfb";
        when(templateRepository.findByDid(did)).thenReturn(Optional.empty());

        // then
        Throwable exception = assertThrows(LegalProseTemplateNotFoundException.class, () -> templateService.getLegalProseTemplateByDid(did));
    }

    @Test
    public void getLegalProseTemplateByDid_returnsMatchingTemplateWithFileDataPopulated_whenFound() {

        // given
        final String did = "did:5gzorro:345nmsdfhbsdjhfb";
        byte[] fileBytes = "Test data".getBytes();

        LegalProseTemplateFile fileEntity = new LegalProseTemplateFile();
        fileEntity.setData(fileBytes);

        LegalProseTemplate templateEntity = new LegalProseTemplate()
                .id(UUID.randomUUID())
                .did(did)
                .name("template 2")
                .description(("description of template 2"))
                .status(TemplateStatus.ACTIVE);

        templateEntity.addFile(fileEntity);

        when(templateRepository.findByDid(did)).thenReturn(Optional.of(templateEntity));

        // then
        LegalProseTemplateDetailDto result = templateService.getLegalProseTemplateByDid(did);

        assertNotNull(result);
        assertEquals(did, result.getId());
        assertEquals(fileBytes, result.getTemplateFileData());
    }

    // create
    @Test
    void createLegalProseTemplate_savesATemplate() throws IOException {

        // given
        ProposeTemplateRequest request = new ProposeTemplateRequest();
        request.setName("t1");
        request.setDescription("template 1 description");

        MultipartFile templateFile = new MockMultipartFile("file1.cta", "file contents".getBytes());

        final String requestingStakeholderId = "stakeholder1";
        UUID expectedId = UUID.randomUUID();
        Mockito.when(uuidSource.newUUID()).thenReturn(expectedId);

        // when
        UUID id = templateService.createLegalProseTemplate(requestingStakeholderId, request, templateFile);

        // then
        LegalProseTemplate expectedEntity = new LegalProseTemplate()
                .id(expectedId)
                .name("t1")
                .description(("template 1 description"))
                .status(TemplateStatus.ACTIVE);
//                .status(TemplateStatus.CREATING);

        LegalProseTemplateFile fileEntity = new LegalProseTemplateFile();
        fileEntity.setData(templateFile.getBytes());
        expectedEntity.addFile(fileEntity);

        verify(templateRepository, times(1)).save(expectedEntity);
        assertEquals(expectedId, id);

    }

    // set status
    @Test
    public void setLegalProseTemplateStatus_throwsExceptionIfTemplateNotFound() {

        // given
        final String did = "did:5gzorro:345nmsdfhbsdjhfb";
        when(templateRepository.findByDid(did)).thenReturn(Optional.empty());

        // then
        Throwable exception = assertThrows(LegalProseTemplateNotFoundException.class, () -> templateService.setLegalProseTemplateStatus(did, true));
    }

    @Test
    public void setLegalProseTemplateStatus_throwsExceptionIfNotInProposedOrArchiveProposedState() {

        // given
        final String did1 = "did:5gzorro:345nmsdfhbsdjhfb1";
        LegalProseTemplate t1 = new LegalProseTemplate()
                .id(UUID.randomUUID())
                .did(did1)
                .name("template 1")
                .description(("description of template 1"))
                .status(TemplateStatus.ACTIVE);

        final String did2 = "did:5gzorro:345nmsdfhbsdjhfb2";
        LegalProseTemplate t2 = new LegalProseTemplate()
                .id(UUID.randomUUID())
                .did(did2)
                .name("template 2")
                .description(("description of template 2"))
                .status(TemplateStatus.ARCHIVED);

        final String did3 = "did:5gzorro:345nmsdfhbsdjhfb3";
        LegalProseTemplate t3 = new LegalProseTemplate()
                .id(UUID.randomUUID())
                .did(did3)
                .name("template 3")
                .description(("description of template 3"))
                .status(TemplateStatus.REJECTED);

        when(templateRepository.findByDid(did1)).thenReturn(Optional.of(t1));
        when(templateRepository.findByDid(did2)).thenReturn(Optional.of(t2));
        when(templateRepository.findByDid(did3)).thenReturn(Optional.of(t3));

        // then
        Throwable exception = assertThrows(LegalProseTemplateStatusException.class, () -> templateService.setLegalProseTemplateStatus(did1, true));
        Throwable exception1 = assertThrows(LegalProseTemplateStatusException.class, () -> templateService.setLegalProseTemplateStatus(did2, true));
        Throwable exception2 = assertThrows(LegalProseTemplateStatusException.class, () -> templateService.setLegalProseTemplateStatus(did3, true));
    }

    @Test
    public void setLegalProseTemplateStatus_setsTemplateInProposedStateToActiveWhenApproved() {

        // given
        final String did1 = "did:5gzorro:345nmsdfhbsdjhfb1";
        LegalProseTemplate t1 = new LegalProseTemplate()
                .id(UUID.randomUUID())
                .did(did1)
                .name("template 1")
                .description(("description of template 1"))
                .status(TemplateStatus.PROPOSED);

        when(templateRepository.findByDid(did1)).thenReturn(Optional.of(t1));

        // when
        templateService.setLegalProseTemplateStatus(did1, true);

        final LegalProseTemplate updatedTemplate = t1.status(TemplateStatus.ACTIVE);

        // then
        verify(templateRepository, times(1)).save(updatedTemplate);
    }

    @Test
    public void setLegalProseTemplateStatus_setsTemplateInProposedStateToRejectedWhenNotApproved() {

        // given
        final String did1 = "did:5gzorro:345nmsdfhbsdjhfb1";
        LegalProseTemplate t1 = new LegalProseTemplate()
                .id(UUID.randomUUID())
                .did(did1)
                .name("template 1")
                .description(("description of template 1"))
                .status(TemplateStatus.PROPOSED);

        when(templateRepository.findByDid(did1)).thenReturn(Optional.of(t1));

        // when
        templateService.setLegalProseTemplateStatus(did1, false);

        final LegalProseTemplate updatedTemplate = t1.status(TemplateStatus.REJECTED);

        // then
        verify(templateRepository, times(1)).save(updatedTemplate);
    }

    @Test
    public void setLegalProseTemplateStatus_setsTemplateInArchiveProposedStateToActiveWhenNotApproved() {

        // given
        final String did1 = "did:5gzorro:345nmsdfhbsdjhfb1";
        LegalProseTemplate t1 = new LegalProseTemplate()
                .id(UUID.randomUUID())
                .did(did1)
                .name("template 1")
                .description(("description of template 1"))
                .status(TemplateStatus.ARCHIVE_PROPOSED);

        when(templateRepository.findByDid(did1)).thenReturn(Optional.of(t1));

        // when
        templateService.setLegalProseTemplateStatus(did1, false);

        final LegalProseTemplate updatedTemplate = t1.status(TemplateStatus.ACTIVE);

        // then
        verify(templateRepository, times(1)).save(updatedTemplate);
    }

    @Test
    public void setLegalProseTemplateStatus_setsTemplateInArchiveProposedStateToArchivedWhenApproved() {

        // given
        final String did1 = "did:5gzorro:345nmsdfhbsdjhfb1";
        LegalProseTemplate t1 = new LegalProseTemplate()
                .id(UUID.randomUUID())
                .did(did1)
                .name("template 1")
                .description(("description of template 1"))
                .status(TemplateStatus.ARCHIVE_PROPOSED);

        when(templateRepository.findByDid(did1)).thenReturn(Optional.of(t1));

        // when
        templateService.setLegalProseTemplateStatus(did1, true);

        final LegalProseTemplate updatedTemplate = t1.status(TemplateStatus.ARCHIVED);

        // then
        verify(templateRepository, times(1)).save(updatedTemplate);
    }

    // archive
    @Test
    public void archiveLegalProseTemplate_throwsExceptionIfTemplateNotFound() {

        // given
        final String did = "did:5gzorro:345nmsdfhbsdjhfb1";
        final String requestingStakeholderId = "stakeholder1";

        when(templateRepository.findByDid(did)).thenReturn(Optional.empty());

        // then
        Throwable exception = assertThrows(LegalProseTemplateNotFoundException.class, () -> templateService.archiveLegalProseTemplate(requestingStakeholderId, did));
    }

    @Test
    public void archiveLegalProseTemplate_throwsExceptionIfTemplateNotActive() {

        // given
        final String did = "did:5gzorro:345nmsdfhbsdjhfb1";
        final String requestingStakeholderId = "stakeholder1";
        final LegalProseTemplate t1 = new LegalProseTemplate()
                .id(UUID.randomUUID())
                .did(did)
                .name("template 1")
                .description(("description of template 1"))
                .status(TemplateStatus.PROPOSED);

        when(templateRepository.findByDid(did)).thenReturn(Optional.of(t1));

        // then
        Throwable exception = assertThrows(LegalProseTemplateStatusException.class, () -> templateService.archiveLegalProseTemplate(requestingStakeholderId, did));
    }

    @Test
    public void archiveLegalProseTemplate_setsTemplateInActiveStateToArchiveProposedAndReturnsProposalId() throws IOException {

        // given
        final String did = "did:5gzorro:345nmsdfhbsdjhfb1";
        final String requestingStakeholderId = "stakeholder1";
        final String createdProposalId = "proposal1";

        LegalProseTemplate t1 = new LegalProseTemplate()
                .id(UUID.randomUUID())
                .did(did)
                .name("template 1")
                .description(("description of template 1"))
                .status(TemplateStatus.ACTIVE);

        when(templateRepository.findByDid(did)).thenReturn(Optional.of(t1));
        when(governanceManagerClient.proposeArchiveTemplate(anyString())).thenReturn(createdProposalId);

        // when
        String result = templateService.archiveLegalProseTemplate(requestingStakeholderId, did);

        final LegalProseTemplate updatedTemplate = t1.status(TemplateStatus.ARCHIVE_PROPOSED);

        // then
        verify(templateRepository, times(1)).save(updatedTemplate);
        verify(governanceManagerClient, times(1)).proposeArchiveTemplate(did);
        assertEquals(createdProposalId, result);

    }


    @Test
    public void completeTemplateCreation_throwsExceptionIfTemplateWithIdMatchingTheProvidedHandleNotFound() {

        // given
        final UUID id = UUID.randomUUID();
        final String did = "did:5gzorro:345nmsdfhbsdjhfb1";

        // when
        when(templateRepository.findById(id)).thenReturn(Optional.empty());

        // then
        Throwable exception = assertThrows(LegalProseTemplateNotFoundException.class, () -> templateService.completeTemplateCreation(id, did));
    }

    @Test
    public void completeTemplateCreation_throwsExceptionIfTemplateNotInCreatingState() {

        // given
        final UUID id = UUID.randomUUID();
        final String did = "did:5gzorro:345nmsdfhbsdjhfb1";

        LegalProseTemplate t1 = new LegalProseTemplate()
                .id(id)
                .did(did)
                .name("template 1")
                .description(("description of template 1"))
                .status(TemplateStatus.PROPOSED);

        // when
        when(templateRepository.findById(id)).thenReturn(Optional.of(t1));

        // then
        Throwable exception = assertThrows(LegalProseTemplateStatusException.class, () -> templateService.completeTemplateCreation(id, did));
    }

    @Test
    public void completeTemplateCreation_setsNewIdAndStatusToApporvedAndRequestsAGovProposal() throws IOException {

        // given
        final UUID id = UUID.randomUUID();
        final String did = "did:5gzorro:345nmsdfhbsdjhfb1";

        LegalProseTemplate t1 = new LegalProseTemplate()
                .id(id)
                .did(did)
                .name("template 1")
                .description(("description of template 1"))
                .status(TemplateStatus.CREATING);

        when(templateRepository.findById(id)).thenReturn(Optional.of(t1));

        // when
        templateService.completeTemplateCreation(id, did);

        final LegalProseTemplate updatedTemplate = t1.did(did).status(TemplateStatus.PROPOSED);

        // then
        verify(templateRepository, times(1)).save(updatedTemplate);
        verify(governanceManagerClient, times(1)).proposeNewTemplate(did);
    }
}
