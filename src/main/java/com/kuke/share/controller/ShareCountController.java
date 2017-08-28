package com.kuke.share.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kuke.auth.util.KuKeAuthConstants;
import com.kuke.common.utils.ResponseMsg;
import com.kuke.core.base.BaseController;
import com.kuke.core.util.DateUtil;
import com.kuke.share.service.ShareCountService;
import com.kuke.util.MessageFormatUtil;
import com.kuke.util.StringUtil;

@Controller
@RequestMapping("/kuke/share")
public class ShareCountController extends BaseController{
	
	@Autowired
	private ShareCountService shareCountService;
	/**
	 * 得到用户当月可分享次数
	 */
	@RequestMapping("/getCurMonthShareTimes")
	public @ResponseBody Object getCurMonthShareTimes(HttpServletRequest request,HttpServletResponse response){
		if(this.getLoginUser() != null){
			try {
				String user_id = this.getLoginUser().getUid();
				String l_code = StringUtil.dealNull(request.getParameter("l_code"));
				int total = 5;
				Map<String,String> map = new HashMap<String, String>();
				String date = DateUtil.getFirstDayOfMonth(new Date());
				if(l_code==null||l_code.equals("")){
					String count = shareCountService.getCurMonthShareCount(user_id, "", date);
					count = count==null?"0":count;
					map.put("times", (total-Integer.parseInt(count)+""));
					return new ResponseMsg(true, "1", "分享次数可用", "1:分享次数可用,2:分享次数已用完",map); 
				}
				else{
					String tcount = shareCountService.getCurMonthShareCount(user_id, "", date);
					String count = shareCountService.getCurMonthShareCount(user_id, l_code, date);
					int tcounter = Integer.parseInt(tcount);
					int counter = Integer.parseInt(count);
					if(Integer.parseInt(tcount)<total){
						if(counter>0&&counter<total){
							shareCountService.updateShareCount(user_id, l_code);
							tcounter ++;
							map.put("times", total-tcounter+"");
							if(total-tcounter == 0){
								map.put("times", "0");
								return new ResponseMsg(false, "2", "分享次数已用完", "1:分享次数可用,2:分享次数已用完",map);
							}else
							return new ResponseMsg(true, "1", "分享次数可用", "1:分享次数可用,2:分享次数已用完",map);
						}else if(counter == 0){
							shareCountService.insertShare(user_id, l_code);
							count = shareCountService.getCurMonthShareCount(user_id, "", date);
							map.put("times", total-Integer.parseInt(count==null?"1":count)+"");
							return new ResponseMsg(true, "1", "分享次数可用", "1:分享次数可用,2:分享次数已用完",map);
						}else{
							map.put("times", "0");
							return new ResponseMsg(false, "2", "分享次数已用完", "1:分享次数可用,2:分享次数已用完",map);
							
						}
					}else{
						map.put("times", "0");
						return new ResponseMsg(false, "2", "分享次数已用完", "1:分享次数可用,2:分享次数已用完",map);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return MessageFormatUtil.formatResultToObject(KuKeAuthConstants.FAILED);
			}
		}else{
			return MessageFormatUtil.formatResultToObject(KuKeAuthConstants.NOMALLOGIN);
		}
	}
}
