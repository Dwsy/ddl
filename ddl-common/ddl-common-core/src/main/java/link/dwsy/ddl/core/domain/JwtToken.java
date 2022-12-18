package link.dwsy.ddl.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <h1>授权中心鉴权之后给客户端的 Token</h1>
 */
@Data
@NoArgsConstructor
public class JwtToken {

    /**
     * JWT
     */
    private String token;

    public JwtToken(String token) {
        this.token = token;
    }
}
