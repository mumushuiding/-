<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'register.jsp' starting page</title>
    
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
     <form:form modelAttribute="user" method="post" action="user/register">
       <table>
         <tr>
           <td>姓名：</td>
           <td><form:input path="username"></form:input></td>
         </tr>
         <tr>
           <td>性别：</td>
           <td><form:radiobuttons path="sex" items="${sexMap }"></form:radiobuttons></td>
         </tr>
         <tr>
           <td>部门：</td>
           <td>
             <form:select path="deptId" items="${deptMap }"></form:select>
           </td>
         </tr>
         <tr>
            <td>出生日期</td>
            <td><form:input path="birthday"></form:input></td>
         </tr>
       </table>
     </form:form>
  </body>
</html>
