package eu._5gzorro.legalproserepository;

import eu._5gzorro.legalproserepository.model.entity.LegalProseTemplate;
import eu._5gzorro.legalproserepository.model.enumureration.TemplateStatus;
import eu._5gzorro.legalproserepository.repository.LegalProseTemplateRepository;
import eu._5gzorro.legalproserepository.repository.specification.LegalProseTemplateSpecs;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class LegalProseTemplatRespositoryIntegrationTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LegalProseTemplateRepository templateRepository;

    @Test
    void whenFilterTextNotEmpty_returnsRecordThatHaveNameOrDescriptionContainingFilterValue() {
        LegalProseTemplate t1 = new LegalProseTemplate()
                .id("t1")
                .name("template 1")
                .description(("description of template 1"))
                .status(TemplateStatus.PROPOSED);

        LegalProseTemplate t2 = new LegalProseTemplate()
                .id("t2")
                .name("template 2")
                .description(("description of template 2"))
                .status(TemplateStatus.ACTIVE);

        // given
        List<LegalProseTemplate> templates = List.of(t1, t2);
        templates.forEach(m -> entityManager.persist(m));
        entityManager.flush();

        // when
        String filterText = "1";

        Pageable page = PageRequest.of(0, 10);
        Specification<LegalProseTemplate> spec = Specification
                .where(LegalProseTemplateSpecs.nameContains(filterText))
                .or(LegalProseTemplateSpecs.descriptionContains(filterText));

        Page<LegalProseTemplate> found = templateRepository.findAll(spec, page);

        // then
        assertEquals(1, found.getTotalElements());
        assertEquals(t1, found.getContent().get(0));
    }

    @Test
    void whenFilterTextEmpty_returnsUnfilteredResults() {
        LegalProseTemplate t1 = new LegalProseTemplate()
                .id("t1")
                .name("template 1")
                .description(("description of template 1"))
                .status(TemplateStatus.PROPOSED);

        LegalProseTemplate t2 = new LegalProseTemplate()
                .id("t2")
                .name("template 2")
                .description(("description of template 2"))
                .status(TemplateStatus.ACTIVE);

        // given
        List<LegalProseTemplate> templates = List.of(t1, t2);
        templates.forEach(m -> entityManager.persist(m));
        entityManager.flush();

        // when
        String filterText = "";

        Pageable page = PageRequest.of(0, 10);
        Specification<LegalProseTemplate> spec = Specification
                .where(LegalProseTemplateSpecs.nameContains(filterText))
                .or(LegalProseTemplateSpecs.descriptionContains(filterText));

        Page<LegalProseTemplate> found = templateRepository.findAll(spec, page);

        // then
        assertEquals(2, found.getTotalElements());
    }

}
