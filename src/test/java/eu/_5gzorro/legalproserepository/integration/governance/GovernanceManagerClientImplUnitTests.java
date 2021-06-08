package eu._5gzorro.legalproserepository.integration.governance;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu._5gzorro.legalproserepository.config.Config;
import eu._5gzorro.legalproserepository.model.AuthData;
import eu._5gzorro.legalproserepository.service.integration.governance.GovernanceManagerClient;
import eu._5gzorro.legalproserepository.service.integration.governance.GovernanceManagerClientImpl;
import eu._5gzorro.legalproserepository.service.integration.governance.dto.ProposeGovernanceDecisionRequest;
import eu._5gzorro.legalproserepository.service.integration.governance.enumeration.GovernanceActionType;
import eu._5gzorro.legalproserepository.utils.MockHelpers;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {GovernanceManagerClientImplUnitTests.GovernanceManagerClientImplUnitTestsContextConfiguration.class })
@TestPropertySource(properties = { "integrations.governanceManager.apiBaseUrl=http://governance.com" })
public class GovernanceManagerClientImplUnitTests {

    @TestConfiguration
    static class GovernanceManagerClientImplUnitTestsContextConfiguration extends Config {

        @Bean
        public GovernanceManagerClient governanceManagerClient() {
            return new GovernanceManagerClientImpl();
        }

    }

    @Autowired
    GovernanceManagerClient governanceManagerClient;

    @MockBean
    AuthData authData;

    @MockBean
    CloseableHttpClient httpClient;

    @Test
    void proposeNewTemplate_returnsIdOfProposal() throws IOException {

        // given
        String proseTemplateId = "template1";
        String mockedProposalId = "proposal1";
        CloseableHttpResponse mockedResponse = MockHelpers.buildMock202Response(Optional.of(mockedProposalId));
        when(httpClient.execute(any(HttpPost.class))).thenReturn(mockedResponse);

        // when
        String resultingProposalId = governanceManagerClient.proposeNewTemplate(proseTemplateId);

        // then
        verify(httpClient, times(1)).execute(argThat(val -> {
            try {
                String requestStr = EntityUtils.toString(((HttpPost) val).getEntity());
                ObjectMapper mapper = new ObjectMapper();
                ProposeGovernanceDecisionRequest request = mapper.readValue(requestStr, ProposeGovernanceDecisionRequest.class);

                return request.getActionType().equals(GovernanceActionType.NEW_LEGAL_PROSE_TEMPLATE) &&
                        request.getActionParams() != null &&
                        request.getActionParams().getEntityIdentityId().equals(proseTemplateId);

            } catch (IOException exception) {
                exception.printStackTrace();
            }

            return false;
        }));

        assertEquals(mockedProposalId, resultingProposalId);
    }

    @Test
    void proposeArchiveTemplate_returnsIdOfProposal() throws IOException {

        // given
        String proseTemplateId = "template1";
        String mockedProposalId = "proposal1";
        CloseableHttpResponse mockedResponse = MockHelpers.buildMock202Response(Optional.of(mockedProposalId));
        when(httpClient.execute(any(HttpPost.class))).thenReturn(mockedResponse);

        // when
        String resultingProposalId = governanceManagerClient.proposeArchiveTemplate(proseTemplateId);

        // then
        verify(httpClient, times(1)).execute(argThat(val -> {
            try {
                String requestStr = EntityUtils.toString(((HttpPost) val).getEntity());
                ObjectMapper mapper = new ObjectMapper();
                ProposeGovernanceDecisionRequest request = mapper.readValue(requestStr, ProposeGovernanceDecisionRequest.class);

                return request.getActionType().equals(GovernanceActionType.ARCHIVE_LEGAL_PROSE_TEMPLATE) &&
                        request.getActionParams() != null &&
                        request.getActionParams().getEntityIdentityId().equals(proseTemplateId);

            } catch (IOException exception) {
                exception.printStackTrace();
            }

            return false;
        }));

        assertEquals(mockedProposalId, resultingProposalId);
    }
}
