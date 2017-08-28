package com.kuke.util;

public class RandomNumUtil {
	 /**
     * 随机生成6位随机验证码方法
      * @return
      * @return String
     */
    public static String createRandomVcode(){
        //验证码
        String vcode = "";
        for (int i = 0; i < 6; i++) {
            vcode = vcode + (int)(Math.random() * 9);
        }
        return vcode;
    }

    public static void main(String[] args) {
		System.out.println(createRandomVcode());
	}
}
