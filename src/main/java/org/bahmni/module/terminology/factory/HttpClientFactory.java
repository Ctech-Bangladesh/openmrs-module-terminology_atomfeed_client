package org.bahmni.module.terminology.factory;


import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Component;

@Component
public class HttpClientFactory {

    public HttpClient createAuthenticatedHttpClient() {
        CredentialsProvider provider = new BasicCredentialsProvider();
        /*TODO: Use Bahmni's client which will cache the login session*/
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("admin", "Admin123");
        provider.setCredentials(AuthScope.ANY, credentials);
        return HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
    }
}
