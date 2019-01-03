package top.ylonline.jpipe.threadpool.util;

import junit.framework.TestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import top.ylonline.jpipe.threadpool.common.Pool;

import java.util.concurrent.Executor;

/**
 * @author Created by YL on 2018/9/17
 */
public class JPipeThreadPoolFactoryBeanTest extends TestCase {
    private ApplicationContext ctx;

    @Override
    public void setUp() throws Exception {
        this.ctx = new ClassPathXmlApplicationContext("pool.xml");
    }

    public void testPool() {
        Pool pool = (Pool) this.ctx.getBean("pool-1");
        assertEquals(-1, pool.getCoreSize());
        assertEquals(20, pool.getMaxSize());
        assertFalse(pool.isPreStartAllCoreThreads());
        assertEquals(12000000, pool.getKeepAlive());
        assertEquals(500, pool.getQueueSize());
    }

    public void testSimple() throws Exception {
        Executor executor = (Executor) this.ctx.getBean("jPipeThreadPool");
        System.out.println(executor);
    }

    public void testComposite() throws Exception {
        Executor executor = (Executor) this.ctx.getBean("jPipeThreadPool-2");
        System.out.println(executor);
    }

    public void testBuilder() throws Exception {
        Executor executor = (Executor) this.ctx.getBean("jPipeThreadPool-3");
        System.out.println(executor);
    }
}