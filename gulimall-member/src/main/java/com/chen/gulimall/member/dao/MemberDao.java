package com.chen.gulimall.member.dao;

import com.chen.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 会员
 * 
 * @author chen
 * @email 
 * @date 2023-08-14 16:04:19
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {

    Integer getMoblieNum(@Param("phone") String phone);

    MemberEntity login(@Param("loginacct") String loginacct);
}
