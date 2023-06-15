package com.demo.elasticsearch.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.TransportUtils;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import javax.net.ssl.SSLContext;

@Configuration
@RequiredArgsConstructor
public class ElasticSearch {

    @Value("${spring.elasticsearch.uris}")
    private String es_uri;

    @Value("${spring.elasticsearch.port}")
    private int port;

    @Value("${spring.elasticsearch.scheme}")
    private String scheme;

    @Value("${spring.elasticsearch.fingerprint}")
    private String fingerprint;

    @Value("${spring.elasticsearch.user}")
    private String user;

    @Value("${spring.elasticsearch.password}")
    private String password;

    @Bean
    public ElasticsearchClient getESClient() {
//        return ClientConfiguration.builder()
//                .connectedTo(es_uri)
//                .build();

        SSLContext sslContext = TransportUtils
                .sslContextFromCaFingerprint(fingerprint);

        BasicCredentialsProvider credsProv = new BasicCredentialsProvider();
        credsProv.setCredentials(
                AuthScope.ANY, new UsernamePasswordCredentials(user, password)
        );

        RestClient restClient = RestClient
                .builder(new HttpHost(es_uri, port, scheme))
                .setHttpClientConfigCallback(hc -> hc
                        .setSSLContext(sslContext)
                        .setDefaultCredentialsProvider(credsProv)
                )
                .build();

        // Create the transport and the API client
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        ElasticsearchClient client = new ElasticsearchClient(transport);
        return client;
    }

}
