package top.ylonline.jpipe.jsp.render;

import lombok.extern.slf4j.Slf4j;
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
    private PrintWriter out;
    private final Object lock;

    public JspRender(PrintWriter out) {
        this.out = out;
        this.lock = this;
    }

    @Override
    protected void writeAndFlush(String json) {
        try {
            synchronized (lock) {
                ensureOpen();
                this.out.println(json);
                this.out.flush();
            }
        } catch (IOException e) {
            log.error("flush 数据到客户端异常", e);
            // trouble = true;
        }
    }

    @Override
    protected String html(JspPagelet pagelet, Map<String, Object> data) {
        JspFragment fragment = pagelet.getJspFragment();
        JspContext context = fragment.getJspContext();
        String var = pagelet.getVar();
        Object attribute = context.getAttribute(var);
        if (attribute != null) {
            throw new RuntimeException("Already has " + var + " in context attribute.");
        }
        context.setAttribute(var, data);
        StringWriter sw = new StringWriter();
        try {
            fragment.invoke(sw);
            return sw.toString();
        } catch (JspException | IOException e) {
            throw new RuntimeException("JspException", e);
        }
    }

    /**
     * Checks to make sure that the stream has not been closed
     */
    private void ensureOpen() throws IOException {
        if (this.out == null) {
            throw new IOException("Stream closed");
        }
    }
}
