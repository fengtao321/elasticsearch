package com.demo.elasticsearch.prop;

import lombok.Data;

@Data
public class Index {
    private String name;
    private int shard;
    private int replica;
    private int from;
    private int size;
    private int timeout;
}
