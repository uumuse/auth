<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="-1">
<title>登录首页_首都图书馆</title>
<meta name="keywords" content="">
<meta name="description" content="">
<link href="/css/third_st_init.css" rel="stylesheet">
<link href="/css/third_st_login.css" rel="stylesheet">
<script type="text/javascript" src="/js/jquery-1.12.4.min.js"></script>
<script type="text/javascript">
	$(function(){
		$("#csign").on("click",function(){
			$(".popUp").hide();
			$(".popBg").hide();
		});
		$(".popUp-btn").on("click",function(){
			$(".popUp").hide();
			$(".popBg").hide();
		});
	});
	
    function submit(){
    	var username = $("#username").val();
    	var pwd = $("#pwd").val();
    	if(username!=null&&pwd!=null){
	    	$.ajax({
	    		url:"/kuke/st/stALEPH",
	    		data:{
	    			id:username,
	    			verification:pwd
	    		},
	    		type:"post",
	    		dataType:"json",
	    		success:function(data){
	    			if(data.flag == true){
	    				window.location.href=data.msg;
	    			}else{
	    				$(".popUp-tit").text("用户名或密码错误!");
	    				$(".popBg").show();
	    				$(".popUp").show();
	    				//alert("用户名或密码错误!");
	    				
	    			}
	    		}
	    	});
    		
    	}else{
    		$(".popUp-tit").text("用户名和密码不能为空!");
			$(".popBg").show();
			$(".popUp").show();
    	}
    } 
</script>
</head>
<body>
<div class="comSite">
	<div class="tsite-top">
		<div class="tsite-logo twd01"><img src="/images/third/tlogo.png" alt="" width="620" height="68"></div>
	</div>
	<div class="tsitebg">
		<div class="tMain twd01">
			<div class="tMain-top"></div>
			<div class="tMain-con">
				<div class="tcon cf">
					<div class="tLeft">
						<h2>会员登录<span>Login</span></h2>
						<div class="tform">
							
							
								<table cellpadding="0" cellspacing="0">
									<tr>
										<td><span class="c1">用户名：</span></td>
										<td><input type="text" id="username" class="c2" onfocus="this.className='c2 c4'" onblur="this.className='c2'"></td>
									</tr>
									<tr>
										<td><span class="c1">密<em>码</em>：</span></td>
										<td><input type="password" id="pwd" class="c2" onfocus="this.className='c2 c4'" onblur="this.className='c2'"></td>
									</tr>
									<tr>
										<td><span class="c1">&nbsp;</span></td>
										<td><input type="submit" onClick="submit()" class="c3" value="登 录"></td>
									</tr>
								</table>
								
								
						</div>
					</div>
					<div class="tRight"></div>
				</div>
			</div>
			<div class="tMain-bottom"></div>
		</div>
		<div class="tfooter">
			<p>京ICP备09067229号<span>京公网安备 110105000296</span></p>
		</div>
	</div>
</div>
<div class="popBg" style="display:none;"></div>
<div class="popUp" style="display:none;">
	<div class="popUp-top pr"><span title="关闭" id="csign"></span></div>
	<div class="popUp-con">
		<p class="popUp-tit"></p>
		<span class="popUp-btn"">关闭</span>
	</div>
</div>
</body>
</html>