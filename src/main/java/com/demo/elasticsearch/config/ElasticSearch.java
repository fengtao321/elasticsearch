package com.demo.elasticsearch.config;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.TransportUtils;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


import javax.net.ssl.SSLContext;
import java.util.concurrent.Executor;

@Configuration
@RequiredArgsConstructor
@EnableAsync
@Slf4j
public class ElasticSearch {
    private final PropsConfig props;

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

    private ElasticsearchTransport getElasticsearchTransport() {
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
        return new RestClientTransport(restClient, new JacksonJsonpMapper());
    }

    @Bean
    public ElasticsearchClient getESClient() {
        ElasticsearchClient client = new ElasticsearchClient(getElasticsearchTransport());
        return client;
    }

    @Bean
    public ElasticsearchAsyncClient getAsyncESClient() {
        ElasticsearchAsyncClient client = new ElasticsearchAsyncClient(getElasticsearchTransport());
        return client;
    }

    @Bean(name = "asyncExecutor")
    public Executor asyncExecutor()  {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(props.getExecutor().getPoolSize());
        executor.setMaxPoolSize(props.getExecutor().getMaxPoolSize());
        executor.setQueueCapacity(props.getExecutor().getQueueCapacity());
        executor.setThreadNamePrefix("AsynchThread-"); //for log
        executor.initialize();
        return executor;
    }

    @Bean
    public SearchSourceBuilder getSearchSourceBuilder(){
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        return sourceBuilder;
    }

}
