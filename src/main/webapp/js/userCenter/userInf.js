//用户信息页面初始化
function userInfoInit(sexValue,year,month,day,constellation,province,city,thisYear){
	//初始化年
	if(year != ""){
		$("#year").val(year);
		$("#year").parent().find('.comSt177').html(year);
	}else{
		$("#year").val(thisYear);
		$("#year").parent().find('.comSt177').html(thisYear);
	}
	//初始化月
	getMonth('year','month','day');
	$("#month").val(month);
	//初始化日
	getDay('year','month','day');
	$("#day").val(day);
	//初始化星座
	$("#constellation").val(constellation);
	$("#constellation").parent().find('.comSt177').html(constellation);
	//初始化省
	$("#province").val(province);
	//初始化市
	getCity('province','city');
	$("#city").val(city);
	$("#city").parent().find('.comSt177').html(city);
	
	$("#month").val(month);
	$("#month").parent().find('.comSt177').html(month);
	$("#day").parent().find('.comSt177').html(day);
	//初始化性别
	if(sexValue == "male"){//男
		$(".comSt174").removeClass("comSt174-on").eq(0).addClass("comSt174-on");
	}else if(sexValue == "female"){//女
		$(".comSt174").removeClass("comSt174-on").eq(1).addClass("comSt174-on");
	}else if(sexValue == "secrecy"){//保密
		$(".comSt174").removeClass("comSt174-on").eq(2).addClass("comSt174-on");
	}else{//男
		sexValue = "male";
		$(".comSt174").removeClass("comSt174-on").eq(0).addClass("comSt174-on");
	}
	$("input[name=sex]").each(function(){
		if(this.value == sexValue){
			this.checked = true;
		}
	});
}
//保存个人资料
function updateUserInfo(){
	debugger;
	//用户名
	var name = $.trim($("#name").val());
	if(name != ""){
		if(name.length < 2 || name.length > 20){
			tips("用户名应该为2-20个字符");
			return;
		}
	}
	//签名
	var signature = $.trim($("#signature").val());
	if(signature != ""){
		if(signature.length > 30){
			tips("签名不应超过30个字");
			return;
		}
	}
	//性别
	var sex = $("input[name=sex]:checked").val();
	//生日
	var year = $("#year").val();
	var month = $("#month").val();
	var day = $("#day").val();
	if(year != ""){
		if(month == ""){
			tips("请选择月份");
			return;
		}
		if(day == ""){
			tips("请选择日期");
			return;
		}
	}
	//星座
	var constellation = $("#constellation").val();
	//省
	var province = $("#province").val();
	//市
	var city = $("#city").val();
	
	name = delHtmlTag(name);
	signature = delHtmlTag(signature);
	
	$.ajax({
		url:"/kuke/userCenter/updateUserInf",
		type:"post",
		dataType:"json",
		async:false,
		data:{signature:signature,sex:sex,constellation:constellation,province:province,
			city:city,year:year,month:month,day:day,nickname:name},
		success:function(data){
			if(data.flag == true){
				//右上角的昵称
				$("#nickname").html(name);
				$(".comSite226b").find(".comSt201").html("保存成功");
				$(".comSite226b").show();
				setTimeout(function(){
					//隐藏提示框
					$(".comSite226b").hide();
					$(".comSite226b").find(".comSt201").html("绑定成功");
				},2000);
			}else{
				tips(data.msg);
			}
		}
	});
}
//跳转到更换头像
function changeImg(){
	for(var j=0;j<$(".comSite206a a").length;j++){
		$(".comSite206a a").removeClass("comN"+(j+1)+"-on");
	}
	$(".comSite206a a").eq(1).addClass("comN2-on");
	$(".comSite208").hide().eq(1).show();
}

//上传头像
var flag = false;
var imgUrl,imgWidth,imgHeight;
var imageSelectObject = null;
var defaultWH = 200;
var x = 0, y = 0, w = defaultWH, h = defaultWH;
var value = 0;
function preview(img, selection) {
    if (!selection.width || !selection.height){
    	return;
    }
    $('#crop_preview_200').css({
        width: Math.round(200 / selection.width * imgWidth),
        height: Math.round(200 / selection.height * imgHeight),
        marginLeft: -Math.round(200 / selection.width * selection.x1),
        marginTop: -Math.round(200 / selection.height * selection.y1)
    });
    $('#crop_preview_50').css({
        width: Math.round(50 / selection.width * imgWidth),
        height: Math.round(50 / selection.height * imgHeight),
        marginLeft: -Math.round(50 / selection.width * selection.x1),
        marginTop: -Math.round(50 / selection.height * selection.y1)
    });
    $('#crop_preview_25').css({
        width: Math.round(25 / selection.width * imgWidth),
        height: Math.round(25 / selection.height * imgHeight),
        marginLeft: -Math.round(25 / selection.width * selection.x1),
        marginTop: -Math.round(25 / selection.height * selection.y1)
    });
    x = selection.x1;
    y = selection.y1;
    w = selection.width;
    h = selection.height;   
//    alert(x+" "+y+" "+w+" "+h);
    console.log(x+" "+y+" "+w+" "+h);
}
//上传头像
function uploadPhoto(){
	var uploadId = "uploadUserPhoto";
	var photoFile = $("#"+uploadId).val();
	if(photoFile != null && photoFile != ''){
//		$("#uploadMsg").html("图片加载中，请稍后....");
		$.ajaxFileUpload( {
			url : '/kuke/userCenter/headImageUpload?uploadId='+uploadId,
			secureuri : false,
			fileElementId : uploadId,
			dataType : 'text',
			success : function(info, textStatus) {
				flag = true;
				info = eval("("+info+")");
				if(info.flag == false){
					tips(info.msg);
				}else if(info.flag == true){
//					$("#uploadMsg").html("图片上传成功");
					imgUrl = info.data.file;
					imgWidth  = info.data.width;
					imgHeight = info.data.height;
					var padding_top = (300 - imgHeight)/2 + "px";
					var padding_left = (300 - imgWidth)/2 + "px";
					$("#photo_main").parent().css("padding-bottom",padding_top);
					$("#photo_main").parent().css("padding-right",padding_left);
					$("#photo_main").attr("src",imgUrl);
					$("#photo_main").attr("width",defaultWH);
					$("#photo_main").attr("height",defaultWH);
					$("#crop_preview_200").attr("src",imgUrl);
					$("#crop_preview_50").attr("src",imgUrl);
					$("#crop_preview_25").attr("src",imgUrl);
					imageSelectObject = $("#photo_main").imgAreaSelect({
						 instance: true,
					     handles: true,
					     aspectRatio: '1:1',
					     x1: x, y1: y, x2: w, y2: h,
					     onSelectEnd: preview
					     
					});
					selection = {x1: x, y1: y, x2: w, y2: h, width: w, height: h };
					preview($("#photo_main"),selection);
			   }
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				//alert("textStatus=" + textStatus + "\nerrorThrown="+ errorThrown);
			}
	});
	}else{
		return false;
	}
}
//保存图片
function cutPhoto(){
	if(flag){
		jQuery.ajax({
			url : "/kuke/userCenter/headImageUpdate",
			type : "post",
			data : {"x":x,"y":y,"w":w,"h":h},
			dataType : 'json',
			timeout : 10000,
			success : function(data, textStatus) {
				if (data.flag == true) {
					cancleChangeImg();
				}else if(data.flag == false){
					tips(data.msg);
				}
			},
			error:function(XMLHttpRequest,textStatus,errorThrown){
				//alert("textStatus="+textStatus+"\nerrorThrown="+errorThrown);
			}
		});
	}else{
		tips("请上传图片");
	}
}
//取消图片
function cancleChangeImg(){
	window.location.href = "/kuke/userCenter/userInf?site=1";
}
function setSite(site){//跳转到相应的选项卡
	for(var j=0;j<$(".comSite206a a").length;j++){
		$(".comSite206a a").removeClass("comN"+(j+1)+"-on");
	}
	if(site == ""){
		site = "0";
	}
	$(".comSite206a a").eq(site).addClass("comN"+(parseInt(site)+1)+"-on");
	$(".comSite208").hide().eq(site).show();
	if(site == 3){//个人设置初始化
		boundinit();
	}
}
//修改密码
function changePwd(){
	var password = $("#password").val();//旧密码
	var newpassword = $("#comSt198a").val();//新密码
	var newcpassword = $("#comSt198b").val();//新确认密码
	if(password == ""){//旧密码判定
		G.id("comSite225c").className = "comSite225 comOn16";
		G.id("comSt200c").innerHTML = "请输入旧密码";
		return;
	}
	if(newpassword == ""){//新密码判定
		G.id("comSite225a").className = "comSite225 comOn16";
		G.id("comSt200a").innerHTML = "请输入新密码";
		return;	
	}else if(newpassword.length < 6 || newpassword.length > 16){
		G.id("comSite225a").className = "comSite225 comOn16";
		G.id("comSt200a").innerHTML = "密码长度为6-16位";
		return;	
	}
	if(newcpassword == ""){//确认密码判定
		G.id("comSite225b").className = "comSite225 comOn16";
		G.id("comSt200b").innerHTML = "请再次输入新密码";
		return;
	}else if(newcpassword != newpassword){
		G.id("comSite225b").className = "comSite225 comOn16";
		G.id("comSt200b").innerHTML = "两次新密码输入不一致";
		return;
	}
	$.ajax({
		url:"/kuke/userCenter/savePassword",
		type:"post",
		dataType:"json",
		async:false,
		data:{oldPass:password,newPass:newpassword,newCPass:newcpassword},
		success:function(data){
			if(data.flag == true){
				$(".comSite226a").show();
				setTimeout(function(){
					//隐藏提示框
					$(".comSite226a").hide();
//					//密码框清0
//					$("#password").val("");//旧密码
//					$("#comSt198a").val("");//新密码
//					$("#comSt198b").val("");//新确认密码
					//跳转到登录
					window.location.href = "/kuke/login/outBreforeIndex";
				},2000);
			}else{
				tips(data.msg);
			}
		}
	});
}
//绑定设置初始化
function boundinit(){
	$.ajax({
		url:"/kuke/userCenter/boundInit",
		type:"post",
		dataType:"json",
		async:false,
		success:function(info){
			if(info.flag == true){
				//初始化个人设置
				$(".comSite206a a").eq(3).addClass("comN4-on");
				$(".comSite208").hide().eq(3).show();
				//手机
				getHtml('phone',info.data.PhoneBoundFlag,'a',info.data.PhoneBoundName);
				//邮箱
				getHtml('email',info.data.EmailBoundFlag,'e',info.data.EmailBoundName);
				//微信
				getHtml('wechat',info.data.WeChatBoundFlag,'b',info.data.WeChatBoundName);
				//QQ
				getHtml('qq',info.data.QQBoundFlag,'c',info.data.QQBoundName);
				//weibo
				getHtml('weibo',info.data.SINABoundFlag,'d',info.data.SINABoundName);
			}else{
				tips(info.msg);
			}
		}
	});
}
//初始化拼接
function getHtml(type,flag,className,name){
//	debugger;
	var html = "<span id='"+type+"infob' class='comSt206' onclick=unbound('"+type+"');>解除绑定</span>";
	if(flag == "0"){//绑定
		var typname = "";
		if(type == 'phone'){
			typname = "手机";
		}else if(type == 'email'){
			typname = "邮箱";
		}else if(type == 'wechat'){
			typname = "微信";
		}else if(type == 'weibo'){
			typname = "微博";
		}else if(type == 'qq'){
			typname = "QQ";
		}
		//清空文字说明
		$("#"+type+"infoa").html("<em class='comSt205"+className+"'>&nbsp;</em>");
		//转换点击按钮
		var html = "<span id='"+type+"infob' class='comSt206' onclick=bound('"+type+"')>绑定<em class='comSt206"+className+"'>"+typname+"</em></span>";
		$("#"+type+"infob").replaceWith(html);
	}else if(flag == "1"){//解除绑定
		//添加文字说明
		$("#"+type+"infoa").html("已绑定：<em class='comSt205"+className+"'>"+name+"</em>");
		//转换点击按钮
		var html = "<span id='"+type+"infob' class='comSt206' onclick=unbound('"+type+"');>解除绑定</span>";
		$("#"+type+"infob").replaceWith(html)
	}
}
//绑定
function bound(type){
//	debugger;
	var className = "";
	var name = "";
	var postUrl = "";
	if(type == 'phone'){
		$(".comSite228a").show();
		$("#phonetip").show();
//		postUrl = "/kuke/userCenter/boundPhone";
//		className = "a";
//		name = "手机";
	}else if(type == 'email'){
		$(".comSite228a").show();
		$("#emailtip").show();
//		postUrl = "/kuke/userCenter/boundEmail";
//		className = "e";
//		name = "邮箱";
	}else if(type == 'wechat'){
		postUrl = "/kuke/sns/wechat";
		className = "b";
		name = "微信";
		window.open(postUrl);
	}else if(type == 'qq'){
		postUrl = "/kuke/sns/qq";
		className = "c";
		name = "QQ";
		window.open(postUrl);
	}else if(type == 'weibo'){
		postUrl = "/kuke/sns/weibo";
		className = "d";
		name = "微博";
		window.open(postUrl);
	}else{
		return;
	}
}
//验证绑定的手机：确定
function boundPhone(){
	var mobile = $("#comSt124").val();
	var code = $("#comSt128").val(); 
	if(mobile == ""){
		document.getElementById("comSt125").innerHTML = "请输入正确的手机号码";
		document.getElementById("comSite151").className = "comSite151 comOn16";
		return;
	}else{
		if(!checkPhone(mobile)){
			document.getElementById("comSt125").innerHTML = "请输入合法的手机号";
			document.getElementById("comSite151").className = "comSite151 comOn16";
			return;
		}
	}
	if(code == ""){
		tips("验证码不能为空");
		document.getElementById("comSt129").innerHTML = "验证码不能为空";
		document.getElementById("comSite154").className = "comSite154 comOn16";
		return;
	}
	$.ajax({
		url:"/kuke/userCenter/boundPhone",
		type:"post",
		data:{mobile:mobile,code:code},
		dataType:"json",
		async:false,
		success:function(info){
			if(info.flag == true){
				$("#phonetip").hide();
				$(".comSite226b").show();//绑定成功
				setTimeout(function(){ 
					//隐藏提示弹层
					$(".comSite226b").hide();
					$(".comSite228a").hide();
					getHtml('phone','1','a',info.data.mobile);
					$("#comSt124").val("");
					$("#comSt128").val(""); 
			    }, 2000);
			}else{
				tips(info.msg);
			}
		}
	});
}
//检查手机是否被注册
function checkUserPhone(mobile){
//	debugger;
	var flag = false;//数据库中没有
	$.ajax({
		url:"/kuke/findPassword/checkUserPhone",
		type:"post",
		data:{mobile:mobile},
		dataType:"json",
		async:false,
		success:function(info){
//			debugger;
			console.log(info);
			if(info.data.result > 0){
				flag = true;
			}
		}
	});
	return flag;
}
//下发验证码
function getPhoneCode(){
	//还原样式
	document.getElementById("comSt125").innerHTML = "";
	document.getElementById("comSite151").className = "comSite151";
	//校验
	var mobile = $("#comSt124").val();
	if(!checkPhone(mobile)){//校验手机是否正确
		document.getElementById("comSt125").innerHTML = "请输入正确的手机号码";
		document.getElementById("comSite151").className = "comSite151 comOn16";
		return;
	}else{
		if(checkUserPhone(mobile)){
			document.getElementById("comSt125").innerHTML = "手机号已经被注册";
			document.getElementById("comSite151").className = "comSite151 comOn16";
			return;
		}
	}
	//下发验证码
	$.ajax({
		type : "POST",
		url :  "/kuke/checkAndIssue",
		dataType : 'json',
		cache : false,
		data : {
			mobile : mobile
		},
		async : false,
		timeout : 30000,
		success : function(data, textStatus) {
			if(data.flag == true){
				$("#UserBoundPhone").attr("onclick","");
				userInfoShowTime();
			}else{
				tips(data.msg);
			}
		},
		error : function(data, textStatus) {
		}
	});
}
//下发验证码后显示倒计时
var userboundt = 180;
function userInfoShowTime(){
		if(userboundt - 1 >= 0){
			userboundt =userboundt - 1;  
		}
	    document.getElementById('UserBoundPhone').innerHTML= timeParse(userboundt);  
	    //每秒执行一次,showTimes()  
	    if(userboundt > 0){
	    	setTimeout("userInfoShowTime()",1000);  
	    }else{
	    	document.getElementById('UserBoundPhone').innerHTML= '发送验证码'; 
	    	$("#UserBoundPhone").attr("onclick","getPhoneCode()");
	    	userboundt = 180;
	    }
}
//点击“去验证”发送邮件
function boundEmail(){
	var email = $("#comSt124a").val();
	if(!checkEmail(email)){
		tips("请输入合法的邮箱");
		return;
	}
	$.ajax({
		url:"/kuke/userCenter/sendBoundEmail",
		type:"post",
		data:{email:email},
		dataType:"json",
		async:false,
		success:function(data){
			if(data.flag == true){
				$("#emailtip").hide();
				$(".comSite226d").show();//绑定成功
				setTimeout(function(){ 
					//隐藏提示弹层
					$(".comSite226d").hide();
					$(".comSite228a").hide();
			    }, 2000);
			}else{
				tips(data.msg);
			}
		}
	});
}
//关闭手机，邮箱弹窗提示
function closeTips(id){
	$(".comSite228a").hide();
	$("#"+id).hide();
}
//绑定后处理函数
function toBound(type,name,flag,msg){
	var className = "";
	if(type == 'phone'){
		className = "a";
	}else if(type == 'email'){
		className = "e";
	}else if(type == 'wechat'){
		className = "b";
	}else if(type == 'qq'){
		className = "c";
	}else if(type == 'weibo'){
		className = "d";
	}else{
		return;
	}
	if(flag == "1"){
		$(".comSite226b").show();
		setTimeout(function(){ 
			//隐藏提示弹层
			$(".comSite226b").hide();
			//添加文字说明
			$("#"+type+"infoa").html("已绑定：<em class='comSt205"+className+"'>"+name+"</em>");
			//转换点击按钮
			var html = "<span id='"+type+"infob' class='comSt206' onclick=unbound('"+type+"');>解除绑定</span>";
			$("#"+type+"infob").replaceWith(html);
	    }, 2000);
	}else{
		tips(msg);
	}
}
//显示解除绑定弹层
function unbound(type){
	var typename = "";
	if(type == 'phone'){
		typename = "手机";
	}else if(type == 'email'){
		typename = "邮箱";
	}else if(type == 'wechat'){
		typename = "微信";
	}else if(type == 'qq'){
		typename = "QQ";
	}else if(type == 'weibo'){
		typename = "微博";
	}
	$(".comSt208").html(typename);
	$(".comSite228a,.comSite229a").show();
	//赋予确定onclick属性
	$(".comSt209a").attr("href","javascript:toUnbound('"+type+"');");
}
//解除绑定
function toUnbound(type){
	$(".comSite228a,.comSite229a").hide();
	var postUrl = "";
	var className = "";
	var name = "";
	if(type == 'phone'){
		postUrl = "/kuke/userCenter/unBoundPhone";
		className = "a";
		name = "手机";
	}else if(type == 'email'){
		postUrl = "/kuke/userCenter/unBoundEmail";
		className = "e";
		name = "邮箱";
	}else if(type == 'wechat'){
		postUrl = "/kuke/userCenter/unBoundWeChat";
		className = "b";
		name = "微信";
	}else if(type == 'qq'){
		postUrl = "/kuke/userCenter/unBoundQQ";
		className = "c";
		name = "QQ";
	}else if(type == 'weibo'){
		postUrl = "/kuke/userCenter/unBoundSINA";
		className = "d";
		name = "微博";
	}else{//验证唯一身份识别
		return;
	}
	$.ajax({
		url:"/kuke/userCenter/boundInit",
		type:"post",
		dataType:"json",
		async:false,
		success:function(info){
			if(info.flag == true){
				var count = info.data.count;
				if(count <= 1){
					tips("该绑定是您身份识别的唯一凭证,不能解绑!");
				}else{
					$.ajax({
						url:postUrl,
						type:"post",
						dataType:"json",
						async:false,
						success:function(data){
							if(data.flag == true){
								$(".comSite226c").show();
								setTimeout(function(){ 
									//隐藏提示弹层
									$(".comSite226c").hide();
									//清空文字说明
									$("#"+type+"infoa").html("<em class='comSt205"+className+"'>&nbsp;</em>");
									//转换点击按钮
									var html = "<span id='"+type+"infob' class='comSt206' onclick=bound('"+type+"')>绑定<em class='comSt206"+className+"'>"+name+"</em></span>";
									$("#"+type+"infob").replaceWith(html);
							    }, 2000);
							}else{
								tips(data.msg);
							}
						}
					});
				}
			}else{
				tips(info.msg);
			}
		}
	});
}