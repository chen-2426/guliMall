package com.chen.gulimall.member.vo;

import lombok.Data;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/9/20 16:53
 * @description
 */
@Data
public class SocialUser {
    private String access_token;
    private String remind_in;
    private long expires_in;
    private String uid;
    private String isRealName;
}
