package top.ylonline.jpipe.jsp.tag;

import top.ylonline.jpipe.jsp.core.ParamSupport;

import javax.servlet.jsp.JspTagException;

/**
 * @author Created by YL on 2018/8/11
 */
public class ParamTag extends ParamSupport {
    private static final long serialVersionUID = 5627590179925507651L;

    public void setName(String name) throws JspTagException {
        this.name = name;
    }

    public void setValue(String value) throws JspTagException {
        this.value = value;
    }
}
