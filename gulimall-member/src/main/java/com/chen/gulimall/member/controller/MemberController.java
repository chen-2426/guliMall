package com.chen.gulimall.member.controller;

import java.util.Arrays;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.chen.gulimall.base.exception.BizCodeEnum;
import com.chen.gulimall.base.exception.PhoneNumExistException;
import com.chen.gulimall.base.exception.UserExistException;
import com.chen.gulimall.member.vo.SocialUser;
import com.chen.gulimall.member.vo.UserLoginVo;
import com.chen.gulimall.member.vo.UserRegistVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.chen.gulimall.member.entity.MemberEntity;
import com.chen.gulimall.member.service.MemberService;
import com.chen.gulimall.base.utils.PageUtils;
import com.chen.gulimall.base.utils.R;



/**
 * 会员
 *
 * @author chen
 * @email 
 * @date 2023-08-14 16:04:19
 */
@RestController
@RequestMapping("member/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("member:member:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("member:member:info")
    public R info(@PathVariable("id") Long id){
		MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("member:member:save")
    public R save(@RequestBody MemberEntity member){
		memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("member:member:update")
    public R update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("member:member:delete")
    public R delete(@RequestBody Long[] ids){
		memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }
    @PostMapping("/regist")
    public R regist(@RequestBody UserRegistVo userRegistVo){
        try {
            memberService.regist(userRegistVo);;
        } catch (UserExistException userException) {
            return R.error(BizCodeEnum.USER_EXIST_EXCEPTION.getCode(), BizCodeEnum.USER_EXIST_EXCEPTION.getMessage());
        } catch (PhoneNumExistException phoneException) {
            return R.error(BizCodeEnum.PHONE_EXIST_EXCEPTION.getCode(), BizCodeEnum.PHONE_EXIST_EXCEPTION.getMessage());
        }
        return R.ok();
    }
    @PostMapping("/login")
    public R login(@RequestBody UserLoginVo userLoginVo){
        MemberEntity memberEntity = memberService.login(userLoginVo);
        if(memberEntity!=null){
            return R.ok().setData(memberEntity);
        }else{
            return R.error(BizCodeEnum.LOGINACCT_PASSWORD_INVAILD_EXCEPTION.getCode(),BizCodeEnum.LOGINACCT_PASSWORD_INVAILD_EXCEPTION.getMessage());
        }

    }

    @PostMapping("/oauth2/login")
    public R oauthlogin(@RequestBody SocialUser socialUser){
        MemberEntity memberEntity = memberService.login(socialUser);
        if(memberEntity!=null){
            return R.ok().setData(memberEntity);
        }else{
            return R.error(BizCodeEnum.LOGINACCT_PASSWORD_INVAILD_EXCEPTION.getCode(),BizCodeEnum.LOGINACCT_PASSWORD_INVAILD_EXCEPTION.getMessage());
        }

    }

}
