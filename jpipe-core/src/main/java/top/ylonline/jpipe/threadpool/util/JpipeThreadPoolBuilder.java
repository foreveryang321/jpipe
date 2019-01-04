package top.ylonline.jpipe.threadpool.util;

import lombok.Data;
import top.ylonline.jpipe.threadpool.JpipeThreadPoolExecutor;
import top.ylonline.jpipe.threadpool.common.Pool;
import top.ylonline.jpipe.threadpool.eager.EagerThreadPool;

/**
 * Jpipe 线程池生成器
 *
 * @author Created by YL on 2018/9/16
 */
@Data
public class JpipeThreadPoolBuilder {
    private Pool pool;

    public JpipeThreadPoolBuilder() {}

    public JpipeThreadPoolBuilder(Pool pool) {
        this.pool = pool;
    }

    public JpipeThreadPoolExecutor build() {
        return new EagerThreadPool().getExecutor(this.pool);
    }
}
