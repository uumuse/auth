<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>唱片首页</title>
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta name="renderer" content="webkit">
<meta name="keywords" content="">
<meta name="description" content="">
<link href="css/init.css" rel="stylesheet">
<link href="css/main.css" rel="stylesheet">
<script type="text/javascript" src="js/jquery-1.12.4.min.js"></script>
<script type="text/javascript" src="js/jquery.qrcode.min.js"></script>
<script type="text/javascript" src="http://malsup.github.io/jquery.blockUI.js"></script>
<script type="text/javascript" src="js/login/login.js"></script>
<script type="text/javascript" src="js/login/regist.js"></script>
</head>
<body>
<!-- <div class="comSite comWd01"> -->
<!-- 	<div class="comSite comTop01 cf"> -->
<!-- 		<a href="#" class="comSt1 fl"><img src="images/logo.png" alt=""></a> -->
<!-- 		<div class="comSite1 cf fl"> -->
<!-- 			<input type="text" placeholder="单曲／专辑／艺术家／视频" class="comSt2 fl"> -->
<!-- 			<a href="#" title="搜索" class="comSt3 fl"></a> -->
<!-- 		</div> -->
<!-- 		<div class="comSite2 fr cf"> -->
<!-- 			<a href="#" class="comSt6 fr">库客会员</a> -->
<!-- 			<!-- 未登录 --> -->
<!-- 			<div id="notLogin" class="comSite fr comSt405"> -->
<!-- 				<a href="#" class="comSt4" onclick="showLogin()">登陆</a> -->
<!-- 				<span class="comSt5">|</span> -->
<!-- 				<a href="#" class="comSt4">注册</a> -->
<!-- 			</div> -->
<!-- 			<!-- 已登录 --> -->
<!-- 			<div id="Login" class="comSite200 pr" style="display:none"> -->
<!-- 				<div class="comSite cf"> -->
<!-- 					<div class="comSite201"><img id="userImage" alt="" width="24" height="24"></div> -->
<!-- 					<p class="comSt202" id="nickName"></p> -->
<!-- 				</div> -->
<!-- 				<div class="comSite202 cf"> -->
<!-- 					<a href="/kuke/userCenter/getFavoriteTrack">我的</a> -->
<!-- 					<a href="/kuke/userCenter/userInf">个人设置</a> -->
<!-- 					<a href="/kuke/userCenter/userAccount">账户设置</a> -->
<!-- 					<a href="/kuke/userCenter/vipCenter">会员中心</a> -->
<!-- 					<a href="javascript:void(0)" onclick="loginOut();">退出</a> -->
<!-- 				</div> -->
<!-- 			</div> -->
<!-- 			<div id="LoginMsg" class="comSite199 pr" style="display:none"><a id="sysmsgid" href="/kuke/userCenter/getSysMessageList" class="comSt168">22</a></div> -->
<!-- 		</div> -->
<!-- 	</div> -->
<!-- 	<div class="comSite comTop02 cf"> -->
<!-- 		<h1 class="comSt7 fl">库客·艺术中心</h1> -->
<!-- 		<div class="comSite3 fl"> -->
<!-- 			<ol class="cf"> -->
<!-- 				<li><a href="#" class="comOn01">唱片</a></li> -->
<!-- 				<li><a href="#">视频</a></li> -->
<!-- 				<li><a href="#">剧院</a></li> -->
<!-- 				<li><a href="#">有声读物</a></li> -->
<!-- 				<li><a href="#">直播</a></li> -->
<!-- 				<li><a href="#">乐谱</a></li> -->
<!-- 			</ol> -->
<!-- 		</div> -->
<!-- 		<a href="#" class="comSt8 fr">下载库客音乐APP</a> -->
<!-- 	</div> -->
<!-- </div> -->
<div class="comSite comWd01">
	<div class="comSite comTop01 cf">
		<a href="#" class="comSt1 fl"><img src="images/logo.png" alt=""></a>
		<div class="comSite1 cf fl">
			<input type="text" placeholder="单曲／专辑／艺术家／视频" class="comSt2 fl">
			<a href="#" title="搜索" class="comSt3 fl"></a>
		</div>
		<div id="notLogin" class="comSite2 fr cf">
			<a href="#" class="comSt6 fr">库客会员</a>
			<span class="comSt4 login" onclick="showLogin()">登陆</span>
			<span class="comSt5">|</span>
			<span class="comSt4 register">注册</span>
		</div>
		<div id="Login" class="comSite198 comSite198a cf" style="display: none;">
			<a href="#" class="comSt203">库客会员</a>
			<div class="comSite200 pr">
				<div class="comSite cf">
					<div class="comSite201"><img id="userImage" alt="" width="24" height="24"></div>
					<p class="comSt202" id="nickName"></p>
				</div>
				<div class="comSite202 cf">
					<a href="/kuke/userCenter/getFavoriteTrack">我的</a>
					<a href="/kuke/userCenter/userInf">个人设置</a>
					<a href="/kuke/userCenter/userAccount">账户设置</a>
					<a href="/kuke/userCenter/vipCenter">会员中心</a>
					<a href="javascript:void(0)" onclick="loginOut();">退出</a>
				</div>
			</div>
			<div class="comSite199 pr"><a id="sysmsgid" href="/kuke/userCenter/getSysMessageList" class="comSt168"></a></div>
		</div>
	</div>
	<div class="comSite comTop02 cf">
		<h1 class="comSt7 fl">库客·艺术中心</h1>
		<div class="comSite3 comSt31 fl">
			<ol class="cf">
				<li><a href="#" class="comOn02">唱片</a></li>
				<li><a href="#">视频</a></li>
				<li><a href="#">剧院</a></li>
				<li><a href="#">有声读物</a></li>
				<li><a href="#">直播</a></li>
				<li><a href="#">乐谱</a></li>
			</ol>
		</div>
		<a href="#" class="comSt8 fr">下载库客音乐APP</a>
	</div>
</div>
<div class="comSite35">
	<div class="comSite36 comWd01 cf">
		<a href="#" class="comOn03">全部</a>
		<a href="#">音乐时期</a>
		<a href="#">艺术家</a>
		<a href="#">乐器</a>
		<a href="#">音乐体裁</a>
		<a href="#">厂牌</a>
		<a href="#">出版时期</a>
		<a href="#">历史录音</a>
		<a href="#">场景音乐</a>
		<a href="#">中国&世界民族</a>
		<a href="#">其他</a>
	</div>
</div>
<div class="comSite48">
	<div class="comSite49 comWd01">
		<ol class="cf">
			<li>
				<div class="comSite50">
					<h2>今天听什么</h2>
					<div class="comSite51 pr">
						<div><img src="images/comSite16.jpg" alt="" width="400" height="400"></div>
						<span class="comSt53"></span>
						<h3>百老汇金曲百老汇金曲百老汇金曲百老汇金曲百老汇金曲百老汇金曲百老汇金曲百老汇金曲百老汇金曲百老汇金曲百老汇金曲</h3>
						<span class="comSt54"></span>
					</div>
				</div>
			</li>
			<li>
				<div class="comSite50">
					<h2>今日艺术家</h2>
					<div class="comSite51 pr">
						<div><img src="images/comSite16.jpg" alt="" width="400" height="400"></div>
						<span class="comSt53"></span>
						<h3>百老汇金曲百老汇金曲百老汇金曲百老汇金曲百老汇金曲百老汇金曲百老汇金曲百老汇金曲百老汇金曲百老汇金曲百老汇金曲</h3>
						<span class="comSt54"></span>
					</div>
				</div>
			</li>
			<li>
				<div class="comSite50">
					<h2>今日乐器</h2>
					<div class="comSite51 pr">
						<div><img src="images/comSite16.jpg" alt="" width="400" height="400"></div>
						<span class="comSt53"></span>
						<h3>百老汇金曲百老汇金曲百老汇金曲百老汇金曲百老汇金曲百老汇金曲百老汇金曲百老汇金曲百老汇金曲百老汇金曲百老汇金曲</h3>
						<span class="comSt54"></span>
					</div>
				</div>
			</li>
		</ol>
	</div>
	<div class="comSite52">
		<div class="comSite53 cf">
			<a href="#" class="fr">全部 &gt;</a>
			<h1>新上唱片<span>——</span></h1>
		</div>
		<div class="comSite54 cf">
			<span class="comSt57 fl"></span>
			<div class="comSite55 pr fl">
				<ol class="cf">
					<li class="fl">
						<div class="comSite56 pr">
							<div><img src="images/comSite16.jpg" alt="" width="210" height="210"></div>
							<span class="comSt55"></span>
							<span class="comSt56"></span>
						</div>
						<p class="comSt59"><a href="">贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲</a></p>
					</li>
					<li class="fl">
						<div class="comSite56 pr">
							<div><img src="images/comSite16.jpg" alt="" width="210" height="210"></div>
							<span class="comSt55"></span>
							<span class="comSt56"></span>
						</div>
						<p class="comSt59"><a href="">贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲</a></p>
					</li>
					<li class="fl">
						<div class="comSite56 pr">
							<div><img src="images/comSite16.jpg" alt="" width="210" height="210"></div>
							<span class="comSt55"></span>
							<span class="comSt56"></span>
						</div>
						<p class="comSt59"><a href="">贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲</a></p>
					</li>
					<li class="fl">
						<div class="comSite56 pr">
							<div><img src="images/comSite16.jpg" alt="" width="210" height="210"></div>
							<span class="comSt55"></span>
							<span class="comSt56"></span>
						</div>
						<p class="comSt59"><a href="">贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲</a></p>
					</li>
					<li class="fl">
						<div class="comSite56 pr">
							<div><img src="images/comSite16.jpg" alt="" width="210" height="210"></div>
							<span class="comSt55"></span>
							<span class="comSt56"></span>
						</div>
						<p class="comSt59"><a href="">贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲</a></p>
					</li>
					<li class="fl">
						<div class="comSite56 pr">
							<div><img src="images/comSite16.jpg" alt="" width="210" height="210"></div>
							<span class="comSt55"></span>
							<span class="comSt56"></span>
						</div>
						<p class="comSt59"><a href="">贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲</a></p>
					</li>
				</ol>
			</div>
			<span class="comSt57 comSt58 fr"></span>
		</div>
	</div>
</div>
<div class="comSite57">
	<div class="comSite58 comWd01">
		<div class="comSite53 comPd04 cf">
			<a href="#" class="fr">全部 &gt;</a>
			<h1>热门分类<span>——</span></h1>
		</div>
		<div class="comSite59 cf">
			<div class="comSite60 fl">
				<div class="comSite61 pr">
					<div><img src="images/comSite16.jpg" alt="" width="275" height="275"></div>
					<span class="comSt55"></span>
					<h2>浪漫主义时期</h2>
				</div>
				<div class="comSite62 comBg01">
					<ol>
						<li class="cf">
							<div class="comSite63 pr fl">
								<div><img src="images/comSite16.jpg" alt="" width="45" height="45"></div>
								<span class="comSt55"></span>
								<span class="comSt60"></span>
							</div>
							<div class="comSite64">
								<p><a href="#">贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲</a></p>
							</div>
						</li>
						<li class="cf">
							<div class="comSite63 pr fl">
								<div><img src="images/comSite16.jpg" alt="" width="45" height="45"></div>
								<span class="comSt55"></span>
								<span class="comSt60"></span>
							</div>
							<div class="comSite64">
								<p><a href="#">贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲</a></p>
							</div>
						</li>
						<li class="cf">
							<div class="comSite63 pr fl">
								<div><img src="images/comSite16.jpg" alt="" width="45" height="45"></div>
								<span class="comSt55"></span>
								<span class="comSt60"></span>
							</div>
							<div class="comSite64">
								<p><a href="#">贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲</a></p>
							</div>
						</li>
					</ol>
				</div>
			</div>
			<div class="comSite60 fl">
				<div class="comSite61 pr">
					<div><img src="images/comSite16.jpg" alt="" width="275" height="275"></div>
					<span class="comSt55"></span>
					<h2>浪漫主义时期</h2>
				</div>
				<div class="comSite62 comBg02">
					<ol>
						<li class="cf">
							<div class="comSite63 pr fl">
								<div><img src="images/comSite16.jpg" alt="" width="45" height="45"></div>
								<span class="comSt55"></span>
								<span class="comSt60"></span>
							</div>
							<div class="comSite64">
								<p><a href="#">贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲</a></p>
							</div>
						</li>
						<li class="cf">
							<div class="comSite63 pr fl">
								<div><img src="images/comSite16.jpg" alt="" width="45" height="45"></div>
								<span class="comSt55"></span>
								<span class="comSt60"></span>
							</div>
							<div class="comSite64">
								<p><a href="#">贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲</a></p>
							</div>
						</li>
						<li class="cf">
							<div class="comSite63 pr fl">
								<div><img src="images/comSite16.jpg" alt="" width="45" height="45"></div>
								<span class="comSt55"></span>
								<span class="comSt60"></span>
							</div>
							<div class="comSite64">
								<p><a href="#">贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲</a></p>
							</div>
						</li>
					</ol>
				</div>
			</div>
			<div class="comSite60 fl">
				<div class="comSite61 pr">
					<div><img src="images/comSite16.jpg" alt="" width="275" height="275"></div>
					<span class="comSt55"></span>
					<h2>浪漫主义时期</h2>
				</div>
				<div class="comSite62 comBg03">
					<ol>
						<li class="cf">
							<div class="comSite63 pr fl">
								<div><img src="images/comSite16.jpg" alt="" width="45" height="45"></div>
								<span class="comSt55"></span>
								<span class="comSt60"></span>
							</div>
							<div class="comSite64">
								<p><a href="#">贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲</a></p>
							</div>
						</li>
						<li class="cf">
							<div class="comSite63 pr fl">
								<div><img src="images/comSite16.jpg" alt="" width="45" height="45"></div>
								<span class="comSt55"></span>
								<span class="comSt60"></span>
							</div>
							<div class="comSite64">
								<p><a href="#">贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲</a></p>
							</div>
						</li>
						<li class="cf">
							<div class="comSite63 pr fl">
								<div><img src="images/comSite16.jpg" alt="" width="45" height="45"></div>
								<span class="comSt55"></span>
								<span class="comSt60"></span>
							</div>
							<div class="comSite64">
								<p><a href="#">贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲</a></p>
							</div>
						</li>
					</ol>
				</div>
			</div>
			<div class="comSite60 fl">
				<div class="comSite61 pr">
					<div><img src="images/comSite16.jpg" alt="" width="275" height="275"></div>
					<span class="comSt55"></span>
					<h2>浪漫主义时期</h2>
				</div>
				<div class="comSite62 comBg04">
					<ol>
						<li class="cf">
							<div class="comSite63 pr fl">
								<div><img src="images/comSite16.jpg" alt="" width="45" height="45"></div>
								<span class="comSt55"></span>
								<span class="comSt60"></span>
							</div>
							<div class="comSite64">
								<p><a href="#">贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲</a></p>
							</div>
						</li>
						<li class="cf">
							<div class="comSite63 pr fl">
								<div><img src="images/comSite16.jpg" alt="" width="45" height="45"></div>
								<span class="comSt55"></span>
								<span class="comSt60"></span>
							</div>
							<div class="comSite64">
								<p><a href="#">贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲</a></p>
							</div>
						</li>
						<li class="cf">
							<div class="comSite63 pr fl">
								<div><img src="images/comSite16.jpg" alt="" width="45" height="45"></div>
								<span class="comSt55"></span>
								<span class="comSt60"></span>
							</div>
							<div class="comSite64">
								<p><a href="#">贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲</a></p>
							</div>
						</li>
					</ol>
				</div>
			</div>
		</div>
		<div class="comSite59 cf">
			<div class="comSite60 fl">
				<div class="comSite61 pr">
					<div><img src="images/comSite16.jpg" alt="" width="275" height="275"></div>
					<span class="comSt55"></span>
					<h2>浪漫主义时期</h2>
				</div>
				<div class="comSite62 comBg05">
					<ol>
						<li class="cf">
							<div class="comSite63 pr fl">
								<div><img src="images/comSite16.jpg" alt="" width="45" height="45"></div>
								<span class="comSt55"></span>
								<span class="comSt60"></span>
							</div>
							<div class="comSite64">
								<p><a href="#">贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲</a></p>
							</div>
						</li>
						<li class="cf">
							<div class="comSite63 pr fl">
								<div><img src="images/comSite16.jpg" alt="" width="45" height="45"></div>
								<span class="comSt55"></span>
								<span class="comSt60"></span>
							</div>
							<div class="comSite64">
								<p><a href="#">贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲</a></p>
							</div>
						</li>
						<li class="cf">
							<div class="comSite63 pr fl">
								<div><img src="images/comSite16.jpg" alt="" width="45" height="45"></div>
								<span class="comSt55"></span>
								<span class="comSt60"></span>
							</div>
							<div class="comSite64">
								<p><a href="#">贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲</a></p>
							</div>
						</li>
					</ol>
				</div>
			</div>
			<div class="comSite60 fl">
				<div class="comSite61 pr">
					<div><img src="images/comSite16.jpg" alt="" width="275" height="275"></div>
					<span class="comSt55"></span>
					<h2>浪漫主义时期</h2>
				</div>
				<div class="comSite62 comBg06">
					<ol>
						<li class="cf">
							<div class="comSite63 pr fl">
								<div><img src="images/comSite16.jpg" alt="" width="45" height="45"></div>
								<span class="comSt55"></span>
								<span class="comSt60"></span>
							</div>
							<div class="comSite64">
								<p><a href="#">贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲</a></p>
							</div>
						</li>
						<li class="cf">
							<div class="comSite63 pr fl">
								<div><img src="images/comSite16.jpg" alt="" width="45" height="45"></div>
								<span class="comSt55"></span>
								<span class="comSt60"></span>
							</div>
							<div class="comSite64">
								<p><a href="#">贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲</a></p>
							</div>
						</li>
						<li class="cf">
							<div class="comSite63 pr fl">
								<div><img src="images/comSite16.jpg" alt="" width="45" height="45"></div>
								<span class="comSt55"></span>
								<span class="comSt60"></span>
							</div>
							<div class="comSite64">
								<p><a href="#">贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲</a></p>
							</div>
						</li>
					</ol>
				</div>
			</div>
			<div class="comSite60 fl">
				<div class="comSite61 pr">
					<div><img src="images/comSite16.jpg" alt="" width="275" height="275"></div>
					<span class="comSt55"></span>
					<h2>浪漫主义时期</h2>
				</div>
				<div class="comSite62 comBg07">
					<ol>
						<li class="cf">
							<div class="comSite63 pr fl">
								<div><img src="images/comSite16.jpg" alt="" width="45" height="45"></div>
								<span class="comSt55"></span>
								<span class="comSt60"></span>
							</div>
							<div class="comSite64">
								<p><a href="#">贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲</a></p>
							</div>
						</li>
						<li class="cf">
							<div class="comSite63 pr fl">
								<div><img src="images/comSite16.jpg" alt="" width="45" height="45"></div>
								<span class="comSt55"></span>
								<span class="comSt60"></span>
							</div>
							<div class="comSite64">
								<p><a href="#">贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲</a></p>
							</div>
						</li>
						<li class="cf">
							<div class="comSite63 pr fl">
								<div><img src="images/comSite16.jpg" alt="" width="45" height="45"></div>
								<span class="comSt55"></span>
								<span class="comSt60"></span>
							</div>
							<div class="comSite64">
								<p><a href="#">贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲</a></p>
							</div>
						</li>
					</ol>
				</div>
			</div>
			<div class="comSite60 fl">
				<div class="comSite61 pr">
					<div><img src="images/comSite16.jpg" alt="" width="275" height="275"></div>
					<span class="comSt55"></span>
					<h2>浪漫主义时期</h2>
				</div>
				<div class="comSite62 comBg08">
					<ol>
						<li class="cf">
							<div class="comSite63 pr fl">
								<div><img src="images/comSite16.jpg" alt="" width="45" height="45"></div>
								<span class="comSt55"></span>
								<span class="comSt60"></span>
							</div>
							<div class="comSite64">
								<p><a href="#">贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲</a></p>
							</div>
						</li>
						<li class="cf">
							<div class="comSite63 pr fl">
								<div><img src="images/comSite16.jpg" alt="" width="45" height="45"></div>
								<span class="comSt55"></span>
								<span class="comSt60"></span>
							</div>
							<div class="comSite64">
								<p><a href="#">贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲</a></p>
							</div>
						</li>
						<li class="cf">
							<div class="comSite63 pr fl">
								<div><img src="images/comSite16.jpg" alt="" width="45" height="45"></div>
								<span class="comSt55"></span>
								<span class="comSt60"></span>
							</div>
							<div class="comSite64">
								<p><a href="#">贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲贝多芬第五交响曲</a></p>
							</div>
						</li>
					</ol>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- 注册弹层及半透明背景 -->
<div id="comSt94" class="comSt94"></div>
<div class="comWd06 comSite114">
	<div class="comSite115 pr"><h1>手机号注册</h1><span title="关闭"></span></div>
	<div class="comSite116" date-step="first">
		<div class="comSite117">
			<div class="comSite133">
				<div class="comSite118"><input type="text" placeholder="请输入手机号" class="comSt95" id="comSt95"></div>
				<p class="comSt104" id="comSt104"></p>
			</div>
			<div class="comSite133">
				<div class="comSite118"><input type="password" placeholder="请输入6-16位密码，区分大小写" class="comSt96" id="comSt96"></div>
				<p class="comSt105" id="comSt105"></p>
			</div>
		</div>
		<div class="comSite128">
			<p class="comSt108">您的手机号：<span class="comSt106"></span></p>
			<div class="comSite129 cf">
				<span class="comSt102">02:59</span>
				<div class="comSite130"><input type="text" id="checkCode" placeholder="请输入验证码"></div>
			</div>
			<p class="comSt104" id="comSt107"></p>
		</div>
		<div class="comSite131" id="comSite131">
			<p>注册成功</p>
			<span class="comSt103">确定</span>
		</div>
		<div class="comSite132">
			<div class="comSite119 cf">
				<span class="comSt97 fr">去登陆</span>
				<span class="comSt98">下一步</span>
			</div>
			<div class="comSite120 cf">
				<div class="comSite121 pr fl">
					<span class="comSt99 comSt100" isAgree="true"></span>
					<input type="checkbox" checked="checked">
				</div>
				<p class="comSite122"><span>同意</span><a href="javascript:void(0)">库客用户注册协议</a></p>
			</div>
			<div class="comSite123">
				<p>注册可享受7天免费会员权益，</p>
				<p>海量曲库随意听</p>
			</div>
		</div>
	</div>
</div>
<!-- 库客用户协议弹层 -->
<div class="comSite124">
	<div class="comSite125">
		<div class="comSite126 pr"><h1>库客用户协议</h1><span title="关闭"></span></div>
		<div class="comSite127">
			<dl>
				<dt>1、库客网络服务条款的接受</dt>
				<dd>库客音乐网站（以下简称“库客 ”），库客 提供的服务完全按照其发布的服务条款和操作规则。本服务条款所称的用户是指完全同意所有条款并完成注册程序或未经注册而使用库客服务（以下简称“本服务”）的用户。</dd>
			</dl>
			<dl>
				<dt>2、服务条款的变更和修改</dt>
				<dd>库客有权随时对服务条款进行修改，并且一旦发生服务条款的变动，库客将在页面上提示修改的内容；当用户使用库客的特殊服务时，应接受库客随时提供的与该特 殊服务相关的规则或说明，并且此规则或说明均构成本服务条款的一部分。用户如果不同意服务条款的修改，可以主动取消已经获得的网络服务；如果用户继续享用 网络服务，则视为用户已经接受服务条款的修改。</dd>
			</dl>
			<dl>
				<dt>3、服务说明</dt>
				<dd>（1）库客运用自己的操作系统通过国际互联网向用户提供丰富的网上资源。除非另有明确规定，增强或强化目前服务的任何新功能，包括新产品，均无条件地适用本服务条款。</dd>
				<dd>（2）库客对网络服务不承担任何责任，即用户对网络服务的使用承担风险。库客不保证服务一定会满足用户的使用要求，也不保证服务不会受中断，对服务的及时性、安全性、准确性也不作担保。</dd>
				<dd>（3）为使用本服务，用户必须：</dd>
				<dd class="comSt101">A.自行配备进入国际互联网所必需的设备，包括计算机、数据机或其他存取装置；</dd>
				<dd class="comSt101">B.自行支付与此服务有关的费用；</dd>
				<dd>（4）用户接受本服务须同意：</dd>
				<dd class="comSt101">A. 提供完整、真实、准确、最新的个人资料；</dd>
				<dd class="comSt101">B. 不断更新注册资料，以符合上述（4）a.的要求。</dd>
				<dd class="comSt101">C.若用户提供任何错误、不实、过时或不完整的资料，并为库客所确知；或者库客有合理理由怀疑前述资料为错误、不实、过时或不完整，库客有权暂停或终止用户的帐号，并拒绝现在或将来使用本服务的全部或一部分。</dd>
			</dl>
		</div>
	</div>
</div>
<!-- 登录 -->
<!-- <div class="comWd06 comSite134"> -->
<!-- 	<div class="comSite135 pr"> -->
<!-- 		<div class="comSite136 cf"> -->
<!-- 			<span class="comOn15">账号登录</span> -->
<!-- 			<span onclick="qrCodeLogin()">扫码登录</span> -->
<!-- 		</div> -->
<!-- 		<span title="关闭" class="comSt114"></span> -->
<!-- 	</div> -->
<!-- 	<div class="comSite116a"> -->
<!-- 		<div class="comSite145"> -->
<!-- 			<div class="comSite142"> -->
<!-- 				<div class="comSite143"> -->
<!-- 					<div class="comSite144"><input name="userName" type="text" placeholder="请输入手机号/邮箱/用户名" class="comSt119" id="comSt119"></div> -->
<!-- 					<p class="comSt120" id="comSt120"></p> -->
<!-- 				</div> -->
<!-- 				<div class="comSite143"> -->
<!-- 					<div class="comSite144"><input name="userPwd" type="password" placeholder="请输入6-16位密码，区分大小写" class="comSt121" id="comSt121"></div> -->
<!-- 					<p class="comSt122" id="comSt122"></p> -->
<!-- 				</div> -->
<!-- 			</div> -->
<!-- 			<div class="comSite137 cf"> -->
<!-- 				<a href="#" class="comSt109" target="_blank">忘记密码</a> -->
<!-- 				<div class="comSite138 pr"> -->
<!-- 					<span id="autoFlagspan" class="comSt110 comSt111">自动登录</span> -->
<!-- 					<input name="autoFlag" type="checkbox" checked="checked" value="1"> -->
<!-- 				</div> -->
<!-- 			</div> -->
<!-- 			<div class="comSite139"><span class="comSt112" onclick="subLogin();">登 录</span></div> -->
<!-- 			<div class="comSite140 cf"> -->
<!-- 				<a href="javascript:void(0)" class="comSt113">注册 &gt;</a> -->
<!-- 				<div class="comSite141 cf"> -->
<!-- 					<span>其他方式：</span> -->
<!-- 					<a href="/kuke/sns/wechat?flag=3" target="_blank" class="comSt115 comSt116"></a> -->
<!-- 					<a href="/kuke/sns/qq?flag=1" target="_blank" class="comSt115 comSt117"></a> -->
<!-- 					<a href="/kuke/sns/weibo?flag=2" target="_blank" class="comSt115 comSt118"></a> -->
<!-- 				</div> -->
<!-- 			</div> -->
<!-- 		</div> -->
<!-- 		<div class="comSite145 pr" style="display:none;"> -->
<!-- 			<div id="qrcode" class="comSite146"></div> -->
<!-- 			<p class="comSt123">打开 <a href="#" target="_blank">库客音乐APP</a> 扫一扫登录</p> -->
<!-- 			<div class="comSite147"><p>扫码成功<br>即将登录</p></div> -->
<!-- 		</div> -->
<!-- 	</div> -->
<!-- </div> -->
<!-- 绑定手机号 -->
<!-- <div class="comWd06 comSite148"> -->
<!-- 	<div class="comSite115 pr"><h1>绑定手机号</h1><span title="关闭"></span></div> -->
<!-- 	<div class="comSite116a"> -->
<!-- 		<div class="comSite149"> -->
<!-- 			<div class="comSite152"> -->
<!-- 				<div class="comSite151" id="comSite151"><input type="text" placeholder="您的手机号码" class="comSt124" id="comSt124"></div> -->
<!-- 				<p class="comSt125" id="comSt125"></p> -->
<!-- 			</div> -->
<!-- 			<div class="comSite150 pr"> -->
<!-- 				<div class="comSite153 cf"> -->
<!-- 					<span class="comSt126 comSt127">发送验证码</span> -->
<!-- 					<div class="comSite154" id="comSite154"><input type="text" placeholder="验证码" class="comSt128" id="comSt128"></div> -->
<!-- 				</div> -->
<!-- 				<p class="comSt129" id="comSt129"></p> -->
<!-- 			</div> -->
<!-- 			<div class="comSite155"><span class="comSt130 comSt130a">下一步</span></div>绑定成功或者失败弹层 -->
<!-- 			<div class="comSite156"><a href="#" class="comSt131">跳过 &gt;</a></div> -->
<!-- 		</div> -->
<!-- 		<div class="comSite157 comSite157a" style="display:none;"> -->
<!-- 			<p class="comSt132">绑定成功<br>开启音乐之旅</p> -->
<!-- 			<p class="comSt134">5秒后，窗口自动关闭</p> -->
<!-- 			<div class="comSite158"><span class="comSt130 comSt130b">确定</span></div> -->
<!-- 		</div> -->
<!-- 		<div class="comSite157 comSite157b" style="display:none;"> -->
<!-- 			<p class="comSt132 comSt133">绑定失败<br>请重新尝试</p> -->
<!-- 			<div class="comSite158"><span class="comSt130 comSt130c">上一步</span></div> -->
<!-- 		</div> -->
<!-- 	</div> -->
<!-- </div> -->

<script>
$(function(){
	// 头部下拉菜单
	$(".comSite200").hover(function(){
		$(this).find(".comSite202").show();
	},function(){
		$(this).find(".comSite202").hide();
	});
	// 新上唱片 鼠标悬停移开切换
	$(".comSite55 li").hover(function(){
		$(this).find(".comSt55,.comSt56").show();
	},function(){
		$(this).find(".comSt55,.comSt56").hide();
	});
	// 热门分类 鼠标悬停移开切换
	$(".comSite61").hover(function(){
		$(this).find(".comSt55").show();
		$(this).find("h2").addClass("comOn08");
	},function(){
		$(this).find(".comSt55").hide();
		$(this).find("h2").removeClass("comOn08");
	});
	// 热门分类小图 鼠标悬停移开切换
	$(".comSite63").hover(function(){
		$(this).find(".comSt55,.comSt60").show();
	},function(){
		$(this).find(".comSt55,.comSt60").hide();
	});
	// 上面三张大图 鼠标悬停移开切换
	$(".comSite51").hover(function(){
		$(this).find(".comSt53,.comSt54,h3").show();
	},function(){
		$(this).find(".comSt53,.comSt54,h3").hide();
	});
	// 同意 库客用户注册协议
	$(".comSite121 input").on("click",function(){
		$(this).parent().find("span").toggleClass("comSt100");
		if($(".comSt99").attr("isAgree") == "true"){
			$(".comSt99").attr("isAgree",false);
		}else{
			$(".comSt99").attr("isAgree",true);
		}
	});
	// 库客用户协议弹层
	$(".comSite122 a").on("click",function(){
		$(".comSite124").show();
		$(".comSite114").hide();
	});
	// 关闭库客用户协议弹层
	$(".comSite126 span").on("click",function(){
		$(".comSite124").hide();
		$(".comSite114").show();
	});
	// 注册初始化
	if($(".comSite116").attr("data-step") == "first"){
		$(".comSite117").show();
		$(".comSite132").show();
		$(".comSite128").hide();
		$(".comSite131").hide();
	}
	// 下一步
	$(".comSt98").on("click",function(){
		if(checkForm.mobile() && checkForm.pwd() && $(".comSite121 input").prop("checked") == true){
			$(".comSt106").html($(".comSt95").val().slice(0,3)+"****"+$(".comSt95").val().slice($(".comSt95").val().length-4,$(".comSt95").val().length));
			$(".comSite117,.comSite131").hide();
			$(".comSite128,.comSite132").show();
			// 发送验证码
			$(".comSite116").attr("data-step") == "second";
			if(checkForm.checkCode() == true && $(".comSite121 input").prop("checked") == true){ // 注册成功
				$(".comSite117,.comSite128,.comSite132").hide();
				$(".comSite131").show();
			}
		}
	});
	// 关闭注册弹层
	$(".comSt103,.comSite115 span").on("click",function(){
		$(".comSite114,.comSt94").hide();
		$(".comSite116").attr("data-step") == "first";
		$(".comSt95,.comSt96").val("");
		$(".comSite130 input").val("");
	});
	// 显示注册弹层
	$(".register").on("click",function(){
		$(".comSt94,.comSite114").show();
		$(".comSite117").show();
		$(".comSite132").show();
		$(".comSite128").hide();
		$(".comSite131").hide();
		$(".comSite116").attr("data-step","first");
	});
// 	//登录弹层选项卡切换
// 	$(".comSite136 span").on("click",function(){
// 		var i = $(".comSite136 span").index(this);
// 		$(".comSite136 span").removeClass("comOn15").eq(i).addClass("comOn15");
// 		$(".comSite145").hide().eq(i).show();
// 	});
// 	//点击账号登录
// 	$(".login").on("click",function(){
// 		$(".comSt119,.comSt121").val("");
// 		$(".comSite134,.comSt94").show();
// 		debugger;
// 		if(t){
// 			window.clearTimeout(t);
// 		}
// 	});
// 	// 关闭登录弹层
// 	$(".comSt114").on("click",function(){
// 		$(".comSite134,.comSt94").hide();
// 		debugger;
// 		if(t){
// 			window.clearTimeout(t);
// 		}
// 	});
	// 登录
// 	$(".comSt112").on("click",function(){
// 		if(checkForm.mobile1() && (checkForm.tel() || checkForm.mail() || checkForm.uname()) && checkForm.pwd1() && $(".comSite138 input").prop("checked") == true){
// 			alert("可以登录啦");
// 		}
// 		if(checkForm.mobile1() && !checkForm.tel() && (checkForm.mail() || checkForm.uname()) && $(".comSite138 input").prop("checked") == true){
// 			$(".comSite134").hide();//登录弹层
// 			$(".comSite148").show();//绑定手机号 
// 		}
// 	});
	// 自动登录
	$(".comSite138 input").on("click",function(){
		$(this).parent().find("span").toggleClass("comSt111");
	});
// 	// 发送验证码
// 	$(".comSt126").on("click",function(){
// 		// 发送验证码
// 		$(this).html("02:59");
// 	});
	// 判断手机号绑定成功或者失败
	$(".comSt130a").on("click",function(){
		if(checkForm.mobile2() && checkForm.checkCode1()){
			if(1>0){ // 绑定成功
				$(".comSite149").hide();
				$(".comSite157a").show();
			}else{ // 绑定失败
				$(".comSite149").hide();
				$(".comSite157b").show();
			}
		}
	});
// 	//绑定失败 返回上一步
// 	$(".comSt130c").on("click",function(){
// 		$(".comSite149").show();
// 		$(".comSite157").hide();
// 	});
// 	// 关闭绑定弹层
// 	$(".comSt130b,.comSite115 span").on("click",function(){
// 		$(".comSite148,.comSt94").hide();
// 		$(".comSite149").show();
// 		$(".comSite157a").hide();
// 	});
	// 去登录
	$(".comSt97").on("click",function(){
		$(".comSite114").hide();
		$(".comSite134").show();
	});
});
// var G = { // 获取id
// 	id : function(id){
// 		return document.getElementById(id);
// 	}
// }
// var checkForm = { // 表单验证
// 	telRule : /^(13[0-9]{9}|14[5|7][0-9]{8}|15[0|1|2|3|5|6|7|8|9][0-9]{8}|17[6|7][0-9]{8}|18[0-9]{9})$/,
// 	mValue : function(){
// 		return G.id("comSt95").value; // 注册的手机号
// 	},
// 	mValue1 : function(){
// 		return G.id("comSt119").value; // 登录的手机、用户名、邮箱
// 	},
// 	mValue2 : function(){
// 		return G.id("comSt124").value; // 登录的手机、用户名、邮箱
// 	},
// 	pwdRule : /\s/, // 空白字符
// 	mail : function(){
// 		var mailRule = /([0-9a-zA-Z_-]+@[0-9a-zA-Z]+.[com|cn|net|com.cn])/; // 有点问题 开始 结束
// 		if(mailRule.test(this.mValue1())){
// 			return true;
// 		}
// 	}, // 邮箱
// 	uname : function(){
// 		var unameRule = /^[0-9a-zA-Z_-]+$/;
// 		if(unameRule.test(this.mValue1())){
// 			return true;
// 		}
// 	}, // 用户名
// 	pValue : function(){
// 		return G.id("comSt96").value; // 注册的密码
// 	},
// 	pValue1 : function(){
// 		return G.id("comSt121").value; // 登录的密码
// 	},
// 	len : function(){
// 		return G.id("comSt96").value.length;
// 	},
// 	len1 : function(){
// 		return G.id("comSt121").value.length;
// 	},
// 	cValue : function(){
// 		return G.id("checkCode").value; // 注册的验证码
// 	},
// 	cValue1 : function(){
// 		return G.id("comSt128").value; // 绑定的验证码
// 	},
// 	tel : function(){
// 		if(this.telRule.test(this.mValue1())){
// 			return true;
// 		}
// 	},
// 	mobile : function(){ // 注册的
// 		if(!this.telRule.test(this.mValue())){ // 这里少一个 如果已注册 提示请勿重复注册
// 			G.id("comSt104").innerHTML = "请输入正确的手机号码";
// 			G.id("comSt95").parentNode.className = "comSite118 comOn14";
// 			return false;
// 		}else{
// 			G.id("comSt104").innerHTML = "";
// 			G.id("comSt95").parentNode.className = "comSite118";
// 			return true;
// 		}
// 	},
// // 	mobile1 : function(){ // 登录的
// // 		if(!this.telRule.test(this.mValue1())){ // 这里少一个 如果已注册 提示请勿重复注册
// // 			G.id("comSt120").innerHTML = "请输入正确的手机号码";
// // 			G.id("comSt120").parentNode.firstChild.className = "comSite144 comOn16";
// // 			return false;
// // 		}else{
// // 			G.id("comSt120").innerHTML = "";
// // 			G.id("comSt120").parentNode.firstChild.className = "comSite144";
// // 			return true;
// // 		}
// // 		if(!this.mValue1()){
// // 			G.id("comSt120").innerHTML = "请输入正确的手机号/邮箱/用户名";
// // 			G.id("comSt120").parentNode.firstChild.className = "comSite144 comOn16";
// // 		}else{
// // 			G.id("comSt120").innerHTML = "";
// // 			G.id("comSt120").parentNode.firstChild.className = "comSite144";
// // 			return true;
// // 		}
// // 	},
// 	mobile2 : function(){ // 绑定的
// 		if(!this.telRule.test(this.mValue2())){ // 这里少一个 如果已注册 提示请勿重复注册
// 			G.id("comSt125").innerHTML = "请输入正确的手机号码";
// 			G.id("comSite151").className = "comSite151 comOn16";
// 			return false;
// 		}else{
// 			G.id("comSt125").innerHTML = "";
// 			G.id("comSite151").className = "comSite151";
// 			return true;
// 		}
// 	},
// 	pwd : function(){ // 注册的
// 		if(this.len() < 6 || this.len() > 16){
// 			G.id("comSt105").innerHTML = "请输入6-16位密码,区分大小写,不含空白字符";
// 			G.id("comSt96").parentNode.className = "comSite118 comOn14";
// 			return false;
// 		}else if(this.pwdRule.test(this.pValue())){
// 			G.id("comSt105").innerHTML = "请输入6-16位密码,区分大小写,不含空白字符";
// 			G.id("comSt96").parentNode.className = "comSite118 comOn14";
// 			return false;
// 		}else{
// 			G.id("comSt105").innerHTML = "";
// 			G.id("comSt96").parentNode.className = "comSite118";
// 			return true;
// 		}
// 	},
// // 	pwd1 : function(){ // 登录的
// // 		if(this.len1() < 6 || this.len1() > 16){
// // 			G.id("comSt122").innerHTML = "请输入6-16位密码,区分大小写,不含空白字符";
// // 			G.id("comSt122").parentNode.firstChild.className = "comSite144 comOn16";
// // 			return false;
// // 		}else if(this.pwdRule.test(this.pValue1())){
// // 			G.id("comSt122").innerHTML = "请输入6-16位密码,区分大小写,不含空白字符";
// // 			G.id("comSt122").parentNode.firstChild.className = "comSite144 comOn16";
// // 			return false;
// // 		}else{
// // 			G.id("comSt122").innerHTML = "";
// // 			G.id("comSt122").parentNode.firstChild.className = "comSite144";
// // 			return true;
// // 		}
// // 	},
// 	checkCode : function(){
// 		if(!this.cValue()){ // 这里少一个验证码和数据库匹配验证
// 			G.id("comSt107").innerHTML = "请输入正确的验证码";
// 			G.id("checkCode").parentNode.className = "comSite130 comOn14";
// 			return false;
// 		}else{
// 			G.id("comSt107").innerHTML = "";
// 			G.id("checkCode").parentNode.className = "comSite130";
// 			return true;
// 		}
// 	},
// 	checkCode1 : function(){ // 绑定的
// 		if(!this.cValue1()){ // 这里少一个验证码和数据库匹配验证
// 			G.id("comSt129").innerHTML = "请输入正确的验证码";
// 			G.id("comSite154").className = "comSite154 comOn16";
// 			return false;
// 		}else{
// 			G.id("comSt129").innerHTML = "";
// 			G.id("comSite154").className = "comSite154";
// 			return true;
// 		}
// 	}

// }
// G.id("comSt95").onblur = function(){checkForm.mobile();}
// G.id("comSt96").onblur = function(){checkForm.pwd();}
// G.id("checkCode").onblur = function(){checkForm.checkCode();}
// // G.id("comSt119").onblur = function(){checkForm.mobile1();}
// // G.id("comSt121").onblur = function(){checkForm.pwd1();}
// G.id("comSt124").onblur = function(){checkForm.mobile2();}
// G.id("comSt128").onblur = function(){checkForm.checkCode1();}
</script>
</body>
</html>