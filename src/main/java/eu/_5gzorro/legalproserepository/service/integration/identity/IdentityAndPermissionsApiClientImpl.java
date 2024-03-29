package eu._5gzorro.legalproserepository.service.integration.identity;

import eu._5gzorro.legalproserepository.dto.identityPermissions.StakeholderStatusDto;
import eu._5gzorro.legalproserepository.httpClient.DIDClient;
import eu._5gzorro.legalproserepository.httpClient.requests.CreateDidRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class IdentityAndPermissionsApiClientImpl implements IdentityAndPermissionsApiClient {

    @Autowired
    private DIDClient didClient;

    private StakeholderStatusDto myStakeholderStatus;

    @Value("${integrations.identity-permissions.did}")
    String did;

    public IdentityAndPermissionsApiClientImpl() {}

    @Override
    public void createDID(CreateDidRequest request) {
        request.authToken(getAuthToken());
        didClient.create(request);
    }

    private String getAuthToken() {
        return didClient.getMyStakeholderCredential(did).getAuthToken();
    }
}
