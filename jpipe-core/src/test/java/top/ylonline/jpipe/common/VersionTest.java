package top.ylonline.jpipe.common;

import org.junit.Test;

/**
 * @author Created by YL on 2018/9/18
 */
public class VersionTest {

    @Test
    public void getVersion() {
        String version = Version.getVersion();
        System.out.println("version: " + version);
    }

    @Test
    public void getVersion1() {
        String version = Version.getVersion(Version.class, "1.0.1");
        System.out.println("version: " + version);
    }
}