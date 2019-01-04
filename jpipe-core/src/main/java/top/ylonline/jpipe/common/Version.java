package top.ylonline.jpipe.common;

import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.security.CodeSource;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Created by YL on 2018/9/10
 */
@Slf4j
public class Version {
    /**
     * Jpipe implementation version, usually is jar version.
     */
    private static final String VERSION = getVersion(Version.class, "");

    static {
        // check if there's duplicated jar
        Version.checkDuplicate(Version.class);
    }

    private Version() {
    }

    public static String getVersion() {
        return VERSION;
    }

    public static String getVersion(Class<?> cls, String defaultVersion) {
        try {
            // find version info from MANIFEST.MF first
            String version = cls.getPackage().getImplementationVersion();
            if (version == null || version.length() == 0) {
                version = cls.getPackage().getSpecificationVersion();
            }
            if (version == null || version.length() == 0) {
                // guess version from jar file name if nothing's found from MANIFEST.MF
                CodeSource codeSource = cls.getProtectionDomain().getCodeSource();
                if (codeSource == null) {
                    log.info("No codeSource for class " + cls.getName() + " when getVersion, use default version " + defaultVersion);
                } else {
                    String file = codeSource.getLocation().getFile();
                    if (file != null && file.length() > 0 && file.endsWith(".jar")) {
                        file = file.substring(0, file.length() - 4);
                        int i = file.lastIndexOf('/');
                        if (i >= 0) {
                            file = file.substring(i + 1);
                        }
                        i = file.indexOf("-");
                        if (i >= 0) {
                            file = file.substring(i + 1);
                        }
                        while (file.length() > 0 && !Character.isDigit(file.charAt(0))) {
                            i = file.indexOf("-");
                            if (i >= 0) {
                                file = file.substring(i + 1);
                            } else {
                                break;
                            }
                        }
                        version = file;
                    }
                }
            }
            // return default version if no version info is found
            return version == null || version.length() == 0 ? defaultVersion : version;
        } catch (Throwable e) {
            // return default version when any exception is thrown
            log.error("return default version, ignore exception " + e.getMessage(), e);
            return defaultVersion;
        }
    }

    private static void checkDuplicate(Class<?> cls, boolean failOnError) {
        checkDuplicate(cls.getName().replace('.', '/') + ".class", failOnError);
    }

    private static void checkDuplicate(Class<?> cls) {
        checkDuplicate(cls, false);
    }

    private static void checkDuplicate(String path, boolean failOnError) {
        try {
            // search in caller's classloader
            Enumeration<URL> urls = Version.class.getClassLoader().getResources(path);
            Set<String> files = new HashSet<>();
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (url != null) {
                    String file = url.getFile();
                    if (file != null && file.length() > 0) {
                        files.add(file);
                    }
                }
            }
            // duplicated jar is found
            if (files.size() > 1) {
                String error = "Duplicate class " + path + " in " + files.size() + " jar " + files;
                if (failOnError) {
                    throw new IllegalStateException(error);
                } else {
                    log.error(error);
                }
            }
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }
    }
}
