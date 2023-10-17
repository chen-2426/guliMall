package com.chen.gulimall.order.feign;

import com.chen.gulimall.order.vo.MemberAddressVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/10/16 20:28
 * @description
 */
@FeignClient("gulimall-member")
public interface MemberFeign {
    @GetMapping("/member/memberreceiveaddress/{memberId}/addresses")
    List<MemberAddressVo> getAddress(@PathVariable("memberId") Long memberId);
}
