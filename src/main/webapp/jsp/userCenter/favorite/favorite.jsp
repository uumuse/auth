<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<%@include file="/jsp/common/CommonMeta.jsp"%>
<title>我的喜欢</title>
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta name="renderer" content="webkit">
<meta name="keywords" content="">
<meta name="description" content="">
<link href="/css/init.css" rel="stylesheet">
<link href="/css/main.css" rel="stylesheet">
<script src="/js/jquery-1.12.4.min.js"></script>
<script src="/js/common/util.js"></script> 
<script src="/js/jquery.qrcode.min.js"></script> 
</head>
<body>
<%@ include file="/jsp/userCenter/userLoginHead.jsp" %>
<%@ include file="/jsp/userCenter/userInfoHead.jsp"%>
<div class="comSite240 comSite240c1">
	<div class="comSite241 comWd02">
		<div class="comSite242 comWd01">
			<div class="comSite243 cf">
<!-- 				<a href="javascript:void(0)" class="comSite243a1">最近播放</a> -->
				<a href="/kuke/userCenter/getFavoriteTrack" class="comSite243a2 comSite243a2-on">我的喜欢</a>
				<a href="/kuke/userCenter/getSubscribeLabel" class="comSite243a3">我的订阅</a>
				<a href="/kuke/userCenter/getUserFolder" class="comSite243a4">我的唱片夹</a>
			</div>
			<div class="comSite244 comSite244a cf">
				<a href="getFavoriteTrack" class="comOn23">单曲</a>
				<a href="getFavoriteCatal">唱片</a>
				<a href="getFavoriteVedio">视频</a>
			</div>
			<div class="comSite245">
				<div class="comSite246 comSite246a"><!-- 单曲切换 -->
					<p class="comSite247">共 <span>${tracksize}</span> 首</p>
					<div class="comSite248 cf">
						<span class="comSite249 pr fl">
							<em class="comSt218 comSt218a">全选</em>
							<input type="checkbox" class="comSt219 comSt219a">
						</span>
						<span id="addToPlayList" class="comSt220 comSt220a fl" onclick ="addToPlayList('2')">添加到播放列表</span>
						<span id="addToFolder" class="comSt221 comSt221a fl" onclick="addToFolder('2','')">添加到唱片夹</span>
						<span id="cancelFavor" class="comSt222 comSt222a fl" onclick="cancelFavor('2')">取消喜欢</span>
					</div>
					<div class="comSite250">
						<input type = "hidden" id="vodka" value="t"/>
						<c:if test="${tracksize != 0}">
							<c:forEach items="${favoriteTrackList}" var="track" varStatus="i">
								<c:if test="${track.showable == 0}"><!-- 下架样式 -->
									<div class="comSite251 cf cOff01"><!-- foreach -->
										<span class="comSite249 pr fl">
											<em class="comSt218 comSt218b" style="background:none;"></em>
										</span>
										<span class="comSt223 comSt223a fl" onclick="cancelShow('2','${track.lcode }')"></span>
										<span class="comSt224 fl" style="width: 20px;">${i.index+1 }</span>
										<span class="comSt225 fl eps" onclick="tips('showOffMessage');">
											${track.title==null?"":track.title }${track.trackDesc==null?"":track.trackDesc}
										</span>
										<span class="comSt226 fl pr">
											<em>${track.timing }</em>
											<span class="isOff isOff01">已下架</span>
										</span>
										<a href="javascript:void(0)" class="comSt232 comSt232a fl eps" onclick="tips('showOffMessage');">${track.ctitle==null?"":track.ctitle}</a>
									</div>
								</c:if>
								<c:if test="${track.showable != 0}"><!-- 未下架样式 -->
									<div class="comSite251 cf comSite251a">
										<span class="comSite249 pr fl">
											<em class="comSt218 comSt218b"></em>
											<input type="checkbox" class="comSt219 comSt219b" name="trackbox" value="${track.lcode }|${track.itemcode }">
										</span>
										<span class="comSt223 comSt223a fl" onclick="cancelShow('2','${track.lcode }')"></span>
										<span class="comSt224 fl" style="width: 20px;">${i.index+1 }</span>
										<span class="comSt225 fl eps" onclick="preTrackPlay('${track.lcode}')">
											${track.title==null?"":track.title }${track.trackDesc==null?"":track.trackDesc}
										</span>
										<span class="comSt226 fl pr">
											<em>${track.timing }</em>
											<span class="comSt227 comSt227a cf">
												<em class="comSt228 comSt228a" onclick="preTrackPlayAdd('${track.lcode}')"></em>
												<em class="comSt229 comSt229a" onclick="addToFolder('2','')"></em>
												<em class="comSt230 comSt230a" onclick="shareTrack('${track.lcode }','${track.itemcode }','${fn:replace(track.title==null?"":track.trackDesc, "'", "")}${fn:replace(track.trackDesc==null?"":track.trackDesc, "'", "")}')"></em>
												<em class="comSt231 comSt231a" onclick="download('1','${track.lcode }','${track.itemcode }','${fn:replace(track.title==null?"":track.trackDesc, "'", "")}${fn:replace(track.trackDesc==null?"":track.trackDesc, "'", "")}','')"></em>
											</span>
										</span>
										<a href="javascript:void(0)" onclick="preTrackPlay('${track.lcode}')" class="comSt232 comSt232a fl eps">${track.ctitle==null?"":track.ctitle}</a>
									</div>
								</c:if>
							</c:forEach>
						</c:if>
						<c:if test="${tracksize == 0}">
							<p class="noInfo">—— 暂无内容 ——</p>
						</c:if>
					</div>
				</div>
				<div class="comSite246 comSite246a" style="display:none;"><!-- 专辑切换 -->
					<p class="comSite247">共 <span>${catasize }</span> 首</p>
					<div class="comSite248 cf">
						<span class="comSite249 pr fl">
							<em class="comSt218 comSt218c">全选</em>
							<input type="checkbox" class="comSt219 comSt219c">
						</span>
						<span class="comSt220 comSt220b fl" onclick="addToPlayList('1')">添加到播放列表</span>
						<span class="comSt221 comSt221b fl" onclick="addToFolder('1','')">添加到唱片夹</span>
						<span class="comSt222 comSt222b fl" onclick = "cancelFavor('1')">取消喜欢</span>
					</div>
					<div class="comSite250 comSite250a">
					<input type = "hidden" id="vodka" value="c"/>
						<c:if test="${catasize != 0}">
							<c:forEach items="${favoriteCatalList}" var="cata" varStatus="s">
								<c:if test="${cata.showable == 0}"><!-- 下架样式 -->
									<c:if test="${(s.index)%4==0 }">
										<ol class="comSite257 comSite257a cf">
									</c:if>
									<li class="comSite258 comSite258a cOff02">
										<div class="comSite259 pr">
											<div><img onerror="javascript:this.src='/images/default_zhuanji_s.jpg';" src="${cata.imgUrl }" alt="" width="210" height="210"></div>
											<span class="offBg01" onclick="tips('showOffMessage');"></span>
											<span class="isOff isOff02">已下架</span>
											<span class="comSt249 comSt249a" onclick ="cancelShow('1','${cata.itemcode}')"></span>
										</div>
										<div class="comSite261 pr" onclick="tips('showOffMessage');">
											<p>
												<c:choose>
													<c:when test="${not empty cata.ctitle}">
														<c:if test="${not empty cata.ctitle}">
														 	${fn:substring(cata.ctitle, 0, 27)}...
														</c:if>
													</c:when>
													<c:otherwise>
														<c:if test="${not empty cata.title}">
														 	${fn:substring(cata.title, 0, 27)}...
														</c:if>
													</c:otherwise>
												</c:choose>
											</p>
										</div>
									</li>
									<c:if test="${(s.index)%4==3 }">
										</ol>
									</c:if>
								</c:if>
								<c:if test="${cata.showable != 0}"><!-- 未下架样式 -->
									<c:if test="${(s.index)%4==0 }">
										<ol class="comSite257 comSite257a cf">
									</c:if>
									<li class="comSite258 comSite258a">
										<div class="comSite259 pr">
											<div><img onerror="javascript:this.src='/images/default_zhuanji_s.jpg';" src="${cata.imgUrl }" alt="" width="210" height="210"></div>
											<span class="comSt53 comSt53a" onclick="openCata('${cata.itemcode}')"></span>
											<span class="comSt249 comSt249a" onclick ="cancelShow('1','${cata.itemcode}')"></span>
											<span class="comSite260 comSite260b"><span class="comSite260a pr"><em class="comSt250 comSt250a"><input type="hidden" value="${cata.itemcode }"></em>
											<input type="checkbox" class="comSt251 comSt251a" name="catabox" value="${cata.itemcode }"></span></span>
											<a href="#" class="comSt252 comSt252a" onclick="openCata('${cata.itemcode}')"></a>
										</div>
										<div class="comSite261 pr">
											<p>
												<c:choose>
													<c:when test="${not empty cata.ctitle}">
														<c:if test="${not empty cata.ctitle}">
														 	${fn:substring(cata.ctitle, 0, 27)}...
														</c:if>
													</c:when>
													<c:otherwise>
														<c:if test="${not empty cata.title}">
														 	${fn:substring(cata.title, 0, 27)}...
														</c:if>
													</c:otherwise>
												</c:choose>
											</p>
											<div class="comSite262 comSite262a">
												<span class="comSt253 comSt253a" onclick="openCata('${cata.itemcode}')"></span>
												<span class="comSt254 comSt254a" onclick="addToFolder('1','')"></span>
												<span class="comSt255 comSt255a" onclick="openCata('${cata.itemcode}')"></span>
												<span class="comSt256 comSt256a" onclick="cancelFavor(1)"></span>
											</div>
										</div>
									</li>
									<c:if test="${(s.index)%4==3 }">
										</ol>
									</c:if>
								</c:if>
							</c:forEach>
						</c:if>
						<c:if test="${catasize == 0}">
							<p class="noInfo">—— 暂无内容 ——</p>
						</c:if>
					</div>
				</div>
				<!-- <div id="flux" >1111</div> -->
				<div class="comSite246 comSite246a" style="display:none;"><!-- 视频切换 -->
					<p class="comSite247">共 <span>${videosize }</span> 首</p>
					<div class="comSite248 cf">
						<span class="comSite249 pr fl">
							<em class="comSt218 comSt218d">全选</em>
							<input type="checkbox" class="comSt219 comSt219d">
						</span>
						<span class="comSt222 comSt222c fl" id ="cancelFavorVideo" onclick="cancelFavor(3)">取消喜欢</span>
					</div>
					<div class="comSite250 comSite250a">
					<input type = "hidden" id="vodka" value="v"/>
						<c:if test="${videosize != 0}">
							<c:forEach items="${favoriteVedioList}" var="video" varStatus="v">
								<c:if test="${video.isshow == 0 }"><!-- 下架样式 -->
									<c:if test="${(v.index)%3==0 }">
										<ol class="comSite257 comSite257b cf">
									</c:if>
									<li class="comSite263 comSite263a">
										<div class="comSite264 pr">
											<div><img src="${video.imgUrl }" alt="" width="280" height="160"></div>
											<span class="offBg01 offBg01a" onclick="tips('showOffMessage');"></span>
											<span class="isOff isOff02">已下架</span>
											<span class="comSt249 comSt249b" onclick="cancelShow('3','${video.source_id}')"></span>
										</div>
										<div class="comSite265 comSite265a" onclick="tips('showOffMessage');">
											<h2>${video.cataloguename }</h2>
											<p>
												${fn:substring(video.cataloguecname, 0, 15)}...
											</p>
										</div>
									</li>
									<c:if test="${(v.index)%3==2 }">
										</ol>
									</c:if>
								</c:if>
								<c:if test="${video.isshow != 0 }"><!-- 未下架样式 -->
									<c:if test="${(v.index)%3==0 }">
										<ol class="comSite257 comSite257b cf">
									</c:if>
									<li class="comSite263 comSite263a" >
										<div class="comSite264 pr">
											<div><img src="${video.imgUrl }" alt="" width="280" height="160"></div>
											<span class="comSt53 comSt53b"></span>
											<span class="comSt249 comSt249b" onclick="cancelShow('3','${video.source_id}')"></span>
											<span class="comSite260 comSite260c">
											<span class="comSite260a pr">
											<em class="comSt250 comSt250b"></em>
											<input type="checkbox" name="videobox" class="comSt251 comSt251b" value="${video.source_id }">
											</span>
											</span>
											<span class="comSt257 comSt257a" onclick="openVideo('${video.source_id }')">PLAY</span>
										</div>
										<div class="comSite265 comSite265a">
											<h2>${video.cataloguename }</h2>
											<p>
												${fn:substring(video.cataloguecname, 0, 15)}...
											</p>
										</div>
									</li>
									<c:if test="${(v.index)%3==2 }">
										</ol>
									</c:if>
								</c:if>
							</c:forEach>
						</c:if>
						<c:if test="${videosize == 0}">
							<p class="noInfo">—— 暂无内容 ——</p>
						</c:if>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<jsp:include page="/jsp/userCenter/footer.jsp"></jsp:include>
<div class="comSite226 comNotice04">
	<p class="comSt201">已添加</p>
</div>
<div id="cancelFavorDiv" class="comTc comTc02" style="display:none;">
	<input type="hidden" id="htype">
	<input type="hidden" id="hvalue">
	<div class="comTcTop pr"><h1>提示</h1><a href="javascript:void(0)" title="关闭" class="comTcSt01"></a></div>
	<div class="comTcCon">
		<div class="comTcSite06"><p>确定要取消收藏吗？</p></div>
		<div class="comTcSite05 cf comTcPd02">
			<a onclick="cancelConfirm()" href="javascript:void(0)" class="comTcSt06">确定</a>
			<a onclick="cancelOper()" href="javascript:void(0)" class="comTcSt07">取消</a>
		</div>
	</div>
</div>


<div id="shareTips" class="comTc comTc06"><!-- 分享到弹层 -->
	<div class="comSite359"><h1>分享到</h1><a href="javascript:void(0)" title="关闭" class="comTcSt01"></a></div>
	<div class="comSite360 cf">
		<a href="javascript:void(0)" onclick="WXShare();" title="微信" class="comSite360a1"></a>
		<a href="javascript:void(0)" onclick="WEIBOShare();" title="微博" class="comSite360a2" onclick="WEIBOShare();"></a>
		<a href="javascript:void(0)" onclick="QZoneShare();" title="QQ空间" class="comSite360a3"></a>
		<a href="javascript:void(0)" onclick="QQShare();" title="腾讯QQ" class="comSite360a4"></a>
	</div>
</div>
<div id="catadiv" class="comTc comTc01">
	<div class="comTcTop pr"><h1>创建新唱片夹</h1><a href="javascript:void(0)" title="关闭" class="comTcSt01"></a></div>
	<div class="comTcCon">
		<div class="comTcSite01 cf">
			<span class="comTcSt02"></span>
			<div class="comTcSite02 pr">
				<input id="catalfoldername" type="text" class="comTcSt03" value="">
				<span class="comTcSt04"></span>
			</div>
		</div>
		<div class="comTcSite05 cf comTcPd01">
			<a href="javascript:doCreate('2','F');" id="createcatal" class="comTcSt06" >确定</a>
			<a href="javascript:void(0)" class="comTcSt07">取消</a>
		</div>
	</div>
</div>
<div id="trackdiv" class="comTc comTc01">
	<div class="comTcTop pr"><h1>创建新单曲夹</h1><a href="javascript:void(0)" title="关闭" class="comTcSt01"></a></div>
	<div class="comTcCon">
		<div class="comTcSite01 cf">
			<span class="comTcSt02"></span>
			<div class="comTcSite02 pr">
				<input id="trackfoldername" type="text" class="comTcSt03" value="">
				<span class="comTcSt04">20</span>
			</div>
		</div>
		<div class="comTcSite05 cf comTcPd01">
			<a href="javascript:doCreate('1','F');" id="createtrack" class="comTcSt06">确定</a>
			<a href="javascript:void(0)" class="comTcSt07">取消</a>
		</div>
	</div>
</div>
<div id="addtotrack" class="comTc comTc04" style="display: none;">
	<div class="comTcTop pr"><h1>添加到单曲夹</h1><a href="javascript:void(0)" title="关闭" class="comTcSt01" onclick="closeDiv(1)"></a></div>
	<div class="comTcCon">
		<div class="comTcSite10">
			<div class="comTcSite14 cf"><span class="comTcSt11" onclick="addToFolderCreate('1');">新建单曲夹</span></div>
			<div class="comTcSite11 cf">
<!-- 				<ol class="cf"> -->
<!-- 					<li><span class="comTcSite12 pr"><em class="comTcSt12 comTcSt12a">贝多芬</em><input type="checkbox" class="comTcSt13 comTcSt13a"></span></li> -->
<!-- 				</ol> -->
			</div>
		</div>
		<div class="comTcSite05 cf comTcPd04">
			<a href="javascript:void(0)" class="comTcSt06" onclick="doAddSourceToFolder('2');">确定</a>
			<a href="javascript:void(0)" class="comTcSt07">取消</a>
		</div>
	</div>
</div>
<div id="addtocatal" class="comTc comTc04" style="display: none;">
	<div class="comTcTop pr"><h1>添加到唱片夹</h1><a href="javascript:void(0)" title="关闭" class="comTcSt01"></a></div>
	<div class="comTcCon">
		<div class="comTcSite10">
			<div class="comTcSite14 cf"><span class="comTcSt11" onclick="addToFolderCreate('2');">新建唱片夹</span></div>
			<div class="comTcSite11 cf">
<!-- 				<ol class="cf"> -->
<!-- 					<li><span class="comTcSite12 pr"><em class="comTcSt12 comTcSt12a">贝多芬</em><input type="checkbox" class="comTcSt13 comTcSt13a"></span></li> -->
<!-- 				</ol> -->
			</div>
		</div>
		<div class="comTcSite05 cf comTcPd04">
			<a href="javascript:void(0)" class="comTcSt06" onclick="doAddSourceToFolder('1');">确定</a>
			<a href="javascript:void(0)" class="comTcSt07">取消</a>
		</div>
	</div>
</div>
<script>
function openCata(code){
	window.location.href = wwwHeadUrl+'/album/'+code;
}
function openVideo(code){
	window.location.href = wwwHeadUrl+'/video/'+code;
}
<!--分享-->
var picurl = "";
var title = "";
var l_code = "";
function shareTrack(_lcode,_itemcode,_title){
// 	debugger;
	picurl = "http://image.kuke.com/images/audio/cata200/"+_itemcode.substring(0,1)+"/"+_itemcode+".jpg";
	title = _title;
	l_code = _lcode;
	//type,picurl,title,l_code
	$(".comTcBg").show();
	$("#shareTips").show();
	
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


<!--分享结束-->

<!--下拉分页-->
$(function(){
 	var currentPage = 1;
	$(window).on("scroll",function(){
 	var more = true;
		 	var num1 = Math.round($(document).height() - $(window).scrollTop());
			var num = $(window).height();
// 			console.log("num1:"+num1+"  num:"+num);
			if(num1 == num){
				if(more==true){
	    			var url ="http://auth.kuke.com/kuke/userCenter/getFavoriteTrackAjax";
	    			if($("#vodka").val()=='t'){
	    				url ="http://auth.kuke.com/kuke/userCenter/getFavoriteTrackAjax";
	    			}
	    			if($("#vodka").val()=='c'){
	    				url ="http://auth.kuke.com/kuke/userCenter/getFavoriteCatalAjax";
	    			}
	    			if($("#vodka").val()=='v'){
	    				url ="http://auth.kuke.com/kuke/userCenter/getFavoriteVedioAjax";
	    			}
					$.ajax({
						type : "POST",
						url : url,
						dataType : "json",
						data:{
							currentPage:++currentPage
							
						},
						success : function(data,textStatus) {
							//debugger;
							if (data.flag == true) {
							console.log(data.scrollInfo);
							more = data.more;
							if($("#vodka").val()=='t'){
			    				$(".comSite250").append(data.scrollInfo);
			    				init();
			    			}
			    			if($("#vodka").val()=='c'){
			    				$(".comSite240").append(data.scrollInfo);
			    				init();
			    			}
			    			if($("#vodka").val()=='v'){
			    				//debugger;
			    				$(".comSite250").append(data.scrollInfo);
			    				init();
			    			}	
						  }
						},
						error : function(textStatus,errorThrown) {
						}
					});
				}
    		}
	});
	
});
<!--下拉分页结束-->

<!--关闭按钮-->
function closeDiv(type){
	if(type==1){
		$("#catadiv").hide();
	}else if(type==2){
		$("#trackdiv").hide();
	}
}



//创建夹子
//显示
function showCreate(type){
	//显示弹层
	$(".comTcBg").show();
	if(type == "2"){
		$("#catadiv").show();
	}else{
		$("#trackdiv").show();
	}
}


function cancelFavorSingle(type,source_id){
// 	debugger;
	if(type=='1'){
		//专辑
		site = '2';
	}
	else if(type=='2'){
		//单曲
		site = '1';
	}else{
		site = '3';
	}
	$.ajax({
		type : "POST",
		url : "http://auth.kuke.com/kuke/userCenter/cancleFavourite",
		data:{
			site      : site,
			type      : type,
			source_id : source_id
		},
		dataType : "json",
		success : function(info,textStatus) {
			if(info.flag=true){
				//定位到标签页		
				window.location.reload();
				$(".comSite244a a").removeClass("comOn23").eq(${site}).addClass("comOn23");
				$(".comSite246a").hide().eq(${site}).show();
				if(${site==0}){
					$("#vodka").val("t");
				}
				if(${site==1}){
					$("#vodka").val("c");
				}
				if(${site==2}){
					$("#vodka").val("v");
				}
			}
		},
		error : function(textStatus,errorThrown) {
			alert("出错了");
		}
	});
}

function cancelShow(type,value){
// 	debugger;
	$(".comTcBg").show();
	$("#cancelFavorDiv").show();
	$("#htype").val(type);
// 	console.log($("#htype").val());
	$("#hvalue").val(value);
// 	console.log($("#hvalue").val());
}
function cancelConfirm(){
	var type = $("#htype").val();
	var value = $("#hvalue").val();
	cancelFavorSingle(type,value);
}
function cancelOper(){
	$("cancelFavorDiv").hide();
}

function cancelFavor(type){
	var source_id = "";
	var site = "";
	if(type=="1"){
		source_id = totalCheckValue(type);
	}else{
		source_id = totalCheckValueLcode(type);
	}
	if(type=='1'){
		//专辑
		site = '2';
	}
	else if(type=='2'){
		//单曲
		site = '1';
	}else{
		site = '3';
	}
	if(source_id != false){
		//type:1:单曲,2:专辑,3:视频
		$.ajax({
			type : "POST",
			url : "http://auth.kuke.com/kuke/userCenter/cancleFavourite",
			data:{
				site      : site,
				type      : type,
				source_id : source_id
			},
			dataType : "json",
			success : function(info,textStatus) {
				if(info.flag=true){
					//定位到标签页		
					window.location.reload();
					$(".comSite244a a").removeClass("comOn23").eq(${site}).addClass("comOn23");
					$(".comSite246a").hide().eq(${site}).show();
					if(${site==0}){
						$("#vodka").val("t");
					}
					if(${site==1}){
						$("#vodka").val("c");
					}
					if(${site==2}){
						$("#vodka").val("v");
					}
				}
			},
			error : function(textStatus,errorThrown) {
				alert("出错了");
			}
		});
	}
	
}
function addToPlayList(type){
//  	debugger;
	var source_id = totalCheckValueLcode(type);
	if(source_id!=false||source_id!=null){
		var index = source_id.lastIndexOf(",");
		source_id = source_id.substring(0,index);
		//window.open(toIndex+'/kuke/wwwnew/play/bridge/dispatch?type=track&op=play&lcodes=' + source_id, 'mbox-bridge-iframe');
		if(type == 2){
			preTrackPlayAdd(source_id);
		}else if(type == 1){
			preCatlogPlayAdd(source_id);
		}
	}
}
function unionCheckBoxValueLcode(arr){
	var a ="";
	var b = ""
	for(i=0;i<arr.length;i++){
		if(arr[i].checked){
			b = arr[i].value.split("|");
			a += b[0]+",";
			}
		}
	return a;
}
function totalCheckValueLcode(type){
	var source_id = "";
	if(type==1){
		var arr = document.getElementsByName("catabox");
		if(unionCheckBoxValueLcode(arr).length==0){
			alert("请选择要操作的专辑");
			return false;
		}
		source_id = unionCheckBoxValueLcode(arr);
		//alert(source_id);
	}else if(type==2){
		var arr = document.getElementsByName("trackbox");
		if(unionCheckBoxValueLcode(arr).length==0){
			alert("请选择要操作的单曲");
			return false;
		}
		source_id = unionCheckBoxValueLcode(arr);
		//alert(source_id);
	}
	else{
		var arr = document.getElementsByName("videobox");
		if(unionCheckBoxValueLcode(arr).length==0){
			alert("请选择要操作的单曲");
			return false;
		}
		source_id = unionCheckBoxValue(arr);
		//alert(source_id);
	}
	return source_id;
}
function unionCheckBoxValue(arr){
// 	debugger;
	var a ="";
	for(i=0;i<arr.length;i++){
// 		console.log(arr[i].checked);
		if(arr[i].checked){
			a += arr[i].value+",";
			}
		}
	return a;
}
function totalCheckValue(type){
// 	debugger;
	var source_id = "";
	if(type==1){
		var arr = document.getElementsByName("catabox");
		if(unionCheckBoxValue(arr).length==0){
			alert("请选择要操作的专辑");
			return false;
		}
		source_id = unionCheckBoxValue(arr);
		//alert(source_id);
	}else if(type==2){
		var arr = document.getElementsByName("trackbox");
		if(unionCheckBoxValue(arr).length==0){
			alert("请选择要操作的单曲");
			return false;
		}
		source_id = unionCheckBoxValue(arr);
		//alert(source_id);
	}
	else{
		var arr = document.getElementsByName("videobox");
		if(unionCheckBoxValue(arr).length==0){
			alert("请选择要操作的视频");
			return false;
		}
		source_id = unionCheckBoxValue(arr);
		//alert(source_id);
	}
	return source_id;
}

function addCalToPlay(){//tianjiazhuanjidaobofangliebiao
	//添加之前检验是否选中值
	if(!totalCheckValue(type)){
		return ;
	}
}

function addToFolder(type,id){
// 	debugger;
	//添加之前检验是否选中值
	if(!totalCheckValue(type)){
		return ;
	}
// 	debugger;
	var typeName = "";
	if(type == "1"){
		typeName = "唱片夹";
	}else if(type == "2"){
		typeName = "单曲夹";
	}
	//显示弹层
	$(".comTcBg").show();
	if(type == "1"){
		$("#addtocatal").show();
		type = "2";
	}else{
		$("#addtotrack").show();
		type = "1";
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
						"<li><span class='comTcSite12 pr'><em class='comTcSt12 comTcSt12a'>"+o[i].foldername+"</em><input onclick=chooseFolderID(this,'"+type+"') type='checkbox' name='toAddID' class='comTcSt13 comTcSt13a' value='"+o[i].id+"'></span></li>" +
						"</ol>";
					}
				}
				if(type == "2"){
					$("#addtocatal").find(".comTcSite11").html(html);
				}else{
					$("#addtotrack").find(".comTcSite11").html(html);
				}
			}
		}
	});
}
//点击选中要添加的夹子,夹子type：1：单曲，2：专辑
function chooseFolderID(obj,type){
	//debugger;
	if(type == "2"){//唱片下选中的
		toFolderID = $("#addtocatal").find("input[name=toAddID]:checked").val();
		typeName = "唱片夹";
	}else{
		toFolderID = $("#addtotrack").find("input[name=toAddID]:checked").val();
		//alert(toFolderID);
		typeName = "单曲夹";
	}
	$(".comTcSt12a").removeClass("comOn33");
	//$(".comTcSite11").find("input[name=toAddID]").attr("checked",false);
	
	$(obj).parent().find(".comTcSt12a").toggleClass("comOn33");
	$(obj).parent().find("input[name=toAddID]").attr("checked",true);
}

//资源添加到夹子 执行
function doAddSourceToFolder(type){
	var toFolderID = '';
	var typeName = '';
	//debugger;
	if(type == "2"){//2:单曲
// 		console.log($("#addtotrack").find("input[name=toAddID]:checked"));
		toFolderID = $("#addtotrack").find("input[name=toAddID]:checked").val();
		typeName = "单曲夹";
	}else{//1：专辑
		toFolderID = $("#addtocatal").find("input[name=toAddID]:checked").val();
		typeName = "唱片夹";
	}
	if(toFolderID == ""){
		alert("您为选中任何"+typeName);
		return;
	}
	var sourceAddID = totalCheckValue(type); 
	$.ajax({
		url:"/kuke/userCenter/addSourceToFolder",
		type:"post",
		dataType:"json",
		async:false,
		data:{source_id:sourceAddID,musicfolder_id:toFolderID},
		success:function(data){
			if(data.flag == true){
				$(".comTc04").hide();
				$(".comTcBg").hide();
				$(".comNotice04").show();
				setTimeout(function(){
					//隐藏提示框
					$(".comNotice04").hide();
					$(".comTcBg").hide();
				},1000);
			}else{
				alert("添加失败,请刷新后重试");
			}
		}
	});
}
//点击确定添加到现有的夹子中
function addtoFolder(type){
	//debugger;
	var folderID = "";
	if(type == "2"){//单曲
		folderID = $("#addtotrack").find("input[name=toAddID]:checked").val();
	}else{
		folderID = $("#addtocatal").find("input[name=toAddID]:checked").val();
	}
	//alert(folderID);
	var source_id = totalCheckValue(type);
// 	debugger;
	$.ajax({
		//url:"/kuke/userCenter/addFolderSourceToFolder",
		url:"/kuke/userCenter/createFavoritesFolder",
		type:"post",
		dataType:"json",
		async:false,
		data:{
			musicfolder_id:folderID,
			premusicfolder_id:addid,
			source_id:source_id
		},
		success:function(data){
// 			debugger;
			if(data.flag == true){
				$(".comTc04").hide();
				$(".trackdiv").hide();
				$(".comNotice04").show();
				/* setTimeout(function(){
					//隐藏提示框
					$(".comNotice04").hide();
					//刷新
					window.location.href = '/kuke/userCenter/getUserFolder?foldertype='+type;
				},1000); */
			}else{
					alert("添加失败,请刷新后重试");
			}
		}
	});
}
//创建并添加
function doCreate(type,flag){
// 	debugger;
	console.log("doCreate is start...");
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
		alert(typeName+"名称应为2-20个字符");
		return;
	}
	var premusicfolder_id = "";
	if(flag == "S"){//从添加到唱片夹过来的
		premusicfolder_id = addid;
	}
	if(type == "1"){
		temp = "2";
	}else{
		temp = "1";
	}
	var source_id = totalCheckValue(temp);
	if(source_id != false){
		$.ajax({
			url:"/kuke/userCenter/createFavoritesFolder",
			type:"post",
			dataType:"json",
			async:false,
			data:{
				type:type,
				folder_name:name,
				source_id:source_id,
				premusicfolder_id:premusicfolder_id
				},
			success:function(data){
				if(data.flag == true){
// 					debugger;
					$(".comTc01").hide();
					$(".comTcBg").hide();
					tips("已添加");
				}else{
					if(data.code == "HASNAME"){
						alert(typeName+"名称已经存在");
					}else{
						alert("创建失败,请重试");
					}
				}
			}
		});
		}
}
//添加到夹子中的创建夹子
function addToFolderCreate(type){
// 	debugger;
	//隐藏添加到唱片夹
	$(".comTc04").hide();
	//赋值点击事件
	if(type == "2"){
		$("#createcatal").attr("href","javascript:doCreate('2','S');");;
	}else{
		$("#createtrack").attr("href","javascript:doCreate('1','S');");;
	}
	//隐藏所有选中
	$(".comTcSt12a").removeClass("comOn33");
	$(".comTcSite11").find("input[name=toAddID]").attr("checked",false);
	//显示创建夹子
	showCreate(type);
}
$(function(){
	init();
	//***关闭分享页
	$(".comTcSt01,.comTcSt07").on("click",function(){
		$(".comTc").hide();
		$(".comTcBg").hide();
	});
});
function init(){
	// 头部下拉菜单
	$(".comSite200").hover(function(){
		$(this).find(".comSite202").show();
	},function(){
		$(this).find(".comSite202").hide();
	});
	// 单曲列表鼠标悬停
	$(".comSite251a").hover(function(){
		$(this).addClass("comOn26");
		$(this).find(".comSt223a").addClass("comOn25");
		$(this).find(".comSt227a").show();
		$(this).find(".comSt232a").addClass("comOn27");
	},function(){
		$(this).removeClass("comOn26");
		$(this).find(".comSt223a").removeClass("comOn25");
		$(this).find(".comSt227a").hide();
		$(this).find(".comSt232a").removeClass("comOn27");
	});
	// 单曲全选
	$(".comSt219a").on("click",function(){
		if(this.checked == true){
			$(this).parent().find(".comSt218a").addClass("comOn24");
// 			$(".comSt218b").addClass("comOn24").prop("checked",true);
// 			$(".comSt219b").addClass("comOn24").prop("checked",true);
			$(".comSt218b").addClass("comOn24");
			$(".comSt219b").addClass("comOn24");
			for(i=0;i<document.getElementsByName("trackbox").length;i++){
				document.getElementsByName("trackbox")[i].checked = "checked";
// 				console.log(document.getElementsByName("trackbox")[i].checked);
			}
		}else{
			$(this).parent().find(".comSt218a").removeClass("comOn24");
// 			$(".comSt218b").removeClass("comOn24").prop("checked",false);
// 			$(".comSt219b").removeClass("comOn24").prop("checked",false);
			$(".comSt218b").removeClass("comOn24");
			$(".comSt219b").removeClass("comOn24");
			for(i=0;i<document.getElementsByName("trackbox").length;i++){
				document.getElementsByName("trackbox")[i].checked = "";
			}
		}
	});
	// 单曲单选
	$(".comSt219b").on("click",function(){
		$(this).parent().find(".comSt218b").toggleClass("comOn24");
	});
	// 唱片列表鼠标悬停
	$(".comSite258a").hover(function(){
		$(this).find(".comSt53a,.comSite260b,.comSt252a,.comSite262a").show();
	},function(){
		if($(this).find(".comSt250a").hasClass("comOn28")){
			$(this).find(".comSt53a,.comSite260b,.comSt252a,.comSite262a").show();
		}else{
			$(this).find(".comSt53a,.comSite260b,.comSt252a,.comSite262a").hide();
		}
	});
	// 唱片全选
	$(".comSt219c").on("click",function(){
		if(this.checked == true){
			$(this).parent().find(".comSt218c").addClass("comOn24");
			$(".comSt250a").addClass("comOn28");
			$(".comSite258a").find(".comSt53a,.comSite260b,.comSt252a,.comSite262a").show();
			for(i=0;i<document.getElementsByName("catabox").length;i++){
				document.getElementsByName("catabox")[i].checked = "checked";
			}
		}else{
			$(this).parent().find(".comSt218c").removeClass("comOn24");
			$(".comSt250a").removeClass("comOn28");
			$(".comSite258a").find(".comSt53a,.comSite260b,.comSt252a,.comSite262a").hide();
			for(i=0;i<document.getElementsByName("catabox").length;i++){
				document.getElementsByName("catabox")[i].checked = "";
			}
		}
	});
	// 唱片单选
	$(".comSt251a").on("click",function(){
		$(this).parent().find(".comSt250a").toggleClass("comOn28");
	});
	// 唱片列表鼠标悬停
	$(".comSite263a").hover(function(){
		$(this).find(".comSt53b,.comSt257a").show();
		$(this).find(".comSite265a").addClass("comOn29");
		$(this).find(".comSite260c").show();
	},function(){
		if($(this).find(".comSt250b").hasClass("comOn28")){
			$(this).find(".comSt53b,.comSt257a").show();
			$(this).find(".comSite265a").addClass("comOn29");
			$(this).find(".comSite260c").show();
		}else{
			$(this).find(".comSt53b,.comSt257a").hide();
			$(this).find(".comSite265a").removeClass("comOn29");
			$(this).find(".comSite260c").hide();
		}
	});
	// 视频全选
	$(".comSt219d").on("click",function(){
		if(this.checked == true){
			$(this).parent().find(".comSt218d").addClass("comOn24");
			$(".comSt250b").addClass("comOn28");
			$(".comSite263a").find(".comSt53b,.comSt257a").show();
			$(".comSite263a").find(".comSite265a").addClass("comOn29");
			$(".comSite263a").find(".comSite260c").show();
			for(i=0;i<document.getElementsByName("videobox").length;i++){
				document.getElementsByName("videobox")[i].checked = "checked";
			}
			
		}else{
			$(this).parent().find(".comSt218d").removeClass("comOn24");
			$(".comSt250b").removeClass("comOn28");
			$(".comSite263a").find(".comSt53b,.comSt257a").hide();
			$(".comSite263a").find(".comSite265a").removeClass("comOn29");
			$(".comSite263a").find(".comSite260c").hide();
			for(i=0;i<document.getElementsByName("videobox").length;i++){
				document.getElementsByName("videobox")[i].checked = "";
			}
		}
	});
	// 视频单选
	$(".comSt251b").on("click",function(){
		$(this).parent().find(".comSt250b").toggleClass("comOn28");
	});
	// 单曲、唱片、视频 选项卡切换
	$(".comSite244a a").on("click",function(){
		//debugger;
		var i = $(".comSite244a a").index(this);
		$(".comSite244a a").removeClass("comOn23").eq(i).addClass("comOn23");
		$(".comSite246a").hide().eq(i).show();
		if(i==0){
			$("#vodka").val("t");
		}
		if(i==1){
			$("#vodka").val("c");
		}
		if(i==2){
			$("#vodka").val("v");
		}
	});
	if(${!empty site}){
		$(".comSite244a a").removeClass("comOn23").eq(${site}).addClass("comOn23");
		$(".comSite246a").hide().eq(${site}).show();
		if(${site==0}){
			$("#vodka").val("t");
		}
		if(${site==1}){
			$("#vodka").val("c");
		}
		if(${site==2}){
			$("#vodka").val("v");
		}
	}
}
</script>
</body>
</html>