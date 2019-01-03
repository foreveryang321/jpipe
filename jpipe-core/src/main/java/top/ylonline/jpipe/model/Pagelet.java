package top.ylonline.jpipe.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import top.ylonline.jpipe.api.PageletBean;
import top.ylonline.jpipe.common.Cts;
import top.ylonline.jpipe.spring.JPipeSpringFactory;

import java.util.Map;

/**
 * @author Created by YL on 2018/8/10
 */
@Data
@NoArgsConstructor
public class Pagelet {
    /**
     * dom id
     */
    private String domId;
    /**
     * template id
     */
    private String templateId;
    /**
     * pagelet component/bean
     */
    private String bean;

    /**
     * js function. default: JP.view
     */
    private String jsMethod = Cts.JS_METHOD;

    public Pagelet(String domId, String templateId, String bean, String jsMethod) {
        this.domId = domId;
        this.templateId = templateId;
        this.bean = bean;
        this.jsMethod = jsMethod;
    }

    /**
     * 参数信息
     */
    private Map<String, String> params;

    public PageletBean getPageletBean() {
        return JPipeSpringFactory.getBean(this.bean, PageletBean.class);
    }
}
