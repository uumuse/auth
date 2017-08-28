<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/jsp/common/CommonMeta.jsp"%>
<title>重置密码_输入手机或邮箱</title>
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
				<div class="comSite164">
					<div class="comSite165" id="comSite165a"><input type="text" placeholder="输入手机号或邮箱地址" class="comSt135" id="comSt135a"></div>
					<p class="comSt136" id="comSt136a"></p>
				</div>
				<div class="comSite166"><a href="javascript:void(0)" class="comSt137 comSt137a">下一步</a></div>
			</div>
		</div>
	</div>
</div>
<jsp:include page="/jsp/userCenter/footer.jsp"></jsp:include>
<script>
$(function(){
	$(".comSt137a").on("click",function(){
		var input = G.id("comSt135a").value;
		if(checkForm1.checkTel()){ // 输入的是手机号
			$.ajax({
				url:"/kuke/findPassword/checkUserPhone",
				type:"post",
				async:false,
				data:{phoneNum:input},
				dataType:"json",
				success:function(info){
					if(info.data.result > 0){
						window.location.href ="/kuke/findPassword/sendFindPhoneNum?mobile="+input;
					}else{
						G.id("comSt136a").innerHTML = "该手机尚未注册";
						G.id("comSite165a").className = "comSite165 comOn17";
					}
				}
			});
		}else if(checkForm1.checkMail()){ // 输入的是邮箱,发送邮件并跳转到发送成功页面
			$.ajax({
				url:"/kuke/findPassword/checkUserEmail",
				type:"post",
				async:false,
				data:{userEmail:input},
				dataType:"json",
				success:function(info){
					if(info.data.result > 0){
						window.location.href ="/kuke/findPassword/sendFindEmail?email="+input;
					}else{
						G.id("comSt136a").innerHTML = "该邮箱尚未注册";
						G.id("comSite165a").className = "comSite165 comOn17";
					}
				}
			});
		}else{
			$("#comSt136a").html("请输入正确的手机号或邮箱地址");
		}
	});
});
var G = { // 获取id
	id : function(id){
		return document.getElementById(id);
	}
}
var checkForm1 = {
	telRule : /^(13[0-9]{9}|14[5|7][0-9]{8}|15[0|1|2|3|5|6|7|8|9][0-9]{8}|17[6|7][0-9]{8}|18[0-9]{9})$/, // 手机号
	mailRule : /^[0-9a-zA-Z_-]+.[0-9a-zA-Z_-]*@[0-9a-zA-Z]+.(com|cn|net|com.cn)$/i, // 邮箱
	mValue : function(){
		return G.id("comSt135a").value;
	},
	checkTxt : function(){
		if(!this.mValue()){
			G.id("comSt136a").innerHTML = "请输入正确的手机号或邮箱地址";
			G.id("comSite165a").className = "comSite165 comOn17";
			return false;
		}else if(!this.checkTel() && !this.checkMail()){
			G.id("comSt136a").innerHTML = "请输入正确的手机号或邮箱地址";
			G.id("comSite165a").className = "comSite165 comOn17";
			return false;
		}else{
			G.id("comSt136a").innerHTML = "";
			G.id("comSite165a").className = "comSite165";
			return true;
		}
	},
	checkTel : function(){
		if(this.telRule.test(this.mValue())){
			return true;
		}
	},
	checkMail : function(){
		if(this.mailRule.test(this.mValue())){
			return true;
		}
	}
}
G.id("comSt135a").onblur = function(){checkForm1.checkTxt();}
</script>
</body>
</html>