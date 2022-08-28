package link.dwsy.ddl.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import link.dwsy.ddl.XO.Enum.Article.CommentType;
import link.dwsy.ddl.entity.QA.QaAnswer;
import link.dwsy.ddl.entity.QA.QaQuestionField;
import link.dwsy.ddl.entity.User.User;
import link.dwsy.ddl.repository.QA.QaAnswerRepository;
import link.dwsy.ddl.repository.QA.QaFieldRepository;
import link.dwsy.ddl.repository.User.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @Author Dwsy
 * @Date 2022/8/28
 */
@SpringBootTest
public class answerTest {

    @Resource
    QaAnswerRepository qaAnswerRepository;
    @Resource
    UserRepository userRepository;
    @Resource
    QaFieldRepository qaFieldRepository;

    public void addAnswerByQuestionId(long uid, long qid, long aid) throws JsonProcessingException {
        User user = new User();
        user.setId(uid);
        QaQuestionField qaQuestionField = null;
        qaQuestionField = qaFieldRepository.findById(qid).get();
//        todo sql查询可通过projections优化
        if (qaQuestionField.isAllow_answer()) {
            //            开启回答
            if (qaAnswerRepository.existsByIdAndQuestionFieldId(aid, qid)) {
                QaAnswer qaAnswer = QaAnswer.builder().user(user).questionField(qaQuestionField).commentType(CommentType.comment)
                        .parentAnswerId(0).textHtml("<b>回答</b>").ua("user-agent:天河一百号").user(user).childQaAnswers(null).build();
                QaAnswer save = qaAnswerRepository.save(qaAnswer);
                System.out.println(new ObjectMapper().writeValueAsString(save));
            } else {
                System.out.println("qa关系错误");
            }
        } else {
            System.out.println("已关闭回答");
        }
    }

    @Test
    public void test() throws JsonProcessingException {
//        addAnswerByQuestionId(3L,1L,1L);
//        replyAnswerByQuestionAndAnswerId(4L, 1L, 2L);
        replyAnswerByQuestionAndAnswerId(4L,1L, 1);
//        replyAnswerByQuestionAndAnswerId(4L, 1L, 1);
//        replyAnswerByQuestionAndAnswerId(3L, 1L, 10);
//        replyAnswerByQuestionAndAnswerId(3L, 1L, 1);


    }

    public void replyAnswerByQuestionAndAnswerId(long uid ,long qid, long aid) throws JsonProcessingException {
        User user = new User();
        user.setId(uid);
        QaQuestionField qaQuestionField = null;
        qaQuestionField = qaFieldRepository.findById(qid).get();
//        qaAnswerRepository.
//        判断是否允许评论
        if (qaQuestionField.isAllow_answer()) {
//            判断是否是相关的Q&A
            if (qaAnswerRepository.existsByIdAndQuestionFieldId(aid, qid)) {
//                判断是否为一级评论
                if (!qaAnswerRepository.isFirstAnswer(aid)) {
                    System.out.println("回复非父级评论错误");
                } else {
//                    回复id

                    long pid = qaAnswerRepository.findParentUserIdByAnswerId(aid);
                    if (pid==0){
                        pid = qaAnswerRepository.findUserIdByAnswerId(aid);
                    }
//                    todo 消息通知被评论方
                    String userNickname = userRepository.findUserNicknameById(pid);
                    QaAnswer qaAnswer = QaAnswer.builder().user(user).questionField(qaQuestionField)
                            .commentType(CommentType.comment).parentUserId(pid)
                            .parentAnswerId(aid)
//                            todo 拼接优化
                            .textHtml("回复" + "<a class=\"reply\">@" +
                                    userNickname +"</a>" + "<b>回答 回答</b>")
                            .ua("user-agent:喜马拉雅山").user(user).childQaAnswers(null).build();
                    QaAnswer save = qaAnswerRepository.save(qaAnswer);
                    System.out.println(new ObjectMapper().writeValueAsString(save));
                }
            } else {
                System.out.println("qa关系错误");
            }
//            只搭二楼模式

        } else {
            System.out.println("已关闭回答");
        }
    }
}