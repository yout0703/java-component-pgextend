package yout.component.pgextend.annotaion;

import java.lang.annotation.*;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

/**
 * @description: Upsert操作的时候需要判断的唯一性索引
 * @author: yout0703
 * @date: 2023-07-07
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Component
public @interface UpsertIndexColumns {

    String[] value() default "unique_id";

    @AliasFor("value")
    String[] columns() default "unique_id";
}
