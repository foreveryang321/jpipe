package top.ylonline.jpipe.spring;

import junit.framework.TestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Created by YL on 2018/9/17
 */
public class JpipeSpringFactoryBeanTest extends TestCase {
    private ApplicationContext ctx;

    @Override
    public void setUp() throws Exception {
        this.ctx = new ClassPathXmlApplicationContext("JpipeSpringFactoryBean.xml");
    }

    public void testContext() {
        JpipeSpringFactoryBean bean1 = this.ctx.getBean("jpipeSpringFactoryBean",
                JpipeSpringFactoryBean.class);
        JpipeSpringFactoryBean bean2 = JpipeSpringFactory.getBean("jpipeSpringFactoryBean",
                JpipeSpringFactoryBean.class);
        assertSame(bean1, bean2);
    }
}