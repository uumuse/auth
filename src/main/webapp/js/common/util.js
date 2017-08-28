//去首页
function goToIndex(){
	window.location.href = wwwHeadUrl;
}

//url跳转
function url_href(url){
	if(url.indexOf("?") >= 0){
		parent.ifream_href(encodeURI(url)+"&random=" + Math.random());
	}else{
		parent.ifream_href(encodeURI(url)+"?random=" + Math.random());
	}
}
//退出
function logout(callback){
	$.ajax({
		url:"/kuke/login/out",
		type:"post",
		dataType:"json",
		success:function(data){
			if(data.flag == true){
				if(callback){
					callback();
				}else{
					window.location.href = wwwHeadUrl;
				}
			}else{
				tips("退出失败,请刷新后重试");
			}
		}
	});
} 
//下载
function downloadNow(url){
	$("#download_a").attr('href',url);
	$("#download_a").attr('disable',false);
	document.getElementById("download_a").click(); //点击a
	$("#download_a").attr('disable',true);
	$(".comTcBg,.comTc02").hide();//关闭弹层
}
//下载权限
function download(type,sourceid,itemcode,name,from){//if(type == "1" || type == "2" || type == "4" || type == "3" || type == "5"){//单曲 || 专辑 || 乐谱 || 有声读物单曲 || 有声读物
//	debugger;
	if(type == "1" || type == "2" || type == "4" || type == "3" || type == "5"){//单曲 || 专辑 || 乐谱  || 有声读物单曲  || 有声读物
		var dataTemp = "";
		var priceid = "";
		if(type == "1"){//单曲
			priceid = "511";
			dataTemp = "lcode="+sourceid+"&itemcode="+itemcode+"&type=1&pay_pro_price_id="+priceid;
		}else if(type == "4"){//乐谱
			priceid = "509";
			dataTemp = "itemcode="+sourceid+"&type=4&pay_pro_price_id="+priceid;
		}else if(type == "3"){//有声读物单曲
			priceid = "513";
			dataTemp = "itemcode="+sourceid+"&type=1&pay_pro_price_id="+priceid;
		}else if(type == "2"){//专辑
			priceid = "510";
			dataTemp = "itemcode="+sourceid+"&type=1&pay_pro_price_id="+priceid;
		}else if(type == "5"){//有声读物
			priceid = "512";
			dataTemp = "itemcode="+sourceid+"&type=1&pay_pro_price_id="+priceid;
		}
		if(dataTemp != ""){
			$.ajax({
				url:"/kuke/play/getAuthDownloadUrl",
				type:"post",
				data:dataTemp,
				async:false,
				dataType:"json",
				success:function(info){
//					debugger;
					console.log(info);
					if(info.code == "ORG_ALREADY_PAID"){
						if(type == '2' || type == '5'){
							window.location.href=wwwHeadUrl+'/album/'+sourceid;
						}else{
							//机构订单已存在
							downloadNow(info.data);
						}
					}else if(info.code == "CLIENT_ALREADY_PAID" || info.code == "1"){
						if(type == '2' || type == '5'){
							window.location.href=wwwHeadUrl+'/album/'+sourceid;
						}else{
							//个人订单已存在
							downloadNow(info.data);
						}
					}else if(info.code == 10){//机构IP购买
						orgPay(type, sourceid, name, itemcode);
					}else if(info.code == 11){//机构开放余额购买
						orgPay(type, sourceid, name, itemcode);
					}else if(info.code == 12){//机构指定账号余额购买
						orgPay(type, sourceid, name, itemcode);
					}else if(info.code == 13){//个人权限下载
						personalPay(type, sourceid, name, itemcode,priceid);
					}else if(info.code == 'NOMALLOGIN'){//未登录
						showLogin();
					}else if(info.code == 4 || info.data.code == 8){//未开通会员或会员已过期
						$(".comTcBg").show();
						$("#toVIPTip").show();
					}
				}
			});
		}
	}
}
//走机构支付
function orgPay(type,sourceid,name,itemcode){
	var temp = getDownMoneyAndInfo(type,sourceid);
	var content = "";
	if(type == "1"){
		content = getFormatName(name)+"<span class=\"comSt302\">"+getTrackKbps(sourceid)+"</span>";
	}else{
		content = getFormatName(name);
	}
	$("#orgPayDownImg").attr("src",getItemCodeUrl(itemcode));
	$("#orgPayDownName").html(content);
	$("#orgPaydownprice").html(temp.price+"元");
	$("#orgDownHref").attr("onclick","orgPayDownload('"+type+"','"+sourceid+"','"+name+"')");
	$(".comTcBg").show();
	$("#orgPay").show();
}
//走个人支付
function personalPay(type,sourceid,name,itemcode,priceid){
	var content = "";
	if(type == "1"){
		content = getFormatName(name)+"<span class=\"comSt302\">"+getTrackKbps(sourceid)+"</span>";
	}else{
		content = getFormatName(name);
	}
	var temp = getDownMoneyAndInfo(type,sourceid);
	$("#downimg").attr("src",getItemCodeUrl(itemcode));
	$("#downname").html(content);
	$("#downprice").html(temp.price+"元");
	$("#downhref").attr("href","/kuke/payment/download?name="+name+"&id="+sourceid+"&priceid="+priceid);
	$(".comTcBg").show();
	$("#pay").show();
}
//用户所在的机构下载
function orgPayDownload(type,id,name){
	var priceid = "";
	if(type == "1"){//单曲
		priceid = "511";
	}else if(type == "2"){//专辑
		priceid = "510";
	}else if(type == "4"){//乐谱
		priceid = "509";
	}else{
		tips("下载类型错误");
		return;
	}
	$.ajax({
		url:"/kuke/payment/payByOrgMoney",
		type:"post",
		data:{id:id,name:name,priceid:priceid},
		async:false,
		dataType:"json",
		success:function(info){
			if(info.flag == true){//可以下载,返回地址
				downloadNow(info.data);
			}else{
				tips(info.msg);
			}
		}
	});
}
//得到下载所需要的钱
function getDownMoneyAndInfo(type,item_id,pay_pro_price_id){
//	debugger;
	var result = new Object();//返回值
	$.ajax({
		url:"/kuke/payment/getDowndloadMoney",
		type:"post",
		data:{type:type,item_id:item_id,pay_pro_price_id:pay_pro_price_id},
		async:false,
		dataType:"json",
		success:function(info){
//			debugger;
			if(info.flag == true){//可以下载,返回地址
				result.flag = true;
				result.price = info.data.cost_price;
			}else{
				result.flag = false;
				result.price = "0.00";
			}
		}
	});
	return result;
}
//播放
function playAuth(type,sourceid){
//	debugger;
	var sourceurl = new Object();//返回值
	sourceurl.flag = false;
	sourceurl.url = "";
	sourceurl.code = "";
	if(type == "1" || type == "2" || type == "3" ||　type == "5"){//单曲 || 视频 || 直播 || 专辑
		var dataTemp = "";
		if(type == "5"){
			dataTemp = "itemcode="+sourceid+"&type=1";
		}else if(type == "1"){
			dataTemp = "lcode="+sourceid+"&type=1";
		}else if(type == "2"){
			dataTemp = "source_id="+sourceid+"&type="+type;
		}else if(type == "3"){
			dataTemp = "source_id="+sourceid+"&type="+type;
		}
		$.ajax({
			url:"/kuke/play/getAuthPlayUrl",
			type:"post",
			data:dataTemp,
			async:false,
			dataType:"json",
			success:function(info){
//				debugger;
				if(info.flag == true){//可以播放,返回地址
//					alert("可以播放");
					sourceurl.flag = true;
					sourceurl.url = info.data.url;
				}
			}
		});
	}
	return sourceurl;
}
//提示
var offMessage = "因版权方要求，此资源暂不可使用。";//提示消息
function tips(msg){
	if(msg == "showOffMessage"){
		msg = offMessage;
	}
	$("#tipsMsg").html(msg);
	$("#tips").show();
	setTimeout(function(){
		//隐藏提示框
		$("#tips").hide();
		$("#tipsMsg").html("");
	},2000);
}
//提示
function tipTarget(targetClass){
	$(targetClass).show();
	setTimeout(function(){
		//隐藏提示框
		$(targetClass).hide();
	},2000);
}
function confirms(title,clicks){
	//赋值
	$("#confirmdivtitle").html("<p>"+title+"</p>");
	$("#confirmdivclick").attr("href","javascript:"+clicks+";");//IE6 7兼容
	//显示
	$(".comTcBg").show();
	$("#confirmdiv").show();
}

//分享方法
//type:sina  qzone  qq  wechat
function share(type,picurl,title,l_code){
//	debugger;
	var shareFlag = false;
	if(l_code == ""){
		return;
	}else{
		//查询分享剩余几次
		$.ajax({
			url:"/kuke/share/getCurMonthShareTimes",
			type:"post",
			async:false,
			dataType:"json",
			success:function(info){
				if(info.flag == true){
					if(info.data.times <= 0){
						tips("您本月分享次数已用完");
					}else{
						shareFlag = true;
					}
				}else{
					tips("您本月分享次数已用完");
				}
			}
		});
	}
	if(shareFlag){//分享次数
		var shareurl = "http://auth.kuke.com/kuke/user/share?l_code="+l_code+"&c="+new Date().getTime();
		var param1 = "?url="+encodeURIComponent(shareurl)+"&title="+encodeURIComponent(title)+"&pic="+encodeURIComponent(picurl);
		var param2 = "?url="+encodeURIComponent(shareurl)+"&title="+encodeURIComponent(title)+"&pics="+encodeURIComponent(picurl);
		if(type == "sina"){
			url = "http://v.t.sina.com.cn/share/share.php"+param1;
			window.open(url);
		}else if(type == "qzone"){
			url = "http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey"+param2;
			window.open(url);
		}else if(type == "qq"){
			url = "http://connect.qq.com/widget/shareqq/index.html"+param2;
			window.open(url);
		}else if(type == "wechat"){
			//shareurl = "http://auth.kuke.com/kuke/user/appshare?l_code=0181dx_01&c=1482292908720";
			$("#shareTips").hide();
			$("#qrcodeshare").show();
			var ua = navigator.userAgent.toLowerCase();
			 if (ua.indexOf('ie') != -1) {
	             $("#divweixinimg").qrcode({
	                 render: "table",
	                 width: 200,
	                 height: 200,
	                 text: shareurl
	             });
	         }else {
	             jQuery('#divweixinimg').qrcode({
	                 width: 200,
	                 height: 200,
	                 text: shareurl
	             });
	         }
		}
		
	}
	//更新一次，最多5次
	$.ajax({
		url:"/kuke/share/getCurMonthShareTimes",
		type:"post",
		async:false,
		data:{l_code:l_code},
		dataType:"json",
		success:function(info){
		}
	});
}
//专辑图片
function getItemCodeUrl(itemcode){
	var imgurl = "http://image.kuke.com/images/audio/cata200/"+itemcode.substring(0,1)+"/"+itemcode+".jpg";
	return imgurl;
}
//得到单曲的品质
function getTrackKbps(lcode){
	if(lcode != null && lcode != ""){
		var code = "";
		$.ajax({
			url:"/kuke/user/getTrackKbps",
			type:"post",
			async:false,
			data:{lcode:lcode},
			dataType:"json",
			success:function(info){
				console.log(info);
				code = info.data;
			}
		});
		return code+"kbps";
	}else{
		return "192kbps";
	}
}
//得到优化的名字，截取76个
function getFormatName(name){
	if(name != null && name != ""){
		if(name.length > 76){
			name = name.substring(0,76);
		}
	}
	return name;
}
//跳转到专辑页面
function openCatalog(itemcode){
	window.open(wwwHeadUrl + '/album/'+itemcode);
}
//跳转到厂牌页面
function openLabel(labelid){
	window.open(wwwHeadUrl + '/label/'+labelid+"_0_1_1");
}
//跳转到艺术家页面
function openArtist(musicianId,musicianType){
	window.open(wwwHeadUrl + '/artist/'+musicianId+'_'+musicianType+'_0_1');
}

//添加音频
function preTrackPlayAdd(source){
	addPlayList(source,wwwHeadUrl);
}
//添加专辑
function preCatlogPlayAdd(source){
	addCataToPlay(source,wwwHeadUrl);
}
//播放音频
function preTrackPlay(source){
	mplayTrack(source,'1',wwwHeadUrl);
}
//播放专辑
function preCatlogPlay(source){
	mplayCata(source,'1',wwwHeadUrl);
}