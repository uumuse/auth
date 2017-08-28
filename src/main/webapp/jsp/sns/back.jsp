<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<!DOCTYPE HTML>
<html>
<head>
</head>
<body>
</body>
<input type="hidden" id="bound" value="${bound}" />
<input type="hidden" id="flag" value="${flag}" />
<input type="hidden" id="boundType" value="${boundType}" />
<input type="hidden" id="boundName" value="${boundName}" />
<input type="hidden" id="boundMessage" value="${boundMessage}" />
<script type="text/javascript">
var bound = document.getElementById("bound").value;
var boundMessage = document.getElementById("boundMessage").value;
if(bound == "1"){
	var boundType = document.getElementById("boundType").value;
	var flag = document.getElementById("flag").value;
	var boundName = document.getElementById("boundName").value;
	var boundMessage = document.getElementById("boundMessage").value;
	window.opener.toBound(boundType,boundName,flag,boundMessage);
	window.close();
}else{
	document.domain = "kuke.com";
	window.opener.location.reload();
// 	console.log(window.opener.location);
// 	console.log(window.parent.location);
// 	console.log(window.location);
	window.close();
}
</script>
</html>