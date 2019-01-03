package top.ylonline.jpipe.freemarker.tag;

import freemarker.template.ObjectWrapper;
import freemarker.template.SimpleHash;
import top.ylonline.jpipe.common.Cts;
import top.ylonline.jpipe.freemarker.tag.PageletTag;
import top.ylonline.jpipe.freemarker.tag.ParamTag;
import top.ylonline.jpipe.freemarker.tag.PipeTag;

/**
 * Shortcut for injecting the tags into Freemarker
 * <pre>
 *     Usage: cfg.setSharedVeriable("jp", new JPipeTags());
 * </pre>
 *
 * @author Created by YL on 2018/8/19
 * @since freemarker 2.3.11
 */
public class JPipeTags extends SimpleHash {

    public JPipeTags(ObjectWrapper wrapper) {
        super(wrapper);
        put(Cts.TAG_PIPE, new PipeTag());
        put(Cts.TAG_PAGELET, new PageletTag());
        put(Cts.TAG_PARAM, new ParamTag());
    }
}
