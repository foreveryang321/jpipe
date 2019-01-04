package top.ylonline.jpipe.boot.context.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.logging.LoggingApplicationListener;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import top.ylonline.jpipe.common.Version;

import static top.ylonline.jpipe.boot.util.JpipeUtils.JPIPE_GITHUB_URL;
import static top.ylonline.jpipe.boot.util.JpipeUtils.LINE_SEPARATOR;

/**
 * Jpipe Welcome Logo {@link ApplicationListener}
 *
 * @author Created by YL on 2018/9/10
 */
@Order(LoggingApplicationListener.DEFAULT_ORDER + 1)
@Slf4j
public class JpipeConfigApplicationListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        String bannerText = buildBannerText();
        if (log.isInfoEnabled()) {
            log.info(bannerText);
        } else {
            System.out.print(bannerText);
        }
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
