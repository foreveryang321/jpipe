package top.ylonline.jpipe.spring;

import org.springframework.context.ApplicationContext;

/**
 * ApplicationContext
 *
 * @author YL
 */
public class JpipeSpringFactory {
    private static ApplicationContext context = null;

    public static void setContext(ApplicationContext context) {
        if (JpipeSpringFactory.context == null) {
            JpipeSpringFactory.context = context;
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
