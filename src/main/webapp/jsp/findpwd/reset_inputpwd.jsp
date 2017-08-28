<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/jsp/common/CommonMeta.jsp"%>
<title>重置密码_输入新密码</title>
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta name="renderer" content="webkit">
<meta name="keywords" content="">
<meta name="description" content="">
<link href="/css/init.css" rel="stylesheet">
<link href="/css/main.css" rel="stylesheet">
<script type="text/javascript" src="/js/jquery-1.12.4.min.js"></script>
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
					<div class="comSite165" id="comSite165c"><input type="password" placeholder="输入新的密码" class="comSt135" id="comSt135c"></div>
					<p class="comSt136" id="comSt136c"></p>
				</div>
				<div class="comSite164">
					<div class="comSite165" id="comSite165d"><input type="password" placeholder="再次输入新的密码" class="comSt135" id="comSt135d"></div>
					<p class="comSt136" id="comSt136d"></p>
				</div>
				<div class="comSite166"><a href="javascript:void(0)" class="comSt137 comSt137c">下一步</a></div>
			</div>
		</div>
	</div>
</div>
<input type="hidden" name="mobile" id="mobile" value="${mobile}">
<input type="hidden" name="time" id="time" value="${time}">
<input type="hidden" name="key" id="key" value="${key}">
<input type="hidden" name="type" id="type" value="${type}">
<input type="hidden" name="email" id="email" value="${email}">
<jsp:include page="/jsp/userCenter/footer.jsp"></jsp:include>
<script>
$(function(){
	var type = $("#type").val();
	$(".comSt137c").on("click",function(){
		var pass = G.id("comSt135c").value;
		var repass = G.id("comSt135d").value;
		if(checkForm1.pwd1() && checkForm1.pwd2()){
			if(type == "email"){
				window.location.href = "/kuke/findPassword/resetPassword?email="+$("#email").val()
				+"&key="+$("#key").val()+"&time="+$("#time").val()+"&pass="+pass+"&rePass="+repass;
			}else{
				window.location.href = "/kuke/findPassword/resetPasswordForphone?mobile="+$("#mobile").val()
				+"&key="+$("#key").val()+"&time="+$("#time").val()+"&pass="+pass+"&rePass="+repass;
			}
		}
	});
});
var G = { // 获取id
	id : function(id){
		return document.getElementById(id);
	}
}
var checkForm1 = {
	v1 : function(){
		return G.id("comSt135c").value;
	},
	v2 : function(){
		return G.id("comSt135d").value;
	},
	len1 : function(){
		return G.id("comSt135c").value.length;
	},
	len2 : function(){
		return G.id("comSt135d").value.length;
	},
	pwd1 : function(){
		if(!this.v1()){
			G.id("comSt136c").innerHTML = "请输入新的密码";
			G.id("comSite165c").className = "comSite165 comOn17";
			return false;
		}else if(this.len1() < 6 || this.len1() > 16){
			G.id("comSt136c").innerHTML = "密码长度为6-16位";
			G.id("comSite165c").className = "comSite165 comOn17";
			return false;
		}else{
			G.id("comSt136c").innerHTML = "";
			G.id("comSite165c").className = "comSite165";
			return true;
		}
	},
	pwd2 : function(){
		if(!this.v2()){
			G.id("comSt136d").innerHTML = "再次请输入新的密码";
			G.id("comSite165d").className = "comSite165 comOn17";
			return false;
		}else if(this.len2() < 6 || this.len2() > 16){
			G.id("comSt136d").innerHTML = "密码长度为6-16位";
			G.id("comSite165d").className = "comSite165 comOn17";
			return false;
		}else if(this.v2() != this.v1()){
			G.id("comSt136d").innerHTML = "两次密码输入不一致";
			G.id("comSite165d").className = "comSite165 comOn17";
			return false;
		}else{
			G.id("comSt136d").innerHTML = "";
			G.id("comSite165d").className = "comSite165";
			return true;
		}
	}
}
</script>
</body>
</html>