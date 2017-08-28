$(function(){
	var bill_type = $("#bill_type").val();
	if(bill_type == "0"){
		$(".comSite430").hide();
	}
});
//去支付按钮
function gotoPay(type){
	var keyword = $("#keyword").val();
	$.ajax({
		url:"/kuke/userCenter/queryLoseBill",
		type:"post",
		data:{KeyWord:keyword},
		dataType:"json",
		async:false,
		success:function(data){
			if(data.flag == true){
				tips("订单已过期!");
				window.location.href = '/kuke/userCenter/userBill';
			}else{
				var pay_type = $("input[name='pay']:checked").val();
				var md5str = $("#md5str").val();
				var checked = $("input[name='remainFlag']:checked").val();
				var balance = $("#balance").val();
				var orgbalance = $("#orgbalance").val();
				var cost_price = $("#cost_price").val();
				if (typeof(checked) == "undefined") { 
					checked = "";
				}   
				if (typeof(pay_type) == "undefined") { 
					pay_type = "";
				} 
				if(pay_type == "" && checked != "1" && type == "per"){
					tips("请选择账户余额或第三方支付");
					return;
				}
				if(type == "per"){//个人
					if(pay_type == "" && (parseFloat(balance) < parseFloat(cost_price))){//第三方为空
						tips("余额不足,请选择第三方支付");
						return;
					}
				}else if(type == "org"){//机构用户
					if(checked == "1"){
						if(pay_type == "" && ((parseFloat(balance)+parseFloat(orgbalance)) < parseFloat(cost_price))){//账户个人余额+账户机构余额+机构余额
							tips("余额不足,请选择第三方支付");
							return;
						}
					}else{
						if(pay_type == "" && ((parseFloat(orgbalance)) < parseFloat(cost_price))){//机构余额
							tips("余额不足,请选择个人余额或第三方支付");
							return;
						}
					}
				}
				window.open("/kuke/payment/pay?md5str="+md5str+"&keyword="+keyword+"&pay_type="+pay_type+"&check="+checked);
				//显示弹层
				$(".comTcBg").show();
				$(".comTc09").show();
			}
		}
	});
}
//重新支付
function Repayment(){
	var keyword = $("#keyword").val();
	window.location.href = '/kuke/payment/choosePayType?keyword='+keyword;
}
//点击已完成支付:只是点击效果
function finishPay(){
	window.location.href = '/kuke/userCenter/userBill';
}

