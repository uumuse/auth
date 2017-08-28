<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>意见反馈</title>
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta name="renderer" content="webkit">
<meta name="keywords" content="">
<meta name="description" content="">
<link href="/css/init.css" rel="stylesheet">
<link href="/css/main.css" rel="stylesheet">
<script type="text/javascript" src="/js/jquery-1.12.4.min.js"></script>
<script type="text/javascript">
	function toCommit(){
		var phone = $("#phone").val();
		var email = $("#email").val();
		var content = $("#content").val();
		if(phone != ""){
			if(!istell(phone)){
				tips("请输入正确的联系电话");
				return;
			}
		}
		if(email != ""){
			if(!checkEmail(email)){
				tips("请输入正确的邮箱地址");
				return;
			}
		}
		if(phone == "" && email == ""){
			tips("请输入至少一种联系方式");
			return;
		}
		if(content == "" || content == null){
			tips("请填写您的宝贵意见");
			return;
		}
		$.ajax({
			url:"/kuke/userCenter/feedback",
			type:"post",
			data:{contact:phone,email:email,content:content,type:"web"},
			dataType:"json",
			async:false,
			success:function(info){
				if(info.flag == true){
					tips(info.msg);
				}else{
					tips(info.msg);
				}
			}
		});
	}
	function istell(str){//02187888822 
		var result=str.match(/\d{11}/);
		if(result==null) return false;
		return true;
	}
	function checkEmail(str){//校验邮箱 
		res = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/; 
		var re = new RegExp(res); 
		return !(str.match(re) == null); 
	} 
</script>
</head>
<body>
<jsp:include page="/jsp/common/footer/commonFootHead.jsp"></jsp:include>
<div class="comSite454 comSite454a6">
	<div class="comSite comWd01"><h1 class="comSt345">——意见反馈——</h1></div>
	<div class="comSite455 comWd02">
		<div class="comSite456 comWd01">
			<div class="comSite479">
				<h2>感谢您使用库客音乐网站</h2>
			</div>
			<div class="comSite480">
				<p>如果您对本站的建设有什么建议，请在此留下宝贵意见。</p>
				<div class="comSite481"><textarea id="content" name="content"></textarea></div>
			</div>
			<div class="comSite482">
				<p class="comSt352">请留下您的联系方式，方便我们与您联系或递送礼品。</p>
				<ol class="comSite483 cf">
					<span>联系电话：</span>
					<div class="comSite484"><input type="text" name="phone" id="phone"></div>
					<p>（座机请写明区号，例如01058693329）</p>
				</ol>
				<ol class="comSite483 cf">
					<span>E-MAIL：</span>
					<div class="comSite484"><input type="text" name="email" id="email"></div>
				</ol>
				<div class="comSite485"><a href="javascript:void(0)" onclick="toCommit();">提交</a></div>
			</div>
		</div>
	</div>
</div>
<jsp:include page="/jsp/userCenter/footer.jsp"></jsp:include>
</body>
</html>
