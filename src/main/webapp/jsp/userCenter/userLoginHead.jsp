<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="comSite comWd01">
	<div class="comSite comTop01 cf">
		<div class="comSt1a fl">
			<a href="javascript:void(0)" onclick="goToIndex();" class="comSt1 fl"><img src="/images/logo.png" alt="首页"></a>
			<c:if test="${!empty orgphoto}"><img id="orgLogoImg" src="${orgphoto}" alt="" height="27" class="comSt1b"></c:if>
		</div>
		<form id="searchForm" action="${wwwurl}/search" type="get" onkeydown="if(window.event.keyCode == 13){return false;}">
			<div class="comSite1 cf fl">
				<input type="text" id="keyWord" name="keyWord" placeholder="单曲／专辑／艺术家／视频" class="comSt2 fl" value="${keyWord }" autocomplete="off">
				<a id="searchSubmit" href="javascript:;" title="搜索" class="comSt3 fl" onclick="submitSearch()"></a>
				<input type="hidden" id="searchType" name="searchType" value="${searchType }">
				<div class="comSite5960" id="searchList" style="z-index:100;">
					<div class="comSite5970 cf" id="trackCf">
						<div class="comSite5980 pr"><span class="comSite5980a1">单曲</span></div>
						<div class="comSite5990">
							<div class="comSite6000" id="trackSearchList">
							</div>
						</div>
					</div>
					<div class="comSite5970 cf" id="cataCf">
						<div class="comSite5980 pr"><span class="comSite5980a2">唱片</span></div>
						<div class="comSite5990">
							<div class="comSite6000" id="cataSearchList">
							</div>
						</div>
					</div>
					<div class="comSite5970 cf" id="artistCf">
						<div class="comSite5980 pr"><span class="comSite5980a3">艺术家</span></div>
						<div class="comSite5990">
							<div class="comSite6000" id="artistSearchList">
							</div>
						</div>
					</div>
					<div class="comSite5970 cf" id="videoCf">
						<div class="comSite5980 pr"><span class="comSite5980a5">视频</span></div>
						<div class="comSite5990">
							<div class="comSite6000" id="videoSearchList">
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
		<div class="comSite2 fr cf">
			<a href="/kuke/userCenter/vipCenter" class="comSt6 fr">库客会员</a>
			<c:if test="${empty uid}">
				<!-- 未登录 -->
				<div class="comSite fr comSt405">
					<a href="javascript:void(0)" class="comSt4" onclick="showLogin()">登录</a>
					<span class="comSt5">|</span>
					<a href="javascript:void(0)" class="comSt4" onclick="showLogin()">注册</a>
				</div>
			</c:if>
			<c:if test="${!empty uid}">
				<!-- 已登录 -->
				<div class="comSite200 pr">
					<div class="comSite cf">
						<div class="comSite201"><img  <c:if test="${empty uphoto}">src="/images/default_headimg_s.jpg"</c:if> <c:if test="${!empty uphoto}">src="${uphoto}"</c:if>  alt="" width="24" height="24"></div>
						<p class="comSt202" id="nickname">${username}</p>
					</div>
					<div class="comSite202a">
						<div class="comSite202 cf">
							<a href="/kuke/userCenter/getFavoriteTrack">我的</a>
							<a href="/kuke/userCenter/userInf">个人设置</a>
							<a href="/kuke/userCenter/userAccount">账户设置</a>
							<a href="/kuke/userCenter/vipCenter">会员中心</a>
							<a href="javascript:void(0)" onclick="logout(callback);">退出</a>
						</div>
					</div>
				</div>
				<div class="comSite199 pr" onclick="querySysMsg();" style="cursor: pointer;">
						<c:if test="${syscount == 0}">
						</c:if>
						<c:if test="${syscount != 0}">
							<a href="javascript:void(0)" class="comSt168">${syscount}</a>
						</c:if>
				</div>
			</c:if>
		</div>
	</div>
	<div class="comSite comTop02 cf">
		<h1 class="comSt7 fl" style="cursor: pointer;" onclick="window.location.href = '${wwwurl}/artcenter';">库客·艺术中心</h1>
		<div class="comSite3 fl">
			<ol class="cf">
				<li><a href="${wwwurl}/albums">唱片</a></li>
				<li><a href="${wwwurl}/videos">视频</a></li>
				<li><a href="${wwwurl}/lives">剧院</a></li>
				<li><a href="${wwwurl}/spokens/0_1">有声读物</a></li>
				<li><a href="${wwwurl}/mbooks">乐谱</a></li>
			</ol>
		</div>
		<div class="comSt8b fr pr">
			<a href="${wwwurl}/download" class="comSt8 comSt8b1">下载库客音乐APP</a>
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
<div class="comTcBg" style="display: none;"></div>
<div id="tips" class="comSite226 comNotice08">
	<p id="tipsMsg" class="comSt201 comSt201a"></p>
</div>
<div class="comSite226 comNotice09">
	<p class="comSt201">已添加到播放列表</p>
</div>
<div id="confirmdiv" class="comTc comTc02">
	<div class="comTcTop pr"><h1>提示</h1><a href="javascript:void(0)" title="关闭" class="comTcSt01"></a></div>
	<div class="comTcCon">
		<div id="confirmdivtitle" class="comTcSite06"><p></p></div>
		<div class="comTcSite05 cf comTcPd02">
			<a id="confirmdivclick" href="javascript:void(0)" class="comTcSt06">确定</a>
			<a href="javascript:void(0)" class="comTcSt07">取消</a>
		</div>
	</div>
</div>
<div id="toVIPTip" class="comTc comTc02">
	<div class="comTcTop pr"><h1>提示</h1><a href="javascript:void(0)" title="关闭" class="comTcSt01"></a></div>
	<div class="comTcCon">
		<div class="comTcSite06"><p>您没有下载权限，是否开通？</p></div>
		<div class="comTcSite05 cf comTcPd02">
			<a href="javascript:void(0)" class="comTcSt06" onclick="window.location.href='/kuke/userCenter/vipProInfo'">是</a>
			<a href="javascript:void(0)" class="comTcSt07" >不，谢谢</a>
		</div>
	</div>
</div>
<div id="HasBuyed" class="comTc comTc02">
	<div class="comTcTop pr"><h1>提示</h1><a href="javascript:void(0)" title="关闭" class="comTcSt01"></a></div>
	<div class="comTcCon">
		<div class="comTcSite06"><p>已经购买，是否直接下载？</p></div>
		<div class="comTcSite05 cf comTcPd02">
			<a id="downloada" href="javascript:void(0)" class="comTcSt06">下载</a>
			<a id="tobilla" href="javascript:void(0)" class="comTcSt07">去订单中心</a>
		</div>
	</div>
</div>
<div id="pay" class="comTc comTc08"><!-- 支付页下载弹层 -->
	<div class="comTcTop pr"><h1>下载</h1><a href="javascript:void(0)" title="关闭" class="comTcSt01"></a></div>
	<div class="comTcCon">
		<div class="comSite365">
			<div class="comSite366 cf">
				<div class="comSite367"><img id="downimg" src="/images/comSite16.jpg" width="60" height="60"></div>
				<div class="comSite368">
					<p class="comSite369" id="downname"></p>
				</div>
			</div>
			<div class="comSite370">
				<p class="comSite371">下载需要：<span id="downprice">10元</span></p>
				<a id="downhref" href="" class="comSt303 comSt304">立即支付</a>
			</div>
		</div>
	</div>
</div>
<!-- 支付页用户所在机构下载弹层 -->
<div id="orgPay" class="comTc comTc08" style="z-index:101;">
	<div class="comTcTop pr"><h1>下载</h1><a href="javascript:void(0)" title="关闭" class="comTcSt01"></a></div>
	<div class="comTcCon">
		<div class="comSite365">
			<div class="comSite366 cf">
				<div class="comSite367"><img id="orgPayDownImg" src="/images/comSite16.jpg" width="60" height="60"></div>
				<div class="comSite368">
					<p class="comSite369" id="orgPayDownName"></p>
				</div>
			</div>
			<div class="comSite370">
			<!-- 您所在的机构已开通下载，是否立即下载？下载需要：X元 使用机构钱下载是否支付 -->
				<p class="comSite371c1" style="margin-bottom:10px;font-size:13px;">您所在的机构已开通下载，是否立即下载？</p>
				<p class="comSite371">48小时之内下载有效</p>
				<div class="comTcSite05 cf comTcPd02" style="padding-top: 25px;">
					<a id="orgDownHref" href="javascript:void(0)" class="comTcSt06">确定</a><!-- orgPayDownload -->
					<a href="javascript:void(0)" class="comTcSt07">取消</a>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- 添加到唱片夹 -->
<div id="addtocatalFolder" class="comTc comTc04">
	<div class="comTcTop pr"><h1>添加到唱片夹</h1><a href="javascript:void(0)" title="关闭" class="comTcSt01"></a></div>
	<div class="comTcCon">
		<div class="comTcSite10">
			<div class="comTcSite14 cf"><span class="comTcSt11" onclick="addToFolderCreate('2');">新建唱片夹</span></div>
			<div class="comTcSite11 cf">
			</div>
		</div>
		<div class="comTcSite05 cf comTcPd04">
			<a href="javascript:void(0)" class="comTcSt06">确定</a>
			<a href="javascript:void(0)" class="comTcSt07">取消</a>
		</div>
	</div>
</div>
<!-- 添加到单曲夹 -->
<div id="addtotrackFolder" class="comTc comTc04">
	<div class="comTcTop pr"><h1>添加到单曲夹</h1><a href="javascript:void(0)" title="关闭" class="comTcSt01"></a></div>
	<div class="comTcCon">
		<div class="comTcSite10">
			<div class="comTcSite14 cf"><span class="comTcSt11" onclick="addToFolderCreate('1');">新建单曲夹</span></div>
			<div class="comTcSite11 cf">
			</div>
		</div>
		<div class="comTcSite05 cf comTcPd04">
			<a href="javascript:void(0)" class="comTcSt06">确定</a>
			<a href="javascript:void(0)" class="comTcSt07">取消</a>
		</div>
	</div>
</div>
<!-- 创建新唱片夹 -->
<div id="catalCreatediv" class="comTc comTc01">
	<div class="comTcTop pr"><h1>创建新唱片夹</h1><a href="javascript:void(0)" title="关闭" class="comTcSt01"></a></div>
	<div class="comTcCon">
		<div class="comTcSite01 cf">
			<span class="comTcSt02"></span>
			<div class="comTcSite02 pr">
				<input id="catalfoldername" type="text" class="comTcSt03" value="">
				<span class="comTcSt04">20</span>
			</div>
		</div>
		<div class="comTcSite05 cf comTcPd01">
			<a href="javascript:void(0)" id="createcatal" class="comTcSt06" onclick="doCreate('2','F');" >确定</a>
			<a href="javascript:void(0)" class="comTcSt07">取消</a>
		</div>
	</div>
</div>
<!-- 创建新单曲夹 -->
<div id="trackCreatediv" class="comTc comTc01">
	<div class="comTcTop pr"><h1>创建新单曲夹</h1><a href="javascript:void(0)" title="关闭" class="comTcSt01"></a></div>
	<div class="comTcCon">
		<div class="comTcSite01 cf">
			<span class="comTcSt02"></span>
			<div class="comTcSite02 pr">
				<input id="trackfoldername" type="text" class="comTcSt03" value="">
				<span class="comTcSt04">20</span>
			</div>
		</div>
		<div class="comTcSite05 cf comTcPd01">
			<a href="javascript:void(0)" id="createtrack" class="comTcSt06" onclick="doCreate('1','F');" >确定</a>
			<a href="javascript:void(0)" class="comTcSt07">取消</a>
		</div>
	</div>
</div>
<!-- 删除唱片夹 -->
<div id="cataldeltip" class="comTc comTc02">
	<div class="comTcTop pr"><h1>提示</h1><a href="javascript:void(0)" title="关闭" class="comTcSt01"></a></div>
	<div class="comTcCon">
		<div class="comTcSite06"><p>确定要删除该唱片夹?</p></div>
		<div class="comTcSite05 cf comTcPd02">
			<a href="javascript:void(0)" class="comTcSt06" onclick="doDel('2');">确定</a>
			<a href="javascript:void(0)" class="comTcSt07">取消</a>
		</div>
	</div>
</div>
<!-- 删除单曲夹 -->
<div id="trackdeltip" class="comTc comTc02">
	<div class="comTcTop pr"><h1>提示</h1><a href="javascript:void(0)" title="关闭" class="comTcSt01"></a></div>
	<div class="comTcCon">
		<div class="comTcSite06"><p>确定要删除该单曲夹</p></div>
		<div class="comTcSite05 cf comTcPd02">
			<a href="javascript:void(0)" class="comTcSt06" onclick="doDel('1');">确定</a>
			<a href="javascript:void(0)" class="comTcSt07">取消</a>
		</div>
	</div>
</div>
<!-- 夹子上传文件 -->
<div class="comTc comTc03">
	<div class="comTcTop pr"><h1>编辑封面</h1><a href="javascript:void(0)" title="关闭" class="comTcSt01"></a></div>
	<div class="comTcCon">
		<div class="comTcSite07 cf">
			<div class="comTcSite08 pr"><span class="comTcSt08">上传封面</span><input id="uploadPhoto" name="uploadPhoto" type="file" class="comTcSt09" onchange="uploadImg('${userFolder.id}');"></div>
			<p class="comTcSt10">支持jpg、gif、png格式图片，文件小于5M</p>
		</div>
		<div class="comTcSite09"><img id="tempFolderImage" src="" alt="" width="300" height="300"></div>
		<div class="comTcSite05 cf comTcPd03">
			<a href="javascript:void(0)" class="comTcSt06" onclick="saveImg(${musicfolder_id});">确定</a>
			<a href="javascript:void(0)" class="comTcSt07">取消</a>
		</div>
	</div>
</div>
<!-- 微信分享 -->
<div id="qrcodeshare" class="comTc comTc10" style="display: none;">
	<div class="comSite596 pr"><a href="javascript:void(0)" title="关闭" class="comTcSt01"></a></div>
	<div class="comSite597">
		<p class="comSt406">打开微信“扫一扫”，网页打开后点击屏幕右上角分享按钮</p>
		<div class="comSite598"><div id="divweixinimg"></div></div>
	</div>
</div>
<a href="" download disable="true" id="download_a" style="display : none;"></a>
<script type="text/javascript" src="/js/jquery.qrcode.min.js"></script>
<script type="text/javascript" src="/js/jquery.blockUI.js"></script>
<script type="text/javascript" src="/js/common/common.js"></script>
<script type="text/javascript" src="/js/common/util.js"></script>
<script type="text/javascript" src="/js/login/login.js"></script>
<script type="text/javascript" src="/js/login/regist.js"></script>
<script type="text/javascript" src="/js/search.js"></script>
<c:if test="${noPlayUrl != '1'}">
	<script type="text/javascript" src="${wwwurl}/js/application.js"></script>
	<script type="text/javascript" src="${wwwurl}/js/mplay.js"></script>
	<script type="text/javascript" src="${wwwurl}/js/addPlaylist.js"></script>
</c:if>
