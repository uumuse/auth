<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html >
<html>
<head>
<%@include file="/jsp/common/CommonMeta.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>个人支付</title>
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta name="renderer" content="webkit">
<meta name="keywords" content="">
<meta name="description" content="">
<link href="/css/init.css" rel="stylesheet">
<link href="/css/main.css" rel="stylesheet">
<script type="text/javascript" src="/js/jquery-1.12.4.min.js"></script>
<script type="text/javascript" src="/js/payment/payment.js"></script>
</head>
<body>
<%@ include file="/jsp/userCenter/userLoginHead.jsp" %>
<input type="hidden" id="keyword" name="keyword" value="${map.keyword}">
<input type="hidden" id="md5str" name="md5str" value="${map.md5Str}">
<input type="hidden" id="balance" name="balance" value="${map.balance}">
<input type="hidden" id="orgbalance" name="orgbalance" value="${map.orgbalance}">
<input type="hidden" id="cost_price" name="cost_price" value="${map.cost_price}">
<input type="hidden" id="bill_type" name="bill_type" value="${map.bill_type}">
<div class="comSite422">
	<div class="comSite comWd01">
		<div class="comSite423"><img src="/images/comSite423.png" alt="" width="560" height="28"></div>
		<div class="comSite424">
			<div class="comSite425">
				<div class="comSite426">
					<p class="comSt326">订单提交成功，现在只差最后一步啦！</p>
					<p class="comSt327">请您在提交订单后1小时内完成支付，否则订单会自动取消！</p>
				</div>
				<div class="comSite427 pr">
					<p class="comSt328">${map.itemname}</p>
					<p class="comSt329">订单号：<span>${map.keyword}</span></p>
					<a href="/kuke/userCenter/userBill" class="comSt333">我的订单</a>
				</div>
			</div>
		</div>
		<div class="comSite428 comSite204a">
			<div class="comSite429">
				<div class="comSite430" >
					<p class="comSt330">支付金额：<span>${map.cost_price}</span></p>
					<div class="comSite431">
						<ol class="cf">
							<li>
								<div class="comSite432 cf">
									<div class="comSite433 pr">
										<span></span>
										<input type="checkbox" name="remainFlag" value="1">
									</div>
									<p class="comSt331">账户余额支付</p>
								</div>
							</li>
							<li>
								<p class="comSt332">可支付余额：<span>${map.balance}</span>元</p>
							</li>
							<li><a href="/kuke/userCenter/userAccount?position=1" class="comSt334">去充值</a></li>
						</ol>
					</div>
					<c:if test="${map.diff_price gt 0.0 }">
						<p class="comSt335">用账户余额付款<span>${map.balance}</span>元，剩余<span>${map.diff_price}</span>元可选择其他方式付款</p>
					</c:if>
				</div>
				<div class="comSite393 cf">
					<span class="comSt309">支付方式：</span>
					<div class="comSite394">
						<div class="comSite395"><h3>第三方支付</h3></div>
						<div class="comSite396 comSite396a">
							<ol class="cf">
								<li class="pr">
									<div><img src="/images/pay01.jpg" alt=""></div>
									<div class="comSite397">
										<div class="comSite398 comSite398a pr">
											<span></span>
											<input type="radio" name="pay" value="alipay">
										</div>
									</div>
								</li>
								<li class="pr">
									<div><img src="/images/pay02.jpg" alt=""></div>
									<div class="comSite397">
										<div class="comSite398 comSite398a pr">
											<span></span>
											<input type="radio" name="pay" value="wechat">
										</div>
									</div>
								</li>
								<li class="pr comOn50">
									<div><img src="/images/pay03.jpg" alt=""></div>
									<div class="comSite397">
										<div class="comSite398 comSite398a pr">
											<span></span>
											<input type="radio" name="pay" value="sdo">
										</div>
									</div>
								</li>
							</ol>
						</div>
						<div class="comSite399">
							<span class="comOn52">网上银行支付</span>
						</div>
						<%@ include file='commonBank.jsp' %>
						<div class="comSite402"><a href="javascript:void(0)" onclick="gotoPay('per');" class="comSt310">去支付</a></div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<jsp:include page="/jsp/userCenter/footer.jsp"></jsp:include>
<div class="comTc comTc08"><!-- 支付页下载弹层 -->
	<div class="comTcTop pr"><h1>下载</h1><a href="javascript:void(0)" title="关闭" class="comTcSt01"></a></div>
	<div class="comTcCon">
		<div class="comSite365">
			<div class="comSite366 cf">
				<div class="comSite367"><img src="/images/comSite16.jpg" width="60" height="60"></div>
				<div class="comSite368">
					<p class="comSite369">贝多芬交响曲贝多芬交响曲贝多芬交响曲贝多芬交响曲贝多芬交响曲贝多芬交响曲贝多芬交响曲贝多芬交响曲贝多芬交响曲贝多芬交响曲贝多芬交响曲贝芬交响曲贝多芬交响曲</p>
				</div>
			</div>
			<div class="comSite370">
				<p class="comSite371">下载需要：<span>10元</span></p>
				<p class="comSite372">（支付成功后48小时内可重复下载）</p>
				<a href="#" class="comSt303 comSt304">立即支付</a>
			</div>
		</div>
	</div>
</div>
<div class="comTc comTc09"><!-- 完成支付弹层 -->
	<div class="comTcTop pr"><h1>提示</h1><a href="javascript:void(0)" title="关闭" class="comTcSt01" onclick="finishPay();"></a></div>
	<div class="comTcCon">
		<div class="comSite436"><p>请在打开的页面中进行支付，支付完成前请不要关闭该窗口</p></div>
		<div class="comSite437 cf">
			<a href="javascript:void(0)" class="comSt338" onclick="finishPay();">已完成支付</a>
			<a href="javascript:void(0)" class="comSt339" onclick="Repayment();">付款遇到问题，重新支付</a>
		</div>
	</div>
</div>
<script>
$(function(){
	
	$(".comSite430").show();
	
	// 头部下拉菜单
	$(".comSite200").hover(function(){
		$(this).find(".comSite202").show();
	},function(){
		$(this).find(".comSite202").hide();
	});
	// 第三方支付
	$(".comSite396 li").on("click",function(){
		var i = $(".comSite396 li").index(this);
		$(".comSite397").hide().eq(i).show();
		$(".comSite397 input").prop("checked",false).eq(i).prop("checked",true);
		$(".comSite396 li").removeClass("comOn51").eq(i).addClass("comOn51");
	});
	// 银行模块显示隐藏
	$(".comSite399 span").on("click",function(){
		$(this).toggleClass("comOn52");
		$(".comSite396b").toggle();
	});
	// 余额支付选中切换
	$(".comSite433 input").on("click",function(){
		$(this).parent().find("span").toggleClass("comOn53");
	});
});
</script>
</body>
</html>