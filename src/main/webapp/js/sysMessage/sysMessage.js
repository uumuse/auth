//显示删除提示
function showDel(type,sysid){
	
	//显示弹层
	$(".comTcBg").show();
	$("#userMessageTip").show();
	//赋值点击事件
	if(type == "1"){//删除单一的
		$("#confirmd").attr("onclick","delSysMessage('"+sysid+"')");
	}else{//删除多个的
		$("#confirmd").attr("onclick","delDlSysMessage()");
	}
}
//取消删除
function cancleDel(){
	$(".comTcBg").hide();
	$(".comTc02").hide();
}
//删除单一数据
function delSysMessage(sysid){
	if(sysid == null || sysid != ""){
		tips("您未选中任何资源,请选择");
	}
	toDel(sysid);
}
//全选删除
function delDlSysMessage(){
//	debugger;
	var sysids = $("input:checkbox[name='sysid']:checked").map(function(index,elem) {
					return $(elem).val();
			   }).get().join(',');
	if(sysids == ""){
		tips("您未选中任何资源,请选择");
		return;
	}else{
		toDel(sysids);
	}
}
//执行删除
function toDel(id){
	$.ajax({
		url:"/kuke/userCenter/delSysMessage",
		type:"post",
		dataType:"json",
		async:false,
		data:{id:id},
		success:function(data){
			if(data.flag == true){
				$(".comTc02").hide();
				$(".comNotice05").show();
				setTimeout(function(){
					//隐藏提示框
					$(".comNotice05").hide();
					//刷新
					window.location.href = '/kuke/userCenter/getSysMessageList';
				},2000);
			}else{
				if(data.code == "NOCHOOSE"){
					tips("您为选中任何数据");
				}else{
					tips(data.msg);
				}
			}
		}
	});
}