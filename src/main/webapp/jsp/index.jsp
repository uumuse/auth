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
<link href="/css/init.css" rel="stylesheet">
<link href="/css/main.css" rel="stylesheet">
<script type="text/javascript" src="/js/jquery-1.12.4.min.js"></script>
<script type="text/javascript" src="/js/jquery.qrcode.min.js"></script>
<script type="text/javascript" src="/js/jquery.blockUI.js"></script>
<script type="text/javascript" src="/js/login/login1.js"></script>
<script type="text/javascript" src="/js/login/regist.js"></script>
<script type="text/javascript">
$(function(){
	// 头部下拉菜单
	$(".comSite200").hover(function(){
		$(this).find(".comSite202a").show();
	},function(){
		$(this).find(".comSite202a").hide();
	});
	//
	$("#loginout").on("click",function(){
		loginOut(callback);
	});
});
function querySysMsg(){//跳转到消息中心 
	window.location.href = '/kuke/userCenter/getSysMessageList'; 
} 
function callback(){
	window.location.href = '/jsp/index.jsp'; 
} 
</script>  
</head>
<body>
<div class="comSite comWd01">
	<div class="comSite comTop01 cf">
		<div class="comSt1a fl">
			<a href="/jsp/index.jsp" class="comSt1"><img src="/images/logo.png" alt="" width="87" height="27"></a>
			<img id="orgLogoImg" src="" alt="" height="27" class="comSt1b">
		</div>
		<div class="comSite1 cf fl">
			<input type="text" placeholder="单曲／专辑／艺术家／视频" class="comSt2 fl">
			<a href="#" title="搜索" class="comSt3 fl"></a>
		</div>
		<div class="comSite2 fr cf">
			<a href="/kuke/userCenter/vipCenter" class="comSt6 fr">库客会员</a>
<!-- 			 未登录  -->
			<div id="notLogin" class="comSite fr comSt405" style="display:none">
				<a href="javascript:void(0)" class="comSt4" onclick="showLogin()">登录</a>
				<span class="comSt5">|</span>
				<a href="javascript:void(0)" class="comSt4" onclick="regist()">注册</a>
			</div>
<!-- 			 已登录  -->
			<div id="Login" class="comSite200 pr" style="display:none">
				<div class="comSite cf">
					<div class="comSite201"><img id="userImage" alt="" width="24" height="24"></div>
					<p class="comSt202" id="nickname"></p>
				</div>
				<div class="comSite202a">
					<div class="comSite202 cf">
						<a href="/kuke/userCenter/getFavoriteTrack">我的</a>
						<a href="/kuke/userCenter/userInf">个人设置</a>
						<a href="/kuke/userCenter/userAccount">账户设置</a>
						<a href="/kuke/userCenter/vipCenter">会员中心</a>
						<a href="javascript:void(0)" id="loginout">退出</a>
					</div>
				</div>
			</div>
			<div id="LoginMsg" class="comSite199 pr" onclick="querySysMsg();" style="display: none;"><a id="sysmsgid" href="javascript:void(0)" class="comSt168">${syscount}</a></div>
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
		<div class="comSt8b fr pr">
			<a href="#" class="comSt8">下载库客音乐APP</a>
			<div class="comSite621">
				<div class="comSite622">
					<h1><span>——</span> 扫描二维码 <span>——</span></h1>
					<p>聆听更多古典音乐</p>
					<div class="comSite623"><img src="/images/qr-download.png" alt="" width="104" height="104"></div>
				</div>
			</div>
		</div>
	</div>
</div>
<!--  播放器  -->
<div class="comSite312 pr">
	<ol class="comSite04 cf">
		<li>
			<a href="#" class="comSite313 pr">
				<div><img src="/images/homebanner.jpg" alt="" width="100%"></div>
				<h2>j.s.巴赫杰作《勃兰登堡协奏曲》</h2>
				<p>六首风格迥异的协奏曲，展现复调音乐的无限魅力</p>
			</a>
		</li>
		<li>
			<a href="#" class="comSite313 pr">
				<div><img src="/images/homebanner.jpg" alt="" width="100%"></div>
				<h2>j.s.巴赫杰作《勃兰登堡协奏曲》</h2>
				<p>六首风格迥异的协奏曲，展现复调音乐的无限魅力</p>
			</a>
		</li>
		<li>
			<a href="#" class="comSite313 pr">
				<div><img src="/images/homebanner.jpg" alt="" width="100%"></div>
				<h2>j.s.巴赫杰作《勃兰登堡协奏曲》</h2>
				<p>六首风格迥异的协奏曲，展现复调音乐的无限魅力</p>
			</a>
		</li>
		<li>
			<a href="#" class="comSite313 pr">
				<div><img src="/images/homebanner.jpg" alt="" width="100%"></div>
				<h2>j.s.巴赫杰作《勃兰登堡协奏曲》</h2>
				<p>六首风格迥异的协奏曲，展现复调音乐的无限魅力</p>
			</a>
		</li>
		<li>
			<a href="#" class="comSite313 pr">
				<div><img src="/images/homebanner.jpg" alt="" width="100%"></div>
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
<!--  主内容  -->
<div class="comSite comWd01">
	<div class="comSite12 cf">
		<div class="comSite25 fr">
			<div class="comSite">
				<h1 class="comSt28"><span>——</span> 艺术家 <span>——</span></h1>
				<div class="comSite26 pr">
					<div class="comSite27 pr">
						<div class="comSite28 cf">
							<div class="comSite29 fl">
								<ol class="cf">
									<li>
										<a href="#" class="comSite30 pr">
											<div class="comSite30bg"><img src="/images/comSite30.png" alt="" width="58" height="58"></div>
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
											<div class="comSite30bg"><img src="/images/comSite30.png" alt="" width="58" height="58"></div>
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
											<div class="comSite30bg"><img src="/images/comSite30.png" alt="" width="58" height="58"></div>
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
							<div class="comSite29 fl">
								<ol class="cf">
									<li>
										<a href="#" class="comSite30 pr">
											<div class="comSite30bg"><img src="/images/comSite30.png" alt="" width="58" height="58"></div>
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
											<div class="comSite30bg"><img src="/images/comSite30.png" alt="" width="58" height="58"></div>
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
											<div class="comSite30bg"><img src="/images/comSite30.png" alt="" width="58" height="58"></div>
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
							<div class="comSite29 fl">
								<ol class="cf">
									<li>
										<a href="#" class="comSite30 pr">
											<div class="comSite30bg"><img src="/images/comSite30.png" alt="" width="58" height="58"></div>
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
											<div class="comSite30bg"><img src="/images/comSite30.png" alt="" width="58" height="58"></div>
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
											<div class="comSite30bg"><img src="/images/comSite30.png" alt="" width="58" height="58"></div>
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
								<div><img src="/images/yueqi-jita.jpg" alt="" width="100%"></div>
								<p>吉他</p>
							</a>
						</li>
						<li class="comSite33">
							<a href="#" class="comSite34">
								<div><img src="/images/yueqi-yuanhao.jpg" alt="" width="100%"></div>
								<p>圆号</p>
							</a>
						</li>
						<li class="comSite33">
							<a href="#" class="comSite34">
								<div><img src="/images/yueqi-qiaojiyue.jpg" alt="" width="100%"></div>
								<p>敲击乐</p>
							</a>
						</li>
					</ol>
					<ol class="cf">
						<li class="comSite33">
							<a href="#" class="comSite34">
								<div><img src="/images/yueqi-changhao.jpg" alt="" width="100%"></div>
								<p>长号</p>
							</a>
						</li>
						<li class="comSite33">
							<a href="#" class="comSite34">
								<div><img src="/images/yueqi-gangqin.jpg" alt="" width="100%"></div>
								<p>钢琴</p>
							</a>
						</li>
						<li class="comSite33">
							<a href="#" class="comSite34">
								<div><img src="/images/yueqi-xiaohao.jpg" alt="" width="100%"></div>
								<p>小号</p>
							</a>
						</li>
					</ol>
					<ol class="cf">
						<li class="comSite33">
							<a href="#" class="comSite34">
								<div><img src="/images/yueqi-xiaohao1.jpg" alt="" width="100%"></div>
								<p>小号</p>
							</a>
						</li>
						<li class="comSite33">
							<a href="#" class="comSite34">
								<div><img src="/images/yueqi-pipa.jpg" alt="" width="100%"></div>
								<p>琵琶</p>
							</a>
						</li>
						<li class="comSite33">
							<a href="#" class="comSite34">
								<div><img src="/images/yueqi-xiaotiqin.jpg" alt="" width="100%"></div>
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
						<li><a href=""><img src="/images/img-changpai.jpg" alt="" width="128" height="108"></a></li>
						<li><a href=""><img src="/images/img-changpai.jpg" alt="" width="128" height="108"></a></li>
					</ol>
					<ol class="comSite315 cf">
						<li><a href=""><img src="/images/img-changpai.jpg" alt="" width="128" height="108"></a></li>
						<li><a href=""><img src="/images/img-changpai.jpg" alt="" width="128" height="108"></a></li>
					</ol>
					<ol class="comSite315 cf">
						<li><a href=""><img src="/images/img-changpai.jpg" alt="" width="128" height="108"></a></li>
						<li><a href=""><img src="/images/img-changpai.jpg" alt="" width="128" height="108"></a></li>
					</ol>
					<ol class="comSite315 cf">
						<li><a href=""><img src="/images/img-changpai.jpg" alt="" width="128" height="108"></a></li>
						<li><a href=""><img src="/images/img-changpai.jpg" alt="" width="128" height="108"></a></li>
					</ol>
					<ol class="comSite315 cf">
						<li><a href=""><img src="/images/img-changpai.jpg" alt="" width="128" height="108"></a></li>
						<li><a href=""><img src="/images/img-changpai.jpg" alt="" width="128" height="108"></a></li>
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
								<div><a href="#"><img src="/images/comSite16.jpg" alt="" width="210" height="210"></a></div>
								<span class="comSt53 comSt53a"></span>
								<span class="comSt56 comSt56a"></span>
							</div>
							<h2 class="comSite17"><a href="">百老汇金曲20首20 Best of Broadway百老汇金曲20首</a></h2>
						</li>
						<li>
							<div class="comSite16 pr">
								<div><a href="#"><img src="/images/comSite16.jpg" alt="" width="210" height="210"></a></div>
								<span class="comSt53 comSt53a"></span>
								<span class="comSt56 comSt56a"></span>
							</div>
							<h2 class="comSite17"><a href="">百老汇金曲20首20 Best of Broadway百老汇金曲20首</a></h2>
						</li>
						<li>
							<div class="comSite16 pr">
								<div><a href="#"><img src="/images/comSite16.jpg" alt="" width="210" height="210"></a></div>
								<span class="comSt53 comSt53a"></span>
								<span class="comSt56 comSt56a"></span>
							</div>
							<h2 class="comSite17"><a href="">百老汇金曲20首20 Best of Broadway百老汇金曲20首</a></h2>
						</li>
						<li>
							<div class="comSite16 pr">
								<div><a href="#"><img src="/images/comSite16.jpg" alt="" width="210" height="210"></a></div>
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
						<div class="comSite319"><img src="/images/jiaoxiangzuopin.png" width="51" height="52"></div>
						<span class="comWd08">交响作品</span>
					</a>
					<a href="#" class="comSite318a1">
						<div class="comSite319"><img src="/images/langmanjinqu.png" width="51" height="52"></div>
						<span class="comWd08">浪漫金曲</span>
					</a>
					<a href="#" class="comSite318a1">
						<div class="comSite319"><img src="/images/gudianzhuyishiqi.png" width="51" height="52"></div>
						<span class="comWd10">古典主义时期</span>
					</a>
					<a href="#" class="comSite318a1">
						<div class="comSite319"><img src="/images/qingyinyue.png" width="51" height="52"></div>
						<span class="comWd07">轻音乐</span>
					</a>
					<a href="#" class="comSite318a1">
						<div class="comSite319"><img src="/images/dangdaijueshiyue.png" width="51" height="52"></div>
						<span class="comWd09">当代爵士乐</span>
					</a>
					<a href="#" class="comSite318a1">
						<div class="comSite319"><img src="/images/shijieminzuyinyue.png" width="51" height="52"></div>
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
								<div><img src="/images/comSite21.jpg" alt="" width="283" height="159"></div>
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
								<div><img src="/images/comSite21.jpg" alt="" width="283" height="159"></div>
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
								<div><img src="/images/comSite21.jpg" alt="" width="283" height="159"></div>
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
								<div><a href="#"><img src="/images/comSite16.jpg" alt="" width="210" height="210"></a></div>
								<span class="comSt53 comSt53b"></span>
								<span class="comSt56 comSt56b"></span>
							</div>
							<h2 class="comSite17"><a href="">百老汇金曲20首20 Best of Broadway百老汇金曲20首</a></h2>
						</li>
						<li>
							<div class="comSite16 pr">
								<div><a href="#"><img src="/images/comSite16.jpg" alt="" width="210" height="210"></a></div>
								<span class="comSt53 comSt53b"></span>
								<span class="comSt56 comSt56b"></span>
							</div>
							<h2 class="comSite17"><a href="">百老汇金曲20首20 Best of Broadway百老汇金曲20首</a></h2>
						</li>
						<li>
							<div class="comSite16 pr">
								<div><a href="#"><img src="/images/comSite16.jpg" alt="" width="210" height="210"></a></div>
								<span class="comSt53 comSt53b"></span>
								<span class="comSt56 comSt56b"></span>
							</div>
							<h2 class="comSite17"><a href="">百老汇金曲20首20 Best of Broadway百老汇金曲20首</a></h2>
						</li>
						<li>
							<div class="comSite16 pr">
								<div><a href="#"><img src="/images/comSite16.jpg" alt="" width="210" height="210"></a></div>
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
<!-- 公共尾部 -->
<div class="comSite442">
	<div class="comSite443">
		<div class="comSite444">
			<ol>
				<li><img src="/images/logo01.png" alt="" width="" width="84" height="32"></li>
				<li><img src="/images/logo02.png" alt="" width="" width="46" height="32"></li>
				<li class="comSt344">|</li>
				<li><img src="/images/logo03.png" alt="" width="" width="223" height="32"></li>
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
</body>
</html>