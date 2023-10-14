package com.chen.gulimall.thirdParty.controller;

import com.chen.gulimall.base.utils.R;
import com.chen.gulimall.thirdParty.component.SmsComponent;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/9/12 17:40
 * @description
 */
@RestController
@RequestMapping(value = "/sms")
public class SmsSendController {
    @Resource
    private SmsComponent smsComponent;

    /**
     * 提供给别的服务进行调用
     * @param phone
     * @param code
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/sendCode")
    public R sendCode(@RequestParam("phone") String phone, @RequestParam("code") String code) {

        //发送验证码
//        smsComponent.sendCode(phone,code);
        System.out.println(phone+code);
        return R.ok();
    }
}
