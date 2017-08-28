<%@page import="com.kuke.auth.util.KuKeAuthConstants"%>
<%@page import="com.kuke.core.engine.ICookie"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
	ICookie.clearAll(request, response);
	ICookie.clear(response,KuKeAuthConstants.SSO_ORG_COOKIE_NAME);
	ICookie.clear(response,KuKeAuthConstants.SSO_USER_COOKIE_NAME);
%>
</body>
<script type="text/javascript">
	window.close();
</script>
</html>