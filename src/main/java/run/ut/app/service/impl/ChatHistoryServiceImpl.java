package run.ut.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import run.ut.app.mapper.ChatHistoryMapper;
import run.ut.app.model.domain.ChatHistory;
import run.ut.app.service.ChatHistoryService;

/**
 * <p>
 *  chat history impl
 * </p>
 *
 * @author chenwenjie.star
 * @since 2021-04-26
 */
@Service
public class ChatHistoryServiceImpl extends ServiceImpl<ChatHistoryMapper, ChatHistory> implements ChatHistoryService {

    @Override
    public ChatHistory getOneByFromUidAndTimeStamp(Long uid, Long timeStamp) {
        return lambdaQuery()
                .eq(ChatHistory::getFromUid, uid)
                .eq(ChatHistory::getTimeStamp, timeStamp)
                .last("limit 1").one();
    }
}