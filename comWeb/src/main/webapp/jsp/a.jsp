<%@ page import="java.util.Date" %><%--
  Created by IntelliJ IDEA.
  User: fahaimac
  Date: 2017/7/19
  Time: 上午11:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Hello Redis</title>
</head>
<body>
index page ,hello
<%= new Date()%>
<a href="/user/getUserById"> getuserbyid ,and return index.jsp</a>
</body>
</html>
