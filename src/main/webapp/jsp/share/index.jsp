<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<%@include file="/jsp/common/CommonMeta.jsp"%>
<meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=no">
<meta name="format-detection" content="telephone=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="-1">
<title>库客音乐</title>
<meta name="keywords" content="">
<meta name="description" content="">
<link href="css/init.css" rel="stylesheet">
<script type="text/javascript" src="/js/jquery-1.12.4.min.js"></script>
<script type="text/javascript" src="/js/play/hhSwipe.js"></script>
<style>
/*public scroll*/
.scroll{max-width:1080px;}
.scroll_box{overflow:hidden;visibility:hidden;position:relative;}
.scroll_wrap{overflow:hidden; position:relative;}
.scroll_wrap li{position:relative;display:block;width:100%;float:left;}
.scroll_wrap li a{display:block;margin:0 auto;position:relative;}
.scroll_position{position:absolute;left:50%;bottom:14px; margin-left:-45px;}
.scroll_position li{display:block;width:6px;height:6px;float:left;margin-left:30px;border:2px solid #ffffff;border-radius:50%;background-color:#ffffff;}
.scroll_position li:first-child{margin-left:0;}
.scroll_position li.on{background:none;}
.scroll_position_bg{position:absolute;bottom:12px;left:42%;padding:0 15px;z-index:380px;height:26px;border-radius:26px;}
.pr{ position:relative;}

.comSite18{ width:100%; position:fixed; left:0; bottom:0; background-color:#ffffff;}
.comSt09{ display:block; width:70px; height:24px; color:#ff2750; text-align:center; line-height:24px; border:1px solid #ff2750; border-radius:13px; font-size:1.2em; position:absolute; right:12px; top:50%; margin-top:-13px;}
.comSt10{ display:block; width:100%;}
#pop-bg{ width:100%; background-color:#000000; opacity:0.6; filter:alpha(opacity=60); position:absolute; top:0; bottom:0; display:none;}
#popup{ width:100%; position:absolute; left:0; top:0; display:none;}
</style>
</head>
<body>
<div>
	<article>
		<div class="scroll pr">
			<div class="scroll_box" id="scroll_img">
				<ul class="scroll_wrap" id="scroll_img_wrap">
					<li><a href="#"><img src="/jsp/share/images/01.jpg" width="100%" /></a></li>
					<li><a href="#"><img src="/jsp/share/images/02.jpg" width="100%" /></a></li>
					<li><a href="#"><img src="/jsp/share/images/03.jpg" width="100%" /></a></li>
				</ul>
			</div>
			<ul class="scroll_position" id='scroll_position'>
				<li class="on"></li>
				<li></li>
				<li></li>
			</ul>
		</div>
	</article>
</div>
<div class="comSite18">
	<div class="comSite19 cf pr">
		<span class="comSt09" id="comSt09" onclick="download()">立即下载</span>
		<a href="" class="comSt10"><img src="images/logo01.png" alt="" width="100%"></a>
	</div>
</div>
<div id="pop-bg"></div>
<div id="popup"><img src="http://kuke.com/hd/yuncd/images/notice.png" alt="" width="100%"></div>
<script type="text/javascript">
$(function(){
	var g1 = $(window).height() - $(".comSite18").height();
	$(".scroll_wrap").height(g1);
});
var slider = Swipe(document.getElementById('scroll_img'),{
	auto : 4000,
	continuous : true,
	callback : function(pos){
		var i = bullets.length;
		while(i--){
			bullets[i].className = ' ';
		}
		bullets[pos].className = 'on';
	}
});
var bullets = document.getElementById('scroll_position').getElementsByTagName('li');

var ua = navigator.userAgent;
var isIOS = /ipod|iPhone|ipad/i.test(ua);
var isAndroid = /android/i.test(ua);
var isWX = /micromessenger/i.test(ua);

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

//if(isWX){
//	G("comSt09").onclick = showpop;
//	G("popup").onclick = G("pop-bg").onclick = hidepop;
//}else{
//	if(isAndroid){
//		G("comSt09").onclick = function(){
//			location.href = "http://static.kuke.com/android/kukemusic.apk";
//		}
//	}else if(isIOS){
//		G("comSt09").onclick = function(){
//			location.href = "https://itunes.apple.com/cn/app/ku-ke-yin-le/id730228198?mt=8";
//		}
//	}
//}
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
			the_href = "http://static.kuke.com/android/kukemusic.apk";
			l_href = "kukemusic://kukemusic/";
		}else if(isIOS){
			the_href = "https://itunes.apple.com/cn/app/ku-ke-yin-le/id730228198?mt=8";
			l_href = "kukemusic://com.kuke.mobile/";
		}
	}else{
		if(isAndroid){
			the_href = "http://static.kuke.com/android/kukemusic.apk";
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
</script>
</body>
</html>