package com.nettystu.serialize;

public interface Serializer {

    // 这样就实现了序列化相关的逻辑，如果想要实现其他序列化算法的话，
    // 只需要继承一下 Serializer，然后定义一下序列化算法的标识，再覆盖一下两个方法即可。
    Serializer DEFAULT =  new JSONSerializer();

    /**
     * 序列化算法
     * @return
     */
    byte getSerializerAlgorithm();


    /**
     * Java 对象转二进制
     * @param object
     * @return
     */
    byte[] serialize(Object object);


    /**
     * 二进制转 Java 对象
     * @param clazz
     * @param bytes
     * @param <T>
     * @return
     */
    <T> T deSerialize(Class<T> clazz, byte[] bytes);

}
