package top.ylonline.jpipe.spring;

import org.springframework.context.ApplicationContext;

/**
 * ApplicationContext
 *
 * @author Created by YL on 2018/9/17
 */
public class JPipeSpringFactory {
    private static ApplicationContext context = null;

    public static void setContext(ApplicationContext context) {
        if (JPipeSpringFactory.context == null) {
            JPipeSpringFactory.context = context;
        }
    }

    /**
     * 获取applicationContext
     */
    private static ApplicationContext getContext() {
        return context;
    }

    public static <T> T getBean(Class<T> clazz) {
        return getContext().getBean(clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return getContext().getBean(name, clazz);
    }
}
