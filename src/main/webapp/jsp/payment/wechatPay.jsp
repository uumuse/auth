<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html >
<html>
<head>
<%@include file="/jsp/common/CommonMeta.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>微信支付</title>
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta name="renderer" content="webkit">
<meta name="keywords" content="">
<meta name="description" content="">
<link href="/css/init.css" rel="stylesheet">
<link href="/css/main.css" rel="stylesheet">
<script type="text/javascript" src="/js/jquery-1.12.4.min.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	debugger;
	var miNum = 0;//全局变量秒数
	var urlCode = '${urlCode}';  
	var bill_type = '${bill_type}';
	var nexturl = '${nexturl}';
	console.log(urlCode != null && urlCode != "");
    if(urlCode != null && urlCode != ""){  
    	$("#wxdiv").show();
    	$("#id_wxtwoCode").attr('src',"/kuke/payment/qr_codeImg?code_url="+urlCode);  
	    //循环执行，每隔3秒钟执行一次 checkOrder
	    var jsterval = window.setInterval(function(){ 
	    	checkOrder(miNum); 
	    }, 2000); 
		function checkOrder(num){ 
			miNum = miNum + 1;
			var payBillKeyword = '${payBillKeyword}';
			$.ajax({
				url:"/kuke/notify/checkBill",
				type:"post",
				data:{payBillKeyword:payBillKeyword,num:num},
				async:true,
				dataType:"json",
				success:function(info){
					console.log(info);
					var data = info.data;
					if(data.flag == "true"){
						clearInterval(jsterval);
						clearInterval(jsterval+1);
						window.location.href = '/kuke/payment/getSuccessPay?keyword='+payBillKeyword;
					}else{
						if(num % 5 == 0 && num != 0){
							$.ajax({
								url:"/kuke/payment/getKeyWordStatus",
								type:"post",
								data:{KeyWord:payBillKeyword,payType:"wechatpay"},
								async:false,
								dataType:"json",
								success:function(info){
									console.log(info);
									if(info.flag == true){
										if(info.data == "2"){
											clearInterval(jsterval);
											window.location.href = '/kuke/payment/getSuccessPay?keyword='+payBillKeyword;
										}
									}
								}
							});
						}else{
							if(num >= 5*60){//大于10分钟,需要重新生成二维码
								clearInterval(jsterval);
								clearInterval(jsterval+1);
								$("#id_wxtwoCode").attr('src',"/kuke/payment/qr_codeImg?code_url=");
							}
						}
					} 
				}
			});
		}
    }  
});
</script>
</head>
<body>
<%@ include file="/jsp/userCenter/userLoginHead.jsp"%>
<div class="comSite203">
	<div class="comSite204 comWd01">
		<div class="comSite486">
			<div class="comSite487 cf">
				<span class="comSt353">应付金额&nbsp;<em>${ total_fee }</em>&nbsp;元</span>
				<p class="comSt354">请您及时付款～<span>订单号：<em>${ payBillKeyword }</em></span></p>
			</div>
			<div class="comSite488">
				<h1>微信支付</h1>
				<div class="comSite489 cf">
					<div class="comSite490">
						<div class="comSite491" id="wxdiv"><img id="id_wxtwoCode" src="/images/qr-zhifu.png" alt="" width="294" height="294"></div>
						<div class="comSite492">
							<p><span>请使用微信扫一扫</span><span class="comSt355">扫描二维码支付</span></p>
						</div>
					</div>
					<div class="comSite493"><img src="/images/notice-saoyisao.png" alt="" width="350"></div>
				</div>
			</div>
			<div class="comSite494 cf"><a href="${ preurl }">选择其他支付方式</a></div>
		</div>
	</div>
</div>
<%@ include file="/jsp/userCenter/footer.jsp"%>
</body>
</html>