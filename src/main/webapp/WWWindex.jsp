<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<!DOCTYPE html >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>艺术中心首页</title>
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta name="renderer" content="webkit">
<meta name="keywords" content="">
<meta name="description" content="">
<link href="css/init.css" rel="stylesheet">
<link href="css/main.css" rel="stylesheet">
<script type="text/javascript" src="js/jquery-1.12.4.min.js"></script>
<script type="text/javascript" src="js/jquery.qrcode.min.js"></script>
<script type="text/javascript" src="js/jquery.blockUI.js"></script>
<script type="text/javascript" src="js/json2.js"></script>
<script type="text/javascript" src="http://auth.kuke.com/js/login/login.js"></script>
<script type="text/javascript" src="http://auth.kuke.com/js/login/regist.js"></script>
</head>
<body>
<div class="comSite comWd01">
	<div class="comSite comTop01 cf">
		<div class="comSt1a fl">
			<a href="http://www.kuke.com" class="comSt1"><img src="images/logo.png" alt="" width="87" height="27"></a>
			<img id="orgLogoImg" src="" alt="" height="27" class="comSt1b">
		</div>
		<div class="comSite1 cf fl">
			<input type="text" placeholder="单曲／专辑／艺术家／视频" class="comSt2 fl">
			<a href="#" title="搜索" class="comSt3 fl"></a>
		</div>
		<div class="comSite2 fr cf">
			<a href="http://auth.kuke.com/kuke/userCenter/vipCenter" class="comSt6 fr">库客会员</a>
			<!-- 未登录 -->
			<div id="notLogin" class="comSite fr comSt405">
				<a href="javascript:void(0)" class="comSt4" onclick="showLogin()">登陆</a>
				<span class="comSt5">|</span>
				<a href="javascript:void(0)" class="comSt4" onclick="regist()">注册</a>
			</div>
			<!-- 已登录 -->
			<div id="Login" class="comSite200 pr" style="display:none">
				<div class="comSite cf">
					<div class="comSite201"><img id="userImage" alt="" width="24" height="24"></div>
					<p class="comSt202" id="nickname"></p>
				</div>
				<div class="comSite202a">
					<div class="comSite202 cf">
						<a href="http://auth.kuke.com/kuke/userCenter/getFavoriteTrack">我的</a>
						<a href="http://auth.kuke.com/kuke/userCenter/userInf">个人设置</a>
						<a href="http://auth.kuke.com/kuke/userCenter/userAccount">账户设置</a>
						<a href="http://auth.kuke.com/kuke/userCenter/vipCenter">会员中心</a>
						<a href="javascript:void(0)" id="loginout">退出</a>
					</div>
				</div>
			</div>
			<div id="LoginMsg" class="comSite199 pr" style="display:none"><a id="sysmsgid" href="http://auth.kuke.com/kuke/userCenter/getSysMessageList" class="comSt168">22</a></div>
		</div>
	</div>
	<div class="comSite comTop02 cf">
		<h1 class="comSt7 fl">库客·艺术中心</h1>
		<div class="comSite3 fl">
			<ol class="cf">
				<li><a href="#" class="comOn01">唱片</a></li>
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
<!-- 播放器 -->
<div class="comSite312 pr">
	<ol class="comSite04 cf">
		<li>
			<a href="#" class="comSite313 pr">
				<div><img src="images/homebanner.jpg" alt="" width="100%"></div>
				<h2>j.s.巴赫杰作《勃兰登堡协奏曲》</h2>
				<p>六首风格迥异的协奏曲，展现复调音乐的无限魅力</p>
			</a>
		</li>
		<li>
			<a href="#" class="comSite313 pr">
				<div><img src="images/homebanner.jpg" alt="" width="100%"></div>
				<h2>j.s.巴赫杰作《勃兰登堡协奏曲》</h2>
				<p>六首风格迥异的协奏曲，展现复调音乐的无限魅力</p>
			</a>
		</li>
		<li>
			<a href="#" class="comSite313 pr">
				<div><img src="images/homebanner.jpg" alt="" width="100%"></div>
				<h2>j.s.巴赫杰作《勃兰登堡协奏曲》</h2>
				<p>六首风格迥异的协奏曲，展现复调音乐的无限魅力</p>
			</a>
		</li>
		<li>
			<a href="#" class="comSite313 pr">
				<div><img src="images/homebanner.jpg" alt="" width="100%"></div>
				<h2>j.s.巴赫杰作《勃兰登堡协奏曲》</h2>
				<p>六首风格迥异的协奏曲，展现复调音乐的无限魅力</p>
			</a>
		</li>
		<li>
			<a href="#" class="comSite313 pr">
				<div><img src="images/homebanner.jpg" alt="" width="100%"></div>
				<h2>j.s.巴赫杰作《勃兰登堡协奏曲》</h2>
				<p>六首风格迥异的协奏曲，展现复调音乐的无限魅力</p>
			</a>
		</li>
	</ol>
	<div class="comSite05 cf">
		<span class="comOn39"></span>
		<span></span>
		<span></span>
		<span></span>
		<span></span>
	</div>
	<span class="comSt288 comSt288a"></span>
	<span class="comSt288 comSt288b"></span>
	<div class="comSite06 cf">
		<div class="comSite07"><a href="#" class="comSt10"></a></div>
		<a href="#" class="comSt11"></a>
	</div>
	<div class="comSite08 cf">
		<div class="comSite09 fl">
			<div class="comSite10 cf">
				<span class="comSt12 comOn41 fr"></span>
				<span class="comSt12 comSt13 fr"></span>
				<span class="comSt14">00:24/5:00</span>
			</div>
			<div class="comSite11"><span class="comSt15" style="width:25%;"></span></div>
			<p class="comSt16">Drinking Songs Msrs,The Bringer of War</p>
		</div>
		<a href="#" class="comSt11 comOn40"></a>
	</div>
</div>
<!-- 主内容 -->
<div class="comSite comWd01">
	<div class="comSite12 cf">
		<div class="comSite25 fr">
			<div class="comSite">
				<h1 class="comSt28"><span>——</span> 艺术家 <span>——</span></h1>
				<div class="comSite26 pr">
					<div class="comSite27 pr">
						<div class="comSite28 cf">
							<div class="comSite29 fl"><!-- foreach -->
								<ol class="cf">
									<li>
										<a href="#" class="comSite30 pr">
											<div class="comSite30bg"><img src="images/comSite30.png" alt="" width="58" height="58"></div>
											<p>巴赫</p>
										</a>
									</li>
									<li>
										<a href="#" class="comSite30 pr">
											<div class="comSite30bg"><img src="" alt="" width="58" height="58"></div>
											<p>巴赫</p>
										</a>
									</li>
								</ol>
								<ol class="cf">
									<li>
										<a href="#" class="comSite30 pr">
											<div class="comSite30bg"><img src="images/comSite30.png" alt="" width="58" height="58"></div>
											<p>巴赫</p>
										</a>
									</li>
									<li>
										<a href="#" class="comSite30 pr">
											<div class="comSite30bg"><img src="" alt="" width="58" height="58"></div>
											<p>巴赫</p>
										</a>
									</li>
								</ol>
								<ol class="cf">
									<li>
										<a href="#" class="comSite30 pr">
											<div class="comSite30bg"><img src="images/comSite30.png" alt="" width="58" height="58"></div>
											<p>巴赫</p>
										</a>
									</li>
									<li>
										<a href="#" class="comSite30 pr">
											<div class="comSite30bg"><img src="" alt="" width="58" height="58"></div>
											<p>巴赫</p>
										</a>
									</li>
								</ol>
							</div>
							<div class="comSite29 fl"><!-- foreach -->
								<ol class="cf">
									<li>
										<a href="#" class="comSite30 pr">
											<div class="comSite30bg"><img src="images/comSite30.png" alt="" width="58" height="58"></div>
											<p>巴赫</p>
										</a>
									</li>
									<li>
										<a href="#" class="comSite30 pr">
											<div class="comSite30bg"><img src="" alt="" width="58" height="58"></div>
											<p>巴赫</p>
										</a>
									</li>
								</ol>
								<ol class="cf">
									<li>
										<a href="#" class="comSite30 pr">
											<div class="comSite30bg"><img src="images/comSite30.png" alt="" width="58" height="58"></div>
											<p>巴赫</p>
										</a>
									</li>
									<li>
										<a href="#" class="comSite30 pr">
											<div class="comSite30bg"><img src="" alt="" width="58" height="58"></div>
											<p>巴赫</p>
										</a>
									</li>
								</ol>
								<ol class="cf">
									<li>
										<a href="#" class="comSite30 pr">
											<div class="comSite30bg"><img src="images/comSite30.png" alt="" width="58" height="58"></div>
											<p>巴赫</p>
										</a>
									</li>
									<li>
										<a href="#" class="comSite30 pr">
											<div class="comSite30bg"><img src="" alt="" width="58" height="58"></div>
											<p>巴赫</p>
										</a>
									</li>
								</ol>
							</div>
							<div class="comSite29 fl"><!-- foreach -->
								<ol class="cf">
									<li>
										<a href="#" class="comSite30 pr">
											<div class="comSite30bg"><img src="images/comSite30.png" alt="" width="58" height="58"></div>
											<p>巴赫</p>
										</a>
									</li>
									<li>
										<a href="#" class="comSite30 pr">
											<div class="comSite30bg"><img src="" alt="" width="58" height="58"></div>
											<p>巴赫</p>
										</a>
									</li>
								</ol>
								<ol class="cf">
									<li>
										<a href="#" class="comSite30 pr">
											<div class="comSite30bg"><img src="images/comSite30.png" alt="" width="58" height="58"></div>
											<p>巴赫</p>
										</a>
									</li>
									<li>
										<a href="#" class="comSite30 pr">
											<div class="comSite30bg"><img src="" alt="" width="58" height="58"></div>
											<p>巴赫</p>
										</a>
									</li>
								</ol>
								<ol class="cf">
									<li>
										<a href="#" class="comSite30 pr">
											<div class="comSite30bg"><img src="images/comSite30.png" alt="" width="58" height="58"></div>
											<p>巴赫</p>
										</a>
									</li>
									<li>
										<a href="#" class="comSite30 pr">
											<div class="comSite30bg"><img src="" alt="" width="58" height="58"></div>
											<p>巴赫</p>
										</a>
									</li>
								</ol>
							</div>
						</div>
					</div>
					<span class="comSt29 comSt29a"></span>
					<span class="comSt29 comSt29b comSt30"></span>
				</div>
				<div class="comSite31"><a href="#">全部</a></div>
			</div>
			<div class="comSite comPd2">
				<h1 class="comSt28"><span>——</span> 乐器 <span>——</span></h1>
				<div class="comSite32">
					<ol class="cf">
						<li class="comSite33">
							<a href="#" class="comSite34">
								<div><img src="images/yueqi-jita.jpg" alt="" width="100%"></div>
								<p>吉他</p>
							</a>
						</li>
						<li class="comSite33">
							<a href="#" class="comSite34">
								<div><img src="images/yueqi-yuanhao.jpg" alt="" width="100%"></div>
								<p>圆号</p>
							</a>
						</li>
						<li class="comSite33">
							<a href="#" class="comSite34">
								<div><img src="images/yueqi-qiaojiyue.jpg" alt="" width="100%"></div>
								<p>敲击乐</p>
							</a>
						</li>
					</ol>
					<ol class="cf">
						<li class="comSite33">
							<a href="#" class="comSite34">
								<div><img src="images/yueqi-changhao.jpg" alt="" width="100%"></div>
								<p>长号</p>
							</a>
						</li>
						<li class="comSite33">
							<a href="#" class="comSite34">
								<div><img src="images/yueqi-gangqin.jpg" alt="" width="100%"></div>
								<p>钢琴</p>
							</a>
						</li>
						<li class="comSite33">
							<a href="#" class="comSite34">
								<div><img src="images/yueqi-xiaohao.jpg" alt="" width="100%"></div>
								<p>小号</p>
							</a>
						</li>
					</ol>
					<ol class="cf">
						<li class="comSite33">
							<a href="#" class="comSite34">
								<div><img src="images/yueqi-xiaohao1.jpg" alt="" width="100%"></div>
								<p>小号</p>
							</a>
						</li>
						<li class="comSite33">
							<a href="#" class="comSite34">
								<div><img src="images/yueqi-pipa.jpg" alt="" width="100%"></div>
								<p>琵琶</p>
							</a>
						</li>
						<li class="comSite33">
							<a href="#" class="comSite34">
								<div><img src="images/yueqi-xiaotiqin.jpg" alt="" width="100%"></div>
								<p>小提琴</p>
							</a>
						</li>
					</ol>
				</div>
			</div>
			<div class="comSite comPd2">
				<h1 class="comSt28"><span>——</span>推荐厂牌<span>——</span></h1>
				<div class="comSite314">
					<ol class="comSite315 cf">
						<li><a href=""><img src="images/img-changpai.jpg" alt="" width="128" height="108"></a></li>
						<li><a href=""><img src="images/img-changpai.jpg" alt="" width="128" height="108"></a></li>
					</ol>
					<ol class="comSite315 cf">
						<li><a href=""><img src="images/img-changpai.jpg" alt="" width="128" height="108"></a></li>
						<li><a href=""><img src="images/img-changpai.jpg" alt="" width="128" height="108"></a></li>
					</ol>
					<ol class="comSite315 cf">
						<li><a href=""><img src="images/img-changpai.jpg" alt="" width="128" height="108"></a></li>
						<li><a href=""><img src="images/img-changpai.jpg" alt="" width="128" height="108"></a></li>
					</ol>
					<ol class="comSite315 cf">
						<li><a href=""><img src="images/img-changpai.jpg" alt="" width="128" height="108"></a></li>
						<li><a href=""><img src="images/img-changpai.jpg" alt="" width="128" height="108"></a></li>
					</ol>
					<ol class="comSite315 cf">
						<li><a href=""><img src="images/img-changpai.jpg" alt="" width="128" height="108"></a></li>
						<li><a href=""><img src="images/img-changpai.jpg" alt="" width="128" height="108"></a></li>
					</ol>
				</div>
				<div class="comSite31"><a href="#">全部</a></div>
			</div>
		</div>
		<div class="comSite13">
			<div class="comSite316">
				<div class="comSite317 c">
					<a href="#" class="comSt19">全部 &gt;</a>
					<h1 class="comSt17">热播唱片<span class="comSt18"> ——</span></h1>
				</div>
				<div class="comSite14 comSite14a comPd07">
					<ol class="c">
						<li>
							<div class="comSite16 pr">
								<div><a href="#"><img src="images/comSite16.jpg" alt="" width="210" height="210"></a></div>
								<span class="comSt53 comSt53a"></span>
								<span class="comSt56 comSt56a"></span>
							</div>
							<h2 class="comSite17"><a href="">百老汇金曲20首20 Best of Broadway百老汇金曲20首</a></h2>
						</li>
						<li>
							<div class="comSite16 pr">
								<div><a href="#"><img src="images/comSite16.jpg" alt="" width="210" height="210"></a></div>
								<span class="comSt53 comSt53a"></span>
								<span class="comSt56 comSt56a"></span>
							</div>
							<h2 class="comSite17"><a href="">百老汇金曲20首20 Best of Broadway百老汇金曲20首</a></h2>
						</li>
						<li>
							<div class="comSite16 pr">
								<div><a href="#"><img src="images/comSite16.jpg" alt="" width="210" height="210"></a></div>
								<span class="comSt53 comSt53a"></span>
								<span class="comSt56 comSt56a"></span>
							</div>
							<h2 class="comSite17"><a href="">百老汇金曲20首20 Best of Broadway百老汇金曲20首</a></h2>
						</li>
						<li>
							<div class="comSite16 pr">
								<div><a href="#"><img src="images/comSite16.jpg" alt="" width="210" height="210"></a></div>
								<span class="comSt53 comSt53a"></span>
								<span class="comSt56 comSt56a"></span>
							</div>
							<h2 class="comSite17"><a href="">百老汇金曲20首20 Best of Broadway百老汇金曲20首</a></h2>
						</li>
					</ol>
				</div>
			</div>
			<div class="comSite comPd08">
				<div class="comSite317 c">
					<a href="#" class="comSt19">全部 &gt;</a>
					<h1 class="comSt17">分类精选<span class="comSt18"> ——</span></h1>
				</div>
				<div class="comSite318 c comPd09">
					<a href="#" class="comSite318a1">
						<div class="comSite319"><img src="images/jiaoxiangzuopin.png" width="51" height="52"></div>
						<span class="comWd08">交响作品</span>
					</a>
					<a href="#" class="comSite318a1">
						<div class="comSite319"><img src="images/langmanjinqu.png" width="51" height="52"></div>
						<span class="comWd08">浪漫金曲</span>
					</a>
					<a href="#" class="comSite318a1">
						<div class="comSite319"><img src="images/gudianzhuyishiqi.png" width="51" height="52"></div>
						<span class="comWd10">古典主义时期</span>
					</a>
					<a href="#" class="comSite318a1">
						<div class="comSite319"><img src="images/qingyinyue.png" width="51" height="52"></div>
						<span class="comWd07">轻音乐</span>
					</a>
					<a href="#" class="comSite318a1">
						<div class="comSite319"><img src="images/dangdaijueshiyue.png" width="51" height="52"></div>
						<span class="comWd09">当代爵士乐</span>
					</a>
					<a href="#" class="comSite318a1">
						<div class="comSite319"><img src="images/shijieminzuyinyue.png" width="51" height="52"></div>
						<span class="comWd10">世界民族音乐</span>
					</a>
				</div>
			</div>
			<div class="comSite comPd08">
				<div class="comSite317 c">
					<a href="#" class="comSt19">全部 &gt;</a>
					<h1 class="comSt17">视频精选<span class="comSt18"> ——</span></h1>
				</div>
				<div class="comSite21 comSite21a comPd09">
					<ol class="c">
						<li>
							<div class="comSite22 pr">
								<div><img src="images/comSite21.jpg" alt="" width="283" height="159"></div>
								<span class="comSt53 comSt53c"></span>
								<span class="comSt22 comSt22a">PALY</span>
							</div>
							<div class="comSite23">
								<a href="#" class="comSite320">
									<h2>理查·施特劳斯</h2>
									<p>英雄的生涯/德沃夏克：第九交响曲“自新大陆”(肯佩)STRAUSS, Right</p>
								</a>
							</div>
						</li>
						<li>
							<div class="comSite22 pr">
								<div><img src="images/comSite21.jpg" alt="" width="283" height="159"></div>
								<span class="comSt53 comSt53c"></span>
								<span class="comSt22 comSt22a">PALY</span>
							</div>
							<div class="comSite23">
								<a href="#" class="comSite320">
									<h2>理查·施特劳斯</h2>
									<p>英雄的生涯/德沃夏克：第九交响曲“自新大陆”(肯佩)STRAUSS, Right</p>
								</a>
							</div>
						</li>
						<li>
							<div class="comSite22 pr">
								<div><img src="images/comSite21.jpg" alt="" width="283" height="159"></div>
								<span class="comSt53 comSt53c"></span>
								<span class="comSt22 comSt22a">PALY</span>
							</div>
							<div class="comSite23">
								<a href="#" class="comSite320">
									<h2>理查·施特劳斯</h2>
									<p>英雄的生涯/德沃夏克：第九交响曲“自新大陆”(肯佩)STRAUSS, Right</p>
								</a>
							</div>
						</li>
					</ol>
				</div>
			</div>
			<div class="comSite316 comPd08">
				<div class="comSite317 c">
					<a href="#" class="comSt19">全部 &gt;</a>
					<h1 class="comSt17">经典有声<span class="comSt18"> ——</span></h1>
				</div>
				<div class="comSite14 comSite14a1 comPd07">
					<ol class="c">
						<li>
							<div class="comSite16 pr">
								<div><a href="#"><img src="images/comSite16.jpg" alt="" width="210" height="210"></a></div>
								<span class="comSt53 comSt53b"></span>
								<span class="comSt56 comSt56b"></span>
							</div>
							<h2 class="comSite17"><a href="">百老汇金曲20首20 Best of Broadway百老汇金曲20首</a></h2>
						</li>
						<li>
							<div class="comSite16 pr">
								<div><a href="#"><img src="images/comSite16.jpg" alt="" width="210" height="210"></a></div>
								<span class="comSt53 comSt53b"></span>
								<span class="comSt56 comSt56b"></span>
							</div>
							<h2 class="comSite17"><a href="">百老汇金曲20首20 Best of Broadway百老汇金曲20首</a></h2>
						</li>
						<li>
							<div class="comSite16 pr">
								<div><a href="#"><img src="images/comSite16.jpg" alt="" width="210" height="210"></a></div>
								<span class="comSt53 comSt53b"></span>
								<span class="comSt56 comSt56b"></span>
							</div>
							<h2 class="comSite17"><a href="">百老汇金曲20首20 Best of Broadway百老汇金曲20首</a></h2>
						</li>
						<li>
							<div class="comSite16 pr">
								<div><a href="#"><img src="images/comSite16.jpg" alt="" width="210" height="210"></a></div>
								<span class="comSt53 comSt53b"></span>
								<span class="comSt56 comSt56b"></span>
							</div>
							<h2 class="comSite17"><a href="">百老汇金曲20首20 Best of Broadway百老汇金曲20首</a></h2>
						</li>
					</ol>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="comSite442"><!-- 公共尾部 -->
	<div class="comSite443">
		<div class="comSite444">
			<ol>
				<li><img src="images/logo01.png" alt="" width="" width="84" height="32"></li>
				<li><img src="images/logo02.png" alt="" width="" width="46" height="32"></li>
				<li class="comSt344">|</li>
				<li><img src="images/logo03.png" alt="" width="" width="223" height="32"></li>
			</ol>
		</div>
		<div class="comSite445">
			<div class="comSite446 comWd11">
				<div class="comSite447 cf comWd12">
					<a href="guanyuwomen.html">关于我们</a>
					<a href="yinsishengming.html">隐私声明</a>
					<a href="shangwuhezuo.html">商务合作</a>
					<a href="banquanxinxi.html">版权信息</a>
					<a href="lianxiwomen.html">联系我们</a>
					<a href="yijianfankui.html">意见反馈</a>
				</div>
			</div>
		</div>
		<div class="comSite448">
			<ol class="comSite449">
				<li><a href="wangluochuanbo.html" rel="nofollow" target="_blank">信息网络传播视听节目许可证</a></li>
				<li><a href="zhizuojingying.html" rel="nofollow" target="_blank">广播电视节目制作经营许可证</a></li>
				<li><a href="chubanxuke.html" rel="nofollow" target="_blank">中华人民共和国互联网出版许可证</a></li>
			</ol>
			<ol class="comSite449">
				<li><a href="wenhuajingying.html" rel="nofollow" target="_blank">中华人民共和国网络文化经营许可证</a></li>
				<li><a href="http://www.itrust.org.cn/yz/pjwx.asp?wm=1171774136" rel="nofollow" target="_blank">中华互联网协会网络诚信推荐联盟</a></li>
			</ol>
			<ol class="comSite449">
				<li><a href="icp.html" rel="nofollow" target="_blank">京ICP证030879号</a></li>
				<li><a href="http://www.miibeian.gov.cn/publish/query/indexFirst.action" rel="nofollow" target="_blank">京ICP备09081529号</a></li>
				<li>COPYRIGHT KUKE.COM.ALL RIGHTS RESERVED</li>
			</ol>
		</div>
	</div>
</div>
<script>
function getPlace(){ // 动态计算大图宽度、高度
	var getHeight = $(".comSite04 li:first").height();
	var getWidth1 = $(".comSite05 span").width() * $(".comSite05 span").size() + 32;
	var getWidth2 = $(window).width();
	if(getWidth2 > 1903){
		$(".comSite04 li").width(1903);
	}
	var getWidth  = $(".comSite04 li:first").width() * $(".comSite04 li").size();
	var getWidth3 = $(".comSite29").width() * $(".comSite29").size();
	$(".comSite28").width(getWidth3); // 艺术家动态获取宽度

	$(".comSite312").height(getHeight);
	$(".comSite04").width(getWidth);
	$(".comSite04 li").width(getWidth2);
	$(".comSite05").width(getWidth1).css("marginLeft",-getWidth1/2);
}
$(function(){
	getPlace();
	$(window).on("resize",function(){ // 窗口改变大小从新计算大图宽度、高度
		getPlace();
	});
	
	// 头部下拉菜单
	$(".comSite200").hover(function(){
		$(this).find(".comSite202a").show();
	},function(){
		$(this).find(".comSite202a").hide();
	});
	
	// 热播唱片鼠标悬停
	$(".comSite14a li").hover(function(){
		$(this).find(".comSt53a,.comSt56a").show();
	},function(){
		$(this).find(".comSt53a,.comSt56a").hide();
	});
	// 经典有声鼠标悬停
	$(".comSite14a1 li").hover(function(){
		$(this).find(".comSt53b,.comSt56b").show();
	},function(){
		$(this).find(".comSt53b,.comSt56b").hide();
	});
	// 视频精选鼠标悬停
	$(".comSite21a li").hover(function(){
		$(this).find(".comSt53c,.comSt22a").show();
	},function(){
		$(this).find(".comSt53c,.comSt22a").hide();
	});
});
$(function(){
	// 艺术家左右滚动
	var index = 0;
	var len = $(".comSite29").size();
	var moveLen = $(".comSite29:first").width();
	var t;
	$(".comSite28").width(moveLen*len);
	if(len < 2){
		$(".comSt29").hide();
	}else{
		$(".comSt29").show();
	}
	$(".comSt29a").on("click",function(){
		$(".comSite28").prepend($(".comSite29:last")).css("left",-moveLen+"px").animate({"left":"0"},500);
		if(index == 0){
			index = len;
		}
		index--;
	});
	$(".comSt29b").on("click",function(){
		$(".comSite28").animate({"left":-moveLen+"px"},500,function(){
			$(".comSite28").append($(".comSite29:first")).css("left","0");
		});
		index++;
		if(index == len){
			index = 0;
		}
	});
	// 大图轮播
	var ii = 0;
	var len1 = $(".comSite04 li").size();
	var moveLen1 = $(".comSite04 li:first").width();
	var t1;
	if(len1 < 2){
		$(".comSt288").hide();
	}
	$(".comSt288a").on("click",function(){
		$(".comSite04").prepend($(".comSite04 li:last")).css("left",-moveLen1+"px").animate({"left":"0"},800);
		if(ii == 0){
			ii = len1;
		}
		ii--;
		$(".comSite05 span").removeClass("comOn39").eq(ii).addClass("comOn39");
	});
	$(".comSt288b").on("click",function(){
		$(".comSite04").animate({"left":-moveLen1+"px"},800,function(){
			$(".comSite04").append($(".comSite04 li:first")).css("left","0");
		});
		ii++;
		if(ii == len1){
			ii = 0;
		}
		$(".comSite05 span").removeClass("comOn39").eq(ii).addClass("comOn39");
	});
	$(".comSite312").hover(function(){
		clearInterval(t1);
	},function(){
		t1 = setInterval(function(){
			$(".comSt288b").trigger("click");
		},6000);
	}).trigger("mouseout");
	
	
	
	var G = { // 获取id
	id : function(id){
		return document.getElementById(id);
	}
	}
	var checkForm = { // 表单验证
	telRule : /^(13[0-9]{9}|14[5|7][0-9]{8}|15[0|1|2|3|5|6|7|8|9][0-9]{8}|17[6|7][0-9]{8}|18[0-9]{9})$/,
	mValue : function(){
		return G.id("comSt95").value; // 注册的手机号
	},
	mValue1 : function(){
		return G.id("comSt119").value; // 登录的手机、用户名、邮箱
	},
	mValue2 : function(){
		return G.id("comSt124").value; // 登录的手机、用户名、邮箱
	},
	pwdRule : /\s/, // 空白字符
	mail : function(){
		var mailRule = /([0-9a-zA-Z_-]+@[0-9a-zA-Z]+.[com|cn|net|com.cn])/; // 有点问题 开始 结束
		if(mailRule.test(this.mValue1())){
			return true;
		}
	}, // 邮箱
	uname : function(){
		var unameRule = /^[0-9a-zA-Z_-]+$/;
		if(unameRule.test(this.mValue1())){
			return true;
		}
	}, // 用户名
	pValue : function(){
		return G.id("comSt96").value; // 注册的密码
	},
	pValue1 : function(){
		return G.id("comSt121").value; // 登录的密码
	},
	len : function(){
		return G.id("comSt96").value.length;
	},
	len1 : function(){
		return G.id("comSt121").value.length;
	},
	cValue : function(){
		return G.id("checkCode").value; // 注册的验证码
	},
	cValue1 : function(){
		return G.id("comSt128").value; // 绑定的验证码
	},
	tel : function(){
		if(this.telRule.test(this.mValue1())){
			return true;
		}
	},
	mobile : function(){ // 注册的
		if(!this.telRule.test(this.mValue())){ // 这里少一个 如果已注册 提示请勿重复注册
			G.id("comSt104").innerHTML = "请输入正确的手机号码";
			G.id("comSt95").parentNode.className = "comSite118 comOn14";
			return false;
		}else{
			G.id("comSt104").innerHTML = "";
			G.id("comSt95").parentNode.className = "comSite118";
			return true;
		}
	},
// 	mobile1 : function(){ // 登录的
// 		if(!this.telRule.test(this.mValue1())){ // 这里少一个 如果已注册 提示请勿重复注册
// 			G.id("comSt120").innerHTML = "请输入正确的手机号码";
// 			G.id("comSt120").parentNode.firstChild.className = "comSite144 comOn16";
// 			return false;
// 		}else{
// 			G.id("comSt120").innerHTML = "";
// 			G.id("comSt120").parentNode.firstChild.className = "comSite144";
// 			return true;
// 		}
// 		if(!this.mValue1()){
// 			G.id("comSt120").innerHTML = "请输入正确的手机号/邮箱/用户名";
// 			G.id("comSt120").parentNode.firstChild.className = "comSite144 comOn16";
// 		}else{
// 			G.id("comSt120").innerHTML = "";
// 			G.id("comSt120").parentNode.firstChild.className = "comSite144";
// 			return true;
// 		}
// 	},
	mobile2 : function(){ // 绑定的
		if(!this.telRule.test(this.mValue2())){ // 这里少一个 如果已注册 提示请勿重复注册
			G.id("comSt125").innerHTML = "请输入正确的手机号码";
			G.id("comSite151").className = "comSite151 comOn16";
			return false;
		}else{
			G.id("comSt125").innerHTML = "";
			G.id("comSite151").className = "comSite151";
			return true;
		}
	},
	pwd : function(){ // 注册的
		if(this.len() < 6 || this.len() > 16){
			G.id("comSt105").innerHTML = "请输入6-16位密码,区分大小写,不含空白字符";
			G.id("comSt96").parentNode.className = "comSite118 comOn14";
			return false;
		}else if(this.pwdRule.test(this.pValue())){
			G.id("comSt105").innerHTML = "请输入6-16位密码,区分大小写,不含空白字符";
			G.id("comSt96").parentNode.className = "comSite118 comOn14";
			return false;
		}else{
			G.id("comSt105").innerHTML = "";
			G.id("comSt96").parentNode.className = "comSite118";
			return true;
		}
	},
// 	pwd1 : function(){ // 登录的
// 		if(this.len1() < 6 || this.len1() > 16){
// 			G.id("comSt122").innerHTML = "请输入6-16位密码,区分大小写,不含空白字符";
// 			G.id("comSt122").parentNode.firstChild.className = "comSite144 comOn16";
// 			return false;
// 		}else if(this.pwdRule.test(this.pValue1())){
// 			G.id("comSt122").innerHTML = "请输入6-16位密码,区分大小写,不含空白字符";
// 			G.id("comSt122").parentNode.firstChild.className = "comSite144 comOn16";
// 			return false;
// 		}else{
// 			G.id("comSt122").innerHTML = "";
// 			G.id("comSt122").parentNode.firstChild.className = "comSite144";
// 			return true;
// 		}
// 	},
	checkCode : function(){
		if(!this.cValue()){ // 这里少一个验证码和数据库匹配验证
			G.id("comSt107").innerHTML = "请输入正确的验证码";
			G.id("checkCode").parentNode.className = "comSite130 comOn14";
			return false;
		}else{
			G.id("comSt107").innerHTML = "";
			G.id("checkCode").parentNode.className = "comSite130";
			return true;
		}
	},
	checkCode1 : function(){ // 绑定的
		if(!this.cValue1()){ // 这里少一个验证码和数据库匹配验证
			G.id("comSt129").innerHTML = "请输入正确的验证码";
			G.id("comSite154").className = "comSite154 comOn16";
			return false;
		}else{
			G.id("comSt129").innerHTML = "";
			G.id("comSite154").className = "comSite154";
			return true;
		}
	}

}
// G.id("comSt95").onblur = function(){checkForm.mobile();}
// G.id("comSt96").onblur = function(){checkForm.pwd();}
// G.id("checkCode").onblur = function(){checkForm.checkCode();}
// G.id("comSt119").onblur = function(){checkForm.mobile1();}
// G.id("comSt121").onblur = function(){checkForm.pwd1();}
// G.id("comSt124").onblur = function(){checkForm.mobile2();}
// G.id("comSt128").onblur = function(){checkForm.checkCode1();}

	$("#loginout").on("click",function(){
		loginOut(callback);
	});
});
function callback(){
	window.location.href = "/WWWindex.jsp";
}
</script>
</body>
</html>