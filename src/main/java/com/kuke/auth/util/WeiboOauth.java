/**
 * 
 */
package com.kuke.auth.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.kuke.util.HttpClientUtil;
import com.kuke.auth.util.KuKeUrlConstants;


/**
 *@Description: TODO(站外分享)
 * @author lyf
 * @date 2013-4-23 上午10:43:53
 * @version V1.0 
 */
public class WeiboOauth {

	
	//站外分享
	public static String share(String uid, String snsType,String title, String content, String imgUrl,String hrefUrl) throws Exception{
		
		String post_url = KuKeUrlConstants.snsShareOut_URL;
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("title", title));
		nvps.add(new BasicNameValuePair("content", content));
		nvps.add(new BasicNameValuePair("img", imgUrl));
		nvps.add(new BasicNameValuePair("uid", uid));
		nvps.add(new BasicNameValuePair("snsType", snsType));
		nvps.add(new BasicNameValuePair("href", hrefUrl));
		return HttpClientUtil.executeServicePOST(post_url, nvps);
	}
	
}
