/*
 * Copyright (c) 2010 Shanda Corporation. All rights reserved.
 *
 * Created on 2010-12-3.
 */

package com.kuke.pay.sdo.config.enums;

/**
 * 定义签名类型
 *
 * @author lujian.jay
 */
public enum SignType {
    RSA("1", "RSA"), MD5("2", "MD5"), PKI("3", "PKI");
    private final String code;
    private final String name;

    SignType(String code, String name) {
        this.name = name;
        this.code = code;
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
