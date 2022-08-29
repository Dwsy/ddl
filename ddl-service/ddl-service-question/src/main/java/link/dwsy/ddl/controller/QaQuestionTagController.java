package link.dwsy.ddl.controller;

import link.dwsy.ddl.XO.Enum.QA.QuestionState;
import link.dwsy.ddl.core.CustomExceptions.CodeException;
import link.dwsy.ddl.core.constant.CustomerErrorCode;
import link.dwsy.ddl.entity.QA.QaQuestionField;
import link.dwsy.ddl.entity.QA.QaTag;
import link.dwsy.ddl.service.impl.QaQuestionTagServiceImpl;
import link.dwsy.ddl.util.PageData;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author Dwsy
 * @Date 2022/8/25
 */
@RestController
@RequestMapping("tag")
public class QaQuestionTagController {
    @Resource
    QaQuestionTagServiceImpl qaQuestionTagService;

    @GetMapping("list")
    public List<QaTag> GetTagList() {

        return qaQuestionTagService.getTagList();
    }

    @GetMapping("question/{id}")
    public PageData<QaQuestionField> GetArticleFieldList(
            @PathVariable(name = "id") Long id,
            @RequestParam(required = false, defaultValue = "1", name = "page") int page,
            @RequestParam(required = false, defaultValue = "8", name = "size") int size,
            @RequestParam(required = false, defaultValue = "ASC", name = "order") String order,
            @RequestParam(required = false, defaultValue = "createTime", name = "properties") String[] properties,
            @RequestParam(required = false, defaultValue = "ask", name = "status") Set<String> statusStr) {
        if (id < 1L || size < 1)
            throw new CodeException(CustomerErrorCode.ParamError);
        Sort sort = Sort.by(Sort.Direction.valueOf(order.toUpperCase()), properties);
        Set<QuestionState> questionStates = new HashSet<>();
        statusStr.forEach(status -> questionStates.add(QuestionState.valueOf(status.toUpperCase())));
        return qaQuestionTagService.getQuestionListById(id, page < 1 ? 1 : page, size > 20 ? 20 : size,questionStates,sort);
    }
}

