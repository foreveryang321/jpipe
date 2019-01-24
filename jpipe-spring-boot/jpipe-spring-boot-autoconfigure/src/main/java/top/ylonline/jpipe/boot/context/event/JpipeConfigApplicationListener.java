package top.ylonline.jpipe.boot.context.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.logging.LoggingApplicationListener;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import top.ylonline.jpipe.common.Version;

import java.util.concurrent.atomic.AtomicBoolean;

import static top.ylonline.jpipe.boot.util.JpipeUtils.JPIPE_GITHUB_URL;
import static top.ylonline.jpipe.boot.util.JpipeUtils.LINE_SEPARATOR;

/**
 * Jpipe Welcome Logo {@link ApplicationListener}
 *
 * @author YL
 */
@Order(LoggingApplicationListener.DEFAULT_ORDER + 1)
@Slf4j
public class JpipeConfigApplicationListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    private static AtomicBoolean processed = new AtomicBoolean(false);

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        if (processed.get()) {
            return;
        }

        String bannerText = buildBannerText();
        if (log.isInfoEnabled()) {
            log.info(bannerText);
        } else {
            System.out.print(bannerText);
        }
        processed.compareAndSet(false, true);
    }

    private String buildBannerText() {
        StringBuilder sb = new StringBuilder(32);
        sb.append(LINE_SEPARATOR)
                .append(LINE_SEPARATOR)
                .append(" :: Jpipe                    :: v").append(Version.getVersion())
                .append(LINE_SEPARATOR)
                .append(" :: Jpipe Github             :: ").append(JPIPE_GITHUB_URL)
                .append(LINE_SEPARATOR)
        ;
        return sb.toString();
    }
}
