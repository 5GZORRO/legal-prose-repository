package eu._5gzorro.legalproserepository.model.exception;

import java.io.IOException;

public class LegalProseTemplateIOException extends RuntimeException {

    private final IOException exception;

    public LegalProseTemplateIOException(IOException exception) {
        this.exception = exception;
    }

    @Override
    public String getMessage() {
        return String.format("Error occurred reading template file contents.  Error was: %s", exception.toString());
    }
}
