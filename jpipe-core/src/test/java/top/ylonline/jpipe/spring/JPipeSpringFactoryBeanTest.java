package top.ylonline.jpipe.spring;

import junit.framework.TestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Created by YL on 2018/9/17
 */
public class JPipeSpringFactoryBeanTest extends TestCase {
    private ApplicationContext ctx;

    @Override
    public void setUp() throws Exception {
        this.ctx = new ClassPathXmlApplicationContext("JPipeSpringFactoryBean.xml");
    }

    public void testContext() {
        JPipeSpringFactoryBean bean1 = this.ctx.getBean("jPipeSpringFactoryBean",
                JPipeSpringFactoryBean.class);
        JPipeSpringFactoryBean bean2 = JPipeSpringFactory.getBean("jPipeSpringFactoryBean",
                JPipeSpringFactoryBean.class);
        assertSame(bean1, bean2);
    }
}