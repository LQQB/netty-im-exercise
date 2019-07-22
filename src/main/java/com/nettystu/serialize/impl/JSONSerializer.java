package com.nettystu.serialize.impl;

import com.alibaba.fastjson.JSON;
import com.nettystu.serialize.Serializer;
import com.nettystu.serialize.SerializerAlgorithm;

public class JSONSerializer implements Serializer {
    public byte getSerializerAlgorithm() {
        return SerializerAlgorithm.JSON;
    }

    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    public <T> T deSerialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes, clazz);
    }
}
