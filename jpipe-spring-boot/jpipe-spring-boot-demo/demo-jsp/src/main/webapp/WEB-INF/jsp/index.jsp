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