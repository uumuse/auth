package com.kuke.core.util;

import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * AES加解密算法
 */

public class AES {
	
    /*
     * 加密用的Key 可以用26个字母和数字组成，最好不要用保留字符，虽然不会错，至于怎么裁决，个人看情况而定
     * 此处使用AES-128-CBC加密模式，key需要为16位。
     */
	public static String cKey = "kukeencrypt00000";
	
    // 加密
    public static String Encrypt(String sSrc, String sKey) throws Exception {
        if (sKey == null) {
            System.out.print("Key为空null");
            return null;
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            System.out.print("Key长度不是16位");
            return null;
        }
        byte[] raw = sKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");//"算法/模式/补码方式"
        IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes());

        return new BASE64Encoder().encode(encrypted);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }
    
   
    
    
    
    // 解密
    public static String Decrypt(String sSrc, String sKey)  {
        try {
            // 判断Key是否正确
            if (sKey == null) {
                System.out.print("Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                System.out.print("Key长度不是16位");
                return null;
            }
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec("0102030405060708"
                    .getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);//先用base64解密
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original);
                return originalString;
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }
    public static String EncryptAndEncode(String sSrc){
    	try {
			String enString = AES.Encrypt(System.currentTimeMillis()+sSrc, cKey);
			  return URLEncoder.encode(enString,"UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
    public static String DecryptAndDecode(String sSrc){
    	try {
			String dstr = AES.Decrypt(URLDecoder.decode(sSrc,"UTF-8"), cKey);
			return dstr.substring(13, dstr.length());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
    public static void main(String[] args) throws Exception {
//        String cSrc ="kuke"+"41758";
        String cSrc ="41756";
        System.out.println(cSrc);
        // 加密
        String encryptStr  = EncryptAndEncode(cSrc);
        System.out.println("加密后的字串是：" + EncryptAndEncode(cSrc));

        // 解密
        String DeString = AES.DecryptAndDecode( encryptStr);
        System.out.println("解密后的字串是：" + DeString);
    }
}