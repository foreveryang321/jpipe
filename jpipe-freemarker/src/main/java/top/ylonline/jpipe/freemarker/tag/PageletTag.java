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
import top.ylonline.jpipe.freemarker.model.FmPagelet;
import top.ylonline.jpipe.freemarker.render.FmRender;

import java.io.IOException;
import java.util.Map;

/**
 * pagelet Directive
 *
 * @author YL
 * @since freemarker 2.3.11
 */
@Slf4j
public class PageletTag implements TemplateDirectiveModel {

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars,
                        TemplateDirectiveBody body) throws TemplateException, IOException {
        if (params.isEmpty()) {
            throw new TemplateModelException("This directive doesn't allow empty parameter");
        }

        if (loopVars.length != 0) {
            throw new TemplateModelException("This directive doesn't allow loop variables");
        }

        if (body == null) {
            return;
        }

        TemplateModel renderModel = env.getVariable(PipeTag.V_NAME_RENDER);
        if (renderModel == null) {
            throw new TemplateModelException("pagelet outside pipe");
        }

        TemplateScalarModel domIdModel = (TemplateScalarModel) params.get(Cts.PAGELET_DOM_ID);
        if (domIdModel == null) {
            throw new NullPointerException("No domid attribute!");
        }
        TemplateScalarModel beanModel = (TemplateScalarModel) params.get(Cts.PAGELET_BEAN);
        if (beanModel == null) {
            throw new NullPointerException("No bean attribute!");
        }
        TemplateScalarModel varModel = (TemplateScalarModel) params.get(Cts.PAGELET_VAR);
        if (varModel == null) {
            throw new NullPointerException("No var attribute!");
        }
        TemplateScalarModel uriModel = (TemplateScalarModel) params.get(Cts.PAGELET_URI);
        TemplateScalarModel jsMethodModel = (TemplateScalarModel) params.get(Cts.PAGELET_JS_METHOD);

        String domid = domIdModel.getAsString();
        String bean = beanModel.getAsString();
        String var = varModel.getAsString();
        String uri = uriModel == null ? "" : uriModel.getAsString();
        String jsmethod = jsMethodModel == null ? "" : jsMethodModel.getAsString();

        FmPagelet pagelet = new FmPagelet(
                domid,
                bean,
                var,
                uri,
                jsmethod,
                env,
                body
        );

        FmRender render = (FmRender) new DefaultObjectWrapperBuilder(Configuration.getVersion())
                .build()
                .unwrap(renderModel);
        render.addPagelet(pagelet);
    }
}
