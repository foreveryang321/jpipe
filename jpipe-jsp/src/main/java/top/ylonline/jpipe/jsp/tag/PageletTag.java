package top.ylonline.jpipe.jsp.tag;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import top.ylonline.jpipe.jsp.model.JspPagelet;
import top.ylonline.jpipe.render.Render;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

/**
 * @author YL
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class PageletTag extends SimpleTagSupport {
    private static final long serialVersionUID = -5042425456206898195L;

    private String domid;
    private String bean;
    private String var;
    private String uri;
    private String jsmethod;

    @Override
    public void doTag() throws JspException, IOException {
        JspTag parent;
        if (!((parent = this.getParent()) instanceof PipeTag)) {
            throw new JspException("pagelet outside pipe");
        }

        PipeTag jpipeTag = (PipeTag) parent;
        Render<JspPagelet> render = jpipeTag.getRender();

        JspPagelet pagelet = new JspPagelet(
                this.domid,
                this.bean,
                this.var,
                this.uri,
                this.jsmethod,
                this.getJspBody()
        );
        
        // 任务放到队列中
        render.addPagelet(pagelet);
    }
}
