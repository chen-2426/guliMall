package com.chen.gulimall.member.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chen.gulimall.base.exception.PhoneNumExistException;
import com.chen.gulimall.base.exception.UserExistException;
import com.chen.gulimall.base.utils.HttpUtils;
import com.chen.gulimall.member.dao.MemberLevelDao;
import com.chen.gulimall.member.entity.MemberLevelEntity;
import com.chen.gulimall.member.vo.SocialUser;
import com.chen.gulimall.member.vo.UserLoginVo;
import com.chen.gulimall.member.vo.UserRegistVo;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.gulimall.base.utils.PageUtils;
import com.chen.gulimall.base.utils.Query;

import com.chen.gulimall.member.dao.MemberDao;
import com.chen.gulimall.member.entity.MemberEntity;
import com.chen.gulimall.member.service.MemberService;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {
    @Autowired
    MemberLevelDao memberLevelDao;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void regist(UserRegistVo userRegistVo) {
        MemberEntity memberEntity = new MemberEntity();
        MemberDao memberDao = this.baseMapper;

        MemberLevelEntity level = memberLevelDao.getDefaultLevel();
        memberEntity.setUsername(userRegistVo.getUserName());
        memberEntity.setLevelId(level.getId());
//       检查手机号和用户名的唯一性
        checkMobileUnique(userRegistVo.getPhone());
        memberEntity.setMobile(userRegistVo.getPhone());

        memberEntity.setNickname(userRegistVo.getUserName());
//        MD5加密 带有盐值加密
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodePassword = bCryptPasswordEncoder.encode(userRegistVo.getPassword());
        memberEntity.setPassword(encodePassword);

        memberDao.insert(memberEntity);
    }

    /**
     * 使用异常机制处理错误传入值
     * @param phone
     */
    @Override
    public void checkMobileUnique(String phone) {
        if(this.getBaseMapper().getMoblieNum(phone)>0){
            throw new PhoneNumExistException();
        }
    }

    @Override
    public MemberEntity login(UserLoginVo userLoginVo) {

        MemberEntity memberEntity = this.getBaseMapper().login(userLoginVo.getLoginacct());
        if(memberEntity == null) return null;
        String password = memberEntity.getPassword();
        if(!new BCryptPasswordEncoder().matches(userLoginVo.getPassword(),password)){
            return memberEntity;
        }
        return null;
    }

    @Override
    public MemberEntity login(SocialUser socialUser) {
        String uid = socialUser.getUid();
        MemberEntity memberEntity = this.baseMapper.selectOne(new QueryWrapper<MemberEntity>().eq("social_uid", uid));
        //查到内容
        if(memberEntity!=null){
            MemberEntity update = new MemberEntity();
            update.setId(memberEntity.getId());
            update.setAccessToken(socialUser.getAccess_token());
            update.setExpiresIn(socialUser.getExpires_in());

            this.baseMapper.updateById(update);
            memberEntity.setAccessToken(socialUser.getAccess_token());
            memberEntity.setExpiresIn(socialUser.getExpires_in());
            return memberEntity;

        }else {
//             未查到
            MemberEntity regist = new MemberEntity();
            Map<String, String> query = new HashMap<>();
            query.put("access_token", socialUser.getAccess_token());
            query.put("uid", socialUser.getUid());
            try {
                HttpResponse response = HttpUtils.doGet("https://api.weibo.com", "/2/users/show.json", "get", new HashMap<String, String>(), query);
                if(response.getStatusLine().getStatusCode()==200){
                    String s = EntityUtils.toString(response.getEntity());
                    JSONObject jsonObject = JSON.parseObject(s);
                    String name = jsonObject.getString("name");
                    String gender = jsonObject.getString("gender");

                    regist.setUsername(name);
                    regist.setGender(gender.equals("m")?1:0);
                }
            } catch (Exception e) {}
            regist.setAccessToken(socialUser.getAccess_token());
            regist.setSocialUid(socialUser.getUid());
            regist.setExpiresIn(socialUser.getExpires_in());
            this.baseMapper.insert(regist);
            return regist;

        }
    }

}
