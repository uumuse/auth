//document.domain = "auth.kuke.com";
$(function(){
	// 点击关闭取消选项框
	$(".comTcSt01,.comTcSt07").on("click",function(){
		$(".comTc").hide();
		$(".comTcBg").hide();
	});
})
//--------- --------------创建夹子 start------------------------------------
//创建夹子
//显示
function showCreate(type){
//	debugger;
	//显示弹层
	$(".comTcBg").show();
	if(type == "2"){
		$("#catalCreatediv").show();
	}else{
		$("#trackCreatediv").show();
	}
}
//创建
function doCreate(type,flag){
//	debugger;
	var name = "";
	var typeName = "";
	if(type == "1"){
		name = $("#trackfoldername").val();
		typeName = "单曲夹";
	}else if(type == "2"){
		name = $("#catalfoldername").val();
		typeName = "唱片夹";
	}
	name = $.trim(name);
	if(name.length < 2 || name.length > 20){
		tips(typeName+"名称应为2-20个字符");
		return;
	}
	var premusicfolder_id = "";
	var source_id = "";
	if(flag == "S"){//从添加到唱片夹过来的
		premusicfolder_id = addid;
		source_id = sourceAddID;
	}
	$.ajax({
		url:"/kuke/userCenter/createFavoritesFolder",
		type:"post",
		dataType:"json",
		async:false,
		data:{type:type,folder_name:name,premusicfolder_id:premusicfolder_id,source_id:source_id},
		success:function(data){
			if(data.flag == true){
//				debugger;
				if(flag == "S"){//从添加到唱片夹过来的
					$(".comTcBg").hide();
					$(".comTc04,.comTc01").hide();
					$(".comNotice04").show();
					setTimeout(function(){
						//隐藏提示框
						$(".comNotice04").hide();
						if(source_id == ""){
							//刷新
							window.location.href = '/kuke/userCenter/getUserFolder?foldertype='+type;
						}
					},2000);
				}else{
					window.location.href = '/kuke/userCenter/getUserFolder?foldertype='+type;
				}
			}else{
				if(data.code == "HASNAME"){
					tips(typeName+"名称已经存在");
				}else{
					tips("创建失败,请刷新后重试");
				}
			}
		}
	});
}
//--------- --------------创建夹子 end------------------------------------
//--------- --------------删除夹子 start---------------------------------
//删除夹子
var delId = "";
//显示
function showDel(type,Id){
	//显示弹层
	$(".comTcBg").show();
	if(type == "2"){
		$("#cataldeltip").show();
	}else{
		$("#trackdeltip").show();
	}
	delId = Id;
}
//删除
function doDel(type){
	$.ajax({
		url:"/kuke/userCenter/delFavoritesFolder",
		type:"post",
		dataType:"json",
		async:false,
		data:{musicfolder_id:delId},
		success:function(data){
//			debugger;
			if(data.flag == true){
				$(".comTc").hide();
				$(".comTcBg").hide();
				$(".comNotice05").show();
				setTimeout(function(){
					//隐藏提示框
					$(".comNotice05").hide();
					//刷新
					window.location.href = '/kuke/userCenter/getUserFolder?foldertype='+type;
				},1000);
			}else{
				if(data.code == "HASNAME"){
					tips(typeName+"名称已经存在");
				}else{
					tips("删除失败,请刷新后重试");
				}
			}
		}
	});
}
//--------- --------------删除夹子 end---------------------------------

//--------- --------------上传夹子 start---------------------------------
//显示上传弹层
function showUpload(){
	$(".comTcBg").show();
	$(".comTc03").show();
}
//上传
function uploadImg(){
	 $.ajaxFileUpload({
		  url:'/kuke/userCenter/folderImgUpload',
		  type:'post',
		  secureuri:false,
		  fileElementId:'uploadPhoto',
		  dataType:'text',
//		  dataType : "jsonp",
//		  jsonp : 'jsoncallback',
		  async:false,
		  success:function(info,textStatus){
//			  alert(textStatus);
			  var va = getCookie("ikuke_userFolder");
			  $("#tempFolderImage").attr("src",va);
		  },
		  error : function(data, status, e){//服务器响应失败处理函数
//			  alert(e);
		  }
	  });
}
//保存
function saveImg(musicfolder_id){
	 $.ajax({
		  url:'/kuke/userCenter/saveFolderImg?musicfolder_id='+musicfolder_id,
		  type:'post',
		  dataType:'json',
		  async:false,
		  success:function(info){
//			  debugger;
			  if(info.flag == false){
				  tips(info.msg);
			  }else{
				  $("#folderImg").attr("src",info.data.url);
				  $(".comTcSt01").click();
				  $("#tempFolderImage").attr("src","");
			  }
		  }
	  });
}
//--------- --------------上传夹子 end---------------------------------

//删除夹子下的资源
function delSource(type,sourceid){
	var typename = "";
	var url = "";
	var id = $("#folderid").val();
	if(type == "2"){
		typename = "唱片";
		url = "/kuke/userCenter/getCatalsOfFolder?musicfolder_id="+id;
	}else{
		typename = "单曲";
		url = "/kuke/userCenter/getTracksOfFolder?musicfolder_id="+id;
	}
	confirms("确定要删除该"+typename+"吗?","doDelSource('"+id+"','"+sourceid+"','"+url+"')");
}
//执行删除资源
function doDelSource(id,sourceid,url){
	$.ajax({
		url:"/kuke/userCenter/delFavoritesSourceOfFolder",
		type:"post",
		dataType:"json",
		async:false,
		data:{source_id:sourceid,musicfolder_id:id},
		success:function(data){
			if(data.flag == true){
				tips("删除成功");
				//刷新
				window.location.href = url;
			}else{
				tips("删除失败,请刷新后重试");
			}
		}
	});
}
//添加喜欢: 1专辑  2单曲  3视频  4专题
function addFavourite(type,source_id,obj){
	$.ajax({
		url:"/kuke/userCenter/addFavourite",
		type:"post",
		dataType:"json",
		async:false,
		data:{type:type,ids:source_id},
		success:function(info){
			if(info.flag == true || info.code == "HASADDED"){
//				alert("收藏成功");
				$(obj).toggleClass("comOn30");
				//改变单击事件
				$(obj).attr("onclick","cancleFavourite('"+type+"','"+source_id+"',this)")
			}else{
				tips("收藏失败");
			}
		}
	});
}
//取消喜欢
function cancleFavourite(type,source_id,obj){
	doCancleFavourite(type,source_id,obj);
}
//执行取消喜欢
function doCancleFavourite(type,source_id,obj){
	$.ajax({
		url:"/kuke/userCenter/cancleFavourite",
		type:"post",
		dataType:"json",
		async:false,
		data:{source_id:source_id,type:type},
		success:function(info){
			if(info.flag == true){
//				alert("取消收藏成功");
				$(obj).toggleClass("comOn30");
				//改变单击事件
				$(obj).attr("onclick","addFavourite('"+type+"','"+source_id+"',this)")
			}else{
				tips(info.msg);
			}
		}
	});
}
//--------- --------------添加到唱片夹 start------------------------------------
var addid = "";
//显示
function addToFolder(type,id,count){
	var typeName = "";
	if(type == "1"){
		typeName = "单曲夹";
	}else if(type == "2"){
		typeName = "唱片夹";
	}
	if(count <= 0){
		tips("该"+typeName+"下没有资源信息");
		return;
	}else{//判断资源是否已下架
		var flag = false;
		$.ajax({
			url:"/kuke/userCenter/checkOffSource",
			type:"post",
			dataType:"json",
			async:false,
			data:{type:type,musicfolder_id:id},
			success:function(info){
				if(info.flag == true){
					tips("该夹子下没有资源或资源已下架");
					flag = true;
				}
			}
		});
		if(flag){
			return;
		}
	}
	//显示弹层
	$(".comTcBg").show();
	if(type == "2"){
		$("#addtocatalFolder").show();
	}else{
		$("#addtotrackFolder").show();
	}
	addid = id;
	$.ajax({
		url:"/kuke/userCenter/queryMyFolders",
		type:"post",
		dataType:"json",
		async:false,
		data:{type:type},
		success:function(info){
			if(info.flag == true){
				var html = "";
				var o = eval("("+info.data+")");
				for(var i = 0; i < o.length; i++){
					if(id != o[i].id){
						html+="<ol class='cf'>" +
						"<li><span class='comTcSite12 pr'><em class='comTcSt12 comTcSt12a'>"+o[i].foldername+"</em><input onclick=chooseFolderID(this) type='checkbox' name='toAddID' class='comTcSt13 comTcSt13a' value='"+o[i].id+"'></span></li>" +
						"</ol>";
					}
				}
				if(type == "2"){
					$("#addtocatalFolder").find(".comTcSt06").attr("href","javascript:savetoFolder('2')");
					$("#addtocatalFolder").find(".comTcSite11").html(html);
				}else{
					$("#addtotrackFolder").find(".comTcSt06").attr("href","javascript:savetoFolder('"+type+"')");
					$("#addtotrackFolder").find(".comTcSite11").html(html);
				}
			}
		}
	});
} 
//点击选中要添加的夹子
function chooseFolderID(obj){
	$(".comTcSt12a").removeClass("comOn33");
	$(".comTcSite11").find("input[name=toAddID]").each(function(){
		this.checked = false;
	});
	
	$(obj).parent().find(".comTcSt12a").toggleClass("comOn33");
	obj.checked = true;
	
//	sourceAddID = $(obj).val();
}
//点击确定添加到现有的夹子中
function savetoFolder(type){
	var typeName = "";
	var folderID = "";
	if(type == "2"){//唱片下选中的
		folderID = $("#addtocatalFolder").find("input[name=toAddID]:checked").val();
		typeName = "唱片夹";
	}else{
		folderID = $("#addtotrackFolder").find("input[name=toAddID]:checked").val();
		typeName = "单曲夹";
	}
	if(folderID == ""){
		tips("您为选中任何"+typeName);
		return;
	}
	$.ajax({
		url:"/kuke/userCenter/addFolderSourceToFolder",
		type:"post",
		dataType:"json",
		async:false,
		data:{musicfolder_id:folderID,premusicfolder_id:addid,type:type},
		success:function(data){
			if(data.flag == true){
				$(".comTc04").hide();
				$(".comNotice04").show();
				setTimeout(function(){
					//隐藏提示框
					$(".comNotice04").hide();
					//刷新
					window.location.href = '/kuke/userCenter/getUserFolder?foldertype='+type;
				},1000);
			}else{
				tips(data.msg);
			}
		}
	});
}
//添加到夹子中的创建夹子
function addToFolderCreate(type){
//	debugger;
	//隐藏添加到唱片夹
	$(".comTc04").hide();
	//赋值点击事件
	if(type == "2"){
		$("#createcatal").attr("onclick","doCreate('2','S');");;
	}else{
		$("#createtrack").attr("onclick","doCreate('1','S');");;
	}
	//隐藏所有选中
	$(".comTcSt12a").removeClass("comOn33");
	$(".comTcSite11").find("input[name=toAddID]").attr("checked",false);
	//显示创建夹子
	showCreate(type);
}
//资源添加到夹子 显示
var sourceAddID = "";
function addSourceToFolder(type,sid,folderid){
	//显示弹层
	$(".comTcBg").show();
	if(type == "2"){
		$("#addtocatalFolder").show();
	}else{
		$("#addtotrackFolder").show();
	}
	sourceAddID = sid;
	$.ajax({
		url:"/kuke/userCenter/queryMyFolders",
		type:"post",
		dataType:"json",
		async:false,
		data:{type:type},
		success:function(info){
			if(info.flag == true){
				var html = "";
				var o = eval("("+info.data+")");
				for(var i = 0; i < o.length; i++){
					if(folderid != o[i].id){
						html+="<ol class='cf'>" +
						"<li><span class='comTcSite12 pr'><em class='comTcSt12 comTcSt12a'>"+o[i].foldername+"</em><input onclick=chooseFolderID(this) type='checkbox' name='toAddID' class='comTcSt13 comTcSt13a' value='"+o[i].id+"'></span></li>" +
						"</ol>";
					}
				}
				if(type == "2"){
					$("#addtocatalFolder").find(".comTcSt06").attr("onclick","doAddSourceToFolder('2')");
					$("#addtocatalFolder").find(".comTcSite11").html(html);
				}else{
					$("#addtotrackFolder").find(".comTcSt06").attr("onclick","doAddSourceToFolder('"+type+"')");
					$("#addtotrackFolder").find(".comTcSite11").html(html);
				}
			}
		}
	});
}
//资源添加到夹子 执行
function doAddSourceToFolder(type){
//	debugger;
	var typeName = "";
	var toFolderID = "";
	if(type == "2"){//唱片下选中的
		toFolderID = $("#addtocatalFolder").find("input[name=toAddID]:checked").val();
		typeName = "唱片夹";
	}else{
		toFolderID = $("#addtotrackFolder").find("input[name=toAddID]:checked").val();
		typeName = "单曲夹";
	}
	if(toFolderID == ""){
		tips("您为选中任何"+typeName);
		return;
	}
	$.ajax({
		url:"/kuke/userCenter/addSourceToFolder",
		type:"post",
		dataType:"json",
		async:false,
		data:{source_id:sourceAddID,musicfolder_id:toFolderID},
		success:function(data){
			console.log(data);
			if(data.flag == true){
				$(".comTc04").hide();
				$(".comNotice04").show();
				setTimeout(function(){
					//隐藏提示框
					$(".comNotice04").hide();
					$(".comTcBg").hide();
				},1000);
			}else{
				tips("添加失败,请刷新后重试");
			}
		}
	});
}

//--------- --------------添加到唱片夹 end------------------------------------
//详细信息
function toDetail(type,id){
	if(type == "2"){
		window.location.href = '/kuke/userCenter/getCatalsOfFolder?musicfolder_id='+id;
	}else{
		window.location.href = '/kuke/userCenter/getTracksOfFolder?musicfolder_id='+id;
	}
}
//批量播放播放按钮
function toDlPlay(type,musicfolder_id,playtype){
	$.ajax({
		url:"/kuke/userCenter/getFolderSource",
		type:"post",
		data:{type:type,musicfolder_id:musicfolder_id},
		dataType:"json",
		async:false,
		success:function(info){
			if(info.flag){
				console.log("data:"+info.data);
				if(info.data != null && info.data != ""){
					if(type == "1"){//单曲
						if("play" == playtype){//播放
							preTrackPlay(info.data);
						}else if("add" == playtype){//添加
							preTrackPlayAdd(info.data);
						}
					}else if(type == "2"){//专辑
						if("play" == playtype){//播放
							preCatlogPlay(info.data);
						}else if("add" == playtype){//添加
							preCatlogPlayAdd(info.data);
						}
					}
				}else{
					tips("该夹子下没有资源或资源已下架");
				}
			}else{
				console.log(info.msg);
			}
		}
	});
}
//显示分享
var picurl = "";
var title = "";
var l_code = "";
function toShare(url,t,code){
	$(".comTcBg").show();
	$("#shareTips").show();
	picurl = url;
	title = t;
	l_code = code;
}
//微信分享
function WXShare(){
	share("wechat",picurl,title,l_code);
}
//QQ分享
function QQShare(){
	share("qq",picurl,title,l_code);
}
//微博分享
function WEIBOShare(){
	share("sina",picurl,title,l_code);
}
//QQ空间分享
function QZoneShare(){
	share("qzone",picurl,title,l_code);
}
function share(url,type,id){
	
}
//返回
function goBack(){
	var nexturl = $("#nexturl").val();
	window.location.href = nexturl;
}
//修改夹子名称:input的id
function changeFolderName(inputid,spanid){
	//可编辑
	$("#"+inputid).attr("disabled",false);
	//现在的名字
	var foldername = $.trim($("#"+inputid).val());
	//显示保存
	var html = "<span class='comSt403' id='"+spanid+"' onclick=saveFloderName('"+inputid+"','"+spanid+"','"+foldername+"')>保存</span>";
	$("#"+spanid).replaceWith(html);
}
//保存夹子名称:inputid,spanid
function saveFloderName(inputid,spanid,name){
	var foldername = $.trim($("#"+inputid).val());
	var folderid = $.trim($("#folderid").val());
	var flag = false;
	if(foldername == name){//没改变
		//不可编辑
		$("#"+inputid).attr("disabled",true);
		//显示编辑
		var html = "<span title='编辑' id='"+spanid+"' class='comSt262 comSt262b' onclick=changeFolderName('"+inputid+"','"+spanid+"')></span>";
		$("#"+spanid).replaceWith(html);
		return;
	}
	if(foldername != null && foldername != ""){//值和之前的不一样
		$.ajax({
			url:"/kuke/userCenter/editFavFolderName",
			type:"post",
			dataType:"json",
			async:false,
			data:{musicfolder_id:folderid,folder_name:foldername},
			success:function(data){
				if(data.flag == true){
					//不可编辑
					$("#"+inputid).attr("disabled",true);
					//显示编辑
					var html = "<span title='编辑' id='"+spanid+"' class='comSt262 comSt262b' onclick=changeFolderName('"+inputid+"','"+spanid+"')></span>";
					$("#"+spanid).replaceWith(html);
				}else{
					tips(data.msg);
				}
			}
		});
	}else{
		tips("请输入单曲夹名称");
	}
}
//删除夹提示
function delFolder(){
	var html = "";
	var foldertype = $("#foldertype").val();
	if(foldertype == '2'){
		html = "<p>确定要删除该唱片夹?</p>";
	}else{
		html = "<p>确定要删除该单曲夹?</p>";
	}
	$(".comTcSite06").html(html);
	$(".comTcBg").show();
	$(".comTc02").show();
}
//删除夹ajax
function toDelFolder(){
	var folderid = $.trim($("#folderid").val());
	$.ajax({
		url:"/kuke/userCenter/delFavoritesFolder",
		type:"post",
		dataType:"json",
		async:false,
		data:{musicfolder_id:folderid},
		success:function(info){
			if(info.flag == true){
				$(".comTc02").hide();
				$(".comSite226e").show();
				setTimeout(function(){
					//隐藏提示框
					$(".comSite226e").hide();
					//跳转到上一页面
					window.location.href = $("#nexturl").val();
				},2000);
			}else{
				tips(info.msg);
			}
		}
	});
}
//关闭提示框
function cancleTip(){
	$(".comTcBg").hide();
	$(".comTc02").hide();
}
//提示框函数,2秒后隐藏,除隐藏外,不做任何操作
function tipsUtil(id){
	$(id).show();
	setTimeout(function(){
		//隐藏提示框
		$(id).hide();
		//跳转到登录
	},2000);
}