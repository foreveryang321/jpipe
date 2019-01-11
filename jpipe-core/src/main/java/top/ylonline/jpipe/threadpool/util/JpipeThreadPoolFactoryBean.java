package top.ylonline.jpipe.threadpool.util;

import lombok.Data;
import org.springframework.beans.factory.FactoryBean;
import top.ylonline.jpipe.threadpool.JpipeThreadPoolExecutor;
import top.ylonline.jpipe.threadpool.common.Pool;
import top.ylonline.jpipe.threadpool.eager.EagerThreadPool;

/**
 * Jpipe 线程池工厂类
 *
 * @author Created by YL on 2018/9/16
 */
@Data
public class JpipeThreadPoolFactoryBean implements FactoryBean {
    private Pool pool;

    @Override
    public Object getObject() throws Exception {
        return new EagerThreadPool().getExecutor(this.pool);
    }

    @Override
    public Class<?> getObjectType() {
        return JpipeThreadPoolExecutor.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
