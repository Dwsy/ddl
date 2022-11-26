package link.dwsy.ddl.XO.RB;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

/**
 * @Author Dwsy
 * @Date 2022/11/24
 */
@Data
public class ReplyInfinityRB {
    @Min(value = 1, message = "回复的id不合法")
    private Long replyId;

    @Size(min = 1, max = 2000, message = "回复内容长度不合法")
    private String content;

    @Min(value = 1, message = "回复类型不合法")
    private Long replyUserId;

    @Min(value = 1, message = "回复对象id不合法")
    private Long replyUserTweetId;

}