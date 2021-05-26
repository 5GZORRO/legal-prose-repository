package eu._5gzorro.legalproserepository.repository.specification;

import eu._5gzorro.legalproserepository.model.entity.LegalProseTemplate;
import eu._5gzorro.legalproserepository.model.enumureration.TemplateCategory;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class LegalProseTemplateSpecs {

    public static Specification<LegalProseTemplate> nameContains(String filterText) {
        return (template, ec, cb) -> {

            if( Strings.isBlank(filterText)) {
                return cb.and();
            }

            return cb.like(cb.upper(template.get("name")), "%" + filterText.toUpperCase() + "%");
        };
    }

    public static Specification<LegalProseTemplate> descriptionContains(String filterText) {
        return (template, ec, cb) -> {

            if( Strings.isBlank(filterText)) {
                return cb.and();
            }

            return cb.like(cb.upper(template.get("description")), "%" + filterText.toUpperCase() + "%");
        };
    }

    public static Specification<LegalProseTemplate> categoryIn(List<TemplateCategory> categories) {
        return (template, ec, cb) -> {

            if(categories == null || categories.size() == 0) {
                return cb.and();
            }

            return template.get("category").in(categories);
        };
    }
}
