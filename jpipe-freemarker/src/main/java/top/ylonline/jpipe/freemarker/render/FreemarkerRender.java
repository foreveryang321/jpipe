package top.ylonline.jpipe.freemarker.render;

import lombok.extern.slf4j.Slf4j;
import top.ylonline.jpipe.render.AbstractRender;

import java.io.IOException;
import java.io.Writer;

/**
 * @author Created by YL on 2018/8/26
 */
@Slf4j
public class FreemarkerRender extends AbstractRender {
    private Writer out;
    private final Object lock;

    private final String lineSeparator;

    public FreemarkerRender(Writer out) {
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

    /**
     * Checks to make sure that the stream has not been closed
     */
    private void ensureOpen() throws IOException {
        if (this.out == null) {
            throw new IOException("Stream closed");
        }
    }

}
