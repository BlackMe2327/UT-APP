package run.ut.app.netty;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import run.ut.app.model.enums.WebSocketMsgTypeEnum;
import run.ut.app.model.support.WebSocketMsg;
import run.ut.app.utils.JsonUtils;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * It is used to manage the mapping between user and channel.
 *
 * @author wenjie
 */

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserChannelManager {

    private ConcurrentHashMap<Long, LinkedList<Channel>> userChannelMap = new ConcurrentHashMap<>(1 << 8);

    private final Lock lock = new ReentrantLock();

    /**
     * Save the mapping of uid and channel
     *
     * @param uid        uid
     * @param channel    channel
     */
    public void add(@NonNull Long uid, @NonNull Channel channel) {
        lock.lock();
        LinkedList<Channel> channels = userChannelMap.get(uid);
        if (ObjectUtils.isEmpty(channels) || channels.size() == 0) {
            LinkedList<Channel> channelLinkedList = new LinkedList<>();
            channelLinkedList.add(channel);
            userChannelMap.put(uid, channelLinkedList);
        } else {
            channels.add(channel);
            userChannelMap.put(uid, channels);
        }
        lock.unlock();
    }

    /**
     * Remove the element by uid
     *
     * @param uid    uid
     */
    public void remove(@NonNull Long uid) {
        userChannelMap.remove(uid);
    }

    /**
     *  Remove the cache by channel
     *
     * @param channel    channel
     */
    public void remove(@NonNull Channel channel) {
        userChannelMap.entrySet().stream().filter(entry -> entry.getValue().contains(channel))
            .forEach(entry -> entry.getValue().remove(channel));
    }

    /**
     * Get channel by uid
     * @param uid     uid
     * @return channel
     */
    @Nullable
    public LinkedList<Channel> get(@NonNull Long uid) {
        return userChannelMap.get(uid);
    }

    /**
     * Clear cache
     */
    public void clearAll() {
        userChannelMap.clear();
    }


    /**
     * Write and flush by uid
     *
     * @param uid      uid
     * @param msgObj   msg object, it will be automatically converted to json.
     * @throws JsonProcessingException If msgObj fails to convert to json.
     */
    public void writeAndFlush(@NonNull Long uid, @NonNull Object msgObj, @NonNull WebSocketMsgTypeEnum typeEnum) throws JsonProcessingException {
        LinkedList<Channel> channelLinkedList = userChannelMap.get(uid);
        if (ObjectUtils.isEmpty(channelLinkedList) || channelLinkedList.size() == 0) {
            return;
        }
        for (Channel channel : channelLinkedList) {
            if (channel.isActive()) {
                WebSocketMsg webSocketMsg = new WebSocketMsg()
                    .setType(typeEnum.getType())
                    .setMsg(msgObj);
                String json = JsonUtils.objectToJson(webSocketMsg);
                TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(json);
                ChannelFuture channelFuture = channel.writeAndFlush(textWebSocketFrame);
                channelFuture.addListener((ChannelFutureListener)future -> {
                    log.debug("对uid：{}, 发送websocket消息：{}", uid, json);
                });

            }
        }
    }

    /**
     * Write and flush to every user
     * @param msgObj msg object, it will be automatically converted to json.
     * @throws JsonProcessingException If msgObj fails to convert to json.
     */
    public void writeAndFlush(@NonNull Object msgObj, @NonNull WebSocketMsgTypeEnum typeEnum) throws JsonProcessingException {
        WebSocketMsg webSocketMsg = new WebSocketMsg()
            .setType(typeEnum.getType())
            .setMsg(msgObj);
        String json = JsonUtils.objectToJson(webSocketMsg);
        TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(json);
        userChannelMap.forEach((uid, channels) -> {
            for (Channel channel : channels) {
                if (channel.isActive()) {
                    ChannelFuture channelFuture = channel.writeAndFlush(textWebSocketFrame);
                    channelFuture.addListener((ChannelFutureListener)future -> {
                        log.debug("对uid：{}, 发送websocket消息：{}", uid, json);
                    });
                }
            }
        });
    }
}
