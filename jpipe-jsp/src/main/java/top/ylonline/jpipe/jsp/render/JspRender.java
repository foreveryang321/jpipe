package top.ylonline.jpipe.jsp.render;

import lombok.extern.slf4j.Slf4j;
import top.ylonline.jpipe.common.JpipeException;
import top.ylonline.jpipe.jsp.model.JspPagelet;
import top.ylonline.jpipe.render.AbstractRender;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

/**
 * @author Created by YL on 2018/8/8
 */
@Slf4j
public class JspRender extends AbstractRender<JspPagelet> {

    public JspRender(PrintWriter out) {
        super(out);
    }

    @Override
    protected String html(JspPagelet pagelet, Map<String, Object> data) {
        JspFragment fragment = pagelet.getJspFragment();
        JspContext context = fragment.getJspContext();
        String var = pagelet.getVar();
        Object attribute = context.getAttribute(var);
        if (attribute != null) {
            throw new JpipeException("Already has " + var + " in context attribute.");
        }
        context.setAttribute(var, data);
        StringWriter sw = new StringWriter();
        try {
            fragment.invoke(sw);
            return sw.toString();
        } catch (JspException | IOException e) {
            throw new JpipeException(e);
        }
    }
}
