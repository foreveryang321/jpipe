// package top.ylonline.jpipe.util;
//
// import lombok.extern.slf4j.Slf4j;
//
// import java.util.Properties;
//
// /**
//  * @author YL
//  */
// @Slf4j
// public class ConfigUtils {
//     private static volatile Properties PROPERTIES;
//
//     private ConfigUtils() {
//     }
//
//     public static Properties getProperties() {
//         if (PROPERTIES == null) {
//             synchronized (ConfigUtils.class) {
//                 if (PROPERTIES == null) {
//                     PROPERTIES = new Properties();
//                 }
//             }
//         }
//         return PROPERTIES;
//     }
//
//     public static String getProperty(String key) {
//         return getProperty(key, null);
//     }
//
//     public static String getProperty(String key, String defaultValue) {
//         return getProperties().getProperty(key, defaultValue);
//     }
// }
