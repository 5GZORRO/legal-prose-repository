package eu._5gzorro.legalproserepository.utils;

import org.apache.http.HttpStatus;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicStatusLine;

import java.io.ByteArrayInputStream;
import java.util.Optional;

public class MockHelpers {

    public static CloseableHttpResponse buildMock200Response(Optional<String> responseBody) {
        ProtocolVersion protocolVersion = new ProtocolVersion("HTTP", 1, 1);
        String reasonPhrase = "OK";
        StatusLine statusline = new BasicStatusLine(protocolVersion, HttpStatus.SC_OK, reasonPhrase);
        MockCloseableHttpResponse mockResponse = new MockCloseableHttpResponse(statusline);
        BasicHttpEntity entity = new BasicHttpEntity();

        if (responseBody.isPresent())
            entity.setContent(new ByteArrayInputStream(responseBody.get().getBytes()));

        mockResponse.setEntity(entity);
        return mockResponse;
    }

    public static CloseableHttpResponse buildMock202Response(Optional<String> responseBody) {
        ProtocolVersion protocolVersion = new ProtocolVersion("HTTP", 1, 1);
        String reasonPhrase = "ACCEPTED";
        StatusLine statusline = new BasicStatusLine(protocolVersion, HttpStatus.SC_ACCEPTED, reasonPhrase);
        MockCloseableHttpResponse mockResponse = new MockCloseableHttpResponse(statusline);
        BasicHttpEntity entity = new BasicHttpEntity();

        if (responseBody.isPresent())
            entity.setContent(new ByteArrayInputStream(responseBody.get().getBytes()));

        mockResponse.setEntity(entity);
        return mockResponse;
    }

}
