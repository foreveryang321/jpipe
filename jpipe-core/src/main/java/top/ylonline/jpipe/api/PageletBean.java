package top.ylonline.jpipe.api;

import java.util.Map;

/**
 * pagelet component
 *
 * @author YL
 */
public interface PageletBean {
    /**
     * 执行 pagelet 逻辑
     *
     * @param param 入参
     *
     * @return 执行结果
     *
     * @throws Exception 业务异常
     */
    Map<String, Object> doExec(final Map<String, String> param) throws Exception;
}
