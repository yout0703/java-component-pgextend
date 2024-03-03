package yout.component.pgextend.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import yout.component.pgextend.injector.PgExtendSqlInjector;

/**
 * @author yout0703
 */
@Configuration
@MapperScan("yout.component.*.mapper")
public class PgExtendConfig {

    /**
     * 注册扩展的方法
     */
    @Bean
    public PgExtendSqlInjector pgExtendSqlInjector() {
        return new PgExtendSqlInjector();
    }
}
