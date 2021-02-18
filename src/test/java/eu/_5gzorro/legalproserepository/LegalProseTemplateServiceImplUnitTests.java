package eu._5gzorro.legalproserepository;


import eu._5gzorro.legalproserepository.config.Config;
import eu._5gzorro.legalproserepository.controller.v1.request.ProposeTemplateRequest;
import eu._5gzorro.legalproserepository.dto.LegalProseTemplateDetailDto;
import eu._5gzorro.legalproserepository.dto.LegalProseTemplateDto;
import eu._5gzorro.legalproserepository.model.entity.LegalProseTemplate;
import eu._5gzorro.legalproserepository.model.entity.LegalProseTemplateFile;
import eu._5gzorro.legalproserepository.model.enumureration.TemplateStatus;
import eu._5gzorro.legalproserepository.model.exception.LegalProseTemplateNotFoundException;
import eu._5gzorro.legalproserepository.model.exception.LegalProseTemplateStatusException;
import eu._5gzorro.legalproserepository.repository.LegalProseTemplateRepository;
import eu._5gzorro.legalproserepository.service.LegalProseTemplateService;
import eu._5gzorro.legalproserepository.service.LegalProseTemplateServiceImpl;
import org.apache.logging.log4j.util.Strings;
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

import javax.swing.plaf.multi.MultiPanelUI;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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


    // Get templates
    @Test
    public void getLegalProseTemplates_returnsMatchingDtosWithNoFileData() {

        // given
        // Entities in db:
        LegalProseTemplateFile templateFile = new LegalProseTemplateFile();
        templateFile.setData("TEST DATA".getBytes(StandardCharsets.UTF_8));

        LegalProseTemplate t1 = new LegalProseTemplate()
                .id("t1")
                .name("template 1")
                .description(("description of template 1"))
                .status(TemplateStatus.PROPOSED);

        t1.addFile(templateFile);

        Page<LegalProseTemplate> expectedPage = new PageImpl(List.of(t1));
        Mockito.when(templateRepository.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class))).thenReturn(expectedPage);

        // when
        Page<LegalProseTemplateDto> result = templateService.getLegalProseTemplates(PageRequest.of(0, 10), "template 1");

        // then
        assertEquals(expectedPage.getSize(), result.getSize());

        for (int i = 0; i < expectedPage.getSize(); i++) {
            assertEquals(expectedPage.getContent().get(i).getId(), result.getContent().get(i).getId());
        }
    }

    // Get template
    @Test
    public void getLegalProseTemplate_throwsLegalProseTemplateNotFoundException_whenNotFound() {

        // given
        final String templateId = "1";
        when(templateRepository.findById(templateId)).thenReturn(Optional.empty());

        // then
        Throwable exception = assertThrows(LegalProseTemplateNotFoundException.class, () -> templateService.getLegalProseTemplate(templateId));
    }

    @Test
    public void getLegalProseTemplate_returnsMatchingTemplateWithFileDataPopulated_whenFound() {

        // given
        final String templateId = "1";
        byte[] fileBytes = "Test data".getBytes();

        LegalProseTemplateFile fileEntity = new LegalProseTemplateFile();
        fileEntity.setData(fileBytes);

        LegalProseTemplate templateEntity = new LegalProseTemplate()
                .id(templateId)
                .name("template 2")
                .description(("description of template 2"))
                .status(TemplateStatus.ACTIVE);

        templateEntity.addFile(fileEntity);

        when(templateRepository.findById(templateId)).thenReturn(Optional.of(templateEntity));

        // then
        LegalProseTemplateDetailDto result = templateService.getLegalProseTemplate(templateId);

        assertNotNull(result);
        assertEquals(templateId, result.getId());
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

        // when
        String proposalId = templateService.createLegalProseTemplate(requestingStakeholderId, request, templateFile);

        // then
        LegalProseTemplate expectedEntity = new LegalProseTemplate()
                .id("t1")
                .name("t1")
                .description(("template 1 description"))
                .status(TemplateStatus.PROPOSED);

        LegalProseTemplateFile fileEntity = new LegalProseTemplateFile();
        fileEntity.setData(templateFile.getBytes());
        expectedEntity.addFile(fileEntity);

        verify(templateRepository, times(1)).save(expectedEntity);

    }




    // set status
    @Test
    public void setLegalProseTemplateStatus_throwsExceptionIfTemplateNotFound() {

        // given
        final String templateId = "1";
        when(templateRepository.findById(templateId)).thenReturn(Optional.empty());

        // then
        Throwable exception = assertThrows(LegalProseTemplateNotFoundException.class, () -> templateService.setLegalProseTemplateStatus(templateId, true));
    }

    @Test
    public void setLegalProseTemplateStatus_throwsExceptionIfNotInProposedOrArchiveProposedState() {

        // given
        LegalProseTemplate t1 = new LegalProseTemplate()
                .id("t1")
                .name("template 1")
                .description(("description of template 1"))
                .status(TemplateStatus.ACTIVE);

        LegalProseTemplate t2 = new LegalProseTemplate()
                .id("t2")
                .name("template 2")
                .description(("description of template 2"))
                .status(TemplateStatus.ARCHIVED);

        LegalProseTemplate t3 = new LegalProseTemplate()
                .id("t3")
                .name("template 3")
                .description(("description of template 3"))
                .status(TemplateStatus.REJECTED);

        when(templateRepository.findById("t1")).thenReturn(Optional.of(t1));
        when(templateRepository.findById("t2")).thenReturn(Optional.of(t2));
        when(templateRepository.findById("t3")).thenReturn(Optional.of(t3));

        // then
        Throwable exception = assertThrows(LegalProseTemplateStatusException.class, () -> templateService.setLegalProseTemplateStatus("t1", true));
        Throwable exception1 = assertThrows(LegalProseTemplateStatusException.class, () -> templateService.setLegalProseTemplateStatus("t2", true));
        Throwable exception2 = assertThrows(LegalProseTemplateStatusException.class, () -> templateService.setLegalProseTemplateStatus("t3", true));
    }

    @Test
    public void setLegalProseTemplateStatus_setsTemplateInProposedStateToActiveWhenApproved() {

        // given
        LegalProseTemplate t1 = new LegalProseTemplate()
                .id("t1")
                .name("template 1")
                .description(("description of template 1"))
                .status(TemplateStatus.PROPOSED);

        when(templateRepository.findById("t1")).thenReturn(Optional.of(t1));

        // when
        templateService.setLegalProseTemplateStatus("t1", true);

        final LegalProseTemplate updatedTemplate = t1.status(TemplateStatus.ACTIVE);

        // then
        verify(templateRepository, times(1)).save(updatedTemplate);
    }

    @Test
    public void setLegalProseTemplateStatus_setsTemplateInProposedStateToRejectedWhenNotApproved() {

        // given
        LegalProseTemplate t1 = new LegalProseTemplate()
                .id("t1")
                .name("template 1")
                .description(("description of template 1"))
                .status(TemplateStatus.PROPOSED);

        when(templateRepository.findById("t1")).thenReturn(Optional.of(t1));

        // when
        templateService.setLegalProseTemplateStatus("t1", false);

        final LegalProseTemplate updatedTemplate = t1.status(TemplateStatus.REJECTED);

        // then
        verify(templateRepository, times(1)).save(updatedTemplate);
    }

    @Test
    public void setLegalProseTemplateStatus_setsTemplateInArchiveProposedStateToActiveWhenNotApproved() {

        // given
        LegalProseTemplate t1 = new LegalProseTemplate()
                .id("t1")
                .name("template 1")
                .description(("description of template 1"))
                .status(TemplateStatus.ARCHIVE_PROPOSED);

        when(templateRepository.findById("t1")).thenReturn(Optional.of(t1));

        // when
        templateService.setLegalProseTemplateStatus("t1", false);

        final LegalProseTemplate updatedTemplate = t1.status(TemplateStatus.ACTIVE);

        // then
        verify(templateRepository, times(1)).save(updatedTemplate);
    }

    @Test
    public void setLegalProseTemplateStatus_setsTemplateInArchiveProposedStateToArchivedWhenApproved() {

        // given
        LegalProseTemplate t1 = new LegalProseTemplate()
                .id("t1")
                .name("template 1")
                .description(("description of template 1"))
                .status(TemplateStatus.ARCHIVE_PROPOSED);

        when(templateRepository.findById("t1")).thenReturn(Optional.of(t1));

        // when
        templateService.setLegalProseTemplateStatus("t1", true);

        final LegalProseTemplate updatedTemplate = t1.status(TemplateStatus.ARCHIVED);

        // then
        verify(templateRepository, times(1)).save(updatedTemplate);
    }

    // archive
    @Test
    public void archiveLegalProseTemplate_throwsExceptionIfTemplateNotFound() {

        // given
        final String templateId = "1";
        final String requestingStakeholderId = "stakeholder1";

        when(templateRepository.findById(templateId)).thenReturn(Optional.empty());

        // then
        Throwable exception = assertThrows(LegalProseTemplateNotFoundException.class, () -> templateService.archiveLegalProseTemplate(requestingStakeholderId, templateId));
    }

    @Test
    public void archiveLegalProseTemplate_throwsExceptionIfTemplateNotActive() {

        // given
        final String templateId = "1";
        final String requestingStakeholderId = "stakeholder1";
        final LegalProseTemplate t1 = new LegalProseTemplate()
                .id("t1")
                .name("template 1")
                .description(("description of template 1"))
                .status(TemplateStatus.PROPOSED);

        when(templateRepository.findById(templateId)).thenReturn(Optional.of(t1));

        // then
        Throwable exception = assertThrows(LegalProseTemplateStatusException.class, () -> templateService.archiveLegalProseTemplate(requestingStakeholderId, templateId));
    }

    @Test
    public void archiveLegalProseTemplate_setsTemplateInActiveStateToArchiveProposedAndReturnsProposalId() {

        // given
        final String templateId = "t1";
        final String requestingStakeholderId = "stakeholder1";
        LegalProseTemplate t1 = new LegalProseTemplate()
                .id("t1")
                .name("template 1")
                .description(("description of template 1"))
                .status(TemplateStatus.ACTIVE);

        when(templateRepository.findById(templateId)).thenReturn(Optional.of(t1));

        // when
        String result = templateService.archiveLegalProseTemplate(requestingStakeholderId, templateId);

        final LegalProseTemplate updatedTemplate = t1.status(TemplateStatus.ARCHIVE_PROPOSED);

        // then
        verify(templateRepository, times(1)).save(updatedTemplate);
        assertTrue(Strings.isNotBlank(result));
    }

}
