<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!-- 个人设置头部 -->
<div class="comSite195">
	<div class="comSite196 comWd01">
		<div class="comSite cf pr">
			<div class="comSite197"><a href="javascript:void(0)" onclick="goToIndex();"><img src="/images/logo.png" alt="" width="87" height="27"></a></div>
			<div class="comSite198 cf">
				<a href="/kuke/userCenter/vipCenter" class="comSt203">库客会员</a>
				<div class="comSite200 pr">
					<div class="comSite cf">
						<div class="comSite201"><img <c:if test="${empty uphoto}">src="/images/200-200img.gif"</c:if> <c:if test="${!empty uphoto}">src="${uphoto}"</c:if> alt="" width="24" height="24"></div>
						<p class="comSt202">${username}</p>
					</div>
					<div class="comSite202 cf">
						<a href="/kuke/userCenter/getFavoriteTrack">我的</a>
						<a href="/kuke/userCenter/userInf">个人设置</a>
						<a href="/kuke/userCenter/userAccount">账户设置</a>
						<a href="/kuke/userCenter/vipCenter">会员中心</a>
						<a href="javascript:void(0)" onclick="logout();">退出</a>
					</div>
				</div>
				<div class="comSite199 pr"><a href="/kuke/userCenter/getSysMessageList" class="comSt168">${syscount}</a></div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	$(function(){
		// 头部下拉菜单
		$(".comSite200").hover(function(){
			$(this).find(".comSite202").show();
		},function(){
			$(this).find(".comSite202").hide();
		});
	});
</script>