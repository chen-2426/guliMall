package com.chen.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.chen.gulimall.product.DTO.CategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.chen.gulimall.product.entity.CategoryEntity;
import com.chen.gulimall.product.service.CategoryService;
import com.chen.gulimall.base.utils.PageUtils;
import com.chen.gulimall.base.utils.R;



/**
 * 商品三级分类
 *
 * @author chen
 * @email 
 * @date 2023-08-14 15:56:08
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 樹形列表
     */
    @GetMapping("/list/tree")
    //@RequiresPermissions("product:category:list")
    public R listwithTree(){
        List<CategoryDTO> categoryDTOS = categoryService.listWithTree();
        return R.ok().put("data", categoryDTOS);
    }

    /**
     * 列表
     */
    @GetMapping("/list")
    //@RequiresPermissions("product:category:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{catId}")
    //@RequiresPermissions("product:category:info")
    public R info(@PathVariable("catId") Long catId){
		CategoryEntity category = categoryService.getById(catId);

        return R.ok().put("category", category);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    //@RequiresPermissions("product:category:save")
    public R save(@RequestBody CategoryEntity category){
		categoryService.save(category);

        return R.ok();
    }

    /**
     * 修改sort
     */
    @PutMapping("/update/sort")
    //@RequiresPermissions("product:category:update")
    public R update(@RequestBody CategoryEntity[] category){
        categoryService.updateBatchById(Arrays.asList(category));

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    //@RequiresPermissions("product:category:update")
    public R update(@RequestBody CategoryEntity category){
		categoryService.updateById(category);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    //@RequiresPermissions("product:category:delete")
    public R delete(@RequestBody Long[] catIds){
		categoryService.removeByIds(Arrays.asList(catIds));
        categoryService.removeMenuByIds(Arrays.asList(catIds));
        return R.ok();
    }

}
