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
    <@jp.pagelet domId="pagelet1" templateId="as.jdfklsjdl" bean="testPagelet1">
        <@jp.param name="id" value="${id}"/>
        <@jp.param name="name" value="${name}"/>
    </@jp.pagelet>
    <@jp.pagelet domId="pagelet2" bean="testPagelet2" jsMethod="JP.view">
        <@jp.param name="id" value="年级信息"/>
        <@jp.param name="name" value="班级信息"/>
        <@jp.param name="age">21</@jp.param>
    </@jp.pagelet>
</@jp.pipe>
</body>
</html>