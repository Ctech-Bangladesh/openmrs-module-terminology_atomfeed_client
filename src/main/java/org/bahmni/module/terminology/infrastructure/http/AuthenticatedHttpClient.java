package org.bahmni.module.terminology.infrastructure.http;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
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

    public <T> T get(String url, Class<T> clazz) {
        try {
            CloseableHttpClient httpClient = buildClient();
            String json = EntityUtils.toString(httpClient.execute(new HttpGet(url)).getEntity());
            try {
                T object = new Gson().fromJson(json, clazz);
                return object;
            } catch (JsonSyntaxException e) {
                throw new RuntimeException("Error while parsing json. The Json is " + json);
            }
        } catch (IOException ex) {
            throw new RuntimeException(format("Error while accessing url %s for class %s", url, clazz.getName()), ex);
        }
    }

    private CloseableHttpClient buildClient() {
        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("admin", "Admin123");
        provider.setCredentials(AuthScope.ANY, credentials);
        return HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
    }
}
