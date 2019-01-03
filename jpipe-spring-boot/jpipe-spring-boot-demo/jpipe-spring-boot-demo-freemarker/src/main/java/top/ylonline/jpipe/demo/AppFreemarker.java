package top.ylonline.jpipe.demo;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.servlet.WebSocketServletAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author YL
 */
@SpringBootApplication(
        exclude = {
                DataSourceAutoConfiguration.class,
                WebSocketServletAutoConfiguration.class,
                JmxAutoConfiguration.class
        }
)
public class AppFreemarker extends SpringBootServletInitializer {

    /**
     * Common
     */
    private static SpringApplicationBuilder configureSpringBuilder(SpringApplicationBuilder builder) {
        return builder.sources(AppFreemarker.class);
    }

    /**
     * for WAR deploy
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return configureSpringBuilder(builder);
    }

    /**
     * for JAR deploy
     */
    public static void main(String[] args) {
        configureSpringBuilder(new SpringApplicationBuilder()).run(args);
    }
}
