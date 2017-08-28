<!DOCTYPE html>
<html>
<script type="text/javascript" src="/js/jquery-1.12.4.min.js"></script>
<script type="text/javascript">
$(function(){
	download();
});
function G(o){
	return document.getElementById(o);
}
function download(){
	var ua = navigator.userAgent;
	var isIOS = /ipod|iPhone|ipad/i.test(ua);
	var isAndroid = /android/i.test(ua);
	var isWX = /micromessenger/i.test(ua);
	var the_href = "";
	var l_href = "";
	if(isWX){
		//G("comSt09").onclick = showpop;
		//G("popup").onclick = G("pop-bg").onclick = hidepop;
		if(isAndroid){
			the_href = "http://static.kuke.com/android/kukemusic.apk";
			l_href = "kukemusic://kukemusic/";
		}else if(isIOS){
			the_href = "/jsp/share/testdown.jsp";
			l_href = "wx665f38aa1ca4ace3://com.kuke.mobile/";
		}
	}else{
		if(isAndroid){
			the_href = "http://static.kuke.com/android/kukemusic.apk";
			l_href = "kukemusic://kukemusic/";
		}else if(isIOS){
			the_href = "https://itunes.apple.com/cn/app/minecraft-pocket-edition/id730228198";
			l_href = "wx665f38aa1ca4ace3://com.kuke.mobile/";
		}
	}
	//打开应用
	//window.location=l_href;//打开某手机上的某个app应用
	//alert(l_href);
	setTimeout(function(){
		alert('setTimeout');
		//window.location=the_href;//如果超时就跳转到app下载页
		alert(the_href);
		//window.location = the_href;
		var iframe = document.getElementById("contentFrame");
		$("#contentFrame").attr("src",the_href);
		//$("#contentFrame").show();
	},500);
}
</script>
<body>
<iframe name="contentFrame" id="contentFrame" allowtransparency="true" 
				scrolling="no" border="0" style="width:920px;filter:none;display:block;_border:none;" frameborder="0" src="about:blank">
</iframe>
</body>
</html>