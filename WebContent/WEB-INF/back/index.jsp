<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>後台首頁</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/standard.css"/>
</head>
<body>
    <div id="wrapper">
        <div id="headerwrap">
        	<div id="header">
            	<p><h1>安安線上書局後台管理系統</h1></p>
        	</div>
        </div>

        <div id="navigationwrap">
        	<div id="navigation">
            	<p>
            		後台首頁&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            		<s:url id="misIndex" namespace="/back/mis" action="index" method="execute"/>
            		<s:a href="%{misIndex}">資訊部</s:a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            		<a href="<%=request.getContextPath()%>/back/sales/index!execute">銷售部</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            		倉管部&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            		採購部&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            		物流部&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            		客服部&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            		財務部&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            	</p>
        	</div>
        </div>

        <div id="contentliquid">
			<div id="contentwrap">
        		<div id="content_up">
           			<p>這裡可能是公司的公告欄Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer nec odio. Praesent libero. Sed cursus ante dapibus diam. Sed nisi. Nulla quis sem at nibh elementum imperdiet. Duis sagittis ipsum. Praesent mauris. Fusce nec tellus sed augue semper porta. Mauris massa. Vestibulum lacinia arcu eget nulla. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Curabitur sodales ligula in libero. Sed dignissim lacinia nunc. </p><p>Sed lacinia, urna non tincidunt mattis, tortor neque adipiscing diam, a cursus ipsum ante quis turpis. Nulla facilisi. Ut fringilla. Suspendisse potenti. Nunc feugiat mi a tellus consequat imperdiet. Vestibulum sapien. Proin quam. Etiam ultrices. Suspendisse in justo eu magna luctus suscipit. Sed lectus. Integer euismod lacus luctus magna. Quisque cursus, metus vitae pharetra auctor, sem massa mattis sem, at interdum magna augue eget diam. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Morbi lacinia molestie dui. Praesent blandit dolor. </p><p>Sed non quam. In vel mi sit amet augue congue elementum. Morbi in ipsum sit amet pede facilisis laoreet. Donec lacus nunc, viverra nec, blandit vel, egestas et, augue. Vestibulum tincidunt malesuada tellus. Ut ultrices ultrices enim. Curabitur sit amet mauris. Morbi in dui quis est pulvinar ullamcorper. Nulla facilisi. Integer lacinia sollicitudin massa. Cras metus. Sed aliquet risus a tortor. Integer id quam. </p>
        		</div>
				<div id="content_down">
					<p>我是另一個content啦啦啦</p>
				</div>
			</div>
		</div>

        <div id="leftcolumnwrap">
        	<div id="left_up">
				<p>
					<a href="<%=request.getContextPath()%>/back/general/queryMails.action">收件夾
				<s:if test="%{worker!=null && worker.inbox!=null}">
					(<s:property value="worker.inbox.mailsCount"/>)
				</s:if>
					</a>
				</p>
            	<p>您好:
            	<s:if test="%{worker!=null && worker.name!=null}">
            		<s:property value="worker.name"/>
            	</s:if>
            	<s:else>
            		訪客
            	</s:else>
            	<s:a namespace="/back" href="logout">(登出)</s:a>
            	</p>
        	</div>
			<div id="left_down">
				<p>Click Me</p>
				<p>Fxxk Me</p>
				<p>oh yes</p>
				<p><a href="<%=request.getContextPath()%>/back/general/takeLongTime.action">測試execAndWait</a></p>
			</div>
        </div>

        <div id="footerwrap">
        	<div id="footer">
            	<p>This is the Footer</p>
        	</div>
        </div>
    </div>
</body>
</html>