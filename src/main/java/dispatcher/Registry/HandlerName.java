package dispatcher.Registry;

import java.lang.annotation.*;

/**
 * @author yihangz
 */ // 定义注解，用于标记handler并指定名称
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface HandlerName {
    String value();
}
