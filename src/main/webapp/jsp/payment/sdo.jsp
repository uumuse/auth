<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="java.util.*" %>
<%@page import="com.kuke.core.util.*" %>
<%@page import="java.text.*" %>
<%@page import="com.kuke.pay.sdo.config.*" %>
	<body onload="document.sdosubmit.submit()">
	<!--  -->
		<form name="sdosubmit" method="post"
			action="${_url }">
            <input type="hidden" name="Amount" value="${_amount }"/>
            <input type="hidden" name="MerchantUserId" value="${_merchantUserId}"/>
            <input type="hidden" name="OrderNo" value="${_orderNo}"/>
            <input type="hidden" name="OrderTime" value="${_orderTime}"/>
            <input type="hidden" name="ProductNo" value="${_productNo}"/>
            <input type="hidden" name="ProductDesc" value="${_productDesc}"/>
            <input type="hidden" name="Remark1" value="${_remark1}"/>
            <input type="hidden" name="Remark2" value="${_remark2}"/>
            <input type="hidden" name="ProductURL" value="${_productURL}"/>
			<input type="hidden" name="BankCode" value="${_bankCode}"/>
			<input type="hidden" name="Version" value="${_version}"/>
            <input type="hidden" name="MerchantNo" value="${_merchantNo }"/>
            <input type="hidden" name="PayChannel" value="${_payChannel }"/>
            <input type="hidden" name="PostBackURL" value="${_postBackURL }"/>
            <input type="hidden" name="NotifyURL" value="${_notifyURL }"/>
            <input type="hidden" name="BackURL" value="${_backURL }"/>
            <input type="hidden" name="CurrencyType" value="${_currencyType }"/>
            <input type="hidden" name="NotifyURLType" value="${_notifyURLType }"/>
            <input type="hidden" name="SignType" value="${_signType }"/>
            <input type="hidden" name="DefaultChannel" value="${_defaultChannel }"/>
            <input type="hidden" name="MAC" value="${_mac }"/>
		</form>
	</body>
</html>
