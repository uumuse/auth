<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<%@include file="/jsp/common/CommonMeta.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>我的订阅</title>
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta name="renderer" content="webkit">
<meta name="keywords" content="">
<meta name="description" content="">
<link href="/css/init.css" rel="stylesheet">
<link href="/css/main.css" rel="stylesheet">
<script type="text/javascript" src="/js/jquery-1.12.4.min.js"></script>
<script type="text/javascript" src="/js/userCenter/label.js"></script>
<script type="text/javascript" src="/js/common/util.js"></script>
</head>
<body>
<%@ include file="/jsp/userCenter/userLoginHead.jsp"%>
<%@ include file="/jsp/userCenter/userInfoHead.jsp"%>
<div class="comSite240 comSite240c1">
	<div class="comSite241 comWd02">
		<div class="comSite242 comWd01">
			<div class="comSite243 cf">
<!-- 				<a href="javascript:void(0)" class="comSite243a1">最近播放</a> -->
				<a href="/kuke/userCenter/getFavoriteTrack" class="comSite243a2">我的喜欢</a>
				<a href="/kuke/userCenter/getSubscribeLabel" class="comSite243a3 comSite243a3-on">我的订阅</a>
				<a href="/kuke/userCenter/getUserFolder" class="comSite243a4">我的唱片夹</a>
			</div>
			<div class="comSite244 comSite244b cf">
				<a href="/kuke/userCenter/getSubscribeLabel" class="comOn23">厂牌</a>
				<a href="/kuke/userCenter/getSubscribeArtist" class="pr">艺术家</a><!-- <span class="comSt284 comSt284a"></span> -->
			</div>
			<div class="comSite245">
				<div class="comSite246 comSite246b"><!-- 切换 -->
					<p class="comSite247">共 <span>${count}</span> 个</p>
					<div class="comSite248 cf">
<!-- 						<span class="comSite249 pr fl"> -->
<!-- 							<em class="comSt218 comSt218b">全选</em> -->
<!-- 							<input type="checkbox" class="comSt219 comSt219b"> -->
<!-- 						</span> -->
<!-- 						<span class="comSt283 comSt283a fl" onclick="cancleDLSubscribeL();">取消订阅</span> -->
					</div>
					<div id="lablediv" class="comSite250 comSite250b">
						<c:if test="${count != 0}">
								<ol class="comSite294 cf">
							<c:forEach varStatus="status" items="${subscribeLabelList}" var="item">
								<c:if test="${item.showable == 0}"><!-- 下架样式 -->
									<li class="comSite295 comSite295a cOff03">
										<div class="comSite295cc1 pr" onclick="tips('showOffMessage');">
											<div class="comSite296"><img src="${item.logoImg}" alt="" style="height: 90px;"></div>
											<p class="comSt279"><a href="javascript:void(0)">${item.displayName}</a></p>
											<span class="offBg01 offBg01b"></span>
											<span class="isOff isOff02">已下架</span>
											<div class="comSite298 comSite298a">
												<div class="comSite299 pr">
												</div>
											</div>
										</div>
										<div class="comSite297"><a  href="javascript:void(0)" class="comSt280 comSt280a" onclick="cancleSubscribeL('${item.rssType}','${item.labelid}');">取消订阅</a></div>
									</li>
									<c:if test="${(status.index + 1) % 5 == 0 && (status.count != fn:length(subscribeLabelList))}">
										</ol>
										<ol class="comSite294 cf">
									</c:if>
								</c:if>
								<c:if test="${item.showable != 0}"><!-- 未下架样式 -->
									<li class="comSite295 comSite295a">
										<div class="comSite295cc1 pr">
											<div class="comSite296"><img onclick="openLabel('${item.labelid}');" src="${item.logoImg}" alt="" style="height: 90px;cursor: pointer;"></div>
											<p class="comSt279"><a href="javascript:void(0)" onclick="openLabel('${item.labelid}');">${item.displayName}</a></p>
											<div class="comSite298 comSite298a">
												<div class="comSite299 pr">
<!-- 													<span class="comSt281 comSt281a"></span> -->
<%-- 													<input type="checkbox" name="labeldata" class="comSt282 comSt282a" isclick="false" checked="false" value="${item.labelid}"> --%>
												</div>
											</div>
<!-- 											<span class="comSt284 comSt284b"></span> -->
										</div>
										<div class="comSite297"><a  href="javascript:void(0)" class="comSt280 comSt280a" onclick="cancleSubscribeL('${item.rssType}','${item.labelid}');">取消订阅</a></div>
									</li>
									<c:if test="${(status.index + 1) % 5 == 0 && (status.count != fn:length(subscribeLabelList))}">
										</ol>
										<ol class="comSite294 cf">
									</c:if>
								</c:if>
							</c:forEach>
									</ol>
						</c:if>
						<c:if test="${count == 0}">
							<p class="noInfo">—— 暂无内容 ——</p>
						</c:if>
					</div>
				</div>
				<div class="comSite246 comSite246b" style="display:none;"></div>
		</div>
	</div>
</div>
<jsp:include page="/jsp/userCenter/footer.jsp"></jsp:include>
<script>
$(function(){
	//下拉加载
 	var currentPage = 1;//当前页
 	var more = true;//是否还有更多
	$(window).on("scroll",function(){
		if($(document).height() - $(window).scrollTop() == $(window).height()){
			if(more==true){
				$.ajax({
					type : "POST",
					url : "/kuke/userCenter/getSubscribeLabelAjax",
					dataType : "json",
					data:{
						currentPage:++currentPage
					},
					success : function(info,textStatus) {
						console.log(info);
						var dataInfo = info.data;
						if (info.flag == true) {
							more = dataInfo.more;
							$("#lablediv").append(dataInfo.scrollInfo);
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
	
	//初始化厂牌 艺术家页面
	var site = 0;
	$(".comSite244b a").removeClass("comOn23").eq(site).addClass("comOn23");
	$(".comSite246b").hide().eq(site).show();
	// 艺术家切换
	$(".comSite301 a").on("click",function(){
		var i = $(".comSite301 a").index(this);
		$(".comSite301 a").removeClass("comOn38").eq(i).addClass("comOn38");
		$(".comSite302a").hide().eq(i).show();
		
		//全选对勾清零
		$(".comSt218c").removeClass("comOn24");
		$(".comSt219c").attr("checked",false);
		//B全部清零
		for(var i = 1; i <= 4; i++){
			$(".comSt281b"+i).removeClass("comOn37");
 			$(".comSt282b"+i).prop("checked",false).attr("isclick","false");
 			$(".comSite295b"+i).removeClass("comOn36");
 			$(".comSite298b"+i).hide();
 			$(".comSt284b"+i).hide();
		}
	});
	
	// 厂牌列表鼠标悬停
	$(".comSt282a").attr("isClick","false");
	$(".comSt282a").prop("checked",false);
	$(".comSt219b").prop("checked",false);
	$(".comSite295a").hover(function(){
		$(this).find(".comSite295cc1").addClass("comOn36");
		$(this).find(".comSite298a").show();
		$(this).find(".comSt284b").show();
		$(this).find(".comSite297").show();
	},function(){
// 		if($(this).find(".comSt282a").attr("isClick") == "false"){
			$(this).find(".comSite295cc1").removeClass("comOn36");
			$(this).find(".comSite298a").hide();
			$(this).find(".comSt284b").hide();
			$(this).find(".comSite297").hide();
// 		}else{
// 			$(this).find(".comSite295cc1").addClass("comOn36");
// 			$(this).find(".comSite298a").show();
// 			$(this).find(".comSt284b").show();
// 			$(this).find(".comSite297").show();
// 		}
	});
	// 厂牌全选
	$(".comSt219b").on("click",function(){
		if($(this).prop("checked") == true){
			$(this).parent().find(".comSt218b").addClass("comOn24");
			$(".comSt281a").addClass("comOn37");
			$(".comSt282a").prop("checked",true).attr("isclick","true");
			$(".comSite295a").addClass("comOn36");
			$(".comSite298a").show();
			$(".comSt284b").show();
		}else{
			$(this).parent().find(".comSt218b").removeClass("comOn24");
			$(".comSt281a").removeClass("comOn37");
			$(".comSt282a").prop("checked",false).attr("isclick","false");
			$(".comSite295a").removeClass("comOn36");
			$(".comSite298a").hide();
			$(".comSt284b").hide();
		}
	});
	// 厂牌单选
	$(".comSt282a").on("click",function(){
		if($(this).prop("checked") == true){
			$(this).attr("isClick","true");
			$(this).parent().find(".comSt281a").addClass("comOn37");
		}else{
			$(this).attr("isClick","false");
			$(this).parent().find(".comSt281a").removeClass("comOn37");
		}
	});
}
</script>
</body>
</html>