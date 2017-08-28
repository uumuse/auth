<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html >
<html>
<head>
<%@include file="/jsp/common/CommonMeta.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>我的唱片夹</title>
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta name="renderer" content="webkit">
<meta name="keywords" content="">
<meta name="description" content="">
<link href="/css/init.css" rel="stylesheet">
<link href="/css/main.css" rel="stylesheet">
<script type="text/javascript" src="/js/jquery-1.12.4.min.js"></script>
<script type="text/javascript" src="/js/userCenter/folder.js"></script>
<script type="text/javascript" src="/js/common/util.js"></script>
<script type="text/javascript" src="/js/ajaxfileupload.js"></script>
<script type="text/javascript" src="/js/jquery.qrcode.min.js"></script>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
</head>
<%@ include file="/jsp/userCenter/userLoginHead.jsp"%>
<%@ include file="/jsp/userCenter/userInfoHead.jsp"%>
<div class="comSite240 comSite240c1">
	<div class="comSite241 comWd02">
		<div class="comSite242 comWd01">
			<div class="comSite243 cf">
<!-- 				<a href="javascript:void(0)" class="comSite243a1">最近播放</a> -->
				<a href="/kuke/userCenter/getFavoriteTrack" class="comSite243a2">我的喜欢</a>
				<a href="/kuke/userCenter/getSubscribeLabel" class="comSite243a3">我的订阅</a>
				<a href="/kuke/userCenter/getUserFolder" class="comSite243a4 comSite243a4-on">我的唱片夹</a>
			</div>
			<div class="comSite245">
				<div class="comSite266 comSite266a" style="display:none;">
					<div class="comSite267"><a  href="javascript:void(0)" onclick="goBack();"  class="comSt258">&lt; 返回</a></div>
					<div class="comSite268 cf">
						<div class="comSite269 pr">
							<div><img id="folderImg" onerror="javascript:this.src='/images/default_zhuanji_s.jpg';" src="${userFolder.imgurl}" alt="" width="210" height="210"></div>
							<span class="comSt259"></span>
							<span class="comSt260 comSt260b" style="width: 24px;overflow: hidden;" onclick="showUpload();"></span>
						</div>
						<div class="comSite270">
							<div class="comSite271 c">
								<input style="width: 200px;" type="text" id="trackfloder" disabled="disabled" class="comSt261 comSt261b" value="${ userFolder.foldername }">
								<span title="编辑" id="trackSpan" class="comSt262 comSt262b" onclick="changeFolderName('trackfloder','trackSpan')"></span>
							</div>
							<p class="comSite277">创建日期：<span>${fn:replace(fn:substring(userFolder.create_date, 0, 10), "-", ".")} </span></p>
							<div class="comSite272 cf">
								<span class="comSt263 comSt263b" onclick="toDlPlay('1','${musicfolder_id}','play');"></span>
								<span class="comSt264 comSt264b" onclick="toDlPlay('1','${musicfolder_id}','add');">添加到播放列表</span>
								<span class="comSt266 comSt266b" onclick="delFolder('1')" >删除</span>
							</div>
						</div>
					</div>
					<div class="comSite273">
						<div id="trackdivcontent" class="comSite250 comSite250a">
							<c:if test="${count != 0}">
								<c:forEach var="item" items="${tracksOfFolder}" varStatus="status">
									<c:if test="${item.showable == 0}"><!-- 下架样式 -->
										<div class="comSite251 cf comSite251b cOff04">
											<a href="javascript:void(0);" class="comSt268" onclick="tips('showOffMessage');"></a>
											<span class="comSt224 fl" style="width: 20px;">${ status.index + 1 }</span>
											<span class="comSt225 fl eps" onclick="tips('showOffMessage');">${item.trackTitle}</span>
											<span class="comSt226 fl pr">
												<em>${item.timing}</em>
												<span class="comSt227 comSt227b cf"></span>
												<span class="isOff isOff03">已下架</span>
											</span>
											<a href="javascript:void(0);" onclick="tips('showOffMessage');" class="comSt232 comSt232b fl eps">${item.calogTitle }</a>
											<em title="删除" class="comSt256 comSt256c1" onclick="delSource('1','${item.source_id}')"></em>
										</div>
									</c:if>
									<c:if test="${item.showable != 0}"><!-- 未下架样式 -->
										<div class="comSite251 cf comSite251b comSite251b1">
											<a href="javascript:void(0);" onclick="preTrackPlay('${item.lcode}');" class="comSt268 comSt268a"></a>
											<span class="comSt224 fl" style="width: 20px;">${ status.index + 1 }</span>
											<span class="comSt225 fl eps" onclick="openCatalog('${item.itemcode}');" style="cursor: pointer;">${item.trackTitle}</span>
											<span class="comSt226 fl pr">
												<em>${item.timing}</em>
												<span class="comSt227 comSt227b cf">
													<em title="添加到播放列表" class="comSt228 comSt228b" onclick="preTrackPlayAdd('${item.lcode}');"></em>
													<em title="添加到单曲夹" class="comSt229 comSt229b" onclick="addSourceToFolder('1','${item.source_id}','${item.musicfolder_id}');"></em>
													<em title="分享" class="comSt230 comSt230b" onclick="toShare('${userFolder.itemcodeurl }','${fn:replace(item.trackDesc,'\'','') }','${item.lcode}');"></em>
													<em title="下载" class="comSt231 comSt231b" onclick="download('1','${item.lcode}','${item.itemcode}','${item.trackTitle}','');"></em>
													<em title="删除" class="comSt256 comSt256c1" onclick="delSource('1','${item.source_id}')"></em>
												</span>
											</span>
											<a href="javascript:void(0);" class="comSt232 comSt232b fl eps" onclick="openCatalog('${item.itemcode}');">${item.calogTitle }</a>
										</div>
									</c:if>
								</c:forEach>
							</c:if>
							<c:if test="${count == 0}">
								<p class="noInfo">—— 暂无内容 ——</p>
							</c:if>
						</div>
					</div>
				</div>
				<div class="comSite266 comSite266a" style="display:none;">
					<div class="comSite267"><a href="javascript:void(0)" onclick="goBack();" class="comSt258">&lt; 返回</a></div>
					<div class="comSite268 cf">
						<div class="comSite269 pr">
							<div><img <c:if test="${empty userFolder.imgurl}">src="/images/comTcUploadBg.png"</c:if> <c:if test="${!empty userFolder.imgurl}">src="${userFolder.imgurl}"</c:if> alt="" width="210" height="210"></div>
							<span class="comSt259"></span>
							<span class="comSt260 comSt260a"></span>
						</div>
						<div class="comSite270">
							<div class="comSite271 c">
								<input type="text" id="catalfloder" disabled="disabled" class="comSt261 comSt261a" value="${ userFolder.foldername }">
								<span title="编辑" id="catalSpan" class="comSt262 comSt262a" onclick="changeFolderName('catalfloder','catalSpan')"></span>
							</div>
							<p class="comSite277">创建日期：<span>${fn:replace(fn:substring(userFolder.create_date, 0, 10), "-", ".")}</span></p>
							<div class="comSite272 cf">
								<span class="comSt263 comSt263a" onclick="toDlPlay('2','${musicfolder_id}','play');"></span>
								<span class="comSt264 comSt264a" onclick="toDlPlay('2','${musicfolder_id}','add');">添加到播放列表</span>
								<span class="comSt265 comSt265a" onclick="addToFolder('2','${musicfolder_id}','${fn:length(catalsOfFolder)}');">添加到唱片夹</span>
								<span class="comSt266 comSt266a" onclick="delFolder('2')" >删除</span>
							</div>
						</div>
					</div>
					<div class="comSite273"></div>
			</div>
		</div>
	</div>
</div>
<jsp:include page="/jsp/userCenter/footer.jsp"></jsp:include>
<div class="comSite226 comSite226e">
	<p class="comSt201">已删除</p>
</div>
<div class="comTc comTc02" style="display: none;">
	<div class="comTcTop pr"><h1>提示</h1><a href="javascript:void(0)" title="关闭" class="comTcSt01"></a></div>
	<div class="comTcCon">
		<div class="comTcSite06"><p>确定要删除该单曲夹?</p></div>
		<div class="comTcSite05 cf comTcPd02">
			<a href="javascript:void(0)" class="comTcSt06" onclick="toDelFolder();">确定</a>
			<a href="javascript:void(0)" class="comTcSt07">取消</a>
		</div>
	</div>
</div>
<div class="comSite226 comNotice04">
	<p class="comSt201">已添加</p>
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
<input type="hidden" id="foldertype" value="${foldertype}">
<input type="hidden" id="nexturl" value="${nexturl}">
<input type="hidden" id="folderid" name="folderid" value="${musicfolder_id}">
<script>
$(function(){
 	var currentPage = 1;//当前页
 	var more = true;//是否还有更多
	$(window).on("scroll",function(){
		var num1 = Math.round($(document).height() - $(window).scrollTop());
		var num = $(window).height();
// 		console.log("num1:"+num1+"  num:"+num);
		if(num1 == num){
		if(more==true){
			$.ajax({
				type : "POST",
				url : "/kuke/userCenter/getTracksOfFolderAjax",
				dataType : "json",
				data:{
					currentPage:++currentPage,
					musicfolder_id:$("#folderid").val()
				},
				success : function(info,textStatus) {
					console.log(info);
					var dataInfo = info.data;
					if (info.flag == true) {
						more = dataInfo.more;
						$("#trackdivcontent").append(dataInfo.scrollInfo);
						//初始化js
						init();
					}
				},
				error : function(textStatus,errorThrown) {
				}
			});
		}
  		}
	});
	//初始化js
	init();
});
function init(){
	//初始化页面参数
	var foldertype = $("#foldertype").val();//夹子的类型
	if(foldertype == "2"){//唱片
		$(".comSite266a").hide().eq(1).show();
	}else{//单曲
		$(".comSite266a").hide().eq(0).show();
	}
	// 头部下拉菜单
	$(".comSite200").hover(function(){
		$(this).find(".comSite202").show();
	},function(){
		$(this).find(".comSite202").hide();
	});
	// 单曲、唱片、视频 选项卡切换
	$(".comSite244a a").on("click",function(){
		var i = $(".comSite244a a").index(this);
		$(".comSite244a a").removeClass("comOn23").eq(i).addClass("comOn23");
		$(".comSite246a").hide().eq(i).show();
	});
	// 单个唱片夹列表鼠标悬停
	$(".comSite275a").hover(function(){
		$(this).find(".comSt53c,.comSt252b,.comSite262b").show();
	},function(){
		$(this).find(".comSt53c,.comSt252b,.comSite262b").hide();
	});
	// 喜欢切换
// 	$(".comSt267b").on("click",function(){
// 		$(this).toggleClass("comOn30");
// 	});
	// 单曲列表鼠标悬停
	$(".comSite251b1").hover(function(){
		$(this).addClass("comOn26").find(".comSt268").addClass("comOn31");
		$(this).find(".comSt232b").addClass("comOn27");
		$(this).find(".comSt227b").show();
	},function(){
		$(this).removeClass("comOn26").find(".comSt268").removeClass("comOn31");
		$(this).find(".comSt232b").removeClass("comOn27");
		$(this).find(".comSt227b").hide();
	});
	// 单曲唱片切换
	$(".comSite244a a").on("click",function(){
		var i = $(".comSite244a a").index(this);
		$(".comSite244a a").removeClass("comOn23").eq(i).addClass("comOn23");
		$(".comSite266a").hide().eq(i).show();
	});
}
</script>
</body>
</html>