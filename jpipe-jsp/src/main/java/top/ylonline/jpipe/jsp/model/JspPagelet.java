package top.ylonline.jpipe.jsp.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import top.ylonline.jpipe.model.Pagelet;

import javax.servlet.jsp.tagext.JspFragment;

/**
 * @author YL
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class JspPagelet extends Pagelet {

    private JspFragment jspFragment;

    public JspPagelet(String domid, String bean, String var, String uri, String jsmethod, JspFragment jspFragment) {
        super(domid, bean, var, uri, jsmethod);
        this.jspFragment = jspFragment;
    }
}
