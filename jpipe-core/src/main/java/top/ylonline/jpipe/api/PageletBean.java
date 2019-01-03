package top.ylonline.jpipe.api;

import java.util.Map;

/**
 * pagelet component
 *
 * @author Created by YL on 2018/8/8
 */
public interface PageletBean {
    /**
     * 执行 pagelet 逻辑
     *
     * @param param 入参
     *
     * @return 执行结果
     */
    Map<String, Object> doExec(Map<String, String> param) throws Exception;
}
