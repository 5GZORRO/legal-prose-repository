package eu._5gzorro.legalproserepository.model.exception;

import eu._5gzorro.legalproserepository.model.enumureration.TemplateStatus;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class LegalProseTemplateStatusException extends RuntimeException {

    private final Set<TemplateStatus> permittedStatuses;
    private final TemplateStatus actualStatus;

    public LegalProseTemplateStatusException(Collection<TemplateStatus> permittedStatuses, TemplateStatus actualStatus) {
        this.permittedStatuses = new HashSet<>(permittedStatuses);
        this.actualStatus = actualStatus;
    }

    public LegalProseTemplateStatusException(TemplateStatus permittedStatus, TemplateStatus actualStatus) {
        this(new HashSet<>(Collections.singleton(permittedStatus)), actualStatus);
    }

    @Override
    public String getMessage() {
        return String.format("This operation is not permitted on a Legal Prose Template in %s state. Permitted states: %s", actualStatus, permittedStatuses);
    }
}
