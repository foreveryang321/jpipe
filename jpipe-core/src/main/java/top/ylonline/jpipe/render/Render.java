package top.ylonline.jpipe.render;

import top.ylonline.jpipe.model.Pagelet;

/**
 * execute tasks，and flush pagelet
 *
 * @author YL
 */
public interface Render<T extends Pagelet> {

    /**
     * 添加任务到队列
     *
     * @param t pagelet 分块任务
     */
    void addPagelet(T t);

    /**
     * 执行任务，并 flush 到客户端
     *
     * @param async 是否异步执行任务
     */
    void render(boolean async);
}
