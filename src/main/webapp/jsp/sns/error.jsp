<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="/css/404.css" rel="stylesheet" type="text/css" />
<script type='text/javascript' src="/js/common/util.js"></script>
<title>绑定失败</title>
</head>
<body>
<div class="main" >
  <div class="div_bg_right">
    <div class="fav_right_content">
    <div class="div_bg">
    	抱歉，您访问的页面异常，暂时无法访问。<span id="div_miao">5</span>秒之后自动跳转到首页 
    </div>
     </div>
    <div class="clear"></div>
  </div>
  <div class="fav_rig_menu">
  	<div class="fav_rig_menu_div"></div>
  </div>
  <div class="clear"></div>
</div>
<script type="text/javascript">
var i=0;
window.setInterval("div_miao();",1000);
function div_miao(){
	i++;
	document.getElementById("div_miao").innerHTML=5-i;
	if(i==5){
		 url_href("/kuke/home/index");
	}
}
</script>
</body>
</html>