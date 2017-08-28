<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!-- document.alipaysubmit.submit()   -->
<!DOCTYPE HTML>
<html>
   <body onload="document.alipaysubmit.submit()">
		<form name="alipaysubmit" method="post" action="${pay_gateway }">
			<input type="hidden" name="_input_charset" value="${input_charset }" />
			<input type="hidden" name="body" value="${body}" />
			<input type="hidden" name="notify_url" value="${notify_url}" />
			<input type="hidden" name="out_trade_no" value="${payBillKeyword}" />
			<input type="hidden" name="partner" value="${partner}" />
			<input type="hidden" name="payment_type" value="${payment_type}" />
			<input type="hidden" name="seller_email" value="${seller_email}" />
			<input type="hidden" name="service" value="${service}" />
			<input type="hidden" name="sign" value="${ItemUrl}" />
			<input type="hidden" name="sign_type" value="${sign_type }" />
			<input type="hidden" name="subject" value="${subject}" />
			<input type="hidden" name="total_fee" value="${total_fee}" />
			<input type="hidden" name="show_url" value="${show_url}" />
			<input type="hidden" name="return_url" value="${return_url}" />
			<input type="hidden" name="paymethod" value="${paymethod}" />
			<input type="hidden" name="defaultbank" value="${defaultbank}" />
			<input type="hidden" name="enable_paymethod" value="${enable_paymethod}" />
		</form>
	</body>
</html>
