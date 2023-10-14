package com.chen.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.gulimall.base.utils.PageUtils;
import com.chen.gulimall.member.entity.MemberEntity;
import com.chen.gulimall.member.vo.SocialUser;
import com.chen.gulimall.member.vo.UserLoginVo;
import com.chen.gulimall.member.vo.UserRegistVo;

import java.util.Map;

/**
 * 会员
 *
 * @author chen
 * @email 
 * @date 2023-08-14 16:04:19
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void regist(UserRegistVo userRegistVo);

    void checkMobileUnique(String phone);

    MemberEntity login(UserLoginVo userLoginVo);

    MemberEntity login(SocialUser userLoginVo);
}

