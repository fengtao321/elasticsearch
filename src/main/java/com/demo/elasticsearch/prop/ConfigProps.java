package com.demo.elasticsearch.prop;

import com.demo.elasticsearch.model.Document;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("doc")
@Data
public class ConfigProps {

    @NestedConfigurationProperty
    private Document clients = new Document();

    @NestedConfigurationProperty
    private Index index = new Index();
}