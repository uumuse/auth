<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/jsp/common/CommonMeta.jsp"%>
<title>重置密码_登录邮箱</title>
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
			<div class="comSite163 comSite163a">
				<div class="comSite167">
					<p class="comSt142">我们已将重设密码的链接发送到你的邮箱：</p>
					<p class="comSt143">${email}</p>
					<p class="comSt144">请在30分钟内收信并进行重置密码操作</p>
				</div>
				<div class="comSite168"><a href="javascript:void(0)" class="comSt145" onclick="openUserEmail();">登录你的邮箱</a></div>
				<dl class="comSite169">
					<dt>没有收到邮件?</dt>
					<dd>1. 尝试到广告邮件、垃圾邮件目录找找看。</dd>
					<dd>2. 再次发送找回密码邮件<span class="comSt146" id="comSt146">02:59</span><span class="comSt146 comSt147 comSt148" id="comSt148">重新发送</span></dd><!-- comSt148 -->
				</dl>
			</div>
		</div>
	</div>
</div>
<jsp:include page="/jsp/userCenter/footer.jsp"></jsp:include>
<script>
	var emailLogin = '${emailLogin}';
	var email = '${email}';
	//登录自己的邮箱网站
	function openUserEmail(){
		window.open(emailLogin);
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
	    document.getElementById('comSt146').innerHTML= timeParse(t);  
	    //每秒执行一次,showTime()  
	    if(t>0){
	    	setTimeout("showTime()",1000);  
	    }else{
	    	$("#comSt148").attr("class","comSt146 comSt147");
	    	$("#comSt148").attr("onclick","resend()");
	    	t = 180;
	    }
	}
	//重新发送
	function resend(){
		$.ajax({
			url:"/kuke/findPassword/reSendFindEmail",
			type:"post",
			async:false,
			data:{email:email},
			dataType:"json",
			success:function(info){
				if(info.flag == true){
					$("#comSt148").attr("class","comSt146 comSt147 comSt148");
			    	$("#comSt148").attr("onclick","");
			    	showTime();
				}else{
					alert(info.msg);
				}
			}
		});
	}
	$(function(){
		//时间运行
		showTime();
	});
</script>
</body>
</html>