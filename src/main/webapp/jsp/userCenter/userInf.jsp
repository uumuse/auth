<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html >
<html>
<head>
<%@include file="/jsp/common/CommonMeta.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta name="renderer" content="webkit">
<meta name="keywords" content="">
<meta name="description" content="">
<link href="/css/init.css" rel="stylesheet">
<link href="/css/main.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="/css/imgareaselect-default.css" />
<script type="text/javascript" src="/js/jquery-1.12.4.min.js"></script>
<script type="text/javascript" src="/js/userCenter/userInf.js"></script>
<script type="text/javascript" src="/js/userCenter/data.js"></script>
<script type="text/javascript" src="/js/common/jquery.imgareaselect.pack.js"></script>
<script type="text/javascript" src="/js/common/ajaxfileupload.js"></script>
<title>个人设置</title>
</head>
<body>
<%@ include file="/jsp/userCenter/userLoginHead.jsp" %>
<div class="comSite203">
	<div class="comSite204 comWd01">
		<div class="comSite205">
			<div class="comSite210a">
				<h1 class="comSt169">个人设置</h1>
				<div class="comSite206 comSite206a cf">
					<a href="javascript:void(0)" class="comN1 comN1-on">基本资料</a>
					<a href="javascript:void(0)" class="comN2">更换头像</a>
					<a href="javascript:void(0)" class="comN3">密码管理</a>
					<a href="javascript:void(0)" class="comN4">绑定设置</a>
				</div>
			</div>
			<div class="comSite207">
				<div class="comSite208 pr">
					<div class="comSite209"><img <c:if test="${empty uphoto}">src="/images/default_headimg_b.jpg"</c:if> <c:if test="${!empty uphoto}">src="${uphoto}"</c:if> alt="" width="180" height="180" onclick="changeImg();" ></div>
					<ol class="comSite211">
						<li class="cf">
							<span class="comSt170">昵称：</span>
							<span class="comSt171"><input id="name" type="text" value="${user.nick_name}" class="comSt172"></span>
						</li>
						<li class="cf">
							<span class="comSt170">签名：</span>
							<span class="comSt171"><input id="signature" type="text" value="${user.signature}" class="comSt172"></span>
						</li>
						<li class="comPd05 cf">
							<span class="comSt170 comSt181">性别：</span>
							<span class="comSt180 pr cf">
								<em class="comSt174 comSt174-on"></em><em class="comSt179">男</em>
								<input type="radio" name="sex" class="comSt175" value="male" />
							</span>
							<span class="comSt180 pr cf">
								<em class="comSt174"></em><em class="comSt179">女</em>
								<input type="radio" name="sex" class="comSt175" value="female" />
							</span>
							<span class="comSt180 pr cf">
								<em class="comSt174"></em><em class="comSt179">保密</em>
								<input type="radio" name="sex" class="comSt175" value="secrecy" />
							</span>
						</li>
						<li class="comPd05 cf">
							<span class="comSt170">生日：</span>
							<span class="comSt176 pr">
								<em class="comSt177">请选择</em>
								<select id="year" class="comSt178" onchange="getMonth('year','month','day')">
									<option value="">请选择</option>
									<c:forEach var="i" begin="1900" end="${thisYear}" varStatus="status">
										<option>${thisYear - i + 1900}</option>
									</c:forEach>
								</select>
							</span>
							<span class="comSt186">年</span>
							<span class="comSt176 pr">
								<em class="comSt177">请选择</em>
								<select id="month" class="comSt178" onchange="getDay('year','month','day')">
								</select>
							</span>
							<span class="comSt186">月</span>
							<span class="comSt176 pr">
								<em class="comSt177">请选择</em>
								<select id="day" class="comSt178" onchange="getConstellation('month','day','constellation');">
								</select>
							</span>
							<span class="comSt186">日</span>
						</li>
						<li class="cf">
							<span class="comSt170">星座：</span>
							<span class="comSt176 pr">
								<em class="comSt177">请选择</em>
								<select id="constellation" class="comSt178" onchange="$(this).parent().find('.comSt177').html($(this).val());">
									<option value="白羊座">白羊座</option>
									<option value="金牛座">金牛座</option>
									<option value="双子座">双子座</option>
									<option value="巨蟹座">巨蟹座</option>
									<option value="狮子座">狮子座</option>
									<option value="处女座">处女座</option>
									<option value="天秤座">天秤座</option>
									<option value="天蝎座">天蝎座</option>
									<option value="射手座">射手座</option>
									<option value="摩羯座">摩羯座</option>
									<option value="水瓶座">水瓶座</option>
									<option value="双鱼座">双鱼座</option>
								</select>
							</span>
						</li>
						<li class="cf">
							<span class="comSt170">地区：</span>
							<span class="comSt176 pr">
								<em class="comSt177">请选择</em>
								<select id="province" class="comSt178"  onChange="getCity('province','city')">
									<option value="">请选择</option>
									<option value="北京市">北京市</option>
									<option value="上海市">上海市</option>
									<option value="天津市">天津市</option>
									<option value="重庆市">重庆市</option>
									<option value="河北省">河北省</option>
									<option value="山西省">山西省</option>
									<option value="内蒙古">内蒙古</option>
									<option value="辽宁省">辽宁省</option>
									<option value="吉林省">吉林省</option>
									<option value="黑龙江">黑龙江</option>
									<option value="江苏省">江苏省</option>
									<option value="浙江省">浙江省</option>
									<option value="安徽省">安徽省</option>
									<option value="福建省">福建省</option>
									<option value="江西省">江西省</option>
									<option value="山东省">山东省</option>
									<option value="河南省">河南省</option>
									<option value="湖北省">湖北省</option>
									<option value="湖南省">湖南省</option>
									<option value="广东省">广东省</option>
									<option value="广西">广西</option>
									<option value="海南省">海南省</option>
									<option value="四川省">四川省</option>
									<option value="贵州省">贵州省</option>
									<option value="云南省">云南省</option>
									<option value="西藏">西藏</option>
									<option value="陕西省">陕西省</option>
									<option value="甘肃省">甘肃省</option>
									<option value="宁夏">宁夏</option>
									<option value="青海省">青海省</option>
									<option value="新疆">新疆</option>
									<option value="香港">香港</option>
									<option value="澳门">澳门</option>
									<option value="台湾省">台湾省</option>
									<option value="其它">其它</option>
								</select>
							</span>
							<span class="comSt186">&nbsp;&nbsp;&nbsp;</span>
<!-- 							<span class="comSt186">省</span> -->
							<span class="comSt176 pr">
								<em class="comSt177">请选择</em>
								<select id="city" class="comSt178" onchange="$(this).parent().find('.comSt177').html($(this).val());">
									
								</select>
							</span>
							<span class="comSt186">&nbsp;&nbsp;&nbsp;</span>
<!-- 						</li> -->
						<li class="comPd06">
							<p class="comSt187"></p>
						</li>
						<li class="comPd06">
							<em class="comSt184" onclick="updateUserInfo();">保 存</em>
						</li>
					</ol>
				</div>
				<div class="comSite208 comSite208a" style="display:none;">
					<div class="comSite212 cf">
						<div class="comSite213 pr">
							<span class="comSt188" >上传头像</span>
							<input id="uploadUserPhoto" name="uploadUserPhoto"  type="file" class="comSt189" onchange="uploadPhoto()" />
						</div>
						<p class="comSt190">支持jpg、gif、png格式图片，文件小于5M</p>
					</div>
					<div class="comSite214 cf">
						<div class="comSite215">
							<div class="comSite222" id="uploadMsg" width="300" height="300">
								<div style="">
									<img id="photo_main" src="/images/default_headimg_b.jpg" alt="" width="300" height="300">
								</div>
							</div>
							<div class="comSite223">
								<a href="javascript:void(0)" class="comSt194" onclick="cutPhoto();">保存</a>
								<a href="javascript:void(0)" class="comSt194 comSt195" onclick="cancleChangeImg();">取消</a>
							</div>
						</div>
						<div class="comSite216">
							<h3 class="comSt196">头像预览</h3>
							<div class="comSite217 cf">
								<div class="comSite218">
									<div style="width:200px;height:200px;overflow:hidden;"><img id="crop_preview_200" <c:if test="${empty uphoto}">src="/images/default_headimg_b.jpg"</c:if> <c:if test="${!empty uphoto}">src="${uphoto}"</c:if> alt="" width="200" height="200"></div>
									<p class="comSt191">大尺寸头像：200*200像素</p>
								</div>
								<div class="comSite219">
									<div class="comSite220" style="width:50px;height:50px;overflow:hidden;"><img id="crop_preview_50"  <c:if test="${empty uphoto}">src="/images/default_headimg_s.jpg"</c:if> <c:if test="${!empty uphoto}">src="${uphoto}"</c:if>  alt="" width="50" height="50"></div>
									<p class="comSt192">中尺寸头像：50*50像素</p>
									<div class="comSite221" style="width:25px;height:25px;overflow:hidden;"><img id="crop_preview_25"  <c:if test="${empty uphoto}">src="/images/default_headimg_s.jpg"</c:if> <c:if test="${!empty uphoto}">src="${uphoto}"</c:if>  alt="" width="25" height="25"></div>
									<p class="comSt192">小尺寸头像：25*25像素</p>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="comSite208 comSite208a" style="display:none;">
					<ol class="comSite224">
						<li class="cf">
							<span class="comSt197">当前密码：</span>
							<div class="comSite225" id="comSite225c">
								<input type="password" class="comSt198" id="password" autocomplete="off" >
							</div>
						</li>
						<li>
							<p class="comSt200" id="comSt200c"></p>
						</li>
						<li class="cf">
							<span class="comSt197">新密码：</span>
							<div class="comSite225" id="comSite225a">
								<input type="password" class="comSt198" id="comSt198a" autocomplete="off" >
							</div>
						</li>
						<li>
							<p class="comSt200" id="comSt200a"></p>
						</li>
						<li class="cf">
							<span class="comSt197">确认新密码：</span>
							<div class="comSite225" id="comSite225b">
								<input type="password" class="comSt198" id="comSt198b" autocomplete="off" >
							</div>
						</li>
						<li>
							<p class="comSt200" id="comSt200b"></p>
						</li>
						<li><a href="javascript:void(0)" class="comSt199 comSt199a" onclick="changePwd();">确认</a></li>
					</ol>
				</div>
				<div class="comSite208 comSite208a" style="display:none;">
					<ol class="comSite227">
						<li class="cf">
							<span class="comSt204 comSt204a">手机</span>
							<span id="phoneinfoa" class="comSt205">已绑定：<em class="comSt205a">138****5678</em></span>
							<span id="phoneinfob" class="comSt206" onclick="unbound('phone');">解除绑定</span>
						</li>
						<li class="cf">
							<span class="comSt204 comSt204e">邮箱</span>
							<span id="emailinfoa" class="comSt205">已绑定：<em class="comSt205b">sjd****dd</em></span>
							<span id="emailinfob" class="comSt206" onclick="unbound('email');">解除绑定</span>
						</li>
						<li class="cf">
							<span class="comSt204 comSt204b">微信</span>
							<span id="wechatinfoa" class="comSt205">已绑定：<em class="comSt205c">sjd****dd</em></span>
							<span id="wechatinfob" class="comSt206" onclick="unbound('wechat');">解除绑定</span>
						</li>
						<li class="cf">
							<span class="comSt204 comSt204c">QQ</span>
							<span id="qqinfoa" class="comSt205">已绑定：<em class="comSt205d">234****67</em></span>
							<span id="qqinfob" class="comSt206" onclick="unbound('qq');">解除绑定</span>
						</li>
						<li class="cf">
							<span class="comSt204 comSt204d">微博</span>
							<span id="weiboinfoa" class="comSt205">已绑定：<em class="comSt205e">sdf@****.com</em></span>
							<span id="weiboinfob" class="comSt206" onclick="unbound('weibo');">解除绑定</span>
						</li>
					</ol>
				</div>
			</div>
		</div>
	</div>
</div>
<jsp:include page="/jsp/userCenter/footer.jsp"></jsp:include>
<div class="comSite226 comSite226a">
	<p class="comSt201">密码修改成功</p>
</div>
<div class="comSite226 comSite226b">
	<p class="comSt201">绑定成功</p>
</div>
<div class="comSite226 comSite226c">
	<p class="comSt201">已解除绑定</p>
</div>
<div class="comSite226 comSite226d">
	<p class="comSt201">邮件发送成功，请登录您的邮箱绑定</p>
</div>
<!-- 解除绑定弹层 -->
<div class="comSite228 comSite228a"></div>
<div class="comSite229 comSite229a">
	<div class="comSite230 pr"><h1>解除绑定</h1><span class="comSt207 comSt207a" title="关闭"></span></div>
	<div class="comSite231">
		<p>确定要解除<span class="comSt208">手机</span>的绑定？</p>
	</div>
	<div class="comSite232 cf">
		<a href="javascript:void(0)" class="comSt209 comSt209a">确定</a>
		<a href="javascript:void(0)" class="comSt210 comSt210a">取消</a>
	</div>
</div>
<!-- 绑定手机号 -->
<div id="phonetip" class="comWd06 comSite148">
	<div class="comSite115 pr"><h1>绑定手机号</h1><span title="关闭" onclick="closeTips('phonetip');"></span></div>
	<div class="comSite116a">
		<div class="comSite149">
			<div class="comSite152">
				<div class="comSite151" id="comSite151"><input placeholder="您的手机号码" class="comSt124" id="comSt124" type="text" onblur="checkPhone();"></div>
				<p class="comSt125" id="comSt125"></p>
			</div>
			<div class="comSite150 pr">
				<div class="comSite153 cf">
					<span id="UserBoundPhone" class="comSt126 comSt127"  onclick="getPhoneCode();">发送验证码</span>
					<div class="comSite154" id="comSite154"><input placeholder="验证码" class="comSt128" id="comSt128" type="text"></div>
				</div>
				<p class="comSt129" id="comSt129"></p>
			</div>
			<div class="comSite155"><span class="comSt130 comSt130a" onclick="boundPhone();">确定</span></div>
		</div>
	</div>
</div>
<!-- 绑定邮箱 -->
<div id="emailtip" class="comWd06 comSite148">
	<div class="comSite115 pr"><h1>绑定邮箱</h1><span title="关闭" onclick="closeTips('emailtip');"></span></div>
	<div class="comSite116a">
		<div class="comSite149">
			<div class="comSite152">
				<div class="comSite151" id="comSite151a"><input placeholder="您的邮箱" class="comSt124" id="comSt124a" type="text"></div>
				<p class="comSt125" id="comSt125a"></p>
			</div>
			<div class="comSite155"><span class="comSt130 comSt130a" onclick="boundEmail();">去验证</span></div>
		</div>
	</div>
</div>
<script>
$(function(){
	// 头部下拉菜单
	$(".comSite200").hover(function(){
		$(this).find(".comSite202").show();
	},function(){
		$(this).find(".comSite202").hide();
	});
	// 修改用户名和邮箱
	$(".comSt173").on("click",function(){
		$(this).parent().find(".comSt172").attr("disabled",false);
	});
	//性别初始化
	userInfoInit('${user.sex}','${year}','${month}','${day}','${user.constellation}','${user.province}','${user.city}','${thisYear}');
	// 性别
	$(".comSt175").on("click",function(){
		var i = $(".comSt175").index(this);
		$(".comSt174").removeClass("comSt174-on").eq(i).addClass("comSt174-on");
	});
	// 关闭弹层
	$(".comSt207a,.comSt210a").on("click",function(){
		$(".comSite228a,.comSite229a").hide();
	});
	//控制导航选项卡
	setSite('${site}');
	// 导航选项卡切换
	$(".comSite206a a").on("click",function(){
		var i = $(".comSite206a a").index(this);
		for(var j=0;j<$(".comSite206a a").length;j++){
			$(".comSite206a a").removeClass("comN"+(j+1)+"-on");
		}
		if(imageSelectObject != null){//移除imageSelectObject对象   保留图片
			imageSelectObject.cancelSelection();
		}
		if(i == 3){//个人设置初始化
			boundinit();
		}else{
			$(".comSite206a a").eq(i).addClass("comN"+(i+1)+"-on");
			$(".comSite208").hide().eq(i).show();
		}
		
	});
});
// 修改密码验证
var checkForm2 = {
	v1 : function(){
		return G.id("comSt198a").value;
	},
	v2 : function(){
		return G.id("comSt198b").value;
	},
	c1 : function(){
		if(!this.v1()){
			G.id("comSite225a").className = "comSite225 comOn16";
			G.id("comSt200a").innerHTML = "请输入新密码";
			return false;
		}else if(this.v1().length < 6 || this.v1().length > 16){
			G.id("comSite225a").className = "comSite225 comOn16";
			G.id("comSt200a").innerHTML = "密码长度为6-16位";
			return false;
		}else{
			G.id("comSite225a").className = "comSite225";
			G.id("comSt200a").innerHTML = "";
			return true;
		}
	},
	c2 : function(){
		if(!this.v2()){
			G.id("comSite225b").className = "comSite225 comOn16";
			G.id("comSt200b").innerHTML = "请再次输入新密码";
			return false;
		}else if(this.v2() != this.v1()){
			G.id("comSite225b").className = "comSite225 comOn16";
			G.id("comSt200b").innerHTML = "两次新密码输入不一致";
			return false;
		}else{
			G.id("comSite225b").className = "comSite225";
			G.id("comSt200b").innerHTML = "";
			return true;
		}
	}
}
var G = {
	id : function(id){
		return document.getElementById(id);
	}
}
G.id("comSt198a").onblur = function(){checkForm2.c1();}
G.id("comSt198b").onblur = function(){checkForm2.c2();}
</script>
</body>
</html>