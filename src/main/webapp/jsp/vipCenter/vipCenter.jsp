<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html >
<html>
<head>
<%@include file="/jsp/common/CommonMeta.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>库客音乐会员-库客艺术中心</title>
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta name="renderer" content="webkit">
<meta name="keywords" content="库客音乐会员，古典音乐，高品质在线收听">
<meta name="description" content="成为库客音乐会员，即刻拥有强大的古典乐库，手机电脑同步，随时随地畅享好音乐。">
<link href="/css/init.css" rel="stylesheet">
<link href="/css/main.css" rel="stylesheet">
<script type="text/javascript" src="/js/jquery-1.12.4.min.js"></script>
</head>
<body>
<%@ include file="/jsp/userCenter/userLoginHead.jsp" %>
<div class="comSite495 pr">
	<c:if test="${userFlag}">
		<c:if test="${dateFlag}">
			<div class="xf" style="display: block;"><a href="javascript:void(0)" class="kbtn kbtn02" id="vipnow">续费</a><p>${userFlagMessage}<span>${data.audio_date}</span></p></div>
		</c:if>
		<c:if test="${!dateFlag}">
			<a href="javascript:void(0)" class="kbtn kbtn01" id="vipnow" style="display: block;">立即开通</a>
		</c:if>
	</c:if>
	<c:if test="${!userFlag}">
		<a href="javascript:void(0)" class="kbtn kbtn01" id="vipnow" style="display: block;">立即开通</a>
	</c:if>
</div>
<div class="comSite496"></div>
<div class="comSite497"></div>
<div class="comSite498"></div>
<input type="hidden" id="uid" name="uid" value="${uid}">
<input type="hidden" id="dateFlag" name="dateFlag" value="${dateFlag}">
<input type="hidden" id="userFlag" name="userFlag" value="${userFlag}">
<jsp:include page="/jsp/userCenter/footer.jsp"></jsp:include>
<script>
$(function(){
	$(".kbtn01").hover(function(){
		$(this).html("加入音乐会员");
	},function(){
		$(this).html("立即开通");
	});
	$("#vipnow").on("click",function(){
		var uid = $("#uid").val();
		if(uid == null || uid == "" ){
			showLogin();
		}else{
			window.location.href = '/kuke/userCenter/vipProInfo';
		}
	});
});
</script>
</body>
</html>