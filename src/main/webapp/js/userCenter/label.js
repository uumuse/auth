//得到厂牌初始化数据
function getLableData(){
	window.location.href= '/kuke/userCenter/getSubscribeLabel';
}
//得到艺术家初始化数据
function getArtistData(){
	window.location.href= '/kuke/userCenter/getSubscribeArtist';
}
//取消订阅:单一厂牌
function cancleSubscribeL(type,id){
	confirms("确定要取消订阅吗?","doSubscribe('"+type+"','"+id+"')");
}
//取消订阅:单一艺术家
function cancleSubscribeA(type,id){
	confirms("确定要取消订阅吗?","doSubscribe('"+type+"','"+id+"')");
}
////取消订阅:厂牌批量
//function cancleDLSubscribeL(){
//	var sourceids = $("input:checkbox[name='labeldata']:checked").map(function(index,elem) {
//		return $(elem).val();
//	}).get().join(',');
//	if(sourceids == ""){
//		tips("请先选择数据");
//		return;
//	}
//	if(confirm("确定要取消订阅吗?")){
//	   doSubscribe('11',sourceids);
//	}
//}
//取消订阅:艺术家批量
//function cancleDLSubscribeA(){
//	var sourceids = $("input:checkbox[name='artistdata']:checked").map(function(index,elem) {
//		return $(elem).val();
//	}).get().join(',');
//	if(sourceids == ""){
//		tips("请先选择数据");
//		return;
//	}
//	if(confirm("确定要取消订阅吗?")){
//	   doSubscribe('71',sourceids);
//	}
//}
//取消订阅:执行
function doSubscribe(type,id){
//	debugger;
	if(id != ""){
		$.ajax({
			url:"/kuke/userCenter/cancleSubscribe",
			type:"post",
			dataType:"json",
			async:false,
			data:{source_id:id},//,type:type
			success:function(data){
				if(data.flag == true){
					tips("取消订阅成功");
					if(type == "71"){//yishujia
						window.location.href = '/kuke/userCenter/getSubscribeArtist';
					}else{//changpai
						window.location.href = '/kuke/userCenter/getSubscribeLabel';
					}
				}else{
					if(data.code == "NOCHOOSE"){
						tips("未选择数据,请选择");
					}else{
						tips(data.msg);
					}
				}
			}
		});
	}else{
		return;
	}
}