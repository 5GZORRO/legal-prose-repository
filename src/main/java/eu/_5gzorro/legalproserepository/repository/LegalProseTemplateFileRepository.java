package eu._5gzorro.legalproserepository.repository;

import eu._5gzorro.legalproserepository.model.entity.LegalProseTemplateFile;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LegalProseTemplateFileRepository extends CrudRepository<LegalProseTemplateFile, Long> {
    Optional<LegalProseTemplateFile> findByLegalProseTemplateId(String templateId);
}
