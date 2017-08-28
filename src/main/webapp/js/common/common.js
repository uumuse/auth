$(function(){
	// 头部下拉菜单
	$(".comSite200").hover(function(){
		$(this).find(".comSite202a").show();
	},function(){
		$(this).find(".comSite202a").hide();
	});
	//关闭确认弹层
	$(".comTcSt01,.comTcSt07").on("click", function(){
		$(".comTcBg,.comTc02").hide();
	});
	//搜索
	$(document).on("click", "#searchSubmit", function(){
		var keyWord = $("#keyWord").val();
		if(keyWord == ''){
			return;
		}
		$("#searchForm").submit();
	});
});
function callback(){
	var WinHref = window.location.href;
	if(WinHref.indexOf("/kuke/userCenter/vipCenter") >= 0){//会员中心跳到会员中心
		window.location.href = '/kuke/userCenter/vipCenter';
	}else{
		window.location.href = wwwHeadUrl;
	}
}
function querySysMsg(){//跳转到消息中心
	window.location.href = '/kuke/userCenter/getSysMessageList';
}
function checkEmail(str){//校验邮箱 
	res = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/; 
	var re = new RegExp(res); 
	return !(str.match(re) == null); 
} 
function checkPhone(phone){//校验手机 
    if(!(/^1(3|4|5|7|8)\d{9}$/.test(phone))){ 
        return false; 
    }else{
    	return true; 
    } 
}
function checkLen(pwd){//校验密码长度
	if(pwd.length >= 6 && pwd.length <= 16){
		return true;
	}else{
		return false;
	}
}
function delHtmlTag(str){
	str = str.replace(/</g,"");
	str = str.replace(/>/g,"");
	return str;
}	
function getCookie(name){
	var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
	if(arr = document.cookie.match(reg)){
		return unescape(arr[2].replace(/\"/g,""));
	}else{
		return "";
	}
}