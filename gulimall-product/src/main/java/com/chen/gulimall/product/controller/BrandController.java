package com.chen.gulimall.product.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.chen.gulimall.base.group.updateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chen.gulimall.product.entity.BrandEntity;
import com.chen.gulimall.product.service.BrandService;
import com.chen.gulimall.base.utils.PageUtils;
import com.chen.gulimall.base.utils.R;

import javax.validation.Valid;


/**
 * 品牌
 *
 * @author chen
 * @email 
 * @date 2023-08-14 15:56:08
 */
@RestController
@RequestMapping("product/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("com.chen.gulimail.product.com:brand:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = brandService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")
    //@RequiresPermissions("com.chen.gulimail.product.com:brand:info")
    public R info(@PathVariable("brandId") Long brandId){
		BrandEntity brand = brandService.getById(brandId);

        return R.ok().put("brand", brand);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("com.chen.gulimail.product.com:brand:save")
    //@Valid用于开启数据校验（来自javax.validation)
    //@Validated用于开启数据分组校验（来自springframework.validation)
    public R save(@Validated(updateGroup.class) @RequestBody BrandEntity brand/**, BindingResult result**/){ //BindingResult用于封装校验结果
//		if(result.hasErrors()){
//            Map<String, String> map = new HashMap<>();
//            result.getFieldErrors().forEach((item) ->{
//                String defaultMessage = item.getDefaultMessage();
//                String field = item.getField();
//                map.put(field,defaultMessage);
//            });
//            return R.error(400,"提交的信息不合法").put("data",map);
//        }
        brandService.save(brand);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    //@RequiresPermissions("com.chen.gulimail.product.com:brand:update")
    public R update(@RequestBody BrandEntity brand){
		brandService.updateDetail(brand);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("com.chen.gulimail.product.com:brand:delete")
    public R delete(@RequestBody Long[] brandIds){
		brandService.removeByIds(Arrays.asList(brandIds));

        return R.ok();
    }

}
