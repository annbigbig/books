<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>後台登入表單</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/back/login/style.css"/>
<!-- 錯誤訊息使用紅字 -->
<style type="text/css">
.errorMessage {
	color: #FF0000;
}
</style>
</head>
<body>
      
  <form method="post" action="<%=request.getContextPath()%>/back/login.action" class="login">
    <p>
      <label for="name">Account:</label>
      <input type="text" name="name" id="name" value="Enter your account ID">
      <!-- 從LoginAction產生的fielderror -->
      <s:fielderror><s:param>name</s:param></s:fielderror>
    </p>

    <p>
      <label for="password">Password:</label>
      <input type="password" name="password" id="password">
      <!-- 從LoginAction產生的fielderror -->
      <s:fielderror><s:param>password</s:param></s:fielderror>
    </p>

    <p class="login-submit">
      <button type="submit" class="login-button">Login</button>
    </p>

    <p class="forgot-password"><a href="<%=request.getContextPath()%>/back/index.action">Forgot your password?</a></p>
  </form>
  
</body>
</html>