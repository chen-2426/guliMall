package com.chen.gulimall.ware.VO;

import com.sun.istack.internal.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/8/25 19:36
 * @description
 */
@Data
public class DoneVo {
    @NotNull
    private Long id;
    private List<itemDoneVo> itemDoneVoList;

}
