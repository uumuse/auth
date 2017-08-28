<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html >
<html>
<head>
<%@include file="/jsp/common/CommonMeta.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>支付完成</title>
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta name="renderer" content="webkit">
<meta name="keywords" content="">
<meta name="description" content="">
<link href="/css/init.css" rel="stylesheet">
<link href="/css/main.css" rel="stylesheet">
<script type="text/javascript" src="/js/jquery-1.12.4.min.js"></script>
</head>
<body>
<%@ include file="/jsp/userCenter/userLoginHead.jsp" %>
<div class="comSite422">
	<div class="comSite comWd01">
		<div class="comSite423"><img src="/images/comSite423-1.png" alt="" width="560" height="28"></div>
		<div class="comSite424">
			<div class="comSite438">
				<div class="comSite439">
					<p class="comSt340">您的订单已支付成功，即刻开始音乐之旅</p>
					<div class="comSite440">
						<p class="comSt341">订单号：<span>${keyword}</span></p>
						<p class="comSt342 comSt343">支付金额：<span>${cost_price}元</span></p>
						<p class="comSt342">支付方式：<span>${paydesc}</span></p>
					</div>
				</div>
				<div class="comSite441">
					<p class="comSt344">安全提醒：请不要将银行卡、密码、手机验证码提供给他人</p>
				</div>
			</div>
		</div>
	</div>
</div>
<jsp:include page="/jsp/userCenter/footer.jsp"></jsp:include>
<!-- 支付成功弹层 -->
<div class="comSite594">
	<p>5秒后自动跳转至我的订单页面</p>
	<div class="comSite595 cf">
		<a href="/kuke/login/index" class="comSt194">去首页</a>
		<a href="/kuke/userCenter/userBill" class="comSt194 comSt195 comSt404">查看订单</a>
	</div>
</div>
<script>
$(function(){
	
	//5秒后跳到我的订单页面
	setTimeout(function(){
		//刷新
		window.location.href = '/kuke/userCenter/userBill';
	},5000);
	
	// 头部下拉菜单
	$(".comSite200").hover(function(){
		$(this).find(".comSite202").show();
	},function(){
		$(this).find(".comSite202").hide();
	});
	// 第三方支付
	$(".comSite396 li").on("click",function(){
		var i = $(".comSite396 li").index(this);
		$(".comSite397").hide().eq(i).show();
		$(".comSite397 input").prop("checked",false).eq(i).prop("checked",true);
		$(".comSite396 li").removeClass("comOn51").eq(i).addClass("comOn51");
	});
	// 银行模块显示隐藏
	$(".comSite399 span").on("click",function(){
		$(this).toggleClass("comOn52");
		$(".comSite396b").toggle();
	});
});
</script>
</body>
</html>