package link.dwsy.ddl.demo;

import link.dwsy.ddl.annotation.IgnoreResponseAdvice;
import link.dwsy.ddl.demo.mapper.tuserMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author Dwsy
 * @Date 2022/8/16
 */

// 需要指定扫描包，全局响应才能生效 （高版本spring boot2）
@SpringBootApplication(scanBasePackages = {"link.dwsy.ddl"})
@EnableDiscoveryClient
@RestController
@MapperScan("link.dwsy.ddl.demo.mapper")
public class start {
    public start(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    public static void main(String[] args) {

        SpringApplication.run(start.class, args);
    }

    private final DiscoveryClient discoveryClient;

    @Resource
    private tuserMapper tmp;

    @GetMapping("/test")
    public List<ServiceInstance> test() {
        return discoveryClient.getInstances("ddl-demo");
    }

    @GetMapping("/test2")
    public String test2() {
//        discoveryClient.getInstances("ddl-demo").forEach(System.out::println);
        return "test";
    }

    @GetMapping("/test1")
    @IgnoreResponseAdvice
    public String test1() {
        throw new RuntimeException("123");
    }
}
