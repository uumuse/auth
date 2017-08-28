package com.kuke.pay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jdom2.JDOMException;

import com.kuke.auth.util.KuKeUrlConstants;
import com.kuke.pay.util.Constants;
import com.kuke.pay.util.XMLUtil;
import com.kuke.pay.util.alipay.AlipaySubmit;
import com.kuke.util.HttpClientUtil;

public class PayAuth {
	
	
	
	
	
	/**
	 * 发起退款申请
	 * @param pay_detail_id
	 * @param refund_keyword
	 * @return
	 * @throws Exception
	 */
	public static String applyRefund(String pay_detail_id, String refund_keyword) throws Exception {
		String result = "false";
		String post_url = "";
		if("2".equals(pay_detail_id)){//支付宝
			post_url = KuKeUrlConstants.alipayApplyRefund;
		}else if("7".equals(pay_detail_id)){//微信
			post_url = KuKeUrlConstants.wechatApplyRefund;
		}else{
			return result;
		}
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("refund_keyword", refund_keyword));
		result = HttpClientUtil.executeServicePOST(post_url, nvps);
		System.out.println("applyRefund result:"+result);
		return result;
	}
	public static void main(String[] args) {
		//把请求参数打包成数组  
        Map<String, String> sParaTemp = new HashMap<String, String>();  
        sParaTemp.put("service", "single_trade_query");  
        sParaTemp.put("partner", Constants.ALIPAY_PARTNER);  
        sParaTemp.put("_input_charset", Constants.ALIPAY_CHARSET);//编码
        sParaTemp.put("sign_type", Constants.ALIPAY_SIGN_TYPE);//编码
        sParaTemp.put("out_trade_no", "20161027161752FAFD5F709");//编码
          
        //建立请求  
        String resXml = null;
		try {
			resXml = AlipaySubmit.buildRequest("", "", sParaTemp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Map wxReturnMap = XMLUtil.doXMLParse(resXml);
			System.out.println(wxReturnMap.get("response"));
			Map wx = XMLUtil.doXMLParse((String)wxReturnMap.get("response"));
			System.out.println(wx.get("trade_status"));
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("resXml:"+resXml);
	}
}

