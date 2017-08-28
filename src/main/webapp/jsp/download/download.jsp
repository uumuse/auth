<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>下载</title>
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta name="renderer" content="webkit">
<meta name="keywords" content="">
<meta name="description" content="">
<link href="/css/init.css" rel="stylesheet">
<link href="/css/main.css" rel="stylesheet">
<link href="/css/prcProgress-default.css" rel="stylesheet">
<script src="/js/jquery-1.12.4.min.js"></script>
<script src="/js/common/util.js"></script> 
<script src="/js/jquery.qrcode.min.js"></script> 
<script src="/js/download/prcProgress.js"></script> 
<script type="text/javascript">  
    $(function(){  
        $("#test").prcProgress({data:{"categoryName":"编制状态","percent":0.34,"categoryInnerText":"发放冻结"}});  
        //可取data对象的任意属性  
        $("#test2").prcProgress({data:{"percent":"15.3242%",categoryName:"橙色",categoryInnerText:"橙色",aaa:"哈哈哈哈"},prcBarBackColor:"#3399CC",onClick:function(a){  
            alert(a.aaa);  
        }});  
        $("#test3").prcProgress({data:{"percent":"0.234",categoryName:"橙色",categoryInnerText:"橙色"},prcBarBackColor:"#3399CC",onClick:function(a){  
            alert(a.categoryName);  
        }});  
        $("#test4").prcProgress({data:{"percent":0.612,categoryName:"橙色",categoryInnerText:"橙色"},prcBarBackColor:"#3399CC",onClick:function(a){  
            alert(a.categoryName);  
        }});  
        $("#test5").prcProgress({data:{"percent":0.22,categoryName:"橙色",categoryInnerText:"橙色"},prcBarDefClass:"red",onClick:function(a){  
            alert(a.categoryName);  
        }});  
        $("#test6").prcProgress({data:{"percent":0.22,categoryName:"橙色",categoryInnerText:"橙色"},prcBarDefClass:"green",onClick:function(a){  
            alert(a.categoryName);  
        }});  
        $("#test7").prcProgress({data:{"percent":0.6532,categoryName:"橙色",categoryInnerText:"橙色"},prcBarDefClass:"black",onClick:function(a){  
            alert(a.categoryName);  
        }});  
});  
var i=0;
function show(i){
	 $("#test").prcProgress({data:{"categoryName":"编制状态","percent":i/100,"categoryInnerText":"发放冻结"}});  
	}
function play_num(i){
		if(i<100){
				show(i);
				i++;
				setTimeout("play_num(" + i + ")", 20);
			}
	}
play_num(0);


</script>  
</head>
<html>
<c:forEach items="${cataList}" var="cata" varStatus="i">
	 <div id="cata_${cata.itemcode }"></div>  
	
</c:forEach>
    <div id="test2"></div>  
    <div id="test3"></div>  
    <div id="test4"></div>  
    <div id="test5"></div>  
    <div id="test6"></div>  
</html>
