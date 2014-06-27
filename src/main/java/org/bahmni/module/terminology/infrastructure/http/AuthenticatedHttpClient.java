package org.bahmni.module.terminology.infrastructure.http;

import com.google.gson.Gson;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static java.lang.String.format;

@Component
public class AuthenticatedHttpClient {

    private final CloseableHttpClient httpClient;

    public AuthenticatedHttpClient() {
        CredentialsProvider provider = new BasicCredentialsProvider();
        /*TODO: Use Bahmni's client which will cache the login session*/
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("admin", "Admin123");
        provider.setCredentials(AuthScope.ANY, credentials);
        httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
    }

    public <T> T get(String url, Class<T> clazz) {
        try {
            return new Gson().fromJson(EntityUtils.toString(httpClient.execute(new HttpGet(url)).getEntity()), clazz);
        } catch (IOException ex) {
            throw new RuntimeException(format("Error while accessing url %s for class %s", url, clazz.getName()), ex);
        }

    }
}
