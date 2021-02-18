package eu._5gzorro.legalproserepository.repository.specification;

import eu._5gzorro.legalproserepository.model.entity.LegalProseTemplate;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.domain.Specification;

public class LegalProseTemplateSpecs {

    public static Specification<LegalProseTemplate> nameContains(String filterText) {
        return (proposal, ec, cb) -> {

            if( Strings.isBlank(filterText)) {
                return cb.and();
            }

            return cb.like(cb.upper(proposal.get("name")), "%" + filterText.toUpperCase() + "%");
        };
    }

    public static Specification<LegalProseTemplate> descriptionContains(String filterText) {
        return (proposal, ec, cb) -> {

            if( Strings.isBlank(filterText)) {
                return cb.and();
            }

            return cb.like(cb.upper(proposal.get("description")), "%" + filterText.toUpperCase() + "%");
        };
    }
}
