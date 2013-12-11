<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:property value="reason"/></title>
<link href="<%=request.getContextPath()%>/back/denied/style.css" rel="stylesheet">
<script src="<%=request.getContextPath()%>/jquery/jquery-1.9.1.js"></script>
<script>
var settimmer = 0;
$(function(){
        window.setInterval(function() {
            var timeCounter = $("b[id=show-time]").html();
            var updateTime = eval(timeCounter)- eval(1);
            $("b[id=show-time]").html(updateTime);

            if(updateTime == 0){
                window.location = ("${url}");
            }
        }, 1000);

});
</script>
</head>
<body>
<s:property value="reason"/><br>
<s:property value="message"/><br>
<div id="my-timer">
        Page Will Redirect with in <b id="show-time">10</b> seconds        
</div>
</body>
</html>