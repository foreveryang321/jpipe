package top.ylonline.jpipe.freemarker.render;

import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import lombok.extern.slf4j.Slf4j;
import top.ylonline.jpipe.freemarker.model.FmPagelet;
import top.ylonline.jpipe.render.AbstractRender;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

/**
 * @author Created by YL on 2018/8/26
 */
@Slf4j
public class FmRender extends AbstractRender<FmPagelet> {
    private Writer out;
    private final Object lock;

    private final String lineSeparator;

    public FmRender(Writer out) {
        this.out = out;
        this.lock = this;
        this.lineSeparator = java.security.AccessController.doPrivileged(
                new sun.security.action.GetPropertyAction("line.separator"));
    }

    @Override
    protected void writeAndFlush(String str) {
        try {
            synchronized (lock) {
                ensureOpen();
                this.out.write(str);
                this.out.write(lineSeparator);
                this.out.flush();
            }
        } catch (IOException e) {
            log.error("flush 数据到客户端异常", e);
            // trouble = true;
        }
    }

    @Override
    protected String html(FmPagelet fmPagelet, Map<String, Object> data) {
        Environment env = fmPagelet.getEnvironment();
        TemplateDirectiveBody body = fmPagelet.getTemplateDirectiveBody();
        String var = fmPagelet.getVar();
        try {
            TemplateModel attribute = env.getVariable(var);
            if (attribute != null) {
                throw new RuntimeException("Already has " + var + " in environment variable.");
            }
            env.setVariable(var, new DefaultObjectWrapperBuilder(Configuration.getVersion())
                    .build()
                    .wrap(data));
            StringWriter sw = new StringWriter();
            body.render(sw);
            return sw.toString();
        } catch (TemplateModelException e) {
            throw new RuntimeException("TemplateModelException", e);
        } catch (TemplateException | IOException e) {
            throw new RuntimeException("TemplateException", e);
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
