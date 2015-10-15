package org.bahmni.module.terminology.infrastructure.http;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.bahmni.module.terminology.infrastructure.config.TRFeedProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;

import static java.lang.String.format;

@Component
public class AuthenticatedHttpClient {

    public static final String USER_NOT_AUTHORIZED = "User not authorized";
    public static final String RESPONSE_PARSE_ERROR = "Error while parsing response json. %s";
    public static final String UNEXPECTED_RESPONSE_STATUS = "Unexpected response status %d";
    public static final String RESOURCE_ACCESS_URL_ERROR = "Error while accessing url %s";
    public static final String RESOURCE_NOT_FOUND = "Resource not found at %s";
    private TRFeedProperties trProperties;

    @Autowired
    public AuthenticatedHttpClient(TRFeedProperties trProperties) {
        this.trProperties = trProperties;
    }

    public <T> T get(final String url, final Class<T> clazz) {
        CloseableHttpClient httpClient = buildClient();
        ResponseHandler<T> responseHandler = new ResponseHandler<T>() {
            public T handleResponse(final HttpResponse response) throws IOException {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    String content = EntityUtils.toString(response.getEntity());
                    try {
                        return new Gson().fromJson(content, clazz);
                    } catch (JsonSyntaxException e) {
                        throw new RuntimeException(format(RESPONSE_PARSE_ERROR,content));
                    }
                } else if (status == HttpStatus.NOT_FOUND.value()) {
                    throw new RuntimeException(format(RESOURCE_NOT_FOUND, url));
                } else if (status == HttpStatus.UNAUTHORIZED.value()) {
                    throw new RuntimeException(USER_NOT_AUTHORIZED);
                } else {
                    throw new RuntimeException(format(UNEXPECTED_RESPONSE_STATUS,status));
                }
            }
        };
        try {
            return httpClient.execute(new HttpGet(url), responseHandler);
        } catch (IOException e) {
            throw new RuntimeException(format(RESOURCE_ACCESS_URL_ERROR, url, clazz.getName()), e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private CloseableHttpClient buildClient() {
        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(trProperties.getTerminologyApiUserName(), trProperties.getTerminologyApiUserPassword());
        provider.setCredentials(AuthScope.ANY, credentials);
        return HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
    }
}
