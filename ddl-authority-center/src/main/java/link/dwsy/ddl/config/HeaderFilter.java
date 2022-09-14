//package link.dwsy.ddl.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.*;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Slf4j
//@Component
//@WebFilter(urlPatterns = { "/*" }, filterName = "headerFilter")
//public class HeaderFilter implements Filter {
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
//        HttpServletResponse response = (HttpServletResponse) resp;
//        //解决跨域访问报错
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
//        //设置过期时间
//        response.setHeader("Access-Control-Max-Age", "3600");
//        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, client_id, uuid, Authorization");
//        // 支持HTTP 1.1.
//        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
//        // 支持HTTP 1.0. response.setHeader("Expires", "0");
//        response.setHeader("Pragma", "no-cache");
//        // 编码
//        response.setCharacterEncoding("UTF-8");
//        chain.doFilter(request, resp);
//    }
//
//    @Override
//    public void init(FilterConfig filterConfig) {
//        log.info("跨域过滤器启动");
//    }
//
//    @Override
//    public void destroy() {
//        log.info("跨域过滤器销毁");
//    }
//}