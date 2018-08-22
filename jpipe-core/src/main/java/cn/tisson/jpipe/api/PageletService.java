package cn.tisson.jpipe.api;

import java.util.Map;

/**
 * pagelet component
 *
 * @author Created by YL on 2018/8/8
 */
public interface PageletService {
    /**
     * 获取 pagelet 数据
     *
     * @param param 入参
     */
    Map<String, Object> doGet(Map<String, String> param) throws Exception;
}
