// package top.ylonline.jpipe.util;
//
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.core.env.CompositePropertySource;
// import org.springframework.core.env.ConfigurableEnvironment;
// import org.springframework.core.env.EnumerablePropertySource;
// import org.springframework.core.env.MutablePropertySources;
// import org.springframework.core.env.PropertySource;
// import org.springframework.util.ObjectUtils;
// import top.ylonline.jpipe.common.Cts;
//
// import java.util.Collections;
// import java.util.LinkedHashMap;
// import java.util.Map;
// import java.util.SortedMap;
// import java.util.TreeMap;
//
// /**
//  * @author YL
//  */
// @Slf4j
// public abstract class EnvUtils {
//
//     /**
//      * Filters Jpipe Properties from {@link ConfigurableEnvironment}
//      *
//      * @param environment {@link ConfigurableEnvironment}
//      *
//      * @return Read-only SortedMap
//      */
//     public static SortedMap<String, Object> filterJpipeProperties(ConfigurableEnvironment environment) {
//         SortedMap<String, Object> map = new TreeMap<>();
//         Map<String, Object> properties = EnvUtils.extractProperties(environment);
//         for (Map.Entry<String, Object> entry : properties.entrySet()) {
//             String propertyName = entry.getKey();
//             Object value = entry.getValue();
//             if (propertyName.startsWith(Cts.JPIPE_PREFIX + Cts.PROPERTY_NAME_SEPARATOR)) {
//                 map.put(propertyName, value);
//             }
//         }
//         return Collections.unmodifiableSortedMap(map);
//     }
//
//     /**
//      * Extras The properties from {@link ConfigurableEnvironment}
//      *
//      * @param environment {@link ConfigurableEnvironment}
//      *
//      * @return Read-only Map
//      */
//     private static Map<String, Object> extractProperties(ConfigurableEnvironment environment) {
//         return Collections.unmodifiableMap(doExtraProperties(environment));
//     }
//
//     private static Map<String, Object> doExtraProperties(ConfigurableEnvironment environment) {
//         Map<String, Object> properties = new LinkedHashMap<>(); // orderly
//         Map<String, PropertySource<?>> map = doGetPropertySources(environment);
//         for (PropertySource<?> source : map.values()) {
//             if (source instanceof EnumerablePropertySource) {
//                 EnumerablePropertySource propertySource = (EnumerablePropertySource) source;
//                 String[] propertyNames = propertySource.getPropertyNames();
//                 if (ObjectUtils.isEmpty(propertyNames)) {
//                     continue;
//                 }
//                 for (String propertyName : propertyNames) {
//                     if (!properties.containsKey(propertyName)) { // put If absent
//                         properties.put(propertyName, propertySource.getProperty(propertyName));
//                     }
//                 }
//             }
//         }
//         return properties;
//
//     }
//
//     private static Map<String, PropertySource<?>> doGetPropertySources(ConfigurableEnvironment environment) {
//         Map<String, PropertySource<?>> map = new LinkedHashMap<>();
//         MutablePropertySources sources = environment.getPropertySources();
//         for (PropertySource<?> source : sources) {
//             extract("", map, source);
//         }
//         return map;
//     }
//
//     private static void extract(String root, Map<String, PropertySource<?>> map, PropertySource<?> source) {
//         if (source instanceof CompositePropertySource) {
//             for (PropertySource<?> nest : ((CompositePropertySource) source)
//                     .getPropertySources()) {
//                 extract(source.getName() + ":", map, nest);
//             }
//         } else {
//             map.put(root + source.getName(), source);
//         }
//     }
// }
