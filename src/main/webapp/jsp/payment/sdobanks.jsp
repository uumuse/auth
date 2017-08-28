<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="java.util.*" %>
<%@page import="com.kuke.core.util.*" %>
<%@page import="java.text.*" %>
<%@page import="com.kuke.pay.sdo.config.*" %>
	<body onload="document.sdobankssubmit.submit()">
	<!-- document.sdobankssubmit.submit() -->
		<form name="sdobankssubmit" method="post"
			action="${paymentGateWayURL }">
            <input type="hidden" name="Amount" value="${_amount }"/>
            <input type="hidden" name="MerchantUserId" value="${_merchantUserId}"/>
            <input type="hidden" name="OrderNo" value="${_orderNo}"/>
            <input type="hidden" name="OrderTime" value="${_orderTime}"/>
            <input type="hidden" name="ProductNo" value="${_productNo}"/>
            <input type="hidden" name="ProductDesc" value="${_productDesc}"/>
            <input type="hidden" name="Remark1" value="${_remark1}"/>
            <input type="hidden" name="Remark2" value="${_remark2}"/>
            <input type="hidden" name="ProductURL" value="${_productURL}"/>
			<input type="hidden" name="BankCode" value="${_bankCode }"/>
			<input type="hidden" name="Version" value="${version }"/>
            <input type="hidden" name="MerchantNo" value="${merchantNo }"/>
            <input type="hidden" name="PayChannel" value="${payChannel }"/>
            <input type="hidden" name="PostBackURL" value="${postBackURL }"/>
            <input type="hidden" name="NotifyURL" value="${notifyURL }"/>
            <input type="hidden" name="BackURL" value="${backURL }"/>
            <input type="hidden" name="CurrencyType" value="${currencyType }"/>
            <input type="hidden" name="NotifyURLType" value="${notifyURLType }"/>
            <input type="hidden" name="SignType" value="${signType }"/>
            <input type="hidden" name="DefaultChannel" value="${defaultChannel }"/>
            <input type="hidden" name="MAC" value="${_mac }"/>
		</form>
	</body>
</html>
