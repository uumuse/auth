package com.kuke.pay.util.app.alipay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.kuke.pay.util.Constants;

public class SignUtils {

	private static final String ALGORITHM = "RSA";

	private static final String SIGN_ALGORITHMS = "SHA1WithRSA";

	private static final String DEFAULT_CHARSET = "UTF-8";

	public static String sign(String content, String privateKey) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
					Base64.decode(privateKey));
			KeyFactory keyf = KeyFactory.getInstance(ALGORITHM);
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);

			java.security.Signature signature = java.security.Signature
					.getInstance(SIGN_ALGORITHMS);

			signature.initSign(priKey);
			signature.update(content.getBytes(DEFAULT_CHARSET));

			byte[] signed = signature.sign();

			return Base64.encode(signed);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	/**
     * 对支付参数信息进行签名
     * @param map 待签名授权信息
     * @return
     */
    public static String getSign(Map<String, String> map, String rsaKey) {
        List<String> keys = new ArrayList<String>(map.keySet());
        // key排序
        Collections.sort(keys);

        StringBuilder authInfo = new StringBuilder();
        boolean first = true;
        for (String key : keys) {
            if (first) {
                first = false;
            } else {
                authInfo.append("&");
            }
            authInfo.append(key).append("=").append(map.get(key)); 
        }
        return SignUtils.sign(authInfo.toString(), rsaKey);
    }
    /**
     * 得到签名数据map
     */
    public static Map<String, String> getSignOrderInfoMap(String out_trade_no,double total_amount,String subject,String body){
    	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Map<String, String> param = new HashMap<String, String>();
        // 公共请求参数
        param.put("app_id", Constants.ALIPAY_APPID);// 商户订单号
        param.put("method", "alipay.trade.app.pay");// 交易金额
        param.put("format", "JSON");
        param.put("charset", "UTF-8");
        param.put("timestamp", sf.format(new Date()));
        param.put("version", "1.0");
        param.put("notify_url", Constants.ALIPAY_NOTIFY_URL); // 支付宝服务器主动通知商户服务
        param.put("sign_type", "RSA");
        
        Map<String, Object> pcont = new HashMap<String, Object>();
        // 支付业务请求参数
        pcont.put("out_trade_no", out_trade_no); // 商户订单号
        pcont.put("total_amount", String.valueOf(total_amount));// 交易金额
        pcont.put("subject", subject); // 订单标题
        pcont.put("body", body);// 对交易或商品的描述
        
        param.put("biz_content", JSONObject.fromObject(pcont).toString()); // 业务请求参数  不需要对json字符串转义
        
        return param;
    }
    

/**
     * 返回签名编码拼接url
     * 
     * @param params
     * @param isEncode
     * @return
     */
    public static String getSignEncodeUrl(Map<String, String> map, boolean isEncode) {
        String sign = map.get("sign");
        String encodedSign = "";
        if (map != null) {
            map.remove("sign");
            List<String> keys = new ArrayList<String>(map.keySet());
            // key排序
            Collections.sort(keys);
            StringBuilder authInfo = new StringBuilder();
            boolean first = true;// 是否第一个
            for (String key: keys) {
                if (first) {
                    first = false;
                } else {
                    authInfo.append("&");
                }
                authInfo.append(key).append("=");
                if (isEncode) {
                    try {
                        authInfo.append(URLEncoder.encode(map.get(key), "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    authInfo.append(map.get(key));
                }
            }
            try {
                encodedSign = authInfo.toString() + "&sign=" + URLEncoder.encode(sign, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return encodedSign.replaceAll("\\+", "%20");
    }
}
