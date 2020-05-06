package run.ut.app.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import run.ut.app.utils.SpringUtils;

/**
 * Heart Beat Handler. If the client's heartbeat frames is not received for a long time
 *
 * @author wenjie
 */

@Slf4j
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {

    private UserChannelManager userChannelManager;

    HeartBeatHandler() {
        userChannelManager = SpringUtils.getBean(UserChannelManager.class);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent)evt;

            if (event.state() == IdleState.READER_IDLE) {
                log.debug("READER_IDLE...");
            } else if (event.state() == IdleState.WRITER_IDLE) {
                log.debug("WRITER_IDLE...");
            } else if (event.state() == IdleState.ALL_IDLE) {
                // Closes the channel which state is ALL_IDLE
                Channel channel = ctx.channel();
                // Clear cache
                userChannelManager.remove(channel.id().asLongText());
                channel.close();
                log.debug("close channel: " + channel.id().asLongText());
            }
        }

    }

}