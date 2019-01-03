package top.ylonline.jpipe.render;

import top.ylonline.jpipe.model.Pagelet;

/**
 * execute tasks，and flush pagelet
 *
 * @author Created by YL on 2018/8/17
 */
public interface Render {

    /**
     * 添加任务到队列
     *
     * @param pagelet pagelet 分块任务
     */
    void addPagelet(Pagelet pagelet);

    /**
     * 执行任务，并 flush 到客户端
     *
     * @param async 是否异步执行任务
     */
    void render(boolean async);
}
