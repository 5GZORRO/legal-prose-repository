package eu._5gzorro.legalproserepository.model.exception;

import javax.persistence.EntityNotFoundException;

public class LegalProseTemplateFileNotFoundException extends EntityNotFoundException {

    private final String templateId;

    public LegalProseTemplateFileNotFoundException(String templateId) {
        this.templateId = templateId;
    }

    @Override
    public String getMessage() {
        return String.format("Legal Prose Template File associated with the template with id '%s' not found", templateId);
    }
}
