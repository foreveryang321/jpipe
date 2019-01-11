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