package com.kuke.auth.util;


public class KuKePayConstants {
	/***
	 * 单个cd价格
	 */
	public static final float CD_PRICE = 10.0f;
	
	/**
	 * 单曲价格
	 */
	public static final float TRACK_PRICE = 2.0f;
	
	public static final float MBOOK_PRICE = 0.2f;

	/**
	 * pay_channel type 字段 标识音频
	 */
	public static final String PAY_AUDIO = "1";
	/**
	 * pay_channel type 字段 标识视频
	 */
	public static final String PAY_VIDEO = "3";
	/**
	 * pay_channel type 字段 标识LIVE
	 */
	public static final String PAY_LIVE = "4";
	/**
	 * pay_channel type 字段 下载
	 */
	public static final String PAY_DOWNLOAD = "5";
	/**
	 * pay_channel type 字段 KUKE币
	 */
	public static final String PAY_MONEY = "6";
	
	
	/**
	 * 验证成功结果
	 */
	public static final String SUCCESS = "SUCCESS";
	/**
	 * 验证失败结果
	 */
	public static final String FAILED = "FAILED";
	/**
	 * 充值
	 */
	public final static String PAYTOCHARGE = "0";
	/**
	 * 付款
	 */
	public final static String PAYTOBUY = "1";
	
	//付款方式
	public static final String pay_org_ip = "10";
	public static final String pay_org = "9";
	public static final String pay_org_user = "8";
	
	
	public static final String KUKEERCHARGE = "500";
	public static final String AUDIOONEMONTH = "501";
	public static final String AUDIOTHREEMONTH = "502";
	public static final String AUDIOSIXMONTH = "503";
	public static final String AUDIOONEYEAR = "504";
	public static final String LIVEONE = "505";
	public static final String LIVEONEMONTH = "506";
	public static final String LIVESIXMONTH = "507";
	public static final String LIVEONEYEAR = "508";
	
	public static final String MUSICBOOKDOWN = "509";
	public static final String CATALDOWN = "510";
	public static final String AUDIODOWN = "511";
	public static final String SPOKENDOWN = "512";
	public static final String SPOKENAUDIODOWN = "513";
	
	public static final String AUDIODOWNCHANNELID = "52";
	public static final String MUSICBOOKCHANNELID = "50";
	public static final String CATALDOWNCHANNELID = "51";
	public static final String SPOKENCHANNELID = "57";
	public static final String SPOKENAUDIOCHANNELID = "58";
	
	public static String DOWNID = KuKePayConstants.AUDIODOWN+","+KuKePayConstants.CATALDOWN+","+KuKePayConstants.MUSICBOOKDOWN+","+KuKePayConstants.SPOKENDOWN+","+KuKePayConstants.SPOKENAUDIODOWN;
	
	/**
	 * 检查Pay_pro_price_id是否存在
	 * @param sourceid
	 * @param priceid
	 * @return
	 */
	public static boolean checkPay_pro_priceid(String priceid){
		boolean flag = false;
		String priceids = KuKePayConstants.KUKEERCHARGE+","+//500
						  KuKePayConstants.AUDIOONEMONTH+","+//501
						  KuKePayConstants.AUDIOTHREEMONTH+","+//502
						  KuKePayConstants.AUDIOSIXMONTH+","+//503
						  KuKePayConstants.AUDIOONEYEAR+","+//504
						  KuKePayConstants.LIVEONE+","+//505
						  KuKePayConstants.LIVEONEMONTH+","+//506
						  KuKePayConstants.LIVESIXMONTH+","+//507
						  KuKePayConstants.LIVEONEYEAR+","+//508
						  KuKePayConstants.MUSICBOOKDOWN+","+//509
						  KuKePayConstants.CATALDOWN+","+//510
						  KuKePayConstants.AUDIODOWN+","+//511
						  KuKePayConstants.SPOKENDOWN+","+//512
						  KuKePayConstants.SPOKENAUDIODOWN+","+//513
						  KuKePayConstants.AUDIODOWNCHANNELID+","+//52
						  KuKePayConstants.MUSICBOOKCHANNELID+","+//50
						  KuKePayConstants.CATALDOWNCHANNELID;//51
		if(priceids.indexOf(priceid) >= 0){
			flag = true;
		}
		return flag;
	}
}
