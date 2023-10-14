package com.chen.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.chen.gulimall.product.VO.AttrGroupWithAttrVO;
import com.chen.gulimall.product.VO.AttrGroupVO;
import com.chen.gulimall.product.VO.AttrAttrRelationVO;
import com.chen.gulimall.product.entity.AttrEntity;
import com.chen.gulimall.product.service.AttrAttrgroupRelationService;
import com.chen.gulimall.product.service.AttrService;
import com.chen.gulimall.product.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chen.gulimall.product.entity.AttrGroupEntity;
import com.chen.gulimall.product.service.AttrGroupService;
import com.chen.gulimall.base.utils.PageUtils;
import com.chen.gulimall.base.utils.R;



/**
 * 属性分组
 *
 * @author chen
 * @email 
 * @date 2023-08-14 15:56:08
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AttrService attrService;
    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;

    @RequestMapping("/attr/relation")
    public void addRelation(@RequestBody List<AttrAttrRelationVO> vos){
        attrAttrgroupRelationService.saveBatch(vos);
    }
    /**
     * 列表
     */
    @RequestMapping("/list/{catelogId}")
    //@RequiresPermissions("com.chen.gulimail.product.com:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params,
                  @PathVariable("catelogId") Long catelogId){
//        PageUtils page = attrGroupService.queryPage(params);
        PageUtils page = attrGroupService.queryPage(params,catelogId);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    //@RequiresPermissions("com.chen.gulimail.product.com:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        Long catelogId = attrGroup.getCatelogId();
        Long[] catelogPath = categoryService.findCatelogPath(catelogId);
        AttrGroupVO attrGroupVO = new AttrGroupVO();
        BeanUtils.copyProperties(attrGroup, attrGroupVO);
        attrGroupVO.setCatelogPath(catelogPath);
        return R.ok().put("attrGroup", attrGroupVO);
    }

    @RequestMapping("/{catelogId}/withattr")
    //@RequiresPermissions("com.chen.gulimail.product.com:attrgroup:info")
    public R getwithattr(@PathVariable("catelogId") Long catelogId){
        List<AttrGroupWithAttrVO> attrGroup = attrGroupService.getWithAttrByCatelogId(catelogId);
        return R.ok().put("data", attrGroup);
    }

    /**
     * 查询关联的全部属性
     * @param attrgroupId
     * @return
     */
    @RequestMapping("/{attrgroupId}/attr/relation")
    public R getAttrRelationship(@PathVariable("attrgroupId")Long attrgroupId){
        List<AttrEntity> attrEntities = attrService.getAttrRelationship(attrgroupId);
        return R.ok().put("data",attrEntities);
    }
    /**
     * 查询未关联的全部属性
     * @param attrgroupId
     * @return
     */
    @RequestMapping("/{attrgroupId}/noattr/relation")
    public R listNoAttrRelationship(@RequestParam Map<String, Object> params,@PathVariable("attrgroupId")Long attrgroupId){
        PageUtils page = attrService.getNoAttrRelationship(params,attrgroupId);
        return R.ok().put("page",page);
    }
    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("com.chen.gulimail.product.com:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("com.chen.gulimail.product.com:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("com.chen.gulimail.product.com:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

    @RequestMapping("/attr/relation/delete")
    //@RequiresPermissions("com.chen.gulimail.product.com:attrgroup:delete")
    public R delete(@RequestBody AttrAttrRelationVO[] attrRelationShipVOs){
        attrAttrgroupRelationService.removeRelation(attrRelationShipVOs);

        return R.ok();
    }

}
