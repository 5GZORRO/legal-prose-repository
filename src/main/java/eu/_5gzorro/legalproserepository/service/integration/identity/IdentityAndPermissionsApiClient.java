package eu._5gzorro.legalproserepository.service.integration.identity;

public interface IdentityAndPermissionsApiClient {
    void createDID(String callbackUrl, String authToken);
}
