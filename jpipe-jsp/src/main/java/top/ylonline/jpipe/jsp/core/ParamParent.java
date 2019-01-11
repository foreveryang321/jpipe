package top.ylonline.jpipe.jsp.core;

/**
 * <p>Interface for tag handlers implementing valid parent tags for
 * &lt;c:param&gt;.</p>
 *
 * @author Created by YL on 2018/8/16
 */
public interface ParamParent {
    /**
     * Adds a parameter to this tag's URL.  The intent is that the
     * &lt;param&gt; subtag will call this to register URL parameters.
     * Assumes that 'name' and 'value' are appropriately encoded and do
     * not contain any meaningful metacharacters; in order words, escaping
     * is the responsibility of the caller.
     *
     * @param name  name attribute
     * @param value name attribute
     *
     * @see ParamSupport
     */
    void put(String name, String value);
}
