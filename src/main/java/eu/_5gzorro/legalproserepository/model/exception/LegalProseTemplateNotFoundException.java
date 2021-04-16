package eu._5gzorro.legalproserepository.model.exception;

import javax.persistence.EntityNotFoundException;

public class LegalProseTemplateNotFoundException extends EntityNotFoundException {

    private final String id;

    public LegalProseTemplateNotFoundException(String id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return String.format("Legal Prose Template with identifier '%s' not found", id);
    }
}
