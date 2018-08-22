package cn.tisson.jpipe.api;

import cn.tisson.jpipe.model.Pagelet;

/**
 * execute tasks，and flush pagelet
 *
 * @author Created by YL on 2018/8/17
 */
public interface Render {
    /**
     * 执行任务，并将任务结果 flush
     */
    void executeAndRenderPagelets();

    /**
     * 添加任务到队列
     *
     * @param pagelet pagelet 分块
     */
    void addPagelet(Pagelet pagelet);
}
