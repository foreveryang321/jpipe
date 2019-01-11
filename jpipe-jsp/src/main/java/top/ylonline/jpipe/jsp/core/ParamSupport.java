package top.ylonline.jpipe.jsp.core;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

/**
 * @author Created by YL on 2018/8/11
 */
public class ParamSupport extends BodyTagSupport {
    private static final long serialVersionUID = 7117079434469392505L;

    protected String name;
    protected String value;

    // *********************************************************************
    // Constructor and initialization

    public ParamSupport() {
        super();
        init();
    }

    private void init() {
        name = value = null;
    }

    // *********************************************************************
    // Tag logic

    /**
     * simply send our name and value to our appropriate ancestor
     */
    @Override
    public int doEndTag() throws JspException {
        Tag t = findAncestorWithClass(this, ParamParent.class);
        if (t == null) {
            throw new JspTagException("PARAM OUTSIDE PARENT");
        }

        // take no action for null or empty names
        if (name == null || "".equals(name)) {
            return EVAL_PAGE;
        }

        // send the parameter to the appropriate ancestor
        ParamParent parent = (ParamParent) t;
        String value = this.value;
        if (value == null) {
            if (bodyContent == null || bodyContent.getString() == null) {
                value = "";
            } else {
                value = bodyContent.getString().trim();
            }
        }
        // set
        parent.put(name, value);
        return EVAL_PAGE;
    }

    /**
     * Releases any resources we may have (or inherit)
     */
    @Override
    public void release() {
        init();
    }
}
