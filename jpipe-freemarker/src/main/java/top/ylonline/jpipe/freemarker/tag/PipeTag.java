package top.ylonline.jpipe.freemarker.tag;

import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.TemplateBooleanModel;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import top.ylonline.jpipe.common.Cts;
import top.ylonline.jpipe.freemarker.render.FreemarkerRender;
import top.ylonline.jpipe.render.Render;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * pipe Directive
 * <pre>
 *     {@link TemplateDirectiveModel} 在 freemarker 2.3.11 版本才加入
 *     用以替代被废弃的 {@link freemarker.template.TemplateTransformModel}
 * </pre>
 *
 * @author Created by YL on 2018/8/19
 * @since freemarker 2.3.11
 */
public class PipeTag implements TemplateDirectiveModel {
    static final String V_NAME_RENDER = PipeTag.class.getName();

    @Override
    public void execute(Environment env, Map params, TemplateModel[] models,
                        TemplateDirectiveBody body) throws TemplateException, IOException {
        // if (params.isEmpty()) {
        //     throw new TemplateModelException("This directive doesn't allow empty parameter");
        // }
        if (models.length != 0) {
            throw new TemplateModelException("This directive doesn't allow loop variables");
        }
        if (body == null) {
            throw new TemplateModelException("This directive doesn't allow empty body");
        }
        TemplateBooleanModel asyncModel = (TemplateBooleanModel) params.get(Cts.PIPE_ASYNC);

        Writer out = env.getOut();
        Render render = new FreemarkerRender(out);
        TemplateModel model = new DefaultObjectWrapperBuilder(Configuration.getVersion())
                .build()
                .wrap(render);
        env.setVariable(V_NAME_RENDER, model);

        body.render(out);

        // async 默认：true
        boolean async = asyncModel == null || asyncModel.getAsBoolean();
        render.render(async);
    }
}
