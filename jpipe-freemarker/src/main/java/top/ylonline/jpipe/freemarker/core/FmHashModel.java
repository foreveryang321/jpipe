package top.ylonline.jpipe.freemarker.core;

import freemarker.template.ObjectWrapper;
import freemarker.template.SimpleHash;
import top.ylonline.jpipe.common.Cts;
import top.ylonline.jpipe.freemarker.tag.PageletTag;
import top.ylonline.jpipe.freemarker.tag.PipeTag;

/**
 * Shortcut for injecting the tags into Freemarker
 * <pre>
 *     Usage: cfg.setSharedVeriable(shared, new {@link FmHashModel}());
 * </pre>
 *
 * @author YL
 * @since freemarker 2.3.11
 */
public class FmHashModel extends SimpleHash {

    private static final long serialVersionUID = -3152770708369117241L;

    public FmHashModel(ObjectWrapper wrapper) {
        super(wrapper);
        put(Cts.TAG_PIPE, new PipeTag());
        put(Cts.TAG_PAGELET, new PageletTag());
    }
}
