<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
  
  <body>
    <a href="hello">hello</a>
    <a href="ajax1">返回Hello World</a>
    <a href="ajax2">返回Hello World</a>
    <a href="user/registerForm">注册</a>
    <a href="user/loginForm">登陆</a>
    <a href="pathVariable/1">测试@PathVariable注解</a>
    <a href="requestHeaderTest">测试@RequestHeader注解</a>
    <a href="cookieValueTest">测试@CookieValue注解</a>
  </body>
</html>
