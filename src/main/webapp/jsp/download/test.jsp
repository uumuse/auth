<%-- <%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%> --%>
<%-- <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>  --%>
<%-- <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<script src="/js/jquery-1.12.4.min.js"></script>
<head>
    <title>HTTP文件下载控件演示页面</title>
<script>
$.ajax({
	//url:"/kuke/play/getAuthDownloadUrl",
	url:"/kuke/play/getAuthPlayUrl",
	type:"GET",
	dataType:"jsonp",
	jsonp: "jsoncallback",
	async:false,
	data:{
		lcode:"QN0730_001",
		type:"1"
	},
	success:function(info){
		debugger;
		if(info.flag!=true){
			alert('您尚未购买该资源');
		}else{
			console.log(info);
			//alert(info.data.url);
			//window.open(info.data.url);
// 			  var progressBar = document.getElementById("test");
			  var progressBar = $("#test");
			  var client = new XMLHttpRequest();
			  client.withCredentials = true;
			// client.open("GET", "magical-unicorns")
			// client.open("GET", "http://music.kuke.com/1483439808484/7c3b9d81959a1baa97e8e77d115e92fe/mp3/320kbps/DHM/G0100017182922/G0100017182922_001_001_full_wm_320.mp3")
			 client.open("PUT",info.data.url,true);
			 client.setRequestHeader("Access-Control-Allow-Origin", "kuke.com"); 
			debugger;
			 client.onprogress = function(pe) {
			    if(pe.lengthComputable) {
			      progressBar.max = pe.total;
			      progressBar.value = pe.loaded;
			    }
			  }
			  client.onloadend = function(pe) {
			    progressBar.value = pe.loaded;
			  }
			  client.send();
		}
	},
	error:function(a,b){
		console.log(a);
		console.log(b);
		alert('fail');
	}
});
</script>
</head>
<html>
<progress id="test" value="" max="">
<progress id="pro">
	<span id="objprogress">85</span>%
</progress>
<%-- <c:forEach> --%>
<%-- 	<c:forEach items="${cataList}" var="cata" varStatus="i"> --%>
<%-- 	 	<progress id="p_${cata.lcode }"></progress> --%>
<%-- 	</c:forEach> --%>
<%-- </c:forEach> --%>


</html>