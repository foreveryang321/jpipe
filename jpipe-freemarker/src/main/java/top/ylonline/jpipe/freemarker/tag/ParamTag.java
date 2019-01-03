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
import top.ylonline.jpipe.model.Pagelet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * param Directive
 *
 * @author Created by YL on 2018/8/19
 * @since freemarker 2.3.11
 */
@Slf4j
public class ParamTag implements TemplateDirectiveModel {

    @Override
    public void execute(Environment env, Map params, TemplateModel[] models,
                        TemplateDirectiveBody body) throws TemplateException, IOException {
        if (params.isEmpty()) {
            throw new TemplateModelException("This directive doesn't allow empty parameter");
        }
        if (models.length != 0) {
            throw new TemplateModelException("This directive doesn't allow loop variables");
        }
        if (body != null) {
            throw new TemplateModelException("This directive doesn't allow body");
        }
        Environment.Namespace namespace = env.getCurrentNamespace();
        TemplateModel pageletModel = namespace.get(PageletTag.V_NAME_PAGELET);
        if (pageletModel == null) {
            throw new TemplateModelException("param outside pagelet");
        }

        TemplateScalarModel nameModel = (TemplateScalarModel) params.get(Cts.PARAM_NAME);
        if (nameModel == null) {
            throw new NullPointerException("No name attribute!");
        }
        TemplateScalarModel valueModel = (TemplateScalarModel) params.get(Cts.PARAM_VALUE);

        String name = nameModel.getAsString();
        String value = valueModel != null ? valueModel.getAsString() : "";

        Pagelet pagelet = (Pagelet) new DefaultObjectWrapperBuilder(Configuration.getVersion())
                .build()
                .unwrap(pageletModel);
        Map<String, String> parameter = pagelet.getParams();
        if (parameter == null) {
            parameter = new HashMap<>();
        }
        parameter.put(name, value);
        pagelet.setParams(parameter);
    }
}
