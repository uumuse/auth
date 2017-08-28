<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://auth.kuke.com/taglib" prefix="kuke" %>
<!DOCTYPE html >
<html>
<head>
<%@include file="/jsp/common/CommonMeta.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>我的消息</title>
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta name="renderer" content="webkit">
<meta name="keywords" content="">
<meta name="description" content="">
<link href="/css/init.css" rel="stylesheet">
<link href="/css/main.css" rel="stylesheet">
<script type="text/javascript" src="/js/jquery-1.12.4.min.js"></script>
<script type="text/javascript" src="/js/sysMessage/sysMessage.js"></script>
<script type="text/javascript" src="/js/common/util.js"></script>
</head>
<body>
<%@ include file="/jsp/userCenter/userLoginHead.jsp" %>
<div class="comSite289">
	<div class="comSite279 comWd01">
		<div class="comSite280">
			<div class="comSite281">
				<h1>我的消息</h1>
			</div>
			<c:if test="${count != 0}">
				<div class="comSite282 pr">
					<span class="comSt271 comSt271a">全选</span>
					<input type="checkbox" class="comSt272 comSt272a">
					<span class="comSt402" title="删除" onclick="showDel('2','');"></span>
				</div>
			</c:if>
			<form id="form" action="/kuke/userCenter/getSysMessageList" method="post">
			<div class="comSite283">
				<c:if test="${count != 0}">
						<ol>
						<c:forEach items="${sysMessageList}" var="item" varStatus="status">
							<li class="comSite284 cf">
							<div class="comSite285">
								<span class="comSt273"><fmt:formatDate value="${item.send_date}" pattern="yyyy-MM-dd"/></span>
								<span class="comSt274" title="删除" onclick="showDel('1','${item.id}');"></span>
							</div>
							<div class="comSite286 cf">
								<div class="comSite287 pr"><span class="comSt275 comSt275a"></span><input name="sysid" type="checkbox" class="comSt276 comSt276a" value="${item.id}"></div>
								<div class="comSite288">
									<h2><a onclick="toSingle('${item.id}');" href="javascript:void(0);">${(item.title == "" || item.title == null)?"系统通知":item.title}</a></h2>
									<p class="eps"><a onclick="toSingle('${item.id}');" href="javascript:void(0);">${item.contents}</a></p>
								</div>
							</div>
							</li>
						</c:forEach>
					</ol>
				</c:if>
				<c:if test="${count == 0}">
					<p class="noInfo">—— 暂无内容 ——</p>
				</c:if>
			</div>
			</form>
		</div>
	</div>
</div>
<c:if test="${count != 0}">
	<kuke:page action="/kuke/userCenter/getSysMessageList" page="${pageInfo }" formId="form"/>
</c:if>
<jsp:include page="/jsp/userCenter/footer.jsp"></jsp:include>
<div class="comSite226 comNotice05">
	<p class="comSt201">已删除</p>
</div>
<div id="userMessageTip" class="comTc comTc02">
	<div class="comTcTop pr"><h1>提示</h1><a href="javascript:void(0)" title="关闭" class="comTcSt01" onclick="cancleDel();"></a></div>
	<div class="comTcCon">
		<div class="comTcSite06"><p>确定要删除选中消息?</p></div>
		<div class="comTcSite05 cf comTcPd02">
			<a id="confirmd" href="javascript:void(0)" class="comTcSt06" onclick="doDel('1');">确定</a>
			<a href="javascript:void(0)" class="comTcSt07" onclick="cancleDel();">取消</a>
		</div>
	</div>
</div>
<script>
$(function(){
	// 头部下拉菜单
	$(".comSite200").hover(function(){
		$(this).find(".comSite202").show();
	},function(){
		$(this).find(".comSite202").hide();
	});
	// 全选
	$(".comSt272a").on("click",function(){
		if($(this).prop("checked") == true){
			$(this).parent().find(".comSt271a").addClass("comOn34");
			$(".comSt276a").prop("checked",true).parent().find(".comSt275").addClass("comOn35");
		}else{
			$(this).parent().find(".comSt271a").removeClass("comOn34");
			$(".comSt276a").prop("checked",false).parent().find(".comSt275").removeClass("comOn35");
		}
	});
	// 单选
	$(".comSt276a").on("click",function(){
		$(this).parent().find(".comSt275a").toggleClass("comOn35");
	});
	$(function(){//分页居中
		var ml = ($(window).width() - ($(".comSite82").width()+100)) / 2;
		$(".comSite81").css("margin-left",ml);
	});
});
function toSingle(id){//
	window.location.href = '/kuke/userCenter/getSingleMessage?id='+id;
}
</script>
</body>
</html>