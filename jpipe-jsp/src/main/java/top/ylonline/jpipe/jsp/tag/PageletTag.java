package top.ylonline.jpipe.jsp.tag;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import top.ylonline.jpipe.common.Cts;
import top.ylonline.jpipe.jsp.core.ParamParent;
import top.ylonline.jpipe.model.Pagelet;
import top.ylonline.jpipe.render.Render;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Created by YL on 2018/8/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class PageletTag extends BodyTagSupport implements ParamParent {
    private static final long serialVersionUID = -5042425456206898195L;

    private String domId;
    private String templateId;
    private String bean;
    private String jsMethod = Cts.JS_METHOD;

    private Map<String, String> parameters;

    public PageletTag() {
        super();
        init();
    }

    private void init() {
        domId = templateId = bean = null;
        parameters = null;
    }

    @Override
    public int doStartTag() throws JspException {
        parameters = new HashMap<>();
        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() throws JspException {
        Tag parent;
        if (!((parent = this.getParent()) instanceof PipeTag)) {
            throw new JspTagException("PAGELET OUTSIDE PIPE");
        }
        PipeTag jpipeTag = (PipeTag) parent;
        Render render = jpipeTag.getRender();

        Pagelet pagelet = new Pagelet(this.domId, this.templateId, this.bean, this.jsMethod);
        pagelet.setParams(this.parameters);

        // 任务放到队列中
        render.addPagelet(pagelet);
        return super.doEndTag();
    }

    @Override
    public void put(String name, String value) {
        parameters.put(name, value);
    }

    @Override
    public void release() {
        init();
    }
}
