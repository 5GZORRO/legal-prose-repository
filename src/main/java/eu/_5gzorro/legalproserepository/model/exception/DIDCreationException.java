package eu._5gzorro.legalproserepository.model.exception;

public class DIDCreationException extends RuntimeException {

    public DIDCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DIDCreationException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getMessage() {
        return String.format("An error occurred generating an identity", this.getCause());
    }
}
