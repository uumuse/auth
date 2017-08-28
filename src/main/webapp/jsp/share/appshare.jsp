<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html >
<html>
<head>
<%@include file="/jsp/common/CommonMeta.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=no">
<meta name="format-detection" content="telephone=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="-1">
<title>单曲分享</title>
<meta name="keywords" content="">
<meta name="description" content="">
<link href="/jsp/share/css/init.css" rel="stylesheet">
<link href="/jsp/share/css/main.css" rel="stylesheet">
<script type="text/javascript" src="/js/jquery-1.12.4.min.js"></script>
<script type="text/javascript" src="/jsp/share/player/appTrackPlayer.js"></script>
</head>
<body class="comBg01">
<div class="comSite">
	<div class="comSite14">
		<h2>${title}</h2>
	</div>
	<div class="comSite15 pr">
		<div><img <c:if test="${empty image}">src="/jsp/share/images/default_zhuanji_b.jpg"</c:if> <c:if test="${!empty image}">src="${image}"</c:if>   alt="" width="100%"></div>
		<span id="pause" class="comSt05"></span>
		<span id="play" class="comSt05 comSt05-on" style="display: none;"></span>
	</div>
	<div class="comSite16 cf">
		<span id="starttime" class="comSt06 comSt06a">00:00</span>
		<div class="comSite17 pr">
			<span class="comSt07"></span>
			<span class="comSt08" style="left:0%"></span>
		</div>
		<span id="endtime" class="comSt06 comSt06b">${timing}</span>
	</div>
</div>
<div class="comSite18">
	<div class="comSite19 cf pr">
		<span onclick="download()" class="comSt09" id="comSt09" style="width:90px;height:100%;margin-top:0;top:0;right:0;background:url(http://kuke.com/hd/downnow.png) no-repeat center center;background-size:70px 26px;border:none;"></span>
		<a href="/jsp/share/index.jsp" class="comSt10"><img src="/jsp/share/images/logo01.png" alt="" width="100%"></a>
	</div>
</div>
<audio src="/mp3/load.mp3" id="playerxEnvXYXWHbCc">您的浏览器不支持 audio 标签。</audio>
<input type="hidden" name="timing" id="timing" value="${timing}">
<input type="hidden" name="c" id="c" value="${c}">
<script>
$(function(){
	$("body").height($(window).height());
	var mtop = ($(".comSite19").height() - $(".comSt09").height()) / 2;
});
</script>
<div id="pop-bg"></div>
<div id="popup"><img src="http://kuke.com/hd/yuncd/images/notice.png" alt="" width="100%"></div>
<script type="text/javascript">
function G(o){
	return document.getElementById(o);
}

function showpop(){ // 显示弹层
	G("pop-bg").style.display = "block";
	G("popup").style.display = "block";
}

function hidepop(){ // 关闭弹层
	G("pop-bg").style.display = "none";
	G("popup").style.display = "none";
}

function download(){
	var ua = navigator.userAgent;
	var isIOS = /ipod|iPhone|ipad/i.test(ua);
	var isAndroid = /android/i.test(ua);
	var isWX = /micromessenger/i.test(ua);
	var the_href = "";
	var l_href = "";
	if(isWX){
		G("comSt09").onclick = showpop;
		G("popup").onclick = G("pop-bg").onclick = hidepop;
		if(isAndroid){
			the_href = "http://static.kuke.com/android/kukemusic20170113.apk";
			l_href = "kukemusic://kukemusic/";
		}else if(isIOS){
			the_href = "https://itunes.apple.com/cn/app/ku-ke-yin-le/id730228198?mt=8";
			l_href = "kukemusic://com.kuke.mobile/";
		}
	}else{
		if(isAndroid){
			the_href = "http://static.kuke.com/android/kukemusic20170113.apk";
			l_href = "kukemusic://kukemusic/";
		}else if(isIOS){
			the_href = "https://itunes.apple.com/cn/app/ku-ke-yin-le/id730228198?mt=8";
			l_href = "kukemusic://com.kuke.mobile/";
		}
	}
	//打开应用
	window.location=l_href;//打开某手机上的某个app应用
	setTimeout(function(){
		window.location=the_href;//如果超时就跳转到app下载页
	},500);
}

$(function(){
	var player = new appTrackPlayer();
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
	$("#pause").click(function(){
		player.mp3_play();
 	});
	
	//暂停
	$("#play").on("click", function(){
		player.mp3_pause();
	});
	
});

</script>
</body>
</html>