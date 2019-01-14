<#assign jp=JspTaglibs["http://java.yl-online.top/jsp/jpipe"] />
<html lang="en">
<head>
    <title>index</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no">
    <script type="text/javascript" src="jpipe.core.js"></script>
    <style type="text/css">
        * {
            width: 100%;
            margin: 0;
            padding: 0;
        }

        h1 {
            text-align: center;
        }

        #pagelet1, #pagelet2 {
            width: 92%;
            height: 200px;
            margin: 20px auto;
            text-align: center;
            border: 1px solid cadetblue;
        }
    </style>
</head>
<body>
<h1>index</h1>
<div id="pagelet1">第 1 个分块任务加载中...</div>
<div id="pagelet2">第 2 个分块任务加载中...</div>
<@jp.pipe>
    <@jp.pagelet domid="pagelet1" bean="testPagelet1" var="item" uri="id=123&name=forever杨">
        <h1>第 1 个分块任务</h1>
        <p>${item.id}</p>
    </@jp.pagelet>
    <@jp.pagelet domid="pagelet2" bean="testPagelet2" var="item2" uri="id=456&name=forever杨2">
        <h1>第 2 个分块任务</h1>
        <p>${item2.name}</p>
    </@jp.pagelet>
</@jp.pipe>
</body>
</html>