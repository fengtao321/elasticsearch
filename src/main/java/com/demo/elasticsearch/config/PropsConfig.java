package com.demo.elasticsearch.config;

import com.demo.elasticsearch.config.prop.Executor;
import com.demo.elasticsearch.config.prop.Index;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("app")
@Data
public class PropsConfig {
    @NestedConfigurationProperty
    private Index index = new Index();

    @NestedConfigurationProperty
    private Executor executor = new Executor();
}