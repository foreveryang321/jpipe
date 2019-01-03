package top.ylonline.jpipe.threadpool.util;

import lombok.Data;
import top.ylonline.jpipe.threadpool.JPipeThreadPoolExecutor;
import top.ylonline.jpipe.threadpool.common.Pool;
import top.ylonline.jpipe.threadpool.eager.EagerThreadPool;

/**
 * JPipe 线程池生成器
 *
 * @author Created by YL on 2018/9/16
 */
@Data
public class JPipeThreadPoolBuilder {
    private Pool pool;

    public JPipeThreadPoolBuilder() {}

    public JPipeThreadPoolBuilder(Pool pool) {
        this.pool = pool;
    }

    public JPipeThreadPoolExecutor build() {
        return new EagerThreadPool().getExecutor(this.pool);
    }
}
