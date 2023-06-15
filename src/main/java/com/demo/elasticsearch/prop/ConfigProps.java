package com.demo.elasticsearch.prop;

import com.demo.elasticsearch.model.Document;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("app")
@Data
public class ConfigProps {
    @NestedConfigurationProperty
    private Index index = new Index();
}