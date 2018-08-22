package cn.tisson.jpipe.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * bigPipe 输出的结果
 *
 * @author Created by YL on 2018/8/15
 */
@Data
@NoArgsConstructor
public class PageletResult implements Serializable {
    private static final long serialVersionUID = 7453855422501349532L;

    private int code = 0;
    private String message = "success";

    private String domId;
    private String bn;
    private String templateId;
    private Map<String, Object> data;

    private PageletResult(String domId, String beanName, String templateId, Map<String, Object> data) {
        this.domId = domId;
        this.bn = beanName;
        this.templateId = templateId;
        this.data = data;
    }

    private PageletResult(int code, String message, String id, String beanName, String templateId) {
        this.code = code;
        this.message = message;
        this.domId = id;
        this.bn = beanName;
        this.templateId = templateId;
    }

    /**
     * 成功返回
     *
     * @param id         dom id
     * @param beanName   bean name
     * @param templateId template id
     * @param data       返回数据
     */
    public static PageletResult ok(String id, String beanName, String templateId, Map<String, Object> data) {
        return new PageletResult(id, beanName, templateId, data);
    }

    /**
     * 异常返回
     *
     * @param id         dom id
     * @param beanName   bean name
     * @param templateId template id
     * @param message    异常信息
     */
    public static PageletResult error(String id, String beanName, String templateId, String message) {
        return new PageletResult(-1, message, id, beanName, templateId);
    }
}
