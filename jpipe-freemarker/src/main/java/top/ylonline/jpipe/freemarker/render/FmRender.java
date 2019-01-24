package top.ylonline.jpipe.freemarker.render;

import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import lombok.extern.slf4j.Slf4j;
import top.ylonline.jpipe.common.JpipeException;
import top.ylonline.jpipe.freemarker.model.FmPagelet;
import top.ylonline.jpipe.render.AbstractRender;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

/**
 * @author YL
 */
@Slf4j
public class FmRender extends AbstractRender<FmPagelet> {

    public FmRender(Writer out) {
        super(out);
    }

    @Override
    protected String html(FmPagelet pagelet, Map<String, Object> data) {
        Environment env = pagelet.getEnvironment();
        TemplateDirectiveBody body = pagelet.getTemplateDirectiveBody();
        String var = pagelet.getVar();
        try {
            TemplateModel attribute = env.getVariable(var);
            if (attribute != null) {
                throw new JpipeException("Already has " + var + " in environment variable.");
            }
            env.setVariable(var, new DefaultObjectWrapperBuilder(Configuration.getVersion())
                    .build()
                    .wrap(data));
            StringWriter sw = new StringWriter();
            body.render(sw);
            return sw.toString();
        } catch (TemplateException | IOException e) {
            throw new JpipeException(e);
        }
    }
}
