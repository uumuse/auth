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
				<a href="/kuke/userCenter/getSubscribeLabel" class="comSite243a3">我的订阅</a>
				<a href="/kuke/userCenter/getUserFolder" class="comSite243a4 comSite243a4-on">我的唱片夹</a>
			</div>
			<div class="comSite244 comSite244b cf">
				<a href="/kuke/userCenter/getUserFolder?foldertype=1">单曲</a>
				<a href="/kuke/userCenter/getUserFolder?foldertype=2">唱片</a>
			</div>
			<div class="comSite245">
				<div class="comSite266 comSite266a">
					<div class="comSite279"><span class="comSt258" onclick="showCreate('1');">+ 创建</span></div>
					<div id="trackdivcontent" class="comSite273">
							<c:if test="${count != 0}">
								<ol class="comSite274 comSite274a cf">
								<c:forEach  var="item" items="${trackFolder}" varStatus="status">
										<li class="comSite275 comSite275a">
											<div class="comSite276 pr">
												<div><img onerror="javascript:this.src='/images/default_zhuanji_s.jpg';" src="${item.imgurl}" alt="" width="210" height="210"></div>
												<span class="comSt53 comSt53c" onclick="toDetail('1','${item.id}');" style="cursor: pointer;"></span>
												<a href="javascript:void(0)" class="comSt252 comSt252b" onclick="toDlPlay('1','${item.id}','play');"></a>
											</div>
											<div class="comSite261 comSite261a pr">
												<p>${item.foldername}（<span>${item.countres}</span>）</p>
												<div class="comSite262 comSite262c">
													<span title="添加到播放列表" class="comSt253 comSt253c"  onclick="toDlPlay('1','${item.id}','add');"></span>
													<span title="添加到单曲夹" class="comSt254 comSt254c" onclick="addToFolder('1','${item.id}','${item.countres}');"></span>
													<span title="删除" class="comSt256 comSt256c" onclick="showDel('1','${item.id}');"></span>
												</div>
											</div>
										</li>
										<c:if test="${(status.index + 1) % 4 == 0 && (status.count != fn:length(trackFolder))}">
											</ol>
											<ol class="comSite274 comSite274a cf">
										</c:if>
								</c:forEach>
								</ol>
							</c:if>
							<c:if test="${count == 0}">
								<p class="noInfo">—— 暂无内容 ——</p>
							</c:if>
					</div>
				</div>
				<div class="comSite266 comSite266a" style="display:none;">
					<div class="comSite279"><span class="comSt258" onclick="showCreate('2');">+ 创建</span></div>
					<div class="comSite273">
<!-- 						<ol class="comSite274 comSite274a cf"> -->
<%-- 						<c:forEach  var="item" items="${CatalFolder}" varStatus="status"> --%>
<!-- 								<li class="comSite275 comSite275a"> -->
<!-- 									<div class="comSite276 pr"> -->
<%-- 										<div><img  <c:if test="${empty item.imgurl}">src="/images/comTcUploadBg.png"</c:if> <c:if test="${!empty item.imgurl}">src="${item.imgurl}"</c:if> alt="" width="210" height="210"></div> --%>
<%-- 										<span class="comSt53 comSt53c" onclick="toDetail('2','${item.id}');"></span> --%>
<!-- 										<a href="#" class="comSt252 comSt252b"></a> -->
<!-- 									</div> -->
<!-- 									<div class="comSite261 comSite261a pr"> -->
<%-- 										<p>${item.foldername}（<span>${item.countres}</span>）</p> --%>
<!-- 										<div class="comSite262 comSite262c"> -->
<!-- 											<span title="添加到播放列表" class="comSt253 comSt253c" onclick="addtoPlay();"></span> -->
<%-- 											<span title="添加到唱片夹" class="comSt254 comSt254c" onclick="addToFolder('2','${item.id}','${item.countres}');"></span> --%>
<%-- 											<span title="删除" class="comSt256 comSt256c" onclick="showDel('2','${item.id}');"></span> --%>
<!-- 										</div> -->
<!-- 									</div> -->
<!-- 								</li> -->
<%-- 								<c:if test="${(status.index + 1) % 4 == 0}"> --%>
<!-- 									</ol> -->
<!-- 									<ol class="comSite274 comSite274a cf"> -->
<%-- 								</c:if> --%>
<%-- 								<c:if test="${ status.count == fn:length(CatalFolder) }"> --%>
<!-- 										</ol> -->
<%-- 								</c:if> --%>
<%-- 						</c:forEach> --%>
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
<div class="comSite226 comNotice05">
	<p class="comSt201">已删除</p>
</div>
<div class="comSite226 comNotice06">
	<p class="comSt201">编辑成功</p>
</div>
<div class="comSite226 comNotice07">
	<p class="comSt201 comSt201a">图片大小超出限制</p>
</div>
<div class="comSite226 comNotice08">
	<p class="comSt201 comSt201a">请选择正确的图片文件</p>
</div>
<div class="comTc comTc02">
	<div class="comTcTop pr"><h1>提示</h1><a href="javascript:void(0)" title="关闭" class="comTcSt01"></a></div>
	<div class="comTcCon">
		<div class="comTcSite06"><p>确定要全部取消喜欢</p></div>
		<div class="comTcSite05 cf comTcPd02">
			<a href="javascript:void(0)" class="comTcSt06">确定</a>
			<a href="javascript:void(0)" class="comTcSt07">取消</a>
		</div>
	</div>
</div>
<script>
$(function(){
 	var currentPage = 1;//当前页
 	var more = true;//是否还有更多
	$(window).on("scroll",function(){
// 		console.log("进来了...");
// 		console.log("0:"+$(document).height());
// 		console.log("1:"+$(window).scrollTop());
// 		console.log("2:"+$(window).height());
// 		console.log("height:"+$(document).height() - $(window).scrollTop() == $(window).height());
		var num1 = Math.round($(document).height() - $(window).scrollTop());
		var num = $(window).height();
// 		console.log("num1:"+num1+"  num:"+num);
		if(num1 == num){
			console.log("more:"+more);
			if(more==true){
				$.ajax({
					type : "POST",
					url : "/kuke/userCenter/getUserFolderAjax",
					dataType : "json",
					data:{
						currentPage:++currentPage,
						foldertype:"1"
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
//初始化js
function init(){
	//初始化页面参数
	var foldertype = '${foldertype}';//夹子的类型
	if(foldertype == "2"){//唱片
		$(".comSite244b a").removeClass("comOn23").eq(1).addClass("comOn23");
		$(".comSite266a").hide().eq(1).show();
	}else{//单曲
		$(".comSite244b a").removeClass("comOn23").eq(0).addClass("comOn23");
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
		$(this).find(".comSt53c,.comSt252b,.comSite262c").css("display","block");
	},function(){
		$(this).find(".comSt53c,.comSt252b,.comSite262c").hide();
	});
	// 喜欢切换
	$(".comSt267b").on("click",function(){
		$(this).toggleClass("comOn30");
	});
	// 单曲列表鼠标悬停
	$(".comSite251b").hover(function(){
		$(this).addClass("comOn26").find(".comSt268").addClass("comOn31");
		$(this).find(".comSt232b").addClass("comOn27");
		$(this).find(".comSt227b").show();
	},function(){
		$(this).removeClass("comOn26").find(".comSt268").removeClass("comOn31");
		$(this).find(".comSt232b").removeClass("comOn27");
		$(this).find(".comSt227b").hide();
	});
	// 单曲唱片切换
	$(".comSite244b a").on("click",function(){
		var i = $(".comSite244b a").index(this);
		$(".comSite244b a").removeClass("comOn23").eq(i).addClass("comOn23");
		$(".comSite266a").hide().eq(i).show();
	});
	// 上传悬停
	$(".comTcSt09").hover(function(){
		$(this).parent().find(".comTcSt08").addClass("comOn32");
	},function(){
		$(this).parent().find(".comTcSt08").removeClass("comOn32");
	});
	// 所有带这个乐器的曲子
	$(".comTcSt13b").on("click",function(){
		$(".comTcSt12b").toggleClass("comOn33");
	});
}
</script>
</body>
</html>