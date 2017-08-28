//###############################################自动登录/登出##########################################
$(document).ready(function(){
	CheckLogin();
});
var headUrl = "http://auth.kuke.com";
var wwwHeadUrl = "http://www.kuke.com";
var initAutoFlag = "1";//自动登录标志
//自动登录
function CheckLogin(){
	$.ajax({
		type : "POST",
		url : headUrl + "/kuke/login/load",
//		dataType : "json",
		dataType : "jsonp",
		jsonp : 'jsoncallback',
		async : false,
		success : function(info,textStatus) {
			//用户信息
			if (info.data.userStatus == 'SUCCESS') {
				$("#Login").show();
				$("#LoginMsg").show();
				//昵称
				$("#nickname").html(info.data.userNickName);
				//设置头像
				setUserImageUrl("#userImage",info.data.userPhoto);
				//未读消息数量
				if(info.data.sysCount == '0'){
					$("#sysmsgid").remove();
				}else{
					$("#sysmsgid").html(info.data.sysCount);
				}
			}else{
				$("#notLogin").show();
			}
			//机构信息
			if (info.data.orgPhoto.length > 0){
				$("#orgLogoImg").attr("src",info.data.orgPhoto);
			}else{
				$("#orgLogoImg").css("display","none");
			}
			//自动登录
			if(info.data.autoflag == "0"){
				initAutoFlag = "0";
			}
			//绑定手机弹出
			if(info.data.phoneFlag == "1"){
				$("#comSt94").show();
				showBoundPhone();
			}
		},
		error : function(textStatus,errorThrown) {
		}
	});
}
//加载头像
function setUserImageUrl(e,c){
	var imgsrc = $(e).attr("src",c);
	   $(e).load(function(){
	      //图片加载成功
	   }).error(function() {
	    //图片加载错误
	      $(e).attr("src",headUrl + "/images/default_headimg_s.jpg");
	   })
}
//自动登录
function autoLogin(obj){
	$(obj).parent().find("span").toggleClass("comSt111");
}
//登出 callback:回调方法
function loginOut(callback){
	$.ajax({
		type : "POST",
		url : headUrl + "/kuke/ssouser/authout",
//		dataType : "json",
		dataType : "jsonp",
		jsonp : 'jsoncallback',
		async : false,
		timeout : 30000,
		success : function(data, textStatus) {
			if (data.flag == true) {
				//隐藏
				$("#notLogin").show();
				$("#Login").hide();
				$("#LoginMsg").hide();
				//清空数据
				//昵称
				$("#nickname").html("");
				//设置头像
				$("#userImage").attr("src","");
				//未读消息数量
				$("#sysmsgid").html("");
				
				if(callback){
					callback();
				}else{
					window.location.href = wwwHeadUrl;
				}
			}
			if (data.flag == false) {
				alert("退出失败");
			}
		},
		error : function(data, textStatus) {
		}
	});
}
//###############################################扫码登录##########################################
var interval;//定时
//扫码登录
function qrCodeLogin(obj){
	//显示
	$("#smdiv").show();
	$("#updiv").hide();
	var i = $(".comSite136 span").index(obj);
	$(".comSite136 span").removeClass("comOn15").eq(i).addClass("comOn15");
	$(".comSite145").hide().eq(i).show();
	//二维码
	var uuid = new Date().getTime();
	uuid = uuid.toString().substring(3);
	var url = "/kuke/qrcode/phoneLogin?uuid=" + uuid;
    generateQRCode("canvas",200, 200,url);
//    generateQRCode("table",200, 200,url);
	
    //增加中心图片
//     var margin = ($("#qrcode").height()-$("#codeico").height())/2;
//     $("#codeico").css("margin",margin);
    interval = setInterval(function(){
    	$.ajax({
    		url:headUrl + "/kuke/qrcode/check?uuid=" + uuid ,
    		type:"post",
//    		dataType : "json",
    		dataType : "jsonp",
    		jsonp : 'jsoncallback',
    		async: true,
    		cache: false,
    		success:function(info){
    			if(info.flag == true){
    				clearInterval(interval);
    				window.location.reload();
    			}
    		}
    	});
      }, 1000);
}
//跳转到账户登录
function upLogin(obj){
	//显示
	$("#smdiv").hide();
	$("#updiv").show();
	var i = $(".comSite136 span").index(obj);
	$(".comSite136 span").removeClass("comOn15").eq(i).addClass("comOn15");
	$(".comSite145").hide().eq(i).show();
	
	//停止定时器调度
	clearInterval(interval);
	clearInterval(interval+1);
}
//初始化二维码
function generateQRCode(rendermethod, picwidth, picheight, url) {
//	$("#qrcode").html("");
//    $("#qrcode").qrcode({ 
//        render: rendermethod, // 渲染方式有table方式（IE兼容）和canvas方式
//        width: picwidth, //宽度 
//        height:picheight, //高度 
//        text: utf16to8(url), //内容 
//        typeNumber:-1,//计算模式
//        correctLevel:2,//二维码纠错级别
//        background:"#ffffff",//背景颜色
//        foreground:"#000000"  //二维码颜色
//    });
//	$("#qrcodeImg").attr('src',"https://api.qrserver.com/v1/create-qr-code/?size=200x200&data="+url);
	$("#qrcodeImg").attr('src',headUrl+'/kuke/payment/qr_codeImg?width=200&height=200&code_url='+url);
}
//编码转换
function utf16to8(str) {   
    var out, i, len, c;   
    out = "";   
    len = str.length;   
    for(i = 0; i < len; i++) {   
    	c = str.charCodeAt(i);   
	    if ((c >= 0x0001) && (c <= 0x007F)) {   
	        out += str.charAt(i);   
	    } else if (c > 0x07FF) {   
	        out += String.fromCharCode(0xE0 | ((c >> 12) & 0x0F));   
	        out += String.fromCharCode(0x80 | ((c >>  6) & 0x3F));   
	        out += String.fromCharCode(0x80 | ((c >>  0) & 0x3F));   
	    } else {   
	        out += String.fromCharCode(0xC0 | ((c >>  6) & 0x1F));   
	        out += String.fromCharCode(0x80 | ((c >>  0) & 0x3F));   
	    }   
    }   
    return out;   
} 
//##################################################用户名、手机号、邮箱登录###############################
//打开登录界面
function showLogin(){
	var wid = 360;
	var hei= 450;
	var ifmWin="<iframe src='' width='"+wid+"px' height='"+hei+"px' allowTransparency='true' frameborder='0' scrolling='no'></iframe>";
	var divcontainer = 
	"<div id='toLoginDIV' class='comWd06 comSite134'>" +
	"	<div class='comSite135 pr'>" +
	"		<div class='comSite136 cf'>" +
	"			<span class='comOn15' onclick='upLogin(this)'>账号登录</span>" +
	"			<span onclick='qrCodeLogin(this)'>扫码登录</span>" +
	"		</div>" +
	"		<span title='关闭' class='comSt114' onclick='closeLogin()'></span>" +
	"	</div>" +
	"	<div class='comSite116a'>" +
	"		<div id='updiv' class='comSite145'>" +
	"			<div class='comSite142'>" +
	"				<div class='comSite143'>" +
	"					<div id='userNameDiv' class='comSite144'><input name='userName' type='text' placeholder='请输入手机号/邮箱/用户名' class='comSt119' id='comSt119'></div>" +
	"					<p class='comSt120' id='comSt120' style='text-align: left;'></p>" +
	"				</div>" +
	"				<div class='comSite143'>" +
	"					<div id='userPwdDiv' class='comSite144'><input name='userPwd' type='password' placeholder='请输入6-16位密码，区分大小写' class='comSt121' id='comSt121'></div>	" +
	"					<p class='comSt122' id='comSt122' style='text-align: left;'></p>" +
	"				</div>" +
	"			</div>" +
	"			<div class='comSite137 cf'>" +
	"				<a href='"+headUrl+"/kuke/findPassword/findpwd' class='comSt109' target='_blank'>忘记密码</a>" +
	"				<div class='comSite138 pr'>" +
	"					<span id='autoFlagspan' class='comSt110 comSt111'>自动登录</span>" +
	"					<input name='autoFlag' type='checkbox' checked='checked' value='1' onclick='autoLogin(this);'>" +
	"				</div>" +
	"			</div>" +
	"			<div class='comSite139'><span class='comSt112' onclick='subLogin();'>登 录</span></div>" +
	"			<div class='comSite140 cf'>" +
	"				<a href='javascript:void(0)' onclick='toRegist()' class='comSt113'>注册 &gt;</a>" +
	"				<div class='comSite141 cf'>" +
	"					<span>其他方式：</span>" +
	"					<a href='"+headUrl+"/kuke/sns/wechat?flag=3' target='_blank' class='comSt115 comSt116'></a>" +
	"					<a href='"+headUrl+"/kuke/sns/qq?flag=1' target='_blank' class='comSt115 comSt117'></a>" +
	"					<a href='"+headUrl+"/kuke/sns/weibo?flag=2' target='_blank' class='comSt115 comSt118'></a>" +
	"				</div>" +
	"			</div>" +
	"		</div>" +
	"		<div id='smdiv' class='comSite145 pr' style='display:none;'>" +
	"			<div id='qrcode' class='comSite146'><img id='qrcodeImg' src=''></div>" +
	"			<p class='comSt123'>打开 <a href='#' target='_blank'>库客音乐APP</a> 扫一扫登录</p>" +
	"			<div class='comSite147'><p>扫码成功<br>即将登录</p></div>" +
	"		</div>" +
	"	</div>" +
	"</div>" ;
	//先移除
	$("#toLoginDIV").remove();
	//再添加
	jQuery(document.body).append(divcontainer);
	$.blockUI({
		message :  jQuery('#toLoginDIV'),
		css: {
			top:  ($(window).height() - hei) /2 + 'px',
            left: ($(window).width() - wid) /2 + 'px',
            width: wid+'px',
			height: hei+'px',
			border : 'none'
		}
	});
	//不自动登录
	if(initAutoFlag == "0"){
		$("input[type='checkbox'][name='autoFlag']").attr("checked",false);
		$("#autoFlagspan").toggleClass("comSt111");
	}
	//登录框绑定回车事件
	$('#toLoginDIV').bind('keyup', function(event) {
		if (event.keyCode == "13") {
			//回车执行查询
			subLogin();
		}
	});
}
//点击关闭
function closeLogin(){
	//停止定时器调度
	clearInterval(interval);
	clearInterval(interval+1);
	//关闭blockUI
	$.unblockUI();
	$(".blockUI").fadeOut("slow"); //支持IE6 
}
//登录
function subLogin(){
	//清除样式
	$("#comSt120").html("");
	$("#userNameDiv").attr('class','comSite144');
	$("#comSt122").html("");
	$('#userPwdDiv').attr('class','comSite144');
	
	//校验参数
	var userName = $("input[type='text'][name='userName']").val();
	var userPwd = $("input[type='password'][name='userPwd']").val();
	var autoFlag = $("input[type='checkbox'][name='autoFlag']:checked").val();
	if(userName == ""){
		$("#comSt120").html("请输入正确的手机号码/邮箱/用户名");
		$("#userNameDiv").addClass('comSite144 comOn16');
		return;
	}
	if(userPwd == ""){
		$("#comSt122").html("请输入6-16位密码,区分大小写,不含空白字符");
		$('#userPwdDiv').addClass('comSite144 comOn16');
		return;
	}
	var type = "";
	if(checkPhone(userName)){//检测是否是手机
		type = "1";
	}else if(checkEmail(userName)){//检测是否是邮箱登录
		type = "2";
	}else{//否则为用户名登录
		type = "3";
	}
	$.ajax({
		type : "POST",
		url : headUrl + "/kuke/ssouser/authuser",
//		dataType : "json",
		dataType : "jsonp",
		jsonp : 'jsoncallback',
		cache : false,
		data : {
			u          : userName,
			auto       : autoFlag,
			p          : userPwd,
			type       : type,
			from_client: "web"
		},
		async : false,
		timeout : 30000,
		success : function(info, textStatus) {
			if (info.flag == true) {
				var u = window.location.href;
				if(u == wwwHeadUrl+"/"){//如果在首页，则跳转到唱片页
					window.location.href =wwwHeadUrl + '/albums';
				}else{//否则当前页面刷新
					window.location.reload();
				}
			}
			if(info.flag == false){//登录失败
				$("#comSt122").html(info.msg);
			}
		},
		error : function(data, textStatus) {
		}
	});
}
//去注册
function toRegist(){
	//关闭登录
	closeLogin();
	//打开注册
	regist();
}
//################################################手机绑定#####################################
function showBoundPhone(){
	var wid = 360;
	var hei= 450;
	var ifmWin="<iframe src='' width='"+wid+"px' height='"+hei+"px' allowTransparency='true' frameborder='0' scrolling='no'></iframe>";
	var divcontainer = 
	"<div id='boundPhone' class='comWd06 comSite148'>" +
	"	<div class='comSite115 pr'><h1>绑定手机号</h1><span title='关闭' onclick='closeBoundPhone()'></span></div>" +
	"	<div class='comSite116a'>" +
	"		<div class='comSite149'>" +
	"			<div class='comSite152'>" +
	"				<div class='comSite151' id='comSite151'><input type='text' placeholder='您的手机号码' class='comSt124' id='comSt124'></div>" +
	"				<p class='comSt125' id='comSt125' style='text-align: left;'></p>" +
	"			</div>" +
	"			<div class='comSite150 pr'>" +
	"				<div class='comSite153 cf'>" +
	"					<span id='releaseCodeBound' class='comSt126 comSt127' onclick='getCode();'>发送验证码</span>" +
	"					<div class='comSite154' id='comSite154'><input type='text' placeholder='验证码' class='comSt128' id='comSt128'></div>" +
	"				</div>" +
	"				<p class='comSt129' id='comSt129' style='text-align: left;'></p>" +
	"			</div>" +
	"			<div class='comSite155'><span class='comSt130 comSt130a' onclick='nextBoundStep();'>下一步</span></div><!-- 绑定成功或者失败弹层 -->" +
	"			<div class='comSite156'><a href='javascript:void(0)' class='comSt131' onclick='closeBoundPhone();' >跳过 &gt;</a></div>" +
	"		</div>" +
	"		<div class='comSite157 comSite157a' style='display:none;'>" +
	"			<p class='comSt132'>绑定成功<br>开启音乐之旅</p>" +
	"			<p class='comSt134'>5秒后，窗口自动关闭</p>" +
	"			<div class='comSite158'><span class='comSt130 comSt130b' onclick='closeBoundPhone();'>确定</span></div>" +
	"		</div>" +
	"		<div class='comSite157 comSite157b' style='display:none;'>" +
	"			<p class='comSt132 comSt133'>绑定失败<br>请重新尝试</p>" +
	"			<div class='comSite158'><span class='comSt130 comSt130c' onclick='retrunBrfore();'>上一步</span></div>" +
	"		</div>" +
	"	</div>" +
	"</div>";
	//先移除
	$("#boundPhone").remove();
	//再添加
	jQuery(document.body).append(divcontainer);
	$.blockUI({
		message :  jQuery('#boundPhone'),
		css: {
			top:  ($(window).height() - hei) /2 + 'px',
            left: ($(window).width() - wid) /2 + 'px',
            width: wid+'px',
			height: hei+'px',
			border : 'none',
			'-webkit-border-radius' : '10px',
			'-moz-border-radius' : '10px'
		},
		overlayCSS: {
			backgroundColor:	'#000',
			opacity:			0.5,
			cursor:				'default'
		}
	});
}
//关闭绑定手机弹层
function closeBoundPhone(){
	//关闭blockUI
	$.unblockUI();
	$(".blockUI").fadeOut("slow"); //支持IE6
}
//下发验证码
function getCode(){
	//清除样式
	document.getElementById("comSt125").innerHTML = "";
	document.getElementById("comSite151").className = "comSite151";
	document.getElementById("comSt129").innerHTML = "";
	document.getElementById("comSite154").className = "comSite154";
	//校验参数
	var mobile = $("#comSt124").val();
	if(!checkPhone(mobile)){//校验手机是否正确
		document.getElementById("comSt125").innerHTML = "请输入正确的手机号码";
		document.getElementById("comSite151").className = "comSite151 comOn16";
		return;
	}
	$.ajax({
		type : "POST",
		url :headUrl +  "/kuke/checkAndIssue",
//			dataType : "json",
	dataType : "jsonp",
	jsonp : 'jsoncallback',
		cache : false,
		data : {
			mobile : mobile
		},
		async : false,
		timeout : 30000,
		success : function(data, textStatus) {
			if(data.flag == true){
				$("#releaseCodeBound").attr("onclick","");
				showTimes();
			}else{
//				if(data.code == "1"){
//					document.getElementById("comSt125").innerHTML = "手机号已被注册,请输入其他手机号码";
//					document.getElementById("comSite151").className = "comSite151 comOn16";
//				}else{
					document.getElementById("comSt125").innerHTML = data.msg;
					document.getElementById("comSite151").className = "comSite151 comOn16";
//				}				
			}
		},
		error : function(data, textStatus) {
		}
	});
}
//下发验证码后显示倒计时
var boundt = 180;
function showTimes(){
		if(boundt - 1 >= 0){
			boundt -= 1;  
		}
	    document.getElementById('releaseCodeBound').innerHTML= timeParse(boundt);  
	    //每秒执行一次,showTimes()  
	    if(boundt > 0){
	    	setTimeout("showTimes()",1000);  
	    }else{
	    	document.getElementById('releaseCodeBound').innerHTML= '发送验证码';
	    	$("#releaseCodeBound").attr("onclick","getCode()");
	    	boundt = 180;
	    }
}
//点击下一步
function nextBoundStep(){
	var code = $("#comSt128").val();
	var mobile = $("#comSt124").val();
	if(code != null && code != ""){
		$.ajax({
			type : "POST",
			url : headUrl + "/kuke/userCenter/boundPhone",
//			dataType : "json",
    		dataType : "jsonp",
    		jsonp : 'jsoncallback',
			cache : false,
			data : {
				mobile : mobile,
				code   : code
			},
			async : false,
			timeout : 30000,
			success : function(data, textStatus) {
				if(data.flag == true){//绑定成功
					//显示绑定成功弹层
					$(".comSite149").hide();
					$(".comSite157a").show();
					
					//5秒后关闭
					setTimeout("closeBoundPhone()",5000);  
				}else{
					if(data.code == "4"){//验证码错误
						document.getElementById("comSt129").innerHTML = "验证码错误";
						document.getElementById("comSite154").className = "comSite154 comOn16";
					}else{//显示 绑定失败,返回上一步
						$(".comSite149").hide();
						$(".comSite157b").show();
						
						//清空定时器
						document.getElementById('releaseCodeBound').innerHTML= '发送验证码';
				    	$("#releaseCodeBound").attr("onclick","getCode()");
				    	boundt = 180;
				    	//清空验证码
						$("#comSt128").val("");
					}
				}
			},
			error : function(data, textStatus) {
			}
		});
	}else{
		document.getElementById("comSt129").innerHTML = "请输入正确的验证码";
		document.getElementById("comSite154").className = "comSite154 comOn16";
	}
}
//绑定失败,返回上一步
function retrunBrfore(){
	$(".comSite149").show();
	$(".comSite157").hide();
}
//####################################校验函数###################################################
//校验邮箱 
function checkEmail(str){
	var res = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/; 
	var re = new RegExp(res); 
	return !(str.match(re) == null); 
} 
//校验手机 
function checkPhone(phone){
    if(!(/^1(3|4|5|7|8)\d{9}$/.test(phone))){ 
        return false; 
    }else{
    	return true; 
    } 
}
//校验密码长度
function checkLen(pwd){
	if(pwd.length >= 6 && pwd.length <= 16){
		return true;
	}else{
		return false;
	}
}