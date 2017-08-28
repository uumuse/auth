<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://auth.kuke.com/taglib" prefix="kuke" %>
<!DOCTYPE html>
<html>
<head>
<%@include file="/jsp/common/CommonMeta.jsp"%>
<title>账户管理</title>
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta name="renderer" content="webkit">
<meta name="keywords" content="">
<meta name="description" content="">
<link href="/css/init.css" rel="stylesheet">
<link href="/css/main.css" rel="stylesheet">
<script type="text/javascript" src="/js/jquery-1.12.4.min.js"></script>
<script type="text/javascript" src="/js/userCenter/user_Account.js"></script>
</head>
<body>
<%@ include file="/jsp/userCenter/userLoginHead.jsp" %>
<div class="comSite203">
	<div class="comSite204 comSite204a comWd01">
		<div class="comSite383">
			<div class="comSite384"><h1>账户管理</h1></div>
			<div class="comSite385 cf">
				<a href="javascript:void(0)" class="comSite385a1">我的账户</a>
				<a href="javascript:void(0)" class="comSite385a2 comSite385a2-on">我的订单</a>
				<a href="javascript:void(0)" class="comSite385a3">服务状态</a>
				<span class="comSt306"></span>
			</div>
			<div class="comSite386">
				<div class="comSite387" style="display:none;"><!-- 我的账户 -->
					<div class="comSite" style="display: none;">
						<div class="comSite388 cf">
							<div class="comSite389 cf">
								<div class="comSite390"><input type="text"  name="personalm" placeholder="请输入其他金额" onclick="cancleM();"></div>
								<p class="comSt307">请输入此次充值的金额：</p>
							</div>
							<div class="comSite391">
								<ol class="cf">
									<li class="cf">
										<div class="comSite392 pr"><span></span><input type="radio" name="m" value="50"></div>
										<span class="comSt308"><em>50</em>元</span>
									</li>
									<li class="cf">
										<div class="comSite392 pr"><span></span><input type="radio" name="m" value="100"></div>
										<span class="comSt308"><em>100</em>元</span>
									</li>
									<li class="cf">
										<div class="comSite392 pr"><span></span><input type="radio" name="m" value="200"></div>
										<span class="comSt308"><em>200</em>元</span>
									</li>
									<li class="cf">
										<div class="comSite392 pr"><span></span><input type="radio" name="m" value="300"></div>
										<span class="comSt308"><em>300</em>元</span>
									</li>
									<li class="cf">
										<div class="comSite392 pr"><span></span><input type="radio" name="m" value="500"></div>
										<span class="comSt308"><em>500</em>元</span>
									</li>
									<li class="cf">
										<div class="comSite392 pr"><span></span><input type="radio" name="m" value="1000"></div>
										<span class="comSt308"><em>1000</em>元</span>
									</li>
								</ol>
							</div>
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
													<input type="radio" name="pay" value="wechatpay">
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
								<%@ include file='/jsp/payment/commonBank.jsp' %>
								<div class="comSite402"><a href="javascript:void(0);" class="comSt310" onclick="gotoCharge()">去支付</a></div>
							</div>
						</div>
					</div>
					<div class="comSite417" style="display:block;"><!-- 我的账户显示余额 -->
						<div class="comSite418 cf">
							<div class="comSite419"><img <c:if test="${empty uphoto}">src="/images/200-200img.gif"</c:if> <c:if test="${!empty uphoto}">src="${uphoto}"</c:if> alt="" width="50" height="50"></div>
							<p>${username}</p>
						</div>
						<div class="comSite420">
							<c:if test="${!empty userAccount}">
								<p>账户：<span>${ userAccount }</span></p>
							</c:if>
							<p>账户余额：<span>${ user.remain_money }</span>元</p>
							<c:if test="${!empty user.org_money && (user.org_money gt 0.00) }">
								<p>机构余额：<span>${ user.org_money }</span>元</p>
							</c:if>
						</div>
						<div class="comSite421"><a href="javascript:void(0);" onclick="showCharge();">充值</a></div>
					</div>
				</div>
				<div class="comSite387" style="display: block;"><!-- 我的订单-->
					<div class="comSite403">
						<div class="comSite404">
							<ol class="cf">
								<li class="comSt315">订单号</li>
								<li class="comSt316">类型</li>
								<li class="comSt317">金额</li>
								<li class="comSt318 comSt318a">全部状态</li>
							</ol>
						</div>
						<form id="form" action="/kuke/userCenter/userBill" method="post">
						<div class="comSite405">
							<c:if test="${fn:length(list) != 0}">
								<c:forEach  items="${list}" var="billMap" varStatus="status">
									<c:if test="${billMap.showable == '0'}">
										<div class="comSite406">
											<div class="comSite407 pr">
												<p><span><fmt:formatDate value="${billMap.create_date}" pattern="yyyy-MM-dd HH:mm:ss" type="date" dateStyle="long" /></span><span class="comSt319">订单号：${billMap.keyword}</span></p>
												<span class="comSt311" title="删除" onclick="deletePayBill('${billMap.keyword}')"></span>
											</div>
											<div class="comSite408 cOff05">
												<ol class="cf">
													<li class="comSt315 cf">
														<div class="comSite409">
															<div class="comSite410"><img onclick="tips('showOffMessage');" onerror="javascript:this.src='/images/default_zhuanji_s.jpg';" src="${billMap.item_image}" alt="" width="50" height="50"></div>
															<p style="overflow: hidden;height: 50px;" onclick="tips('showOffMessage');">${billMap.item_name}</p>
														</div>
													</li>
													<li class="comSt316">${billMap.pay_channel_name}</li>
													<li class="comSt317"><span>${billMap.cost_price}</span>元</li>
													<li class="comSt318">
														<span class="isOff isOff04">已下架</span>
													</li>
												</ol>
											</div>
										</div>
									</c:if>
									<c:if test="${billMap.showable != '0'}">
										<div class="comSite406">
											<div class="comSite407 pr">
												<p><span><fmt:formatDate value="${billMap.create_date}" pattern="yyyy-MM-dd HH:mm:ss" type="date" dateStyle="long" /></span><span class="comSt319">订单号：${billMap.keyword}</span></p>
												<span class="comSt311" title="删除" onclick="deletePayBill('${billMap.keyword}')"></span>
											</div>
											<div class="comSite408">
												<ol class="cf">
													<li class="comSt315 cf">
														<div class="comSite409">
															<div class="comSite410" <c:if test="${!empty billMap.item_url}">onclick="javascript:window.location.href='${wwwurl}${billMap.item_url}';" style="cursor: pointer;"</c:if>><img  onerror="javascript:this.src='/images/default_zhuanji_s.jpg';" src="${billMap.item_image}" alt="" width="50" height="50"></div>
															<p style="overflow: hidden;height: 50px;"><c:if test="${empty billMap.item_url}">${billMap.item_name}</c:if> <c:if test="${!empty billMap.item_url}"><a href="${wwwurl}${billMap.item_url}">${billMap.item_name}</a></c:if> </p>
														</div>
													</li>
													<li class="comSt316">${billMap.pay_channel_name}</li>
													<li class="comSt317"><span>${billMap.cost_price}</span>元</li>
													<li class="comSt318">
														<c:if test="${billMap.pay_status == '0'}">
															<!-- 支付订单失效 -->
															<div class="comSite411 cf" style="display:block">
																<span class="comSt312 comSt320 comSt320a1">已失效</span>
															</div>
														</c:if>
														<c:if test="${billMap.pay_status == '1'}">
															<!-- 支付未成功 -->
															<div class="comSite411 cf" style="display:block">
																<span class="comSt312">待付款</span>
																<a href="javascript:void(0);" class="comSt313" onclick="topay('${billMap.keyword}')">付款</a>
																<a href="javascript:void(0);" class="comSt313 comSt314" onclick="toCanclePayBill('${billMap.keyword}')">取消订单</a>
															</div>
														</c:if>
														<c:if test="${billMap.pay_status == '2' && billMap.bill_type == '1' && (billMap.pay_channel_id == '50' || billMap.pay_channel_id == '51' || billMap.pay_channel_id == '52')}">
															<!-- 支付成功   属于下载范围 -->
															<div class="comSite411 cf" style="display:block">
																<span class="comSt312">交易成功</span>
																<a item_name='${billMap.item_name}' href="javascript:void(0);" class="comSt313" onclick="preDownload('${billMap.pay_pro_price_id}','${billMap.item_id}',this,'${wwwurl}${billMap.item_url}');">下载</a>
															</div>
														</c:if>
														<c:if test="${billMap.pay_status == '2' && billMap.bill_type == '1' && billMap.pay_channel_id != '50' && billMap.pay_channel_id != '51' && billMap.pay_channel_id != '52'}">
															<!-- 支付成功  服务类的 -->
															<div class="comSite411 cf" style="display:block">
																<span class="comSt312 comSt320 comSt320a1">已付款</span>
															</div>
														</c:if>
														<c:if test="${billMap.pay_status == '2' && billMap.bill_type == '0'}">
															<!-- 充值成功 -->
															<div class="comSite411 cf" style="display:block">
																<span class="comSt312 comSt320 comSt320a1">交易成功</span>
															</div>
														</c:if>
														<c:if test="${billMap.pay_status == '3'}">
															<!-- 取消状态  未付款 -->
															<div class="comSite411 cf" style="display:block">
																<span class="comSt312 comSt320">未付款</span>
																<span class="comSt312 comSt320">已取消</span>
															</div>
														</c:if>
													</li>
												</ol>
											</div>
										</div>
									</c:if>
								</c:forEach>
							</c:if>
							<c:if test="${fn:length(list) == 0}">
								<p class="noInfo">—— 暂无内容 ——</p>
							</c:if>
						</div>
						</form>
						<c:if test="${site == 1}"><!-- 我的订单才加载分页 -->
							<kuke:page action="/kuke/userCenter/userBill" page="${pageInfo}" formId="form"/>
						</c:if>
					</div>
				</div>
				<div class="comSite387" style="display: none;"><!-- 我的服务 -->
					<div class="comSite412">
						<div class="comSite413">
							<ol class="cf">
								<li class="comSt323">服务名称</li>
								<li class="comSt324">到期时间</li>
								<li class="comSt325">操作</li>
							</ol>
						</div>
						<div class="comSite414">
							<div class="comSite415">
							<c:if test="${!empty info}">
								<ol class="cf">
									<li class="comSt323">${info.audio_pro_name}</li>
									<c:if test="${info.audio_flag == 'inDate' }">
										<li class="comSt324" style="display:block;">${info.audio_date}</li>
									</c:if>
									<c:if test="${info.audio_flag != 'inDate' }">
										<li class="comSt324" style="display:block;">已到期</li>
									</c:if>
									<li class="comSt325"><a href="/kuke/userCenter/vipProInfo" class="comSt322">续期</a></li>
								</ol>
							</c:if>
							<c:if test="${empty info}">
								<div class="comSite416" style="display: block;"><p>您尚未订购服务</p></div>
							</c:if>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<jsp:include page="/jsp/userCenter/footer.jsp"></jsp:include>
<div class="comTc comTc09"><!-- 完成支付弹层 -->
	<div class="comTcTop pr"><h1>提示</h1><a href="javascript:void(0)" title="关闭" class="comTcSt01" onclick="finishPay();"></a></div>
	<div class="comTcCon">
		<div class="comSite436"><p>请在打开的页面中进行支付，支付完成前请不要关闭该窗口</p></div>
		<div class="comSite437 cf">
			<a href="javascript:void(0)" class="comSt338" onclick="finishPay();">已完成支付</a>
			<a href="javascript:void(0)" class="comSt339" onclick="repayment();">付款遇到问题，重新支付</a>
		</div>
	</div>
</div>
<script>
// $(".comSite385 a").eq(i).addClass("comSite385a"+(${site}+1)+"-on");
// $(".comSite387").hide().eq(${site}).show();
$(function(){
	
	// 头部下拉菜单
	$(".comSite200").hover(function(){
		$(this).find(".comSite202").show();
	},function(){
		$(this).find(".comSite202").hide();
	});
	// 充值金额选择
	$(".comSite392 input").on("click",function(){
		var i = $(".comSite392 input").index(this);
		$(".comSite392 span").removeClass("comOn49").eq(i).addClass("comOn49");
		
		//自定义金额置空
		$("input[name='personalm']").val("");
	});
	// 第三方支付
	$(".comSite396 li").on("click",function(){
		var i = $(".comSite396 li").index(this);
		$(".comSite397").hide().eq(i).show();
		$(".comSite397 input").prop("checked",false).eq(i).prop("checked",true);
		$(".comSite396 li").removeClass("comOn51").eq(i).addClass("comOn51");
	});
	// 选项卡切换
	$(".comSite385 a").on("click",function(){
			var i = $(".comSite385 a").index(this);
// 			for(j=0;j<$(".comSite385 a").size();j++){
// 				$(".comSite385 a").removeClass("comSite385a"+(j+1)+"-on");
// 			}
// 			$(".comSite385 a").eq(i).addClass("comSite385a"+(i+1)+"-on");
// 			$(".comSite387").hide().eq(i).show();
			if(i == 0){//我的账户
				window.location.href = '/kuke/userCenter/userAccount';
			}else if(i == 1){//我的订单
				window.location.href = '/kuke/userCenter/userBill';
			}else if(i == 2){//我的服务
				window.location.href = '/kuke/userCenter/userServStatus';
			}
		});
	// 银行模块显示隐藏
	$(".comSite399 span").on("click",function(){
		$(this).toggleClass("comOn52");
		$(".comSite396b").toggle();
	});
	//分页居中
	pageCenter();
});
function pageCenter(){
	//分页居中
	var ml = ($(".comSite386").width()-$(".comSite81").width()+260) / 2;
	$(".comSite81").css("margin-left",ml);
}
</script>
</body>
</html>