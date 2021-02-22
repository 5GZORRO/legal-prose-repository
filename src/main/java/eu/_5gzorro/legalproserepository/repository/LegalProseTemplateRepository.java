package eu._5gzorro.legalproserepository.repository;

import eu._5gzorro.legalproserepository.model.entity.LegalProseTemplate;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface LegalProseTemplateRepository extends PagingAndSortingRepository<LegalProseTemplate, String>, JpaSpecificationExecutor<LegalProseTemplate> {
}
