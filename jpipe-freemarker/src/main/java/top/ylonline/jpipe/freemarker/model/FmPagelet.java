package top.ylonline.jpipe.freemarker.model;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import top.ylonline.jpipe.model.Pagelet;

/**
 * @author Created by YL on 2019/1/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FmPagelet extends Pagelet {
    private Environment environment;
    private TemplateDirectiveBody templateDirectiveBody;

    public FmPagelet(String domid, String bean, String var, String uri, String jsmethod, Environment environment,
                     TemplateDirectiveBody templateDirectiveBody) {
        super(domid, bean, var, uri, jsmethod);
        this.environment = environment;
        this.templateDirectiveBody = templateDirectiveBody;
    }
}
