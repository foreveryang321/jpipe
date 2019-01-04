<#--<#assign pipe="top.ylonline.jpipe.freemarker.tag.PipeTag"?new() />-->
<#--<#assign pagelet="top.ylonline.jpipe.freemarker.tag.PageletTag"?new() />-->
<#--<#assign param="top.ylonline.jpipe.freemarker.tag.ParamTag"?new() />-->
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
<@jp.pipe async=true>
    <@jp.pagelet domId="pagelet1" templateId="templateId1" bean="testPagelet1">
        <@jp.param name="id" value="1" />
        <@jp.param name="age" value="11" />
    </@jp.pagelet>
    <@jp.pagelet domId="pagelet2" templateId="templateId2" bean="testPagelet2" jsMethod="JP.view">
        <@jp.param name="id" value="3" />
        <@jp.param name="age" value="33" />
    </@jp.pagelet>
</@jp.pipe>
</body>
</html>