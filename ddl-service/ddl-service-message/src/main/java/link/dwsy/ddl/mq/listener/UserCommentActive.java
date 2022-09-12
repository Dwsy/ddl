package link.dwsy.ddl.mq.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import link.dwsy.ddl.XO.Message.UserCommentNotifyMessage;
import link.dwsy.ddl.mq.UserActiveConstants;
import link.dwsy.ddl.mq.process.UserCommentActiveProcess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author Dwsy
 * @Date 2022/9/8
 */
@Component
@Slf4j
public class UserCommentActive {
    @Resource
    UserCommentActiveProcess userCommentActiveProcess;

    @RabbitListener(queues = UserActiveConstants.QUEUE_DDL_USER_ARTICLE_COMMENT_ACTIVE)
    public void sendNotify(UserCommentNotifyMessage message) throws JsonProcessingException {
        userCommentActiveProcess.sendNotify(message);


        log.info("用户{}comment记录{}成功", message.getFormUserId(), message.getUserActiveType().toString());
    }


}
