package com.kuke.test.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kuke.common.utils.ResponseMsg;
import com.kuke.core.base.BaseController;
import com.kuke.pay.controller.PayController;
import com.kuke.test.mapper.TestMapper;
import com.kuke.util.IdGenerator;
import com.kuke.util.MD5;
import com.kuke.util.StringUtil;

@Controller
@RequestMapping(value = "/kuke")
public class TestController extends BaseController{
	private static Logger logger = LoggerFactory.getLogger(PayController.class);
	
	@Autowired
	private TestMapper testMapper;
	
	@RequestMapping("/test")
	public @ResponseBody ResponseMsg test(HttpServletRequest request, HttpServletResponse response){
		try {
			List list = testMapper.getDownUser();
			logger.info("TestController list:"+list.size());
			List paramList = new ArrayList();
			for(int i = 0; i < list.size();i++){
				Map m = (Map) list.get(i);
				String org_id =StringUtil.dealNull((String) m.get("org_id"));
				if(!"".equals(org_id)){
					Map params = new HashMap();
					String user_id = IdGenerator.getUUIDHex32();
					params.put("user_id", user_id);
					params.put("name", StringUtil.dealNull((String) m.get("user_name")));
					params.put("password", new MD5().getMD5ofStr(StringUtil.dealNull((String) m.get("password"))));
					params.put("org_id", StringUtil.dealNull((String) m.get("org_id")));
					paramList.add(params);
				}
			}
			logger.info("TestController paramList:"+paramList.size());
			int count = testMapper.insertUser(paramList);
			logger.info("TestController count:"+count);
			int count1 = testMapper.insertDownUser(paramList);
			logger.info("TestController count1:"+count1);
			int count2 = testMapper.insertpertain(paramList);
			logger.info("TestController count2:"+count2);
			return new ResponseMsg(true,"TestController list:"+list.size()+"TestController paramList:"
			+paramList.size()+"TestController count:"+count+"TestController count1:"+count1+"TestController count2:"+count2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseMsg(false,"");
	}
	
}
