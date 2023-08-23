package com.chen.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chen.gulimall.product.VO.BrandVO;
import com.chen.gulimall.product.entity.BrandEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chen.gulimall.product.entity.CategoryBrandRelationEntity;
import com.chen.gulimall.product.service.CategoryBrandRelationService;
import com.chen.gulimall.base.utils.PageUtils;
import com.chen.gulimall.base.utils.R;



/**
 * 品牌分类关联
 *
 * @author chen
 * @email 
 * @date 2023-08-14 15:56:08
 */
@RestController
@RequestMapping("product/categorybrandrelation")
public class CategoryBrandRelationController {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    @RequestMapping("/brands/list")
    public R brandsList(@RequestParam(value = "catId") long catId){
        List<BrandEntity> vos = categoryBrandRelationService.getBrandsByCatId(catId);
        List<BrandVO> collect = vos.stream().map(item -> {
            BrandVO brandVO = new BrandVO();
            BeanUtils.copyProperties(item, brandVO);
            return brandVO;
        }).collect(Collectors.toList());
        return R.ok().put("data", collect);
    }
    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("com.chen.gulimail.product.com:categorybrandrelation:list")
    public R list(@RequestParam long brandId){
        List<CategoryBrandRelationEntity> brand_id = categoryBrandRelationService.list(
                new QueryWrapper<CategoryBrandRelationEntity>().eq("brand_id", brandId));
        return R.ok().put("data", brand_id);
    }

    @RequestMapping("/catelog/list")
    //@RequiresPermissions("com.chen.gulimail.product.com:categorybrandrelation:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryBrandRelationService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("com.chen.gulimail.product.com:categorybrandrelation:info")
    public R info(@PathVariable("id") Long id){
		CategoryBrandRelationEntity categoryBrandRelation = categoryBrandRelationService.getById(id);

        return R.ok().put("categoryBrandRelation", categoryBrandRelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("com.chen.gulimail.product.com:categorybrandrelation:save")
    public R save(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){

		categoryBrandRelationService.saveDetail(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("com.chen.gulimail.product.com:categorybrandrelation:update")
    public R update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
		categoryBrandRelationService.updateById(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("com.chen.gulimail.product.com:categorybrandrelation:delete")
    public R delete(@RequestBody Long[] ids){
		categoryBrandRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
