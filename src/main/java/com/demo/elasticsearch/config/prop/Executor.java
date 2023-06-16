package com.demo.elasticsearch.config.prop;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Executor {
//    @JsonFormat(shape= JsonFormat.Shape.STRING)
    private int poolSize;

    private int keepAlive;

    private int queueCapacity;

//    @JsonFormat(shape= JsonFormat.Shape.STRING)
    private int maxPoolSize;



    public void setQueueCapacity(String value) {
        this.queueCapacity = value.toLowerCase().equals("max")? Integer.MAX_VALUE : Integer.parseInt(value);
    }

    public void setMaxPoolSize(String value) {
        this.maxPoolSize = value.toLowerCase().equals("max")? Integer.MAX_VALUE : Integer.parseInt(value);
    }
}
