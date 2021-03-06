package top.ylonline.jpipe.jsp.tag;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.ylonline.jpipe.jsp.model.JspPagelet;
import top.ylonline.jpipe.jsp.render.JspRender;
import top.ylonline.jpipe.render.Render;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * @author YL
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PipeTag extends TagSupport {
    private static final long serialVersionUID = -2778987434612099107L;

    private Render<JspPagelet> render;

    private Boolean async;

    public PipeTag() {
        super();
        init();
        async = true;
    }

    public Render<JspPagelet> getRender() {
        return this.render;
    }

    private void init() {
        render = null;
    }

    @Override
    public int doStartTag() throws JspException {
        try {
            // flush the frame to client first
            this.pageContext.getOut().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.render = new JspRender(this.pageContext.getResponse().getWriter());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() throws JspException {
        this.render.render(this.async);
        return super.doEndTag();
    }

    /**
     * Releases any resources we may have (or inherit)
     */
    @Override
    public void release() {
        init();
        async = null;
    }
}
