<%@ page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jp" uri="http://java.yl-online.top/jsp/jpipe" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>index</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no">
    <script type="text/javascript" src="${ctx}/resources/jpipe.js"></script>
</head>
<body>
<h1>index</h1>
<div id="pagelet1"></div>
<div id="pagelet2"></div>
<jp:pipe>
    <jp:pagelet domId="pagelet1" templateId="as.jdfklsjdl" bean="testPagelet1">
        <jp:param name="id" value="${id}"/>
        <jp:param name="name" value="${name}"/>
    </jp:pagelet>
    <jp:pagelet domId="pagelet2" bean="testPagelet2">
        <jp:param name="id" value="年级信息"/>
        <jp:param name="name" value="班级信息"/>
        <jp:param name="age">21</jp:param>
    </jp:pagelet>
</jp:pipe>
</body>
</html>