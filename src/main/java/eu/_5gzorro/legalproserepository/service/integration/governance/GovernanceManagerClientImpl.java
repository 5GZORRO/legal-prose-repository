package eu._5gzorro.legalproserepository.service.integration.governance;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu._5gzorro.legalproserepository.model.AuthData;
import eu._5gzorro.legalproserepository.service.integration.governance.dto.ActionParamsDto;
import eu._5gzorro.legalproserepository.service.integration.governance.dto.ProposeGovernanceDecisionRequest;
import eu._5gzorro.legalproserepository.service.integration.governance.enumeration.GovernanceActionType;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GovernanceManagerClientImpl implements GovernanceManagerClient {

    private static final Logger log = LogManager.getLogger(GovernanceManagerClientImpl.class);

    @Autowired
    AuthData authData;

    @Autowired
    CloseableHttpClient httpClient;

    @Value("${integrations.governanceManager.apiBaseUrl}")
    private String baseUrl;

    @Override
    public String proposeNewTemplate(String legalProseTemplateId) throws IOException {
        return sendGovernanceProposal(legalProseTemplateId, GovernanceActionType.NEW_LEGAL_PROSE_TEMPLATE);
    }

    @Override
    public String proposeArchiveTemplate(String legalProseTemplateId) throws IOException {
        return sendGovernanceProposal(legalProseTemplateId, GovernanceActionType.ARCHIVE_LEGAL_PROSE_TEMPLATE);
    }

    private String sendGovernanceProposal(String legalProseTemplateId, GovernanceActionType actionType) throws IOException {

        String url = baseUrl + "/governance-actions";
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("Accept", "application/json");

        ObjectMapper objectMapper = new ObjectMapper();

        ActionParamsDto actionParams = new ActionParamsDto();
        actionParams.setEntityIdentityId(legalProseTemplateId);

        ProposeGovernanceDecisionRequest request = new ProposeGovernanceDecisionRequest();
        request.setActionType(actionType);
        request.setActionParams(actionParams);

        httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(request),
                ContentType.APPLICATION_JSON));

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {

            StatusLine status = response.getStatusLine();

            if(status.getStatusCode() == HttpStatus.SC_ACCEPTED) {
                return EntityUtils.toString(response.getEntity());
            }
            else {
                throw new HttpResponseException(status.getStatusCode(), status.getReasonPhrase());
            }
        }
    }


}
