/*
 * Copyright (c) 2010 Shanda Corporation. All rights reserved.
 *
 * Created on 2010-12-3.
 */

package com.kuke.pay.util;

import com.kuke.pay.sdo.config.Configuration;
import com.kuke.pay.sdo.config.Configuration_1;
import com.kuke.pay.sdo.config.enums.SignType;
import com.shanda.security.MD5;
import com.shanda.rsasign.RSASign;





/**
 * 签名工具类
 *
 * @author lujian.jay
 */
public class Sign {

    private Sign() {

    }

    private static String getSign(String signString, String signTypeCode, String key) {
        SignType signType = SignType.getByCode(signTypeCode);
        String signResult = "";
        if (signType == SignType.MD5) {
            signResult = MD5.getMD5(new String(signString + key).getBytes());
        } else if (signType == SignType.RSA) {
            signResult = RSASign.getInstance().sign(signString);
        }
        return signResult;
    }

    private static boolean _verifySign(String signString, String mac, String signTypeCode, String key) {
        SignType signType = SignType.getByCode(signTypeCode);
        if(signType == null){
            return false;
        }
        String signResult = "";
        boolean verifyResult = false;
        if (signType == SignType.MD5) {
            signResult = MD5.getMD5(new String(signString + key).getBytes());
            if (signResult.equalsIgnoreCase(mac)) {
                verifyResult = true;
            } else {
                verifyResult = false;
                System.out.println("actual mac is :" + signResult);
            }
        } else if (signType == SignType.RSA) {
            verifyResult = RSASign.getInstance().verifySign(signString, mac);
        }
        if (!verifyResult) {
            System.out.println("verify sign error, origin string:" + signString + "; sign:" + mac + ";key:" + key
                    + "; sighType:" + signType.getName());
        }
        return verifyResult;
    }

    public static String makeSign(String amount,String orderNo , String merchantNo
    		,String merchantUserId , String orderTime , String productNo , String productDesc
    		,String remark1,String remark2,String bankCode,String productURL){
        String toSignString = Configuration.version + amount + orderNo +merchantNo
        + merchantUserId + Configuration.payChannel + Configuration.postBackURL + Configuration.notifyURL
        + Configuration.backURL + orderTime + Configuration.currencyType 
        + Configuration.notifyURLType + Configuration.signType
        + productNo + productDesc + remark1 + remark2 + bankCode
        + Configuration.defaultChannel + productURL;
        
        return Sign.getSign(toSignString, Configuration.signType, Configuration.key);
    }
    public static String makeSign_1(String amount,String orderNo , String merchantNo
    		,String merchantUserId , String orderTime , String productNo , String productDesc
    		,String remark1,String remark2,String bankCode,String productURL){
        String toSignString = Configuration_1.version + amount + orderNo +merchantNo
        + merchantUserId + Configuration_1.payChannel + Configuration_1.postBackURL + Configuration_1.notifyURL
        + Configuration_1.backURL + orderTime + Configuration_1.currencyType 
        + Configuration_1.notifyURLType + Configuration_1.signType
        + productNo + productDesc + remark1 + remark2 + bankCode
        + Configuration_1.defaultChannel + productURL;
        
        return Sign.getSign(toSignString, Configuration_1.signType, Configuration_1.key);
    }
    
    public static boolean verifySign(String amount , String payAmount ,String orderNo
    		,String serialNo,String status,String merchantNo , String payChannel
    		,String discount,String signType ,String payTime,String currencyType
    		,String productNo,String productDesc,String remark1,String remark2
    		,String exInfo,String mac){
        StringBuffer toSignString = new StringBuffer();
        toSignString.append(amount).append("|");
        toSignString.append(payAmount).append("|");
        toSignString.append(orderNo).append("|");
        toSignString.append(serialNo).append("|");
        toSignString.append(status).append("|");
        toSignString.append(merchantNo).append("|");
        toSignString.append(payChannel).append("|");
        toSignString.append(discount).append("|");
        toSignString.append(signType).append("|");
        toSignString.append(payTime).append("|");
        toSignString.append(currencyType).append("|");
        toSignString.append(productNo).append("|");
        toSignString.append(productDesc).append("|");
        toSignString.append(remark1).append("|");
        toSignString.append(remark2).append("|");
        toSignString.append(exInfo);
        
        return _verifySign(toSignString.toString(), mac, Configuration.signType, "|" + Configuration.key);
    }
}
