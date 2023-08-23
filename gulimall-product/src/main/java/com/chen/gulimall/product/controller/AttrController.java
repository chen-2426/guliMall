package com.chen.gulimall.product.controller;

import java.util.Arrays;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.chen.gulimall.product.VO.AttrRespVO;
import com.chen.gulimall.product.VO.AttrVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chen.gulimall.product.entity.AttrEntity;
import com.chen.gulimall.product.service.AttrService;
import com.chen.gulimall.base.utils.PageUtils;
import com.chen.gulimall.base.utils.R;



/**
 * 商品属性
 *
 * @author chen
 * @email 
 * @date 2023-08-14 15:56:07
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("com.chen.gulimail.product.com:attr:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }
    @RequestMapping("/{type}/list/{catelogId}")
    public R listCategrorAttrs(@PathVariable("catelogId") Long catelogId,
                               @RequestParam Map<String, Object> params,
                               @PathVariable("type")String type){
        PageUtils page = attrService.queryBaseAttrPage(catelogId,params,type);
        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    //@RequiresPermissions("com.chen.gulimail.product.com:attr:info")
    public R info(@PathVariable("attrId") Long attrId){
		AttrRespVO attr = attrService.getAttrInfo(attrId);

        return R.ok().put("attr", attr);
    }


    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("com.chen.gulimail.product.com:attr:save")
    public R save(@RequestBody AttrVO attr){
		attrService.saveAttr(attr);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("com.chen.gulimail.product.com:attr:update")
    public R update(@RequestBody AttrVO attr){
		attrService.updateAttr(attr);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("com.chen.gulimail.product.com:attr:delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
