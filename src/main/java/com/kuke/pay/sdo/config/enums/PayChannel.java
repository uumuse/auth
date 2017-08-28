/*
 * Copyright (c) 2010 Shanda Corporation. All rights reserved.
 *
 * Created on 2010-12-3.
 */

package com.kuke.pay.sdo.config.enums;

/**
 * TODO.
 *
 * @author lujian.jay
 */
public enum PayChannel {
    SNDA_CARD("03", "盛大卡支付"),      
    NET_BANK("04", "网上银行支付"),    
    BIND_BANK("07", "一点充支付"),      
    SNDA_BALANCE("14", "盛大钱包余额支付"),
    MOBILE_CARD("18", "手机卡支付"),      
    BANK_CARD("19", "储蓄卡支付"),      
    CREDIT_CARD("24", "信用卡支付"),      
    OTHERS("99", "其它支付方式");    
    
    private final String code;
    private final String name;

    PayChannel(String code, String name) {
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
