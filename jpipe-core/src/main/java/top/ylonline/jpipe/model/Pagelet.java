package top.ylonline.jpipe.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import top.ylonline.jpipe.api.PageletBean;
import top.ylonline.jpipe.common.Cts;
import top.ylonline.jpipe.spring.JpipeSpringFactory;
import top.ylonline.jpipe.util.StrUtils;
import top.ylonline.jpipe.util.UriQueryDecoder;

import java.util.HashMap;
import java.util.List;
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
    private String domid;

    /**
     * pagelet component/bean
     */
    private String bean;

    /**
     * var attr
     */
    private String var;

    /**
     * uri
     */
    private String uri;

    /**
     * js method. default: JP.view
     */
    private String jsmethod;

    public Pagelet(String domid, String bean, String var, String uri, String jsmethod) {
        this.domid = domid;
        this.bean = bean;
        this.var = var;
        this.uri = uri;
        this.jsmethod = StrUtils.isNotBlank(jsmethod) ? jsmethod : Cts.JS_METHOD;
    }

    public PageletBean getPageletBean() {
        return JpipeSpringFactory.getBean(this.bean, PageletBean.class);
    }

    /**
     * 解析 uri 参数
     *
     * @return 解析后的 uri 参数 key/value
     */
    public Map<String, String> getParameters() {
        Map<String, String> map = new HashMap<>(8);
        if (StrUtils.isNotBlank(this.uri)) {
            if (!this.uri.startsWith(Cts.SYMBOL_Q)) {
                this.uri = Cts.SYMBOL_Q + this.uri;
            }
            UriQueryDecoder decoder = new UriQueryDecoder(this.uri);
            Map<String, List<String>> parameters = decoder.parameters();
            for (Map.Entry<String, List<String>> entry : parameters.entrySet()) {
                String key = entry.getKey();
                List<String> value = entry.getValue();
                map.put(key, value != null ? value.get(0) : null);
            }
        }
        return map;
    }
}
