package com.rural.pojo;

import lombok.Data;

/**
 * @author xh
 * @create 2022-11-18  18:46
 */
@Data
public class WxInfo {
    private String code;
    private String rawData;
    private String encryptedData;
    private String iv;
    private String signature;
}
