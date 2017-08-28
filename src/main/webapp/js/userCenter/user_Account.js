//显示充值
function showCharge(){
	$(".comSite417").hide();
	$(".comSite").show();
}
//取消选中固定金额
function cancleM(){
	$(".comSite392").each(function(){
		$(this).find("span").removeClass("comOn49");
		$(this).find("input").attr("checked",false);
	});
}
//充值
function gotoCharge(){
	var pay_price = "";
	var m = $("input[name='m']:checked").val();//选择的
	var pm = $("input[name='personalm']").val();//个人的
	if (typeof(m) == "undefined") { 
		m = "";
	} 
	if(m == "" && pm == ""){
		tips("请输入充值金额");
		return;
	}else if(m != ""){
		pay_price = m;
	}else if(pm != ""){
		if(!isPInt(pm)){
			tips("请输入正整数金额");
			return;
		}
		pay_price = pm;
	}
	var pay_type = $("input[name='pay']:checked").val();
	if (typeof(pay_type) == "undefined") { 
		pay_type = "";
	} 
	if(pay_type == ""){
		tips("请选择支付方式");
		return;
	}
	window.open('/kuke/payment/chargePay?pay_type='+pay_type+"&pay_price="+pay_price);
	
	//显示弹层
	$(".comTcBg").show();
	$(".comTc09").show();
}
//删除订单
function deletePayBill(keyword){
	confirms("确定要删除该订单吗?","doDeletePayBill('"+keyword+"')");
}
//执行删除订单
function doDeletePayBill(keyword){
	if(keyword != ""){
		$.ajax({
			url:"/kuke/userCenter/deleteBill",
			type:"post",
			data:{keyword:keyword},
			dataType:"json",
			async:false,
			success:function(data){
//				debugger;
				if(data.flag == true){
					//alert("删除成功");
					window.location.href = '/kuke/userCenter/userBill';
				}else{
					tips("删除失败,请刷新后重试");
				}
			}
		});
	}else{
		console.log("keyword为空");
	}
}
//点击付款
function topay(keyword){
	if(keyword != ""){
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
					window.location.href = '/kuke/payment/choosePayType?keyword='+keyword;
				}
			}
		});
	}else{
		console.log("keyword为空");
	}
}
//取消订单
function toCanclePayBill(keyword){
	confirms("确定要取消该订单吗?","doToCanclePayBill('"+keyword+"')");
}
//执行取消订单
function doToCanclePayBill(keyword){
	if(keyword != ""){
		$.ajax({
			url:"/kuke/userCenter/cancleBill",
			type:"post",
			data:{keyWord:keyword},
			dataType:"json",
			async:false,
			success:function(data){
				if(data.flag == true){
					//alert("删除成功");
					window.location.href = '/kuke/userCenter/userBill';
				}else{
					if(data.code == "2"){
						tips("订单已支付成功,不能取消");
					}else{
						tips("删除失败,请刷新后重试");
					}
				}
			}
		});
	}else{
		console.log("keyword为空");
	}
}
//下载
function preDownload(priceid,sourceid,obj,url){
	var stype = "";
	if(priceid == "511"){//单曲
		stype = "1";
	}else if(priceid == "510"){//专辑
		stype = "2";
		window.location.href = url;
		return;
	}else if(priceid == "509"){//乐谱
		stype = "4";
	}else if(priceid == "513"){//有声读物单曲
		stype = "3";
	}else if(priceid == "512"){//有声读物
		stype = "5";
	}
	var name = $(obj).attr("item_name");
	download(stype,sourceid,'',name,'bill');
}
//重新支付:我的账户界面
function repayment(){
	window.location.href = '/kuke/userCenter/userAccount?position=1';
}
//点击已完成支付:只是点击效果,我的账户界面
function finishPay(){
	window.location.href = '/kuke/userCenter/userBill';
}
//判断是否为正整数
function isPInt(str){
    var g = /^[1-9]*[1-9][0-9]*$/;
    return g.test(str);
}