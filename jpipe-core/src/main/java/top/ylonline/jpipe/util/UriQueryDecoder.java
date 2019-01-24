package top.ylonline.jpipe.util;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * URI 解码器
 * <pre>
 *     uri要包含?
 *     比如：
 *          http://im.189.cn?Status=12323&Reason=是非得失
 *          ?Status=12323&Reason=是非得失
 * </pre>
 *
 * @author YL
 */
public class UriQueryDecoder {
    private static final int DEFAULT_MAX_PARAMS = 1024;

    private final Charset charset;
    private final String uri;
    private final boolean hasPath;
    private final int maxParams;
    private String path;
    private Map<String, List<String>> params;
    private int nParams;

    /**
     * Creates a new decoder that decodes the specified URI. The decoder will assume that the query
     * string is encoded in UTF-8.
     */
    public UriQueryDecoder(String uri) {
        this(uri, Charset.forName("UTF-8"));
    }

    /**
     * Creates a new decoder that decodes the specified URI encoded in the specified charset.
     */
    public UriQueryDecoder(String uri, boolean hasPath) {
        this(uri, Charset.forName("UTF-8"), hasPath);
    }

    /**
     * Creates a new decoder that decodes the specified URI encoded in the specified charset.
     */
    public UriQueryDecoder(String uri, Charset charset) {
        this(uri, charset, true);
    }

    /**
     * Creates a new decoder that decodes the specified URI encoded in the specified charset.
     */
    public UriQueryDecoder(String uri, Charset charset, boolean hasPath) {
        this(uri, charset, hasPath, DEFAULT_MAX_PARAMS);
    }

    /**
     * Creates a new decoder that decodes the specified URI encoded in the specified charset.
     */
    public UriQueryDecoder(String uri, Charset charset, boolean hasPath, int maxParams) {
        if (uri == null) {
            throw new NullPointerException("uri");
        }
        if (charset == null) {
            throw new NullPointerException("charset");
        }
        if (maxParams <= 0) {
            throw new IllegalArgumentException("maxParams: " + maxParams + " (expected: > 0)");
        }
        this.uri = uri;
        this.charset = charset;
        this.maxParams = maxParams;
        this.hasPath = hasPath;
    }

    /**
     * Creates a new decoder that decodes the specified URI. The decoder will assume that the query
     * string is encoded in UTF-8.
     */
    public UriQueryDecoder(URI uri) {
        this(uri, Charset.forName("UTF-8"));
    }

    /**
     * Creates a new decoder that decodes the specified URI encoded in the specified charset.
     */
    public UriQueryDecoder(URI uri, Charset charset) {
        this(uri, charset, DEFAULT_MAX_PARAMS);
    }

    /**
     * Creates a new decoder that decodes the specified URI encoded in the specified charset.
     */
    public UriQueryDecoder(URI uri, Charset charset, int maxParams) {
        if (uri == null) {
            throw new NullPointerException("uri");
        }
        if (charset == null) {
            throw new NullPointerException("charset");
        }
        if (maxParams <= 0) {
            throw new IllegalArgumentException("maxParams: " + maxParams + " (expected: > 0)");
        }

        String rawPath = uri.getRawPath();
        if (rawPath != null) {
            hasPath = true;
        } else {
            rawPath = "";
            hasPath = false;
        }
        // Also take care of cut of things like "http://localhost"
        this.uri = uri.getRawQuery() == null ? rawPath : rawPath + '?' + uri.getRawQuery();

        this.charset = charset;
        this.maxParams = maxParams;
    }

    /**
     * Returns the uri used to initialize this {@link UriQueryDecoder}.
     */
    public String uri() {
        return uri;
    }

    /**
     * Returns the decoded path string of the URI.
     */
    public String path() {
        if (path == null) {
            if (!hasPath) {
                path = "";
            } else {
                int pathEndPos = uri.indexOf('?');
                path = decodeComponent(pathEndPos < 0 ? uri : uri.substring(0, pathEndPos),
                        charset);
            }
        }
        return path;
    }

    /**
     * Returns the decoded key-value parameter pairs of the URI.
     */
    public Map<String, List<String>> parameters() {
        if (params == null) {
            if (hasPath) {
                int pathEndPos = uri.indexOf('?');
                if (pathEndPos >= 0 && pathEndPos < uri.length() - 1) {
                    decodeParams(uri.substring(pathEndPos + 1));
                } else {
                    params = Collections.emptyMap();
                }
            } else {
                if (uri.isEmpty()) {
                    params = Collections.emptyMap();
                } else {
                    decodeParams(uri);
                }
            }
        }
        return params;
    }

    public List<String> getParameter(String key) {
        if (params == null) {
            Map<String, List<String>> parameters = this.parameters();
            return parameters.get(key);
        }
        return params.get(key);
    }

    private void decodeParams(String s) {
        Map<String, List<String>> params = this.params = new LinkedHashMap<>();
        nParams = 0;
        String name = null;
        // Beginning of the unprocessed region
        int pos = 0;
        // End of the unprocessed region
        int i;
        // Current character
        char c;
        for (i = 0; i < s.length(); i++) {
            c = s.charAt(i);
            if (c == '=' && name == null) {
                if (pos != i) {
                    name = decodeComponent(s.substring(pos, i), charset);
                }
                pos = i + 1;
                // http://www.w3.org/TR/html401/appendix/notes.html#h-B.2.2
            } else if (c == '&' || c == ';') {
                if (name == null && pos != i) {
                    // We haven't seen an `=' so far but moved forward.
                    // Must be a param of the form '&a&' so add it with
                    // an empty value.
                    if (!addParam(params, decodeComponent(s.substring(pos, i), charset), "")) {
                        return;
                    }
                } else if (name != null) {
                    if (!addParam(params, name, decodeComponent(s.substring(pos, i), charset))) {
                        return;
                    }
                    name = null;
                }
                pos = i + 1;
            }
        }

        // Are there characters we haven't dealt with?
        if (pos != i) {
            // Yes and we haven't seen any `='.
            if (name == null) {
                addParam(params, decodeComponent(s.substring(pos, i), charset), "");
            } else { // Yes and this must be the last value.
                addParam(params, name, decodeComponent(s.substring(pos, i), charset));
            }
        } else if (name != null) {
            // Have we seen a name without value?
            addParam(params, name, "");
        }
    }

    private boolean addParam(Map<String, List<String>> params, String name, String value) {
        if (nParams >= maxParams) {
            return false;
        }

        List<String> values = params.get(name);
        if (values == null) {
            // Often there's only 1 value.
            values = new ArrayList<>(1);
            params.put(name, values);
        }
        values.add(value);
        nParams++;
        return true;
    }

    public static String decodeComponent(final String s) {
        return decodeComponent(s, Charset.forName("UTF-8"));
    }

    public static String decodeComponent(final String s, final Charset charset) {
        if (s == null) {
            return "";
        }
        final int size = s.length();
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            final char c = s.charAt(i);
            if (c == '%' || c == '+') {
                modified = true;
                break;
            }
        }
        if (!modified) {
            return s;
        }
        final byte[] buf = new byte[size];
        // position in `buf'.
        int pos = 0;
        for (int i = 0; i < size; i++) {
            char c = s.charAt(i);
            switch (c) {
                case '+':
                    // "+" -> " "
                    buf[pos++] = ' ';
                    break;
                case '%':
                    if (i == size - 1) {
                        throw new IllegalArgumentException(
                                "unterminated escape" + " sequence at end of string: " + s);
                    }
                    c = s.charAt(++i);
                    if (c == '%') {
                        // "%%" -> "%"
                        buf[pos++] = '%';
                        break;
                    }
                    if (i == size - 1) {
                        throw new IllegalArgumentException(
                                "partial escape" + " sequence at end of string: " + s);
                    }
                    c = decodeHexNibble(c);
                    final char c2 = decodeHexNibble(s.charAt(++i));
                    if (c == Character.MAX_VALUE || c2 == Character.MAX_VALUE) {
                        throw new IllegalArgumentException(
                                "invalid escape sequence `%" + s.charAt(i - 1) + s.charAt(i)
                                        + "' at index " + (i - 2) + " of: " + s);
                    }
                    c = (char) (c * 16 + c2);
                    // Fall through.
                default:
                    buf[pos++] = (byte) c;
                    break;
            }
        }
        return new String(buf, 0, pos, charset);
    }

    private static char decodeHexNibble(final char c) {
        if ('0' <= c && c <= '9') {
            return (char) (c - '0');
        } else if ('a' <= c && c <= 'f') {
            return (char) (c - 'a' + 10);
        } else if ('A' <= c && c <= 'F') {
            return (char) (c - 'A' + 10);
        } else {
            return Character.MAX_VALUE;
        }
    }
}
