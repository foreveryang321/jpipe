package top.ylonline.jpipe.freemarker.tag;

import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;
import lombok.extern.slf4j.Slf4j;
import top.ylonline.jpipe.common.Cts;
import top.ylonline.jpipe.freemarker.render.FreemarkerRender;
import top.ylonline.jpipe.model.Pagelet;
import top.ylonline.jpipe.render.Render;
import top.ylonline.jpipe.util.StrUtils;

import java.io.IOException;
import java.util.Map;

/**
 * pagelet Directive
 *
 * @author Created by YL on 2018/8/19
 * @since freemarker 2.3.11
 */
@Slf4j
public class PageletTag implements TemplateDirectiveModel {
    static final String V_NAME_PAGELET = PageletTag.class.getName();

    @Override
    public void execute(Environment env, Map params, TemplateModel[] models,
                        TemplateDirectiveBody body) throws TemplateException, IOException {
        if (params.isEmpty()) {
            throw new TemplateModelException("This directive doesn't allow empty parameter");
        }
        if (models.length != 0) {
            throw new TemplateModelException("This directive doesn't allow loop variables");
        }

        Environment.Namespace namespace = env.getCurrentNamespace();
        TemplateModel renderModel = namespace.get(PipeTag.V_NAME_RENDER);
        if (renderModel == null) {
            throw new TemplateModelException("pagelet outside pipe");
        }

        TemplateScalarModel domIdModel = (TemplateScalarModel) params.get(Cts.PAGELET_DOM_ID);
        if (domIdModel == null) {
            throw new NullPointerException("No domId attribute!");
        }
        TemplateScalarModel templateIdModel = (TemplateScalarModel) params.get(Cts.PAGELET_TEMPLATE_ID);
        TemplateScalarModel beanModel = (TemplateScalarModel) params.get(Cts.PAGELET_BEAN);
        if (beanModel == null) {
            throw new NullPointerException("No bean attribute!");
        }
        TemplateScalarModel jsMethodModel = (TemplateScalarModel) params.get(Cts.PAGELET_JS_METHOD);

        String domId = domIdModel.getAsString();
        String templateId = templateIdModel != null ? templateIdModel.getAsString() : "";
        String bean = beanModel.getAsString();
        String jsMethod = jsMethodModel != null ? jsMethodModel.getAsString() : Cts.JS_METHOD;

        Pagelet pagelet = new Pagelet(domId, templateId, bean, jsMethod);

        if (body != null) {
            TemplateModel pageletModel = new DefaultObjectWrapperBuilder(Configuration.getVersion())
                    .build()
                    .wrap(pagelet);
            env.setVariable(V_NAME_PAGELET, pageletModel);
            body.render(env.getOut());
        }
        Render render = (FreemarkerRender) new DefaultObjectWrapperBuilder(Configuration.getVersion())
                .build()
                .unwrap(renderModel);
        render.addPagelet(pagelet);
    }
}
