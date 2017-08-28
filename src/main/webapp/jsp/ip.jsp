<%@page import="org.apache.commons.lang.StringUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%
	String ip = "";
	String XForwardedFor = request.getHeader("X-Forwarded-For");
	String XRealIP = request.getHeader("X-Real-IP");
	int index = 0;
	if(StringUtils.isNotEmpty(XForwardedFor) && !"unKnown".equalsIgnoreCase(XForwardedFor)){
	    //多次反向代理后会有多个ip值，第一个ip才是真实ip
	    index = XForwardedFor.indexOf(",");
	    if(index != -1){
	    	ip = XForwardedFor.substring(0,index);
	    }else{
	    	ip = XForwardedFor;
	    }
	}else{
		if(StringUtils.isNotEmpty(XRealIP) && !"unKnown".equalsIgnoreCase(XRealIP)){
			 ip = XRealIP;
		}else{
			 ip = request.getRemoteAddr();
		}
	}
%>
<html>
<head></head>
<script type="text/javascript">
	console.log('XForwardedFor:'+'<%=XForwardedFor %>');
	console.log('XRealIP:'+'<%=XRealIP %>');
	console.log('request.getRemoteAddr():'+'<%=request.getRemoteAddr() %>');
</script>
<body>
<%=ip %>
</body>
</html>