package link.dwsy.ddl.entity.Article;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import link.dwsy.ddl.XO.Enum.Article.CommentType;
import link.dwsy.ddl.entity.BaseEntity;
import link.dwsy.ddl.entity.User.User;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Set;

/**
 * @Author Dwsy
 * @Date 2022/8/24
 */

@Entity
@Table(name = "article_comment")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(value = {"handler", "hibernateLazyInitializer"})
public class ArticleComment extends BaseEntity {

    //    @ManyToOne
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private ArticleField articleField;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String text;

    int upNum = 0;

    int downNum = 0;


    private long parentCommentId = 0;

    @Transient
    private Set<ArticleComment> childComments;

    @Enumerated(EnumType.ORDINAL)
    private CommentType commentType = CommentType.comment;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String ua;

    @Override
    public String toString() {
        return "ArticleComment{" +
                "user=" + user +
                ", text='" + text + '\'' +
                ", upNum=" + upNum +
                ", commentType=" + commentType +
                ", ua='" + ua + '\'' +
                '}';
    }
}