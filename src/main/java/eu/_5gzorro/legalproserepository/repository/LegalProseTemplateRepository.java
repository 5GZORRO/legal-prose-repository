package eu._5gzorro.legalproserepository.repository;

import eu._5gzorro.legalproserepository.model.entity.LegalProseTemplate;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.UUID;

public interface LegalProseTemplateRepository extends PagingAndSortingRepository<LegalProseTemplate, UUID>, JpaSpecificationExecutor<LegalProseTemplate> {
    Optional<LegalProseTemplate> findByDid(String did);
    boolean existsByDid(String did);
    void deleteByDid(String did);
}
