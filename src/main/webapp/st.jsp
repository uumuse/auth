<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="/js/lib/jquery-1.7.1.min.js"></script> 
<title>库客数字音乐图书馆-古典音乐第一门户</title>
<% 
	String client_id = request.getParameter("client_id").toString();  

%>
<script type="text/javascript">
	var from_url = document.referer;
	//if(from_url.indexOf("http://shoutu.chaoxing.com") > 0){
		$.ajax({
			url:"http://123.127.171.162/ssoserver/oauth2authorize",
			dataType : "jsonp",
			data:{
				client_id:<%=client_id%>,
				
			},
			success : function(data){
				if(data.uname!=''){
					$.ajax({
						url:"http://auth.kuke.com/kuke/authorize/stAuth",
						dataType : "json",
						data:{
							uname  : data.uname,
							time   : data.time,
							ak     : data.ak
							},
						success : function(data){
							if(data=="success"){
								window.location.href="http://auth.kuke.com/kuke/orgUser?orgName=stlib&orgPwd=stlib";
							}
							else{
								window.location.href="http://www.kuke.com";
							}
						}
					}) 
				}else{
								window.location.href="http://www.kuke.com";
							}
			}
		}) 
	//}

</script>
</head>
<body>

</body>
</html>