package link.dwsy.ddl.repository;

import link.dwsy.ddl.XO.Enum.QA.QuestionState;
import link.dwsy.ddl.entity.QA.QaGroup;
import link.dwsy.ddl.entity.QA.QaQuestionContent;
import link.dwsy.ddl.entity.QA.QaQuestionField;
import link.dwsy.ddl.entity.QA.QaTag;
import link.dwsy.ddl.entity.User.User;
import link.dwsy.ddl.repository.QA.QaContentRepository;
import link.dwsy.ddl.repository.QA.QaFieldRepository;
import link.dwsy.ddl.repository.QA.QaGroupRepository;
import link.dwsy.ddl.repository.QA.QaQuestionTagRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author Dwsy
 * @Date 2022/8/28
 */

@SpringBootTest
public class fieldContentTest {
    public static void main(String[] args) {
        System.out.println(new QaQuestionField().getQuestionState());
        QaQuestionField question = QaQuestionField.builder().title("发起提问").summary("问题简介").questionState(QuestionState.Ask).build();
        System.out.println(question.getQuestionState());
        question = QaQuestionField.builder().title("发起提问").summary("问题简介").build();
        System.out.println(question.getQuestionState());
        //todo 枚举在使用builder的时候不会赋值

    }
    @Resource
    QaContentRepository qaContentRepository;
    @Resource
    QaGroupRepository qaGroupRepository;
    @Resource
    QaQuestionTagRepository qaQuestionTagRepository;

    @Resource
    QaFieldRepository qaFieldRepository;

    @Test
    public void saveQuestion() {
        Set<Long> longs = Set.of(1L, 2L, 4L);
        HashSet<QaTag> qaTags = new HashSet<>(qaQuestionTagRepository.findAllById(longs));
        QaGroup qaGroup = qaGroupRepository.findById(1L).get();
        User user = new User();
        user.setId(3L);
        QaQuestionContent qaQuestionContent = QaQuestionContent.builder().textHtml("<h1>QA_TEST<h1>").textMd("TEST").textPure("PURE").build();

        System.out.println(new QaQuestionField());

        QaQuestionField question = QaQuestionField.builder().user(user).title("发起提问").summary("问题简介").qaQuestionContent(qaQuestionContent).questionState(QuestionState.Ask)
                .qaGroup(qaGroup).questionTags(qaTags).build();

        QaQuestionField qaQuestionField = qaFieldRepository.save(question);

        qaContentRepository.setQuestionFieldLd(qaQuestionField.getId(), qaQuestionField.getQaQuestionContent().getId());
    }
}