package eu._5gzorro.legalproserepository.service.integration.identity;

import eu._5gzorro.legalproserepository.httpClient.requests.CreateDidRequest;

public interface IdentityAndPermissionsApiClient {
    void createDID(CreateDidRequest request);
}
