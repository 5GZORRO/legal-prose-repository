package eu._5gzorro.legalproserepository.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu._5gzorro.legalproserepository.controller.v1.api.LegalProseTemplatesController;
import eu._5gzorro.legalproserepository.controller.v1.request.ProposeTemplateRequest;
import eu._5gzorro.legalproserepository.model.AuthData;
import eu._5gzorro.legalproserepository.service.LegalProseTemplateService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.MimeTypeUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = LegalProseTemplatesController.class)
public class LegalProseTemplatesControllerTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    protected ObjectMapper mapper;

    @MockBean
    private LegalProseTemplateService templateService;

    @MockBean
    AuthData authData;

    @Test
    public void proposeLegalProseTemplate_OK() throws Exception {
        final ProposeTemplateRequest request = new ProposeTemplateRequest();
        request.setName("File name");
        request.setDescription("This is a file");

        final MockMultipartFile model = getFileModel(request);
        final MockMultipartFile file = createDummyFile();

        UUID expectedId = UUID.randomUUID();
        String stakeholderId = "stakeholder1";
        Mockito.when(templateService.createLegalProseTemplate(stakeholderId, request, file)).thenReturn(expectedId);
        Mockito.when(authData.getUserId()).thenReturn(stakeholderId);

        final MvcResult result = mvc.perform(
                MockMvcRequestBuilders.multipart("/api/v1/legal-prose-templates")
                        .file(file)
                        .file(model))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        final String response = result.getResponse().getContentAsString();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(expectedId.toString(), response);
    }

    private MockMultipartFile getFileModel(final ProposeTemplateRequest request) throws JsonProcessingException {
        return new MockMultipartFile("proposeTemplateRequest", "", "application/json", mapper.writeValueAsString(request).getBytes());
    }

    private MockMultipartFile createDummyFile() throws IOException {
        String fileName = "data/dummy-accord-template.cta";
        ClassLoader classLoader = getClass().getClassLoader();

        File file = new File(classLoader.getResource(fileName).getFile());

        return new MockMultipartFile("templateFile", "dummy-accord-template.cta", MimeTypeUtils.ALL_VALUE, Files.readAllBytes(file.toPath()));
    }
}
