$(document).ready(function() {
	//$.CheckLogin();
});
var www = "http://www.kuke.com";
var auth = "http://auth.kuke.com";

function regist(){
	wid = 360,hei= 450;
//	wid = 500,hei= 600;
//	var registUrl = "http://auths.kuke.com/kuke/regist";
	var registUrl = "";
	var ifmWin="<iframe src='"+registUrl+"' width='"+wid+"px' height='"+hei+"px' allowTransparency='true' frameborder='0' scrolling='no'></iframe>";
	//var divcontainer = "<div id='loginDIV' style='display:none'><div style='width:16px;cursor:pointer;position:absolute;right:14px;top:14px;' onclick='jQuery.unblockUI()'><img src='/images/no.gif' width='16' height='15'></img></div>"+ifmWin+"</div>";
	var divcontainer = 
		"<div id='loginDIV' class='comWd06 comSite114'>"+
			"<div class='comSite115 pr'><h1>手机号注册</h1><span title='关闭' onclick='closeLogin();'></span></div>"+
			"<div class='comSite116' date-step='first'>"+
				"<div class='comSite117' style='display:block;'>"+
					"<div class='comSite133'>"+
						"<div class='comSite118'><input name='mobile' type='text' placeholder='请输入手机号' class='comSt95' id='comSt95'></div>"+
						"<p class='comSt104' id='comSt104'  style='text-align: left;'></p>"+
					"</div>"+
					"<div class='comSite133'>"+
						"<div class='comSite118'><input name='pwd' type='password' placeholder='请输入6-16位密码，区分大小写' class='comSt96' id='comSt96'></div>"+
						"<p class='comSt105' id='comSt105'  style='text-align: left;'></p>"+
					"</div>"+
				"</div>"+
				"<div class='comSite128'>"+
					"<p class='comSt108'>您的手机号：<span class='comSt106'></span></p>"+
					"<div class='comSite129 cf'>"+
						"<span class='comSt102' id='releaseCode' onclick='releaseCode()'>获取验证码</span>"+
						"<div class='comSite130'><input type='text' id='checkCode' placeholder='请输入验证码'></div>"+
					"</div>"+
					"<p class='comSt104' id='comSt107' style='text-align: left;'></p>"+
				"</div>"+
				"<div class='comSite131' id='comSite131'>"+
					"<p>注册成功</p>"+
					"<span class='comSt103' onclick='window.location.reload();'>确定</span>"+
				"</div>"+
				"<div class='comSite132'>"+
					"<div class='comSite119 cf'>"+
						"<span class='comSt97 fr' onclick='showLogin()'>去登录</span>"+
						"<span class='comSt98' onclick='isMobileExist()'>下一步</span>"+
					"</div>"+
					"<div class='comSite120 cf'>"+
						"<div class='comSite121 pr fl'>"+
							"<span id='comSt99' class='comSt99 comSt100' isAgree='true'></span>"+
							"<input type='checkbox' checked='checked' onclick='toAgree();'>"+
						"</div>"+
						"<p class='comSite122'><span>同意</span><a href='javascript:void(0)' onclick='userFile();'>库客用户注册协议</a></p>"+
					"</div>"+
					"<div class='comSite123'>"+
						"<p>注册可享受7天免费会员权益，</p>"+
						"<p>海量曲库随意听</p>"+
					"</div>"+
				"</div>"+
			"</div>"+
		"</div>";
	//先移除
	$("#loginDIV").remove();
	
	jQuery(document.body).append(divcontainer);
//	debugger;
	$.blockUI({
		message :  jQuery('#loginDIV'),
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
function G (id){
 	return document.getElementById(id);
}
var telRule = /^(13[0-9]{9}|14[5|7][0-9]{8}|15[0|1|2|3|5|6|7|8|9][0-9]{8}|17[6|7][0-9]{8}|18[0-9]{9})$/;
var pwdRule = /\s/;// 空白字符

function checkMobile(){
//	debugger;
		var value =  $(".comSt95").val();
		if(!checkPhone(value)){ // 这里少一个 如果已注册 提示请勿重复注册
			G("comSt104").innerHTML = "请输入正确的手机号码";
			G("comSt95").parentNode.className = "comSite118 comOn14";
			return false;
		}else{
			G("comSt104").innerHTML = "";
			G("comSt95").parentNode.className = "comSite118";
			return true;
		}
}
function len(){
		return G("comSt96").value.length;
	}
function checkPwd(){
//	debugger;
	var pValue = $("input[type='password'][name='pwd']").val();
	if(this.len() < 6 || this.len() > 16){
		G("comSt105").innerHTML = "请输入6-16位密码,区分大小写,不含空白字符";
		G("comSt96").parentNode.className = "comSite118 comOn14";
		return false;
	}else if(pwdRule.test(pValue)){
		G("comSt105").innerHTML = "请输入6-16位密码,区分大小写,不含空白字符";
		G("comSt96").parentNode.className = "comSite118 comOn14";
		return false;
	}else{
		G("comSt105").innerHTML = "";
		G("comSt96").parentNode.className = "comSite118";
		return true;
	}
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
//var t = 15;
function showTime(){
//	debugger;
		if(t-1>=0){
			t =t - 1;  
		}
	    document.getElementById('releaseCode').innerHTML= timeParse(t);  
	    //每秒执行一次,showTime()  
	    if(t>0){
	    	setTimeout("showTime()",1000);  
	    }
	    if(t==0){  
	    	document.getElementById('releaseCode').innerHTML= '重新获取验证码';
	    	$("#releaseCode").attr("onclick","releaseCode()");
	    }  
}
//下发验证码
function releaseCode(){
		t = 180;//初始化秒数
		var mobile = $(".comSt95").val();
		$("#comSt107").html("");
		$.ajax({
			type : "POST",
			dataType : "jsonp",
			jsonp : 'jsoncallback',
			async: false, 
			url : auth + "/kuke/issuedWeb",
			//jsonpCallback: "success_jsonpCallback",  
			data : {
				mobile : mobile
			},
			success : function(data,textStatus) {
				debugger;
				console.log(data);
				data = eval("("+data+")");
				if (data.flag == true) {
					$("#comSt107").html(data.msg);
					$("#releaseCode").attr("onclick","");
					showTime();
				}
				if (data.flag == false) {
					var s = data.msg;
					$("#comSt107").html(s);
				}
			},
			error : function(data,textStatus) {
				console.log(data);
				console.log(textStatus);
//				debugger;
			}
		});
}
function isMobileExist(){
//	debugger;
	if(checkMobile() && checkPwd() && checkUserFile()){
	var mobile = $(".comSt95").val();
	$.ajax({
		type : "POST",
		url : auth + "/kuke/checkMobile",
//		dataType : 'json',
		dataType : "jsonp",
		jsonp : 'jsoncallback',
		cache : false,
		data : {
			mobile : mobile,
			from_client:'web'
		},
		async : false,
		timeout : 30000,
		success : function(data, textStatus) {
			console.log(data);
			if (data.flag == true) {
//				$("#comSt107").html(data.msg);
				$(".comSt106").html($(".comSt95").val());
				$(".comSite117,.comSite131").hide();
				$(".comSite128,.comSite132").show();
				
				$('.comSt98')[0].onclick=function(){
					nextStep();
				}
				//直接获取验证码
				releaseCode();
				
//				$(".comSite117,.comSite128,.comSite132").hide();
//				$(".comSite131").show();
			}
			if (data.flag == false) {
				var s = data.msg;
				$("#comSt105").html(data.msg);
			}
		},
		error : function(data, textStatus) {
		}
	});
	
	}
}
function checkUserFile(){
	if($(".comSite121 input").prop("checked") == true){
		return true;
	}else{
		$("#comSt105").html("请先同意用户协议");
		return false;
	}
}
//输入手机号密码后的下一步
function nextStep(){
	if(checkMobile() && checkPwd() && checkUserFile()){
//		debugger;
		
		var mobile = $(".comSt95").val();
		var pwd = $("input[type='password'][name='pwd']").val();
		var code = $("#checkCode").val();
		if(code!=null&&code!=""){
			$.ajax({
				type : "POST",
				url : auth + "/kuke/regist",
//				dataType : 'json',
				dataType : "jsonp",
				jsonp : 'jsoncallback',
				cache : false,
				data : {
					mobile : mobile,
					pwd    : pwd,
					code   : code,
					from_client:'web'
				},
				async : false,
				timeout : 30000,
				success : function(data, textStatus) {
//					debugger;
					if (data.flag == true) {
//						$("#comSt107").html(data.msg);
						
						$(".comSite117,.comSite128,.comSite132").hide();
						$(".comSite131").show();
					}
					if (data.flag == false) {
						var s = data.msg;
						$("#comSt107").html(data.msg);
					}
				},
				error : function(data, textStatus) {
				}
			});
		}else{
			$("#comSt107").html("请输入验证码");
		}
	}
}

	function checkCode1(){ // 绑定的
	if(!this.cValue1()){ // 这里少一个验证码和数据库匹配验证
		G.id("comSt129").innerHTML = "请输入正确的验证码";
		G.id("comSite154").className = "comSite154 comOn16";
		return false;
	}else{
		G.id("comSt129").innerHTML = "";
		G.id("comSite154").className = "comSite154";
		return true;
	}
}
function checkEmail(str){ 
	res = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/; 
	var re = new RegExp(res); 
	return !(str.match(re) == null); 
} 
function checkPhone(phone){ 
    //var phone = document.getElementById('phone').value;
    if(!(/^1(3|4|5|7|8)\d{9}$/.test(phone))){ 
        return false; 
    }else{
    	return true; 
    } 
}

function checkLen(pwd){
	if(pwd.length>=6&&pwd.length<=16){
		return true;
	}else{
		return false;
	}
}
$(function(){
	// 新上唱片 鼠标悬停移开切换
	$(".comSite55 li").hover(function(){
		$(this).find(".comSt55,.comSt56").show();
	},function(){
		$(this).find(".comSt55,.comSt56").hide();
	});
	// 热门分类 鼠标悬停移开切换
	$(".comSite61").hover(function(){
		$(this).find(".comSt55").show();
		$(this).find("h2").addClass("comOn08");
	},function(){
		$(this).find(".comSt55").hide();
		$(this).find("h2").removeClass("comOn08");
	});
	// 热门分类小图 鼠标悬停移开切换
	$(".comSite63").hover(function(){
		$(this).find(".comSt55,.comSt60").show();
	},function(){
		$(this).find(".comSt55,.comSt60").hide();
	});
	// 上面三张大图 鼠标悬停移开切换
	$(".comSite51").hover(function(){
		$(this).find(".comSt53,.comSt54,h3").show();
	},function(){
		$(this).find(".comSt53,.comSt54,h3").hide();
	});
	// 登录弹层选项卡切换
	$(".comSite136 span").on("click",function(){
		var i = $(".comSite136 span").index(this);
		$(".comSite136 span").removeClass("comOn15").eq(i).addClass("comOn15");
		$(".comSite145").hide().eq(i).show();
	});
	// 库客用户协议弹层
	$(".comSite122 a").on("click",function(){
		$(".comSite124").show();
		$(".comSite114").hide();
	});
	// 关闭库客用户协议弹层
	$(".comSite126 span").on("click",function(){
		$(".comSite124").hide();
		$(".comSite114").show();
	});
	// 注册初始化
	if($(".comSite116").attr("data-step") == "first"){
		$(".comSite117").show();
		$(".comSite132").show();
		$(".comSite128").hide();
		$(".comSite131").hide();
	}
	// 下一步
//	$(".comSt98").on("click",function(){
//		
//	});
	// 关闭注册弹层
	$(".comSt103,.comSite115 span").on("click",function(){
		$(".comSite114,.comSt94").hide();
		$(".comSite116").attr("data-step") == "first";
		$(".comSt95,.comSt96").val("");
		$(".comSite130 input").val("");
	});
	// 显示注册弹层
	$(".register").on("click",function(){
		//$(".comSt94,.comSite114").show();
		//$(".comSite117").show();
		//$(".comSite132").show();
		//$(".comSite128").hide();
		//$(".comSite131").hide();
		//$(".comSite116").attr("data-step","first");
	});
	// 显示登录弹层
	$(".login").on("click",function(){
		$(".comSt119,.comSt121").val("");
		$(".comSite134,.comSt94").show();
	});
	// 关闭登录弹层
	$(".comSt114").on("click",function(){
		$(".comSite134,.comSt94").hide();
	});
	// 登录
//	$(".comSt112").on("click",function(){
//		if(checkForm.mobile1() && (checkForm.tel() || checkForm.mail() || checkForm.uname()) && checkForm.pwd1() && $(".comSite138 input").prop("checked") == true){
//			alert("可以登录啦");
//		}
//		if(checkForm.mobile1() && !checkForm.tel() && (checkForm.mail() || checkForm.uname()) && $(".comSite138 input").prop("checked") == true){
//			$(".comSite134").hide();
//			$(".comSite148").show();
//		}
//	});
	// 自动登录
	$(".comSite138 input").on("click",function(){
		$(this).parent().find("span").toggleClass("comSt111");
	});
	// 发送验证码
	$(".comSt126").on("click",function(){
		// 发送验证码
		$(this).html("02:59");
	});
	// 判断手机号绑定成功或者失败
//	$(".comSt130a").on("click",function(){
//		if(mobile2() && checkCode1()){
//			if(1>0){ // 绑定成功
//				$(".comSite149").hide();
//				$(".comSite157a").show();
//			}else{ // 绑定失败
//				$(".comSite149").hide();
//				$(".comSite157b").show();
//			}
//		}
//	});
	// 绑定失败 返回上一步
	$(".comSt130c").on("click",function(){
		$(".comSite149").show();
		$(".comSite157").hide();
	});
	// 关闭绑定弹层
	$(".comSt130b,.comSite115 span").on("click",function(){
		$(".comSite148,.comSt94").hide();
		$(".comSite149").show();
		$(".comSite157a").hide();
	});
	// 去登录
	$(".comSt97").on("click",function(){
		$(".comSite114").hide();
		$(".comSite134").show();
	});
});
function toAgree(){
	if($("#comSt99").attr("isAgree") == "true"){
		$("#comSt99").attr("isAgree",false);
		$("#comSt99").attr("class","comSt99");
	}else{
		$("#comSt99").attr("isAgree",true);
		$("#comSt99").attr("class","comSt99 comSt100");
	}
}
function closeUserFile(){
	//关闭blockUI
	$.unblockUI();
	//打开注册
	regist();
}
function userFile(){//用户协议
	wid = 846,hei= 531;
	var url = "";
	var ifmWin="<iframe src='"+url+"' width='"+wid+"px' height='"+hei+"px' allowTransparency='true' frameborder='0' scrolling='no'></iframe>";
	var divcontainer =
			"<div id='userFileDIV' class='comSite124'>" +
			"	<div class='comSite125'>" +
			"		<div class='comSite126 pr'><h1>库客用户协议</h1><span title='关闭' onclick='closeUserFile();'></span></div>" +
			"		<div class='comSite127'>" +
			"			<dl>" +
			"				<dt>1、库客网络服务条款的接受</dt>" +
			"				<dd>库客音乐网站（以下简称“库客 ”），库客 提供的服务完全按照其发布的服务条款和操作规则。本服务条款所称的用户是指完全同意所有条款并完成注册程序或未经注册而使用库客服务（以下简称“本服务”）的用户。</dd>" +
			"			</dl>" +
			"			<dl>" +
			"				<dt>2、服务条款的变更和修改</dt>" +
			"				<dd>库客有权随时对服务条款进行修改，并且一旦发生服务条款的变动，库客将在页面上提示修改的内容；当用户使用库客的特殊服务时，应接受库客随时提供的与该特 殊服务相关的规则或说明，并且此规则或说明均构成本服务条款的一部分。用户如果不同意服务条款的修改，可以主动取消已经获得的网络服务；如果用户继续享用 网络服务，则视为用户已经接受服务条款的修改。</dd>" +
			"			</dl>" +
			"			<dl>" +
			"				<dt>3、服务说明</dt>" +
			"				<dd>（1）库客运用自己的操作系统通过国际互联网向用户提供丰富的网上资源。除非另有明确规定，增强或强化目前服务的任何新功能，包括新产品，均无条件地适用本服务条款。</dd>" +
			"				<dd>（2）库客对网络服务不承担任何责任，即用户对网络服务的使用承担风险。库客不保证服务一定会满足用户的使用要求，也不保证服务不会受中断，对服务的及时性、安全性、准确性也不作担保。</dd>" +
			"				<dd>（3）为使用本服务，用户必须：</dd>" +
			"				<dd class='comSt101'>A.自行配备进入国际互联网所必需的设备，包括计算机、数据机或其他存取装置；</dd>" +
			"				<dd class='comSt101'>B.自行支付与此服务有关的费用；</dd>" +
			"				<dd>（4）用户接受本服务须同意：</dd>" +
			"				<dd class='comSt101'>A. 提供完整、真实、准确、最新的个人资料；</dd>" +
			"				<dd class='comSt101'>B. 不断更新注册资料，以符合上述（4）a.的要求。</dd>" +
			"				<dd class='comSt101'>C.若用户提供任何错误、不实、过时或不完整的资料，并为库客所确知；或者库客有合理理由怀疑前述资料为错误、不实、过时或不完整，库客有权暂停或终止用户的帐号，并拒绝现在或将来使用本服务的全部或一部分。</dd>" +
			"			</dl>" +
			"			<dl>" +
			"				<dt>4、用户应遵守以下法律及法规：</dt>" +
			"				<dd>（1）用户同意遵守《中华人民共和国保守国家秘密法》、《中华人民共和国计算机信息系统安全保护条例》、《计算机软件保护条例》等有关计算机相关互联网规 定的法律和法规、实施办法。在任何情况下，库客合理地认为用户的行为可能违反上述法律、法规，库客可以在任何时候，不经事先通知终止向该用户提供服务。</dd>" +
			"				<dd>（2）用户应了解国际互联网的无国界性，应特别注意遵守当地所有有关的法律和法规。</dd>" +
			"			</dl>" +
			"			<dl>" +
			"				<dt>5、用户隐私权制度</dt>" +
			"				<dd>（1）当用户注册库客的服务时，用户须提供个人信息。库客收集个人信息的目的是为用户提供尽可能多的个人化网上服务以及为广告商提供一个方便的途径来接触 到适合的用户，并且可以发送具有相关性的内容和广告。在此过程中，广告商绝对不会接触到用户的个人信息。 库客不会在未经合法用户授权时，公开、编辑或透露其个人信息除非有下列情况：</dd>" +
			"				<dd class='comSt101'>A.有关法律规定或库客合法服务程序规定；</dd>" +
			"				<dd class='comSt101'>B.在紧急情况下，为维护用户及公众的权益；</dd>" +
			"				<dd class='comSt101'>C.为维护库客的商标权；</dd>" +
			"				<dd class='comSt101'>D.其他需要公开、编辑或透露个人信息的情况；</dd>" +
			"			</dl>" +
			"			<dl>" +
			"				<dt>6、用户帐号、密码和安全</dt>" +
			"				<dd>（1）用户一旦注册成功，便成为库客的合法用户，将得到一个密码和帐号。用户有义务保证密码和帐号的安全。用户对利用该密码和帐号所进行的一切活动负全部责任；因此所衍生的任何损失或损害，库客 无法也不负担任何责任。</dd>" +
			"				<dd>（2）用户的密码和帐号遭到未授权的使用或发生其他任何安全问题，用户可以立即通知库客，并且用户在每次连线结束，应结束帐号使用，否则用户可能得不到库 客的安全保护。若因您未及时更新基本资料，导致服务无法提供或提供时发生任何错误，您不得将此作为取消交易，拒绝付款的理由，本公司亦不承担任何责任。</dd>" +
			"			</dl>" +
			"			<dl>" +
			"				<dt>7、对用户信息的存储和限制</dt>" +
			"				<dd>库客不对用户所发出的服务要求失败负责。Kuk 有权判断用户的行为是否符合库客 服务条款规定的权利，如果库客认为用户违背了服务条款的规定，库客有中断向其提供网络服务的权利。</dd>" +
			"			</dl>" +
			"			<dl>" +
			"				<dt>8、禁止用户从事以下行为：</dt>" +
			"				<dd>（1）干扰或破坏本服务或与本服务相连的服务器和网络，或不遵守本服务网络使用之规定。</dd>" +
			"				<dd>（2）故意或非故意违反任何相关的中国法律、法规、规章、条例等其他具有法律效力的规范。</dd>" +
			"				<dd>（3）对于经由本服务而传送的内容，库客不保证前述内容的正确性、完整性或品质。用户在接受本服务时，有可能会接触到令人不快、不适当或令人厌恶的内容。 在任何情况下，库客均不对任何内容负责，包括但不限于任何内容发生任何错误或纰漏以及衍生的任何损失或损害。库客有权（但无义务）自行拒绝或删除经由本服 务提供的任何内容。用户使用上述内容，应自行承担风险。</dd>" +
			"				<dd>（4）库客有权利在认为有必要的情况下述情况下，对内容进行保存或披露。</dd>" +
			"			</dl>" +
			"			<dl>" +
			"				<dt>9、用户不得对本服务进行复制、出售或用作其他商业用途。</dt>" +
			"			</dl>" +
			"			<dl>" +
			"				<dt>10、关于库客部分音乐收费服务：一旦用成为库客的合法用户，则认为用户知道并愿意接受，并同意库客通过收费方式向用户收取部分收费服务的费用。</dt>" +
			"			</dl>" +
			"			<dl>" +
			"				<dt>11、库客的知识产权及其他权利</dt>" +
			"				<dd>（1）库客对本服务及本服务所使用的软件包含受知识产权或其他法律保护的资料享有相应的权利；</dd>" +
			"				<dd>（2）经由本服务传送的资讯及内容，受到著作权法、商标法、专利法或其他法律的保护；未经库客明示授权许可，用户不得进行修改、出租、散布或衍生其他作品。</dd>" +
			"				<dd>（3）用户对本服务所使用的软件有非专属性使用权，但自己不得或许可任何第三方复制、修改、出售或衍生产品。</dd>" +
			"				<dd>（4） 库客，库客设计图样以及其他库客图样、产品及服务名称，均为库客公司所享有的商标。任何人不得使用、复制或用作其他用途。</dd>" +
			"			</dl>" +
			"			<dl>" +
			"				<dt>12、免责声明</dt>" +
			"				<dd>（1）库客对于任何包含、经由或连接、下载或从任何与有关本服务所获得的任何内容、信息或广告，不声明或保证其正确性或可靠性；并且对于用户经本服务上的广告、展示而购买、取得的任何产品、信息或资料，库客不负保证责任。用户自行负担使用本服务的风险。</dd>" +
			"				<dd>（2）库客有权但无义务，改善或更正本服务任何部分之任何疏漏、错误。</dd>" +
			"				<dd>（3）库客不保证（包括但不限于）：</dd>" +
			"				<dd class='comSt101'>A.本服务适合用户的使用要求；</dd>" +
			"				<dd class='comSt101'>B.本服务不受干扰，及时、安全、可靠或不出现错误，包括黑客入侵，网络中断，电信问题及其他不可抗力等;c用户经由本服务取得的任何产品、服务或其他材料符合用户的期望；</dd>" +
			"				<dd>（4） 用户使用经由本服务下载的或取得的任何资料，其风险自行负担；因该使用而导致用户电脑系统损坏或资料流失，用户应负完全责任；</dd>" +
			"				<dd>（5） 基于以下原因而造成的利润、商业信誉、资料损失或其他有形或无形损失，库客不承担任何直接、间接、附带、衍生或惩罚性的赔偿：a本服务使用或无法使用；b用户资料遭到未授权的使用或修改；c其他与本服务相关的事宜。</dd>" +
			"				<dd>（6） 用户在浏览网际网路时自行判断使用库客的检索目录。该检索目录可能会引导用户进入到被认为具有攻击性或不适当的网站，库客没有义务查看检索目录所列网站的内容，因此，对其正确性、合法性、正当性不负任何责任。</dd>" +
			"			</dl>" +
			"			<dl>" +
			"				<dt>13、由于用户经由本服务张贴或传送内容、与本服务连线、违反本服务条款或侵害其他人的任何权利导致任何第三人提出权利主张，用户同意赔偿库客 及其分公司、关联公司、代理人或其他合作伙伴及员工，并使其免受损害。</dt>" +
			"			</dl>" +
			"			<dl>" +
			"				<dt>14、服务的修改和终止</dt>" +
			"				<dd>库客有权在任何时候，暂时或永久地修改或终止本服务（或任何一部分），无论是否通知。库客对本服务的修改或终止对用户和任何第三人不承担任何责任。库客有 权基于任何理由，终止用户的帐号、密码或使用本服务， 库客采取上述行为均不需通知，并且对用户和任何第三人不承担任何责任。</dd>" +
			"			</dl>" +
			"			<dl>" +
			"				<dt>15、通知</dt>" +
			"				<dd>库客向用户发出的通知，采用电子音乐搜索或页面公告或常规信件的形式。服务条款的修改或其他事项变更时，库客将会以上述形式进行通知。</dd>" +
			"			</dl>" +
			"			<dl>" +
			"				<dt>16、禁止服务的商业化</dt>" +
			"				<dd>用户使用库客各项服务的权利是个人的。用户只能是一个单独的个体而不能是一个公司或实体的商业性组织。用户承诺不经极限音乐公司同意，不能利用库客服务进行销售或其他商业用途。</dd>" +
			"			</dl>" +
			"			<dl>" +
			"				<dt>17、法律的适用和管辖</dt>" +
			"				<dd>本服务条款的生效、履行、解释及争议的解决均适用中华人民共和国法律。本服务条款因与中华人民共和国现行法律相抵触而导致部分无效，不影响其他部分的效力。</dd>" +
			"			</dl>" +
			"		</div>" +
			"	</div>" +
			"</div>";
	
	//先移除
	$("#userFileDIV").remove();
	
	jQuery(document.body).append(divcontainer);
	$.blockUI({
		message :  jQuery('#userFileDIV'),
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