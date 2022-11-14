package com.sahay.config;


import com.sahay.client.CbsClient;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.client.protocol.RequestDefaultHeaders;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

import java.util.*;

import static org.springframework.ws.transport.http.HttpComponentsMessageSender.*;

@Configuration
public class SoapConfig {


    @Value("${cbs.endpoint}")
    private String CBS_URL;

    @Value("${cbs.username}")
    private String username;

    @Value("${cbs.password}")
    private String password;

    @Bean
    public Jaxb2Marshaller jaxb2Marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setPackagesToScan("com.sahay.cbs");
        return marshaller;
    }

    @Bean
    public CbsClient customerClient(Jaxb2Marshaller marshaller) throws Exception {

        CbsClient cbsClient = new CbsClient();
        cbsClient.setDefaultUri(CBS_URL);
        cbsClient.setMarshaller(marshaller);
        cbsClient.setUnmarshaller(marshaller);
        cbsClient.setMessageSender(componentsMessageSender());
        return cbsClient;

    }

    @Bean
    public HttpComponentsMessageSender componentsMessageSender() {
        HttpComponentsMessageSender messageSender = new HttpComponentsMessageSender();
        messageSender.setHttpClient(httpClient(username, password));
        return messageSender;
    }

    private HttpClient httpClient(String username, String password) {
        List<Header> headers = new ArrayList<>();
        BasicHeader authHeader = new BasicHeader("Authorization", "Basic " + base64authUserPassword(username, password));
        headers.add(authHeader);

        RequestDefaultHeaders requestDefaultHeaders = new RequestDefaultHeaders(headers);

        return HttpClientBuilder.create()
                .addInterceptorFirst(new RemoveSoapHeadersInterceptor()).addInterceptorLast(requestDefaultHeaders).build();

    }

    public String base64authUserPassword(String username, String password) {
        String authCredentials = username + ":" + password;
        String encodedAuth = new String(Base64.getEncoder().encode(authCredentials.getBytes()));
        return encodedAuth;
    }


}
