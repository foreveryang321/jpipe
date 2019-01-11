# Jpipe

A Java implementation of Facebook's bigPipe technology.

Jpipe 是通过自定义标签实现的，所以对后端代码零侵入。

> HTML 是完成前台页面的功能，而自定义标签可以在后台完成某些操作。



## 特性

- jsp 标签支持
- freemarker 指令支持
- freemarker 中使用 jsp 标签



<!-- more -->



## 为什么使用 BigPipe
- 解决速度瓶颈
- 降低延迟时间



## BigPipe 适合什么场景

主要适用于：

1. 请求时间较长，后端程序需要读取多个API获取数据
2. 页面内容可以划分成多个区块显示，且各个区块之间的关联不大（松耦合）
3. 对SEO需求较弱



## 多种实现方式的对比

| 类型          | 请求数 | 服务器端压力       | 用户体验 | 网页加载速度 | 模块加载顺序 | 实现难度 | 后期维护难度 |
| ------------- | ------ | ------------------ | -------- | ------------ | ------------ | -------- | ------------ |
| 普通          | 1      | 小                 | 差       | 慢           | 文档流顺序   | 简单     | 一般         |
| Ajax          | 多     | 大                 | 好       | 快           | 不确定       | 困难     | 困难         |
| 单线程BigPipe | 1      | 小                 | 好       | 慢           | 自定义       | 一般     | 一般         |
| 多线程BigPipe | 1      | 一般（线程池引起） | 好       | 最快         | 不确定       | 最困难   | 一般         |



## 开始

### 线程池配置

| 属性                       | 类型    | 是否必填 | 缺省值 | 说明             | 描述                                                         |
| -------------------------- | ------- | -------- | ------ | ---------------- | ------------------------------------------------------------ |
| core-size                  | int     | 否       | -1     | 核心线程数       | 最小空闲线程数，无论如何都会存活的最小线程数                 |
| max-size                   | int     | 否       | 1024   | 最大线程数       | Jpipe 能创建用来处理 pagelet 的最大线程数                    |
| queue-size                 | int     | 否       | 1024   | 最大等待对列数   | 请求并发大于 max-size，则被放入队列等待                      |
| keep-alive                 | long    | 否       | 60000  | 最大空闲时间(ms) | 超过这个空闲时间，且线程数大于 core-size 的，被回收直到线程数等于core-size |
| pre-start-all-core-threads | boolean | 否       | false  | 预热线程池       | 是否预先启动 core-size 个线程                                |


### pepe 标签、指令

| 属性  | 类型    | 是否必填 | 缺省值 | 说明                    | 描述 |
| ----- | ------- | -------- | ------ | ----------------------- | ---- |
| async | boolean | 否       | true   | 是否异步执行pagelet任务 |      |



### pagelet 标签、指令

| 属性       | 类型   | 是否必填 | 缺省值  | 说明             | 描述 |
| ---------- | ------ | -------- | ------- | ---------------- | ---- |
| domid     | string | 是       |         | html document Id |      |
| bean | string | 是       |         | spring bean name           ||
| var       | string | 是       |         | variable 参数 |      |
| uri       | string | 否       |         | uri 参数 |      |
| jsmethod | string | 否 | JP.view | 包装数据的js函数 | |



### 与Spring集成

- 通过`JpipeThreadPoolFactoryBean`类

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
		http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean class="top.ylonline.jpipe.spring.JpipeSpringFactoryBean"/>
    
    <bean id="pool-1" class="top.ylonline.jpipe.threadpool.common.Pool">
        <property name="coreSize" value="-1"/>
        <property name="maxSize" value="20"/>
        <property name="preStartAllCoreThreads" value="false"/>
        <property name="keepAlive" value="12000000"/>
        <property name="queueSize" value="500"/>
    </bean>

    <!-- 工场模式 -->
    <bean class="top.ylonline.jpipe.threadpool.util.JpipeThreadPoolFactoryBean">
        <property name="pool" ref="pool-1"/>
    </bean>

    <!-- 或者 
    <bean class="top.ylonline.jpipe.threadpool.util.JpipeThreadPoolFactoryBean">
        <property name="pool">
            <bean class="top.ylonline.jpipe.threadpool.common.Pool">
                <property name="coreSize" value="4"/>
                <property name="maxSize" value="10"/>
                <property name="preStartAllCoreThreads" value="true"/>
                <property name="keepAlive" value="60000"/>
                <property name="queueSize" value="500"/>
            </bean>
        </property>
    </bean>
	-->
</beans>
```



- 通过`JpipeThreadPoolBuilder`类

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
		http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean class="top.ylonline.jpipe.spring.JpipeSpringFactoryBean"/>
    
    <!-- builder 模式 -->
    <bean id="jpipeThreadPoolBuilder" class="top.ylonline.jpipe.threadpool.util.JpipeThreadPoolBuilder">
        <property name="pool">
            <bean class="top.ylonline.jpipe.threadpool.common.Pool">
                <property name="coreSize" value="1"/>
                <property name="maxSize" value="1"/>
                <property name="preStartAllCoreThreads" value="true"/>
                <property name="keepAlive" value="1"/>
                <property name="queueSize" value="1"/>
            </bean>
        </property>
    </bean>
    <bean id="jpipeThreadPool-3" factory-bean="jpipeThreadPoolBuilder" factory-method="build"/>
</beans>
```

- 通过 JavaBean 方式

```java
@Bean
public JpipeSpringFactoryBean jpipeSpringFactoryBean(){
    return new JpipeSpringFactoryBean();
}

@Bean
public JpipeThreadPoolExecutor jpipeThreadPoolExecutor() {
    Pool pool = new Pool();
    pool.setCoreSize(10);
    pool.setMaxSize(1024);
    pool.setPreStartAllCoreThreads(true);
    pool.getKeepAlive(60000);
    pool.getQueueSize(512);
    // return new EagerThreadPool().getExecutor(pool);
    return new JpipeThreadPoolBuilder(pool).build();
}
```



- 通过 spring-boot starter

    - pom.xml 依赖
    ```xml
    <dependency>
        <groupId>top.ylonline.jpipe</groupId>
        <artifactId>jpipe-spring-boot-starter</artifactId>
        <version>${version}</version>
    </dependency>
    ```

    - application.yml 配置
    ```yaml
    jpipe:
      # enabled: true
      pool:
        pre-start-all-core-threads: true
        core-size: -1
        max-size: 20
        queue-size: 10
        keep-alive: 10000
    ```



### 定义一个 pagelet

使用 Spring 的 `@Service` 定义一个pagelet，实现 PageletBean 接口的 doExec 方法

```java
@Service("testPagelet1")
public class PageletServiceTest implements PageletBean {

    @Override
    public Map<String, Object> doExec(final Map<String, String> params) {
        Map<String, Object> data = new HashMap<>(params);
        try {
            TimeUnit.MILLISECONDS.sleep(new Random().nextInt(5000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return data;
    }
}
```



## jpipe js

> jpipe.core.js
```javascript
;(function (root, factory) {
    if (typeof exports === 'object') {
        module.exports = exports = factory();
    } else if (typeof define === 'function' && define.amd) {
        define([], factory);
    } else {
        root.JP = factory();
    }
}(this, function () {
    var JP = JP || (function (window) {
        return {
            view: function (json) {
                var id = json['id'];
                document.getElementById(id).innerHTML = json['html'];
            }
        };
    }(window));
    return JP;
}));
```



### JSP 标签

- 引入标签 `<%@ taglib prefix="jp" uri="http://java.yl-online.top/jsp/jpipe" %>`
- 使用自定义标签，最好放到`</body>`上面，这样就不会堵塞首屏dom的渲染
```html
<%@ page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jp" uri="http://java.yl-online.top/jsp/jpipe" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html lang="en">
<head>
    <title>index</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no">
    <script type="text/javascript" src="${ctx}/resources/jpipe.core.js"></script>
</head>
<body>
<h1>index</h1>
<div id="pagelet1"></div>
<div id="pagelet2"></div>
<jp:pipe>
    <jp:pagelet domid="pagelet1" bean="testPagelet1" var="item" uri="id=123&name=forever杨">
        <h1>jspbody support</h1>
        <p>${item.id}</p>
    </jp:pagelet>
    <jp:pagelet domid="pagelet2" bean="testPagelet2" var="item2" uri="id=456&name=forever杨2">
        <h1>jspbody support</h1>
        <p>${item2.name}</p>
    </jp:pagelet>
</jp:pipe>
</body>
</html>
```
略：部署到 Tomcat、Jetty 等容器



### FTL 指令
- 通过 freemarker Configuration 配置命名空间
```java
@Configuration
public class MvcWevConfig {
    @Resource
    private freemarker.template.Configuration configuration;

    @PostConstruct
    public void setConfiguration() {
        Version version = freemarker.template.Configuration.getVersion();
        DefaultObjectWrapper wrapper = new DefaultObjectWrapperBuilder(version).build();
        this.configuration.setSharedVariable("jp", new FmHashModel(wrapper));
    }
}
```


```html
<#--<#assign pipe="top.ylonline.jpipe.freemarker.tag.PipeTag"?new() />-->
<#--<#assign pagelet="top.ylonline.jpipe.freemarker.tag.PageletTag"?new() />-->
<html lang="en">
<head>
    <title>index</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no">
    <script type="text/javascript" src="jpipe.core.js"></script>
</head>
<body>
<h1>index</h1>
<div id="pagelet1"></div>
<div id="pagelet2"></div>
<@jp.pipe>
    <@jp.pagelet domid="pagelet1" bean="testPagelet1" var="item" uri="id=123&name=forever杨">
        <h1>testPagelet1 jspbody support</h1>
        <p>${item.id}</p>
    </@jp.pagelet>
    <@jp.pagelet domid="pagelet2" bean="testPagelet2" var="item2" uri="id=456&name=forever杨2">
        <h1>testPagelet2 jspbody support</h1>
        <p>${item2.name}</p>
    </@jp.pagelet>
</@jp.pipe>
</body>
</html>
```



- 通过 assign 指令配置

```html
<#assign pipe="top.ylonline.jpipe.freemarker.tag.PipeTag"?new() />
<#assign pagelet="top.ylonline.jpipe.freemarker.tag.PageletTag"?new() />
<html lang="en">
<head>
    <title>index</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no">
    <script type="text/javascript" src="jpipe.core.js"></script>
</head>
<body>
<h1>index</h1>
<div id="pagelet1"></div>
<div id="pagelet2"></div>
<@jp.pipe>
    <@jp.pagelet domid="pagelet1" bean="testPagelet1" var="item" uri="id=123&name=forever杨">
        <h1>testPagelet1 jspbody support</h1>
        <p>${item.id}</p>
    </@jp.pagelet>
    <@jp.pagelet domid="pagelet2" bean="testPagelet2" var="item2" uri="id=456&name=forever杨2">
        <h1>testPagelet2 jspbody support</h1>
        <p>${item2.name}</p>
    </@jp.pagelet>
</@jp.pipe>
</body>
</html>
```



### 在 FTL 中使用自定义 JSP 标签

FTL 是支持使用 JSP 标签的。如果你的项目本来没有使用 JSP 模版，不推荐这种使用做法。因为自定义 JSP 标签是在 JSP 环境中来写作操作的，需要引入 支持 JSP 1.1或者 JSP 1.2的 Servlet 容器（Tomcat、Jetty等Servlet容器部署），而 FTL 可以在非 Servlet 等 Web 环境中使用。

> 更准确的解释是：尽管 Servlet 容器没有本地的JSP支持，你也可以在 FreeMarker 中使用JSP标签库。 只是确保对JSP 1.2版本(或更新)的 javax.servlet.jsp.* 包在Web应用程序中可用就行。如果你的servlet容器只对JSP 1.1支持， 那么你不得不将下面六个类(比如你可以从Tomcat 5.x或Tomcat 4.x的jar包中提取)复制到Web应用的 WEB-INF/classes/...目录下： javax.servlet.jsp.tagext.IterationTag， javax.servlet.jsp.tagext.TryCatchFinally， javax.servlet.ServletContextListener， javax.servlet.ServletContextAttributeListener， javax.servlet.http.HttpSessionAttributeListener， javax.servlet.http.HttpSessionListener。但是要注意， 因为容器只支持JSP 1.1，通常是使用较早的Servlet 2.3之前的版本，事件监听器可能就不支持，因此JSP 1.2标签库来注册事件监听器会正常工作。

> 截止发稿：JSP已经发布到 2.3版本



#### index.ftl 代码

使用 `<#assign jp=JspTaglibs["http://java.yl-online.top/jsp/jpipe"] />` 引入自定义 JSP 标签
```html
<#assign jp=JspTaglibs["http://java.yl-online.top/jsp/jpipe"] />
<html lang="en">
<head>
    <title>index</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no">
    <script type="text/javascript" src="jpipe.core.js"></script>
</head>
<body>
<h1>index</h1>
<div id="pagelet1"></div>
<div id="pagelet2"></div>
<@jp.pipe>
    <@jp.pagelet domid="pagelet1" bean="testPagelet1" var="item" uri="id=123&name=forever杨">
        <h1>testPagelet1 support</h1>
        <p>${item.id}</p>
    </@jp.pagelet>
    <@jp.pagelet domid="pagelet2" bean="testPagelet2" var="item2" uri="id=456&name=forever杨2">
        <h1>testPagelet2 support</h1>
        <p>${item2.name}</p>
    </@jp.pagelet>
</@jp.pipe>
</body>
</html>
```



#### 部署到 undertow

> maven 依赖


```xml
<!-- undertow 部署 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-undertow</artifactId>
</dependency>
<!-- undertow 部署 -->

<dependency>
    <groupId>javax.servlet.jsp</groupId>
    <artifactId>javax.servlet.jsp-api</artifactId>
    <version>2.3.3</version>
</dependency>
```
由于 undertow 等容器没有 jsp-api 环境，所以需要依赖 javax.servlet.jsp-api 包，同时要通过 TaglibFactory 配置 freemarker 的 classpathTlds。没有这个配置，会报错：freemarker.ext.jsp.TaglibFactory$TaglibGettingException: No TLD was found for the "http://java.yl-online.top/jsp/jpipe" JSP taglib URI. (TLD-s are searched according the JSP 2.2 specification. In development- and embedded-servlet-container setups you may also need the "MetaInfTldSources" and "ClasspathTlds" freemarker.ext.servlet.FreemarkerServlet init-params or the similar system properites.)
> Configuration


```java
@Configuration
public class MvcWevConfig {
    @Resource
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @PostConstruct
    public void loadClassPathTlds() {
        List<String> classpathTlds = new ArrayList<>();
        classpathTlds.add("/META-INF/Jpipe.tld");
        freeMarkerConfigurer.getTaglibFactory().setClasspathTlds(classpathTlds);
    }
}
```



#### 部署到 Tomcat

> maven 依赖


```xml
<!-- 外部 Tomcat 部署 -->
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>javax.servlet-api</artifactId>
    <scope>provided</scope>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-tomcat</artifactId>
    <scope>provided</scope>
</dependency>
<dependency>
    <groupId>org.apache.tomcat.embed</groupId>
    <artifactId>tomcat-embed-jasper</artifactId>
    <scope>provided</scope>
</dependency>
<!-- 外部 Tomcat 部署 -->
```
由于 Tomcat、Jetty中已经有 jsp-api 环境了，这里不需要再依赖 javax.servlet.jsp-api 包

