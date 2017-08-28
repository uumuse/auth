<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html >
<html>
<head>
<%@include file="/jsp/common/CommonMeta.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta name="renderer" content="webkit">
<meta name="keywords" content="">
<meta name="description" content="">
<link href="/css/init.css" rel="stylesheet">
<link href="/css/main.css" rel="stylesheet">
<script type="text/javascript" src="/js/jquery-1.12.4.min.js"></script>
<script type="text/javascript" src="/jsp/share/player/TrackPlayer.js"></script>
<script type="text/javascript" src="/js/common/util.js"></script>
</head>
<body>
<div class="comSite comWd01">
	<div class="comSite comTop01 cf">
		<a href="javascript:void(0)" onclick="goToIndex();" class="comSt1 fl"><img src="/images/logo.png" alt=""></a>
		<div class="comSite1 cf fl">
			<input type="text" placeholder="单曲／专辑／艺术家／视频" class="comSt2 fl">
			<a href="#" title="搜索" class="comSt3 fl"></a>
		</div>
		<div class="comSite2 fr cf">
			<!-- 未登录 -->
			<a href="/kuke/userCenter/vipCenter" class="comSt6 fr">库客会员</a>
			<div id="notLogin" class="comSite fr comSt405">
				<a href="javascript:void(0)" class="comSt4" onclick="showLogin()">登陆</a>
				<span class="comSt5">|</span>
				<a href="javascript:void(0)" class="comSt4" onclick="regist()">注册</a>
			</div>
		</div>
	</div>
	<div class="comSite comTop02 cf">
		<h1 class="comSt7 fl">库客·艺术中心</h1>
		<div class="comSite3 comSt31 fl">
			<ol class="cf">
				<li><a href="#" class="comOn02">唱片</a></li>
				<li><a href="#">视频</a></li>
				<li><a href="#">剧院</a></li>
				<li><a href="#">有声读物</a></li>
				<li><a href="#">直播</a></li>
				<li><a href="#">乐谱</a></li>
			</ol>
		</div>
		<a href="#" class="comSt8 fr">下载库客音乐APP</a>
	</div>
</div>
<div class="comSite599 cf comWd01">
	<div class="comSite600"><img <c:if test="${empty image}">src="/jsp/share/images/default_zhuanji_b.jpg"</c:if> <c:if test="${!empty image}">src="${image}"</c:if>   alt="" width="200" height="200"></div>
	<div class="comSite601">
		<span class="comSt407">单曲</span>
		<h2>${title}</h2>
		<ol>
			<li>专辑号：<span>${item_code}</span></li>
			<li>所属厂牌：<span>${labelid}</span></li>
			<li>单曲描述：<span>${trackDesc}</span></li>
		</ol>
		<a id="playbutn" href="javascript:void(0)" class="comSt409">播放</a>
		<a id="pausebutn" href="javascript:void(0)" class="comSt409-pause" style="display: none;">暂停</a>
	</div>
</div>
<audio src="/mp3/load.mp3" id="playerxEnvXYXWHbCc">您的浏览器不支持 audio 标签。</audio>
<div class="comTcBg" style="display: none;"></div>
<div class="comSite602" style="display: none;">
	<p>当前播放链接已失效</p>
	<div class="comSite603 cf">
		<a href="javascript:void(0);" class="comSt410" onclick="stayHtml();">留在当前页</a>
		<a href="javascript:void(0)" class="comSt410 comSt410a">去首页</a>
	<div>
</div>
<script type="text/javascript">
$(function(){
	var player = new TrackPlayer();
	(function(player){
		var item_code = '${item_code }';
		var labelid = '${labelid }';
		var l_code = '${l_code }';
		var c = '${c}';
		var array = new Array();
		array.push(l_code);
		array.push(item_code);
		array.push(c);
		array.push(labelid);
		player.item_l_code_array.push(array);
	})(player);
	//player.mp3_play(0);
	
	//播放
	$("#playbutn").on("click", function(){
		player.mp3_play();
 	});
	
	//暂停
	$("#pausebutn").on("click", function(){
		player.mp3_pause();
	});
});
function stayHtml(){
	$(".comTcBg").hide();
	$(".comSite602").hide();
}
</script>
</body>
</html>