package com.nettystu.attributes;

import com.nettystu.session.Session;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

public interface Attributes {
    AttributeKey<Session> SESSION = AttributeKey.newInstance("session");
}
