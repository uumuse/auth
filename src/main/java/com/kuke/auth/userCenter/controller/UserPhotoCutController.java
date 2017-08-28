package com.kuke.auth.userCenter.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kuke.auth.log.util.LogUtil;
import com.kuke.auth.regist.service.impl.RegistService;
import com.kuke.auth.ssologin.util.UserAuthUtils;
import com.kuke.auth.util.KuKeAuthConstants;
import com.kuke.auth.util.KuKeUrlConstants;
import com.kuke.auth.util.PropertiesHolder;
import com.kuke.common.utils.ResponseMsg;
import com.kuke.core.base.BaseController;
import com.kuke.core.engine.ICookie;
import com.kuke.core.redis.RedisUtil;
import com.kuke.util.ImageCut;
import com.kuke.util.PicClientUtil;
import com.kuke.util.SaveImage;

@Controller
@RequestMapping("/kuke/userCenter")
public class UserPhotoCutController extends BaseController {

	static Logger logger = LoggerFactory.getLogger(UserPhotoCutController.class);
	
	@Autowired
	private RegistService registService;
	
	@Autowired
	private RedisUtil redisUtil;
	/**
	 * 切图
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/headImageUpdate")
	public @ResponseBody ResponseMsg processSubmit(HttpServletRequest request,HttpServletResponse response) {
		boolean flag = false;
		String code = "FAILED";
		String msg = "保存失败";
		String codeDesc = "NOMALLOGIN:用户未登录;"
						+ "FAILED:保存失败;"
						+ "SUCCESS:保存成功;";
		File tempImgFile = null;
		File realImgFile = null;
		try {
			request.setCharacterEncoding("UTF-8");
			com.kuke.auth.login.bean.User user = this.getLoginUser();
			if (KuKeAuthConstants.SUCCESS.equals("SUCCESS")) {
				String x = request.getParameter("x");
				String y = request.getParameter("y");
				String w = request.getParameter("w");
				String h = request.getParameter("h");
				// 图片的tmp地址
				String tmpPath = String.valueOf(PropertiesHolder.getContextProperty("imgUrl")) + String.valueOf(PropertiesHolder.getContextProperty("user.tmp.url"));
				String tmpName = ICookie.get(request, "userPhoto");
				String tmpUrl = tmpPath + tmpName;
				tempImgFile = new File(tmpUrl);
				
				System.out.println(request.getSession().getServletContext().getRealPath(String.valueOf(PropertiesHolder.getContextProperty("user.photo.tmp.url")))+"/test.jpg");
				SaveImage.saveImageToDisk(tmpUrl, request.getSession().getServletContext().getRealPath(String.valueOf(PropertiesHolder.getContextProperty("user.photo.tmp.url")))+"/test.jpg");
				
				// 用户的地址
				String savePath = String.valueOf(PropertiesHolder.getContextProperty("user.photo.tmp.url"));
				String saveName = UUID.randomUUID().toString() + "."+StringUtils.substringAfterLast(tmpName, ".");;
				String saveUrl = request.getSession().getServletContext().getRealPath(savePath + saveName);
				//切图
				if (ImageCut.abscut(request.getSession().getServletContext().getRealPath(String.valueOf(PropertiesHolder.getContextProperty("user.photo.tmp.url")))+"/test.jpg", saveUrl, Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(w), Integer.parseInt(h))) {
					//准备上传的文件
					realImgFile = new File(saveUrl);
					//上传图片到服务器
					boolean uploadFlag = PicClientUtil.uploadFile(KuKeUrlConstants.touploadurl, realImgFile , saveName ,"user_photo_200");
					System.out.println("uploadFlag:"+uploadFlag);
					if(uploadFlag){//上传成功
						//更新数据库
						registService.updateUserPhoto(user.getUid(), saveName);
						//更新到缓存
						try{
							String key = UserAuthUtils.getInstance().getUserInfoKey(user.getUid());
							Map<String, String> userMap = new HashMap<String, String>();
							userMap.put("photo", saveName);
							this.redisUtil.hashMultipleSet(key, userMap);	
						}catch (Exception e) {
							e.printStackTrace();
						}
						flag = true;
						code = "SUCCESS";
						msg = "保存成功";
						ICookie.clear(response, "userPhoto");
						
						//记录日志
						LogUtil.addModUserInfoLog(request);
					}
				}
			}else{
				code = "NOMALLOGIN";
				msg = "用户未登录";
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
//			if(flag){//成功则删除本地文件
//				try {
//					if (realImgFile != null) {
//						FileUtils.forceDelete(realImgFile);
//					}
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
		}
		return new ResponseMsg(flag, code, msg, codeDesc);
	}
	
}
