package com.chen.gulimall.ware.VO;

import lombok.Data;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/8/25 19:37
 * @description
 */
@Data
public class itemDoneVo {
    private Long itemId;
    private Integer status;
    private String  reason;

}
