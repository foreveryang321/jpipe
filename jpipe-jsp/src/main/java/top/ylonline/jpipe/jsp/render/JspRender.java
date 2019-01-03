package top.ylonline.jpipe.jsp.render;

import lombok.extern.slf4j.Slf4j;
import top.ylonline.jpipe.render.AbstractRender;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Created by YL on 2018/8/8
 */
@Slf4j
public class JspRender extends AbstractRender {
    private PrintWriter out;
    private final Object lock;

    public JspRender(PrintWriter out) {
        this.out = out;
        this.lock = this;
    }

    @Override
    protected void writeAndFlush(String str) {
        try {
            synchronized (lock) {
                ensureOpen();
                this.out.println(str);
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
