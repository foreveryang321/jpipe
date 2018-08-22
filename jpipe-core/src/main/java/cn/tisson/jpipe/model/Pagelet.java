package cn.tisson.jpipe.model;

import cn.tisson.jpipe.api.PageletService;
import lombok.Data;

import java.util.Map;

/**
 * @author Created by YL on 2018/8/10
 */
@Data
public class Pagelet {
    /**
     * dom id
     */
    private String domId;
    /**
     * templete id
     */
    private String templateId;
    /**
     * pagelet component/bean
     */
    private String beanName;
    /**
     * pagelet component/bean
     */
    private PageletService pageletService;
    /**
     * 是否异步，default true
     */
    private boolean async;
    /**
     * 异步超时时间，default 60s
     */
    private long timeout;
    /**
     * 参数信息
     */
    private Map<String, String> params;
}
