<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/jsp/common/CommonMeta.jsp"%>
<title>重置密码_输入验证码</title>
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta name="renderer" content="webkit">
<meta name="keywords" content="">
<meta name="description" content="">
<link href="/css/init.css" rel="stylesheet">
<link href="/css/main.css" rel="stylesheet">
<script src="/js/jquery-1.12.4.min.js"></script>
</head>
<body>
<div class="comSite159">
	<div class="comSite160 comWd01">
		<div class="comSite161">
			<div class="comSite162">
				<h1>重置密码</h1>
			</div>
			<div class="comSite163">
				<p class="comSt138">您的手机号：${mobile}</p>
				<div class="comSite164" style="height: auto;">
					<div class="comSite167 cf">
						<span class="comSt139" id="countTime" onclick="releaseCode()">重新获取验证码</span>
						<div class="comSite165 comSite165b" id="comSite165b"><input type="text" placeholder="请输入手机验证码" class="comSt135" id="comSt135b"></div>
					</div>
					<p class="comSt136" id="comSt136b" style="height: 26px;"></p>
				</div>
				<div class="comSite166"><a href="javascript:void(0)" class="comSt137 comSt137b">下一步</a></div>
			</div>
		</div>
	</div>
</div>
<input type="hidden" name="mobile" id="mobile" value="${mobile}">
<jsp:include page="/jsp/userCenter/footer.jsp"></jsp:include>
<script>
//下发验证码
function releaseCode(){
		var mobile = $("#mobile").val();
		$.ajax({
			type : "POST",
			url : "/kuke/issued",
			dataType : 'json',
			cache : false,
			data : {
				mobile : mobile
			},
			async : false,
			timeout : 30000,
			success : function(data, textStatus) {
				debugger;
				if (data.flag == true) {
					G.id("comSt136b").innerHTML = data.msg;
					$("#countTime").attr("onclick","");
					showTime();
				}
				if (data.flag == false) {
					var s = data.msg;
					G.id("comSt136b").innerHTML = s;
				}
			},
			error : function(data, textStatus) {
			}
		});
}
//格式化时间
function timeParse(time){
	var min = Math.floor(time/60);
	var sec = time%60;
	if(min>=1){
		if(sec<10){
			return "0" + min + ":0" + sec;
		}else
			return "0" + min + ":" + sec;
	}else{
		if(sec<10){
			return "00:0" + sec;
		}else
			return "00:" + sec;
	}
}
//下发验证码后显示倒计时
var t = 180;
function showTime(){
		if(t-1>=0){
			t -= 1;  
		}
	    document.getElementById('countTime').innerHTML= timeParse(t);  
	    //每秒执行一次,showTime()  
	    if(t>0){
	    	setTimeout("showTime()",1000);  
	    }else{
	    	document.getElementById('countTime').innerHTML= '重新获取验证码';
	    	$("#countTime").attr("onclick","releaseCode()");
	    	t = 180;
	    }
}
$(function(){
	var result = '${result}';
	if(result != ""){//验证验证码时出错
		$("#comSt136b").html(result);
	}else{
		$("#countTime").attr("onclick","");
		showTime();
	}
	$(".comSt137b").on("click",function(){
		$("#comSt136b").html("");
		if(checkForm1.checkCode()){
			var code = G.id("comSt135b").value;
			window.location.href="/kuke/findPassword/verifyPhoneNum?mobile="+$("#mobile").val()+"&code="+code;
		}
	});
});
var G = { //获取id
	id : function(id){
		return document.getElementById(id);
	}
}
var checkForm1 = {
	mValue : function(){
		return G.id("comSt135b").value;
	},
	checkCode : function(){
		if(!this.mValue()){
			G.id("comSt136b").innerHTML = "请输入正确的验证码";
			G.id("comSite165b").className = "comSite165 comSite165b comOn17";
			return false;
		}else{
			G.id("comSt136b").innerHTML = "";
			G.id("comSite165b").className = "comSite165 comSite165b";
			return true;
		}
	}
}
</script>
</body>
</html>