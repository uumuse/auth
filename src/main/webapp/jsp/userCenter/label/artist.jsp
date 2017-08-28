<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html >
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
				<div class="comSite246 comSite246b"></div>
				<div class="comSite246 comSite246b" style="display:none;"><!-- 切换 -->
					<p class="comSite247">共 <span>${count}</span> 位</p>
<!-- 					<div class="comSite300 pr"> -->
<!-- 						<div class="comSite301 cf"> -->
<!-- 							<a href="javascript:void(0)" class="comOn38">作曲家</a> -->
<!-- 							<a href="javascript:void(0)">演奏家</a> -->
<!-- 							<a href="javascript:void(0)">指挥家</a> -->
<!-- 							<a href="javascript:void(0)">乐团</a> -->
<!-- 						</div> -->
<!-- 					</div> -->
					<div class="comSite248 cf">
<!-- 					<span class="comSite249 pr fl"> -->
<!-- 						<em class="comSt218 comSt218c">全选</em> -->
<!-- 						<input type="checkbox" class="comSt219 comSt219c"> -->
<!-- 					</span> -->
<!-- 					<span class="comSt283 comSt283a fl" onclick="cancleDLSubscribeA();">取消订阅</span> -->
					</div>
					<div class="comSite250 comSite250b">
						<div id="artistdiv" class="comSite302 comSite302a">
								<c:if test="${count != 0}">
											<ol class="comSite294 cf">
									<c:forEach varStatus="status" items="${list}" var="item">
										<li class="comSite295 comSite295b1 pr">
											<div class="comSite295cc1 pr">
												<div class="comSite296"><img onclick="openArtist('${item.musicianId}','${item.mType}');" src="${item.musicianImg}" alt="" width="120" height="120" style="cursor: pointer;"></div>
												<p class="comSt279"><a href="javascript:void(0)" onclick="openArtist('${item.musicianId}','${item.mType}');" style="cursor: pointer;">${item.fullName}</a></p>
												<div class="comSite298 comSite298b1">
													<div class="comSite299 pr">
<!-- 														<span class="comSt281 comSt281b1"></span> -->
<%-- 														<input name="artistdata" type="checkbox" class="comSt282 comSt282b1" isclick="false" checked="false" value="${item.musicianId}"> --%>
													</div>
												</div>
<!-- 												<span class="comSt284 comSt284b comSt284b1"></span> -->
											</div>
											<div class="comSite297"><a href="javascript:void(0)" class="comSt280 comSt280a"  onclick="cancleSubscribeA('71','${item.musicianId}');">取消订阅</a></div>
										</li>
										<c:if test="${(status.index + 1) % 5 == 0 && (status.count != fn:length(list))}">
											</ol>
											<ol class="comSite294 cf">
										</c:if>
									</c:forEach>
											</ol>
								</c:if>
								<c:if test="${count == 0}">
									<p class="noInfo">—— 暂无内容 ——</p>
								</c:if>
						</div>
						<div class="comSite302 comSite302a" style="display:none;">
<!-- 								<ol class="comSite294 cf"> -->
<%-- 							<c:forEach varStatus="status" items="${list2}" var="item"> --%>
<!-- 								<li class="comSite295 comSite295b2 pr"> -->
<%-- 									<div class="comSite296"><img  <c:if test="${empty item.imgurl}">src="/images/200-200img.gif"</c:if> <c:if test="${!empty item.imgurl}">src="${item.imgurl}"</c:if>  alt="" width="120" height="120"></div> --%>
<%-- 									<p class="comSt279">${item.full_name}</p> --%>
<%-- 									<div class="comSite297"><a href="javascript:void(0)" class="comSt280 comSt280a"  onclick="cancleSubscribe('${item.id}');">取消订阅</a></div> --%>
<!-- 									<div class="comSite298 comSite298b2"> -->
<!-- 										<div class="comSite299 pr"> -->
<!-- 											<span class="comSt281 comSt281b2"></span> -->
<%-- 											<input name="artistdata" type="checkbox" class="comSt282 comSt282b2" isclick="false" checked="false" value="${item.id}"> --%>
<!-- 										</div> -->
<!-- 									</div> -->
<!-- 									<span class="comSt284 comSt284b comSt284b2"></span> -->
<!-- 								</li> -->
<%-- 								<c:if test="${(status.index + 1) % 5 == 0}"> --%>
<!-- 									</ol> -->
<!-- 									<ol class="comSite294 cf"> -->
<%-- 								</c:if> --%>
<%-- 								<c:if test="${ status.count == fn:length(list2) }"> --%>
<!-- 									</ol> -->
<%-- 								</c:if> --%>
<%-- 							</c:forEach> --%>
						</div>
						<div class="comSite302 comSite302a" style="display:none;">
<!-- 								<ol class="comSite294 cf"> -->
<%-- 							<c:forEach varStatus="status" items="${list3}" var="item"> --%>
<!-- 								<li class="comSite295 comSite295b3 pr"> -->
<%-- 									<div class="comSite296"><img  <c:if test="${empty item.imgurl}">src="/images/200-200img.gif"</c:if> <c:if test="${!empty item.imgurl}">src="${item.imgurl}"</c:if>  alt="" width="120" height="120"></div> --%>
<%-- 									<p class="comSt279">${item.full_name}</p> --%>
<%-- 									<div class="comSite297"><a href="javascript:void(0)" class="comSt280 comSt280a"  onclick="cancleSubscribe('${item.id}');">取消订阅</a></div> --%>
<!-- 									<div class="comSite298 comSite298b3"> -->
<!-- 										<div class="comSite299 pr"> -->
<!-- 											<span class="comSt281 comSt281b3"></span> -->
<%-- 											<input name="artistdata" type="checkbox" class="comSt282 comSt282b3" isclick="false" checked="false" value="${item.id}"> --%>
<!-- 										</div> -->
<!-- 									</div> -->
<!-- 									<span class="comSt284 comSt284b comSt284b3"></span> -->
<!-- 								</li> -->
<%-- 								<c:if test="${(status.index + 1) % 5 == 0}"> --%>
<!-- 									</ol> -->
<!-- 									<ol class="comSite294 cf"> -->
<%-- 								</c:if> --%>
<%-- 								<c:if test="${ status.count == fn:length(list3) }"> --%>
<!-- 									</ol> -->
<%-- 								</c:if> --%>
<%-- 							</c:forEach> --%>
						</div>
						<div class="comSite302 comSite302a" style="display:none;">
<!-- 								<ol class="comSite294 cf"> -->
<%-- 							<c:forEach varStatus="status" items="${list4}" var="item"> --%>
<!-- 								<li class="comSite295 comSite295b4 pr"> -->
<%-- 									<div class="comSite296"><img  <c:if test="${empty item.imgurl}">src="/images/200-200img.gif"</c:if> <c:if test="${!empty item.imgurl}">src="${item.imgurl}"</c:if>  alt="" width="120" height="120"></div> --%>
<%-- 									<p class="comSt279">${item.full_name}</p> --%>
<%-- 									<div class="comSite297"><a href="javascript:void(0)" class="comSt280 comSt280a"  onclick="cancleSubscribe('${item.id}');">取消订阅</a></div> --%>
<!-- 									<div class="comSite298 comSite298b4"> -->
<!-- 										<div class="comSite299 pr"> -->
<!-- 											<span class="comSt281 comSt281b4"></span> -->
<%-- 											<input name="artistdata" type="checkbox" class="comSt282 comSt282b4" isclick="false" checked="false" value="${item.id}"> --%>
<!-- 										</div> -->
<!-- 									</div> -->
<!-- 									<span class="comSt284 comSt284b comSt284b3"></span> -->
<!-- 								</li> -->
<%-- 								<c:if test="${(status.index + 1) % 5 == 0}"> --%>
<!-- 									</ol> -->
<!-- 									<ol class="comSite294 cf"> -->
<%-- 								</c:if> --%>
<%-- 								<c:if test="${ status.count == fn:length(list4) }"> --%>
<!-- 									</ol> -->
<%-- 								</c:if> --%>
<%-- 							</c:forEach> --%>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<jsp:include page="/jsp/userCenter/footer.jsp"></jsp:include>
<script>
$(function(){
	
	$(function(){
	 	var currentPage = 1;//当前页
	 	var more = true;//是否还有更多
		$(window).on("scroll",function(){
		if($(document).height() - $(window).scrollTop() == $(window).height()){
			if(more==true){
				$.ajax({
					type : "POST",
					url : "/kuke/userCenter/getSubscribeArtistAjax",
					dataType : "json",
					data:{
						currentPage:++currentPage
					},
					success : function(info,textStatus) {
						console.log(info);
						var dataInfo = info.data;
						if (info.flag == true) {
							more = dataInfo.more;
							$("#artistdiv").append(dataInfo.scrollInfo);
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
	});
	//初始化js
	init();
});
//初始化js
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
	var site = 1;
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
		$(this).addClass("comOn36");
		$(this).find(".comSite298a").show();
		$(this).find(".comSt284b").show();
	},function(){
		if($(this).find(".comSt282a").attr("isClick") == "false"){
			$(this).removeClass("comOn36");
			$(this).find(".comSite298a").hide();
			$(this).find(".comSt284b").hide();
		}else{
			$(this).addClass("comOn36");
			$(this).find(".comSite298a").show();
			$(this).find(".comSt284b").show();
		}
	});
	//作曲家列表鼠标悬停
	$(".comSt282b1").attr("isClick","false");
	$(".comSt282b1").prop("checked",false);
	$(".comSt219c").prop("checked",false);
	$(".comSite295b1").hover(function(){
		$(this).addClass("comOn36");
		$(this).find(".comSite298b1").show();
		$(this).find(".comSt284b1").show();
		$(this).find(".comSite297").show();
	},function(){
// 		if($(this).find(".comSt282b1").attr("isClick") == "false"){
			$(this).removeClass("comOn36");
			$(this).find(".comSite298b1").hide();
			$(this).find(".comSt284b1").hide();
			$(this).find(".comSite297").hide();
// 		}else{
// 			$(this).addClass("comOn36");
// 			$(this).find(".comSite298b1").show();
// 			$(this).find(".comSt284b1").show();
// 			$(this).find(".comSite297").show();
// 		}
	});
	//演奏家列表鼠标悬停
	$(".comSt282b2").attr("isClick","false");
	$(".comSt282b2").prop("checked",false);
	$(".comSt219c").prop("checked",false);
	$(".comSite295b2").hover(function(){
		$(this).addClass("comOn36");
		$(this).find(".comSite298b2").show();
		$(this).find(".comSt284b2").show();
	},function(){
		if($(this).find(".comSt282b2").attr("isClick") == "false"){
			$(this).removeClass("comOn36");
			$(this).find(".comSite298b2").hide();
			$(this).find(".comSt284b2").hide();
		}else{
			$(this).addClass("comOn36");
			$(this).find(".comSite298b2").show();
			$(this).find(".comSt284b2").show();
		}
	});
	//指挥家列表鼠标悬停
	$(".comSt282b3").attr("isClick","false");
	$(".comSt282b3").prop("checked",false);
	$(".comSt219c").prop("checked",false);
	$(".comSite295b3").hover(function(){
		$(this).addClass("comOn36");
		$(this).find(".comSite298b3").show();
		$(this).find(".comSt284b3").show();
	},function(){
		if($(this).find(".comSt282b3").attr("isClick") == "false"){
			$(this).removeClass("comOn36");
			$(this).find(".comSite298b3").hide();
			$(this).find(".comSt284b3").hide();
		}else{
			$(this).addClass("comOn36");
			$(this).find(".comSite298b3").show();
			$(this).find(".comSt284b3").show();
		}
	});
	//乐团列表鼠标悬停
	$(".comSt282b4").attr("isClick","false");
	$(".comSt282b4").prop("checked",false);
	$(".comSt219c").prop("checked",false);
	$(".comSite295b4").hover(function(){
		$(this).addClass("comOn36");
		$(this).find(".comSite298b4").show();
		$(this).find(".comSt284b4").show();
	},function(){
		if($(this).find(".comSt282b4").attr("isClick") == "false"){
			$(this).removeClass("comOn36");
			$(this).find(".comSite298b4").hide();
			$(this).find(".comSt284b4").hide();
		}else{
			$(this).addClass("comOn36");
			$(this).find(".comSite298b4").show();
			$(this).find(".comSt284b4").show();
		}
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
	// 艺术家全选
	$(".comSt219c").on("click",function(){
		var flag = true;
		//对勾
		if($(this).prop("checked") == true){
			$(this).parent().find(".comSt218c").addClass("comOn24");
		}else{
			$(this).parent().find(".comSt218c").removeClass("comOn24");
			flag = false;
		}
		var i = 1;
		if(flag){
 			$(".comSt281b"+i).addClass("comOn37");
 			$(".comSt282b"+i).prop("checked",true).attr("isclick","true");
 			$(".comSite295b"+i).addClass("comOn36");
 			$(".comSite298b"+i).show();
 			$(".comSt284b"+i).show();
		}else{
 			$(".comSt281b"+i).removeClass("comOn37");
 			$(".comSt282b"+i).prop("checked",false).attr("isclick","false");
 			$(".comSite295b"+i).removeClass("comOn36");
 			$(".comSite298b"+i).hide();
 			$(".comSt284b"+i).hide();
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
	//作曲家单选
	$(".comSt282b1").on("click",function(){
		if($(this).prop("checked") == true){
			$(this).attr("isClick","true");
			$(this).parent().find(".comSt281b1").addClass("comOn37");
		}else{
			$(this).attr("isClick","false");
			$(this).parent().find(".comSt281b1").removeClass("comOn37");
		}
	});
	//演奏家单选
	$(".comSt282b2").on("click",function(){
		if($(this).prop("checked") == true){
			$(this).attr("isClick","true");
			$(this).parent().find(".comSt281b2").addClass("comOn37");
		}else{
			$(this).attr("isClick","false");
			$(this).parent().find(".comSt281b2").removeClass("comOn37");
		}
	});
	//指挥家单选
	$(".comSt282b3").on("click",function(){
		if($(this).prop("checked") == true){
			$(this).attr("isClick","true");
			$(this).parent().find(".comSt281b3").addClass("comOn37");
		}else{
			$(this).attr("isClick","false");
			$(this).parent().find(".comSt281b3").removeClass("comOn37");
		}
	});
	//乐团单选
	$(".comSt282b4").on("click",function(){
		if($(this).prop("checked") == true){
			$(this).attr("isClick","true");
			$(this).parent().find(".comSt281b4").addClass("comOn37");
		}else{
			$(this).attr("isClick","false");
			$(this).parent().find(".comSt281b4").removeClass("comOn37");
		}
	});
}
</script>
</body>
</html>