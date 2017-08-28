<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/jsp/common/CommonMeta.jsp"%>
<title>重置密码_输入新密码</title>
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta name="renderer" content="webkit">
<meta name="keywords" content="">
<meta name="description" content="">
<link href="/css/init.css" rel="stylesheet">
<link href="/css/main.css" rel="stylesheet">
<script type="text/javascript" src="/js/jquery-1.12.4.min.js"></script>
</head>
<body>
<div class="comSite159">
	<div class="comSite160 comWd01">
		<div class="comSite161">
			<div class="comSite162">
				<h1>重置密码</h1>
			</div>
			<div class="comSite163">
				<div class="comSite167">
					<p class="comSt140" id="comSt140"></p>
					<p class="comSt141">5秒后自动关闭,跳转至 <a href="javascript:void(0)">首页</a></p>
				</div>
				<div class="comSite166"><a href="javascript:void(0)" class="comSt137 comSt137e">确定</a></div>
			</div>
		</div>
	</div>
</div>
<jsp:include page="/jsp/userCenter/footer.jsp"></jsp:include>
<script>
var msg = '${msg}';
var wwwurl = '${wwwurl}';
$("#comSt140").html(msg);
$(function(){
	setTimeout(function(){
		window.location.href = wwwurl;
	},5000);
	$(".comSt137e,.comSt141 a").on("click",function(){
		// 跳转至首页
		window.location.href = wwwurl;
	});
});
</script>
</body>
</html>