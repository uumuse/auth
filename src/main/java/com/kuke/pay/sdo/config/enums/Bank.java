/*
 * Copyright (c) 2010 Shanda Corporation. All rights reserved.
 *
 * Created on 2010-12-3.
 */

package com.kuke.pay.sdo.config.enums;

/**
 * Enums for banks
 *
 * @author lujian.jay
 */
public enum Bank {
    CZB("CZB","浙商银行"),
    YDXH("YDXH","尧都信用合作联社"),
    CZCB("CZCB","浙江稠州商业银行"),
    ICBC("ICBC","工商银行"),
    CCB("CCB","建设银行"),
    ABC("ABC","农业银行"),
    CMB("CMB","招商银行"),
    COMM("COMM","交通银行"),
    CMBC("CMBC","民生银行"),
    CIB("CIB","兴业银行"),
    GDB("GDB","广东发展银行"),
    CEB("CEB","光大银行"),
    CITIC("CITIC","中信银行"),
    SDB("SDB","深圳发展银行"),
    HXB("HXB","华夏银行"),
    NBCB("NBCB","宁波银行"),
    SZPAB("SZPAB","平安银行"),
    BOS("BOS","上海银行"),
    NJCB("NJCB","南京银行"),
    HKBEA("HKBEA","东亚银行"),
    SPDB("SPDB","浦东发展银行"),
    HCCB("HCCB","杭州银行"),
    GNXS("GNXS","广州市农村信用合"),
    GZCB("GZCB","广州银行"),
    SHRCB("SHRCB","上海市农村商业银"),
    CBHB("CBHB","渤海银行"),
    HKBCHINA("HKBCHINA","汉口银行"),
    ZHNX("ZHNX","珠海市农村信用合"),
    WZCB("WZCB","温州银行"),
    SDE("SDE","顺德农信社"),
    BOC("BOC","中国银行"),
    PSBC("PSBC","中国邮政储蓄银行"),
    IPSD("IPSD","上海环讯"),
    CHINAPAY("CHINAPAY","银联"),
    BCCB("BCCB","北京银行"),
    SXJS("SXJS","晋商银行"),
    BJRCB("BJRCB","北京农商行"),
    SNXS("SNXS","深圳农村商业银行"),
    SDTBNK("SDTBNK","盛大测试银行");
    
    private final String code;
    private final String name;

    Bank(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
    public static SignType getByCode(String code) {
        for (SignType signType : SignType.values()) {
            if (signType.getCode().equals(code)) {
                return signType;
            }
        }
        return null;
    }

}
