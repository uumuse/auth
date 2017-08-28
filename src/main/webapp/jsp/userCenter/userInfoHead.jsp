<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="comSite233">
	<div class="comSite234 comWd02">
		<div class="comSite235 cf">
			<div class="comSite236 pr">
				<div><img <c:if test="${empty uphoto}">src="/images/default_headimg_b.jpg"</c:if> <c:if test="${!empty uphoto}">src="${uphoto}"</c:if> alt="" width="160" height="160"></div>
				<span class="comSt211" onclick="window.location.href = '/kuke/userCenter/userInf?site=1'"></span>
			</div>
			<div class="comSite237">
				<div class="comSite238 c">
					<input type="text" class="comSt212" disabled="disabled" value="${username}">
					<span class="comSt213" onclick="window.location.href = '/kuke/userCenter/userInf'">编辑个人资料</span>
				</div>
				<div class="comSite239 cf">
					<span class="comSt214">所属机构：<c:if test="${empty orgname}">个人</c:if> <c:if test="${!empty orgname}">${orgname}</c:if></span>
					<span class="comSt215">|</span>
					<span class="comSt214">服务有效期：${validdate}</span>
					<span class="comSt215">|</span>
					<a href="/kuke/userCenter/vipCenter" class="comSt216">库客会员</a>
					<a href="/kuke/userCenter/vipProInfo" class="comSt217">续费</a>
				</div>
			</div>
		</div>
	</div>
</div>