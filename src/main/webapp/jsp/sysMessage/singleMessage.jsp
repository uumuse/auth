<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html >
<html>
<head>
<%@include file="/jsp/common/CommonMeta.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>我的消息单条消息</title>
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta name="renderer" content="webkit">
<meta name="keywords" content="">
<meta name="description" content="">
<link href="/css/init.css" rel="stylesheet">
<link href="/css/main.css" rel="stylesheet">
<script type="text/javascript" src="/js/jquery-1.12.4.min.js"></script>
<script type="text/javascript" src="/js/sysMessage/sysMessage.js"></script>
<script type="text/javascript" src="/js/userCenter/util.js"></script>
</head>
<body>
<%@ include file="/jsp/userCenter/userLoginHead.jsp" %>
<div class="comSite289">
	<div class="comSite279 comWd01">
		<div class="comSite280">
			<div class="comSite281">
				<h1>我的消息</h1>
			</div>
			<div class="comSite290">
				<a href="/kuke/userCenter/getSysMessageList">&lt; 返回</a>
			</div>
			<div class="comSite291">
				<ol>
					<li class="comSite292 pr">
						<h2>${(userMessage.title == "" || userMessage.title == null)?"系统通知":userMessage.title}</h2>
						<p>${userMessage.contents}</p>
						<div class="comSite293">来自 <span>库客音乐</span><span class="comSt277"><fmt:formatDate value="${userMessage.send_date}" pattern="yyyy-MM-dd"/></span></div>
						<span class="comSt278" title="删除" onclick="delSysMessage('${userMessage.id}');"></span>
					</li>
				</ol>
			</div>
		</div>
	</div>
</div>
<jsp:include page="/jsp/userCenter/footer.jsp"></jsp:include>
<script>
$(function(){
	// 背景高度
	setInterval(function(){
		$(".comSite289").height($(window).height()-$(".comSite195").height()-40);
	},20);
	// 头部下拉菜单
	$(".comSite200").hover(function(){
		$(this).find(".comSite202").show();
	},function(){
		$(this).find(".comSite202").hide();
	});
	// 全选
	$(".comSt272a").on("click",function(){
		if($(this).prop("checked") == true){
			$(this).parent().find(".comSt271a").addClass("comOn34");
			$(".comSt276a").prop("checked",true).parent().find(".comSt275").addClass("comOn35");
		}else{
			$(this).parent().find(".comSt271a").removeClass("comOn34");
			$(".comSt276a").prop("checked",false).parent().find(".comSt275").removeClass("comOn35");
		}
	});
	// 单选
	$(".comSt276a").on("click",function(){
		$(this).parent().find(".comSt275a").toggleClass("comOn35");
	});
});
</script>
</body>
</html>