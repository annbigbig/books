<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="refresh" content="1;url=<s:url includeParams="all" />"/>
<title>自定義等待頁</title>
<style type="text/css">
body {background-color:#5050b5;}
div {color:#FFFFFF;}
.div_table-cell{
	width:800px; 
	height:600px;	
	background-color:#5050b5; 
	display:table-cell; 
	text-align:center; 
	vertical-align:middle;
	border:solid 2px #fff;	
	}
 
/* IE6 hack */
.div_table-cell span{
	height:100%; 
	display:inline-block;
	}
 
/* 讓table-cell下的所有元素都居中 */
.div_table-cell *{ vertical-align:middle;}
</style>
</head>
<body>
	<div class="div_table-cell">
		<span></span>
		Please Wait...<s:property value="progress"/>%
	</div>
</body>
</html>