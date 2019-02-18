package com.nettystu.util;

import com.nettystu.attributes.Attributes;
import com.nettystu.session.Session;
import io.netty.channel.Channel;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionUtil {


    //    SessionUtil 里面维持了一个 useId -> channel 的映射 map
    private static final Map<String, Channel> userIdChannelMap = new ConcurrentHashMap<>();

    public static void bindSession(Session session, Channel channel) {
        userIdChannelMap.put(session.getUserId(), channel);
        channel.attr(Attributes.SESSION).set(session);
    }

    public static void unBindSession(Channel channel) {
        if(hasLogin(channel)) {
            userIdChannelMap.remove(getSession(channel).getUserId());
            channel.attr(Attributes.SESSION).set(null);
        }
    }

    public static boolean hasLogin(Channel channel) {
        return channel.hasAttr(Attributes.SESSION);
    }

    public static Session getSession(Channel channel) {
        return channel.attr(Attributes.SESSION).get();
    }


    /**
     * getChannel() 方法，这样就可以通过 userId 拿到对应的 channel
     * @param userId
     * @return
     */
    public static Channel getChannel(String userId) {
        return userIdChannelMap.get(userId);
    }


}
