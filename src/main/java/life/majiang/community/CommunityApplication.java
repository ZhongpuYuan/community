package life.majiang.community;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// community同级及子级目录的注解类都会自动被加载到容器中
@SpringBootApplication

// 自动装配所有的mapper文件
@MapperScan("life.majiang.community.mapper")
public class CommunityApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommunityApplication.class, args);
    }
}