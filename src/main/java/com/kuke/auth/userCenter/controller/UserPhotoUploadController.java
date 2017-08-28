package com.kuke.auth.userCenter.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import net.coobird.thumbnailator.Thumbnails;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.kuke.auth.login.bean.User;
import com.kuke.auth.regist.service.impl.RegistService;
import com.kuke.auth.util.KuKeAuthConstants;
import com.kuke.auth.util.KuKeUrlConstants;
import com.kuke.auth.util.PropertiesHolder;
import com.kuke.common.utils.ResponseMsg;
import com.kuke.core.base.BaseController;
import com.kuke.core.engine.ICookie;
import com.kuke.core.redis.RedisUtil;
import com.kuke.util.HttpClientUtil;
import com.kuke.util.ImageCut;
import com.kuke.util.PathUtil;
import com.kuke.util.PicClientUtil;



@Controller
@RequestMapping("/kuke/userCenter")
public class UserPhotoUploadController extends BaseController {

	static Logger logger = LoggerFactory.getLogger(UserPhotoUploadController.class);
	
	@Autowired
	private RegistService registService;
	
	@Autowired
	private RedisUtil redisUtil;
	
	/**
	 * 头像上传
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/headImageUpload")
	public @ResponseBody ResponseMsg headImageUpload(MultipartHttpServletRequest request,HttpServletResponse response) {
		Map<String, String> params = getParameterMap(request);
		boolean flag = false;
		String code = "UPLOAD_FAIL";
		String msg = "上传失败";
		String codeDesc = "FORMAT_ERROR:文件类型错误;"
						+ "UPLOAD_SUCCESS:上传成功;"
						+ "SIZE_ERROR:文件大小错误;"
						+ "UPLOAD_FAIL:上传失败;";
		Map<String, String> data = new HashMap<String, String>();
		File saveFile = null;
		try {
			request.setCharacterEncoding("UTF-8");
			String uploadId = request.getParameter("uploadId");
			MultipartFile patch = request.getFile(uploadId);
			String uploadPath = String.valueOf(PropertiesHolder.getContextProperty("user.photo.tmp.url"));
			// 保存的地址
			String savePath = request.getSession().getServletContext().getRealPath(uploadPath);
			// 上传的文件名 
			String uploadFileName = patch.getOriginalFilename();
			// 获取文件后缀名
			String fileType = StringUtils.substringAfterLast(uploadFileName, ".");
			//文件类型判断:jpg|JPG|gif|GIF|png|PNG
			if (String.valueOf(PropertiesHolder.getContextProperty("upload.file.type")).indexOf(fileType) == -1) {
				code = "FORMAT_ERROR";
				msg = "文件类型错误";
			}else{
				logger.debug("上传的文件名：{},文件后缀名：{},文件大小：{}",new Object[] {StringUtils.substringBeforeLast(uploadFileName, "."), fileType, patch.getSize() });
				if(patch.getSize() > 5242880){//5M
					code = "SIZE_ERROR";
					msg = "文件大小错误";
				}else{
					// 图片新名字
					String saveName = UUID.randomUUID().toString();
					// 图片新名字.jpg
					String finalName =  saveName + ("".equals(fileType) ? "" : "." + fileType);
					// 创建文件
					saveFile = new File(savePath + File.separator + finalName);
					// 判断文件夹是否存在，不存在则创建
					if (!saveFile.getParentFile().exists()) {
						saveFile.getParentFile().mkdirs();
					}
					
					// 写入文件
					FileUtils.writeByteArrayToFile(saveFile, patch.getBytes());
					
					//上传图片到服务器临时地址
					boolean uploadFlag = PicClientUtil.uploadFile(KuKeUrlConstants.touploadurl, saveFile , finalName ,"user_tmp");
					System.out.println("uploadFlag:"+uploadFlag);
					if(uploadFlag){
						BufferedImage bi = ImageIO.read(new File(savePath + File.separator + finalName));
						int srcWidth = bi.getWidth(); // 源图宽度
						int srcHeight = bi.getHeight(); // 源图高度
						bi.flush();
						bi = null;
						
						//宽高比
						int[] CSize =null;
						if(srcWidth <= ImageCut.divWidth || srcHeight <= ImageCut.divWidth ){
							CSize = ImageCut.CSize(savePath + File.separator + finalName, srcWidth, srcHeight);
						}else{
							CSize = ImageCut.CSize(savePath + File.separator + finalName, ImageCut.divWidth, ImageCut.divWidth);
						}
						flag = true;
						code = "UPLOAD_SUCCESS";
						msg = "上传成功";
						
						//服务器上的地址
						String path = String.valueOf(PropertiesHolder.getContextProperty("imgUrl")) + String.valueOf(PropertiesHolder.getContextProperty("user.tmp.url")) + finalName;
						
						System.out.println(path);
						data.put("file", path);
						data.put("width", String.valueOf(CSize[0]));
						data.put("height", String.valueOf(CSize[1]));
						// 保存文件的基本信息到本地
						ICookie.set(response, "userPhoto", finalName);
					}
					
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
//			try {
//				if (saveFile != null) {
//					FileUtils.forceDelete(saveFile);
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
		}
		return new ResponseMsg(flag, code, msg, codeDesc, data);
	}
	/**
	 * app头像上传
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/appHeadImageUpload")
	public @ResponseBody ResponseMsg appHeadImageUpload(MultipartHttpServletRequest request,HttpServletResponse response){
		String codeDesc = "1:上传成功;2:上传失败;3:图片类型错误,可上传类型jpg|JPG|gif|GIF|png|PNG;4:图片大小不能大于5M；5：file为空";
		ResponseMsg respMsg = null;
		User user = this.getLoginUser();
		File destFile = null;
		boolean flag = false;
		if(user != null){
			try {
				request.setCharacterEncoding("UTF-8");
				response.setContentType("text/html;charset=UTF-8");
				MultipartFile patch = request.getFile("headImg");
				System.out.println("file是否为空："+patch.isEmpty() != null?"是":patch.getOriginalFilename());
				if(patch == null){
					respMsg = new ResponseMsg(false, "5", "file为空", codeDesc);
				}
				// 上传文件临时地址
				String uploadTemp = PathUtil.getWebRoot() + "/upload/tmp/";
				// 上传文件的名称
				String origFileName = patch.getOriginalFilename();
				// 上传文件后缀名称
				String fileSuffix = StringUtils.substringAfterLast(origFileName ,".");
				// 文件类型判断
				if (String.valueOf("jpg|JPG|gif|GIF|png").indexOf(fileSuffix) == -1) {
					respMsg = new ResponseMsg(false, "3", "图片类型错误,可上传类型jpg|JPG|gif|GIF|png", codeDesc);
				}else if(patch.getSize() > 5242880){
					respMsg = new ResponseMsg(false, "4", "图片大小不能大于5M", codeDesc);
				}else{
					String newFileName = UUID.randomUUID().toString();
					String destFileName = newFileName + ("".equals(fileSuffix) ? "" : "." + fileSuffix);
					destFile = new File(uploadTemp + File.separator + destFileName);
					if (!destFile.getParentFile().exists()) {
						destFile.getParentFile().mkdirs();
					}
					// 写入文件
					FileUtils.writeByteArrayToFile(destFile, patch.getBytes());
					
					//压缩成200*200比例
					Thumbnails.of(uploadTemp + File.separator + destFileName).size(200,200).toFile(uploadTemp + File.separator + destFileName);
					
					//上传图片到服务器
					String touploadurl = "http://Upload.kuke.com/v2upload";
					flag = PicClientUtil.uploadFile(touploadurl, destFile , newFileName + "." + fileSuffix ,"user_photo_200");
					if(flag){
						//1.更新数据库
						registService.updateUserPhoto(user.getUid(), newFileName + "." + fileSuffix);
						//2.更新到缓存
						try{
							String key = "userInfo:" + user.getUid();
							Map<String, String> userMap = new HashMap<String, String>();
							userMap.put("photo", newFileName + "." + fileSuffix);
							this.redisUtil.hashMultipleSet(key, userMap);	
						}catch (Exception e) {
							e.printStackTrace();
						}
						respMsg = new ResponseMsg(true, "1", "上传成功", codeDesc, String.valueOf(PropertiesHolder.getContextProperty("imgUrl"))+String.valueOf(PropertiesHolder.getContextProperty("user.photo.url"))+newFileName + "." + fileSuffix);
					}else{
						respMsg = new ResponseMsg(false, "2", "上传失败", codeDesc);
					}
				}
			}catch(Exception e){
				e.printStackTrace();
				respMsg = new ResponseMsg(false, "2", "上传失败", codeDesc);
			}finally{
				try {
					if(flag){
						if (destFile != null) {
							FileUtils.forceDelete(destFile);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}else{
			respMsg = new ResponseMsg(false,KuKeAuthConstants.NOMALLOGIN, "用户未登陆", codeDesc);
		}
		return respMsg;
	}
	/**
	 * 
	 * @param str
	 * @return
	 */
	private String dealNull(String str){
		if(str == null || "".equals(str.trim()) || "null".equals(str.trim())){
			str = "";
		}
		return str.trim();
	}
}
