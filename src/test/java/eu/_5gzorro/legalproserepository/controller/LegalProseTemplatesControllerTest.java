package eu._5gzorro.legalproserepository.controller;

import eu._5gzorro.legalproserepository.controller.v1.api.LegalProseTemplatesController;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = LegalProseTemplatesController.class)
public class LegalProseTemplatesControllerTest {

    @Autowired
    private MockMvc mvc;

//    @Test
//    void whenPathVariableIsInvalid_thenReturnsStatus400() throws Exception {
//        mvc.perform(get(""))
//                .andExpect(status().isBadRequest());
//    }

//    @Test
//    void whenRequestParameterIsInvalid_thenReturnsStatus400() throws Exception {
//        mvc.perform(get("/api/v1/legal-prose-templates")
//                .param("filterText", "4"))
//                .andExpect(status().isBadRequest());
//    }

}
