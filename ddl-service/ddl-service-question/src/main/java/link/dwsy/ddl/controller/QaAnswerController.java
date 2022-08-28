package link.dwsy.ddl.controller;

import link.dwsy.ddl.core.CustomExceptions.CodeException;
import link.dwsy.ddl.core.constant.CustomerErrorCode;
import link.dwsy.ddl.entity.QA.QaAnswer;
import link.dwsy.ddl.service.impl.QaAnswerServiceServiceImpl;
import link.dwsy.ddl.util.PageData;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author Dwsy
 * @Date 2022/8/26
 */
@RestController
@RequestMapping("answer")
public class QaAnswerController {
    @Resource
    private QaAnswerServiceServiceImpl qaAnswerServiceService;

    @GetMapping("/{id}")
    public PageData<QaAnswer> GetCommentById(
            @RequestParam(required = false, defaultValue = "1", name = "page") int page,
            @RequestParam(required = false, defaultValue = "8", name = "size") int size,
            @PathVariable("id") Long qid) {
        if (qid < 1 || size < 1)
            throw new CodeException(CustomerErrorCode.ParamError);

        return qaAnswerServiceService.getByQuestionId(qid, page < 1 ? 1 : page, size > 20 ? 20 : size);
    }
}
