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
<div class="comSite203">
	<div class="comSite204 comWd01">
		<div class="comSite499">
			<div class="comSite500">
				<h1>开通会员</h1>
			</div>
			<div class="comSite501">
				<h2>库客音乐会员</h2>
				<p>全球顶级唱片公司授权，海量正版音乐在线听</p>
			</div>
			<div class="comSite502">
				<p class="comSt356">请选择</p>
				<div class="comSite503 cf">
					<c:forEach items="${audioPro}" var="item" varStatus="status" >
						<c:if test="${status.index == 0}">
							<input type="hidden" id="firstTime" name="firstTime" value="${item.month}">
							<input type="hidden" id="firstPrice" name="firstPrice" value="${item.price}">
							<input type="hidden" id="firstID" name="firstID" value="${item.id}">
							<a href="javascript:void(0)" class="comOn55" onclick="setPrice('${item.month}','${item.price}','${item.id}');">${item.month}个月</a>
						</c:if>
						<c:if test="${status.index != 0}">
							<a href="javascript:void(0)" onclick="setPrice('${item.month}','${item.price}','${item.id}');">${item.month}个月</a>
						</c:if>
					</c:forEach>
				</div>
				<p class="comSt357"><span id="needMoney"></span>元</p>
			</div>
			<div class="comSite504 c">
				<a href="javascript:void(0)" onclick="goservice();" class="cNext">下一步</a>
				<span>点击下一步表示您接受<a href="javascript:void(0)" class="quanyiBtn">《库客音乐会员权益说明》</a></span>
			</div>
		</div>
	</div>
</div>
<div class="quanyi-bg"></div>
<div class="quanyi">
	<div class="quanyi-tit pr"><h1>库客音乐会员权益说明</h1><span title="关闭"></span></div>
	<div class="quanyi-con">
		<dl class="qInfo01">
			<dt>音乐会员权益：</dt>
			<dd>库客音乐会员可收听唱片频道、有声读物频道的全部唱片；</dd>
			<dd>会员权益可在网站、客户端同步使用；</dd>
		</dl>
		<ol class="qInfo02">
			<li>音乐会员服务不包含：</li>
			<li>视频、剧院频道；</li>
			<li>下载需要另行支付费用；</li>
		</ol>
		<dl class="qInfo01">
			<dt>音乐会员资费标准</dt>
			<dd>1个月<span>￥30</span></dd>
			<dd>3个月<span>￥80</span></dd>
			<dd>6个月<span>￥150</span></dd>
			<dd>12个月<span>￥280</span></dd>
		</dl>
		<dl class="qInfo01 qInfo03">
			<dt>版权声明：</dt>
			<dd>库客提供的全部资源仅用户个人用户欣赏使用，不得复制、修改、传播或用于商业用途，违反上述声明者，将依法追究法律责任。</dd>
		</dl>
	</div>
</div>
<jsp:include page="/jsp/userCenter/footer.jsp"></jsp:include>
<input type="hidden" id="checkedtime" name="checkedtime" value="">
<input type="hidden" id="checkedid" name="checkedid" value="">
<script>
$(function(){
	$(".quanyiBtn").on("click",function(){
		$(".quanyi-bg,.quanyi").show();
	});	
	$(".quanyi-tit span").on("click",function(){
		$(".quanyi-bg,.quanyi").hide();
	});
	// 月份切换
	$(".comSite503 a").on("click",function(){
		$(this).addClass("comOn55").siblings().removeClass("comOn55");
	});
	//初始化赋值价格
	$("#needMoney").html($("#firstPrice").val());
	$("#checkeddata").val($("#firstTime").val());
	$("#checkedid").val($("#firstID").val());
	// 头部下拉菜单
	$(".comSite200").hover(function(){
		$(this).find(".comSite202").show();
	},function(){
		$(this).find(".comSite202").hide();
	});
});
function setPrice(time,price,id){
	$("#needMoney").html(price);
	$("#checkedtime").val(time);
	$("#checkedid").val(id);
}
function goservice(){
	var id = $("#checkedid").val();
	window.open("/kuke/payment/goservice?price_id="+id);
}
</script>
</body>
</html>
