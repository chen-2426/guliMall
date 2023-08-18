package com.chen.gulimall.product.service.impl;

import com.chen.gulimall.product.DTO.CategoryDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.gulimall.base.utils.PageUtils;
import com.chen.gulimall.base.utils.Query;

import com.chen.gulimall.product.dao.CategoryDao;
import com.chen.gulimall.product.entity.CategoryEntity;
import com.chen.gulimall.product.service.CategoryService;

import javax.annotation.Nullable;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    /**
     * 三级查找
     * @return 封装为数据传输类型的三级查询结果
     */
    @Override
    public List<CategoryDTO> listWithTree() {
        List<CategoryEntity> list = this.list();;
        List<CategoryDTO> res = list.stream().filter(CategoryEntity -> CategoryEntity.getParentCid() == 0)
                .map(a -> {
                    CategoryDTO categoryDTO = new CategoryDTO();
                    BeanUtils.copyProperties(a, categoryDTO);
                    categoryDTO.setChildren(this.getChildren(a.getCatId(),list));
                    return categoryDTO;
                })
                .sorted((a,b)->(a.getSort()==null?a.getSort():0)-(b.getSort()==null?b.getSort():0))
                .collect(Collectors.toList());
        return res;
    }

    public List<CategoryDTO> getChildren(Long id,List<CategoryEntity> list ){

        List<CategoryDTO> res = list.stream().filter(CategoryEntity -> CategoryEntity.getParentCid() == id)
                .map(a -> {
                    CategoryDTO categoryDTO = new CategoryDTO();
                    BeanUtils.copyProperties(a, categoryDTO);
                    categoryDTO.setChildren(this.getChildren(a.getCatId(),list));
                    return categoryDTO;
                })
                .sorted((a,b)->(a.getSort()==null?a.getSort():0)-(b.getSort()==null?b.getSort():0))
                .collect(Collectors.toList());
        return res;
    }

    @Override
    public boolean removeMenuByIds(Collection<? extends Serializable> idList) {
        //todo 验证是否存在childrens,应用后会报出MySQLSyntaxErrorException错误，测试环境下无任何问题；
//        List<CategoryEntity> categoryEntities = this.listByIds(idList);
//
//        List<CategoryDTO> categoryDTOS = categoryEntities.stream()
//                .map(a -> {
//                    CategoryDTO categoryDTO = new CategoryDTO();
//                    BeanUtils.copyProperties(a, categoryDTO);
//                    categoryDTO.setChildren(this.getChildren(a.getCatId(),categoryEntities));
//                    return categoryDTO;
//                })
//                .sorted((a,b)->(a.getSort()==null?a.getSort():0)-(b.getSort()==null?b.getSort():0))
//                .collect(Collectors.toList());
//        idList = categoryDTOS.stream()
//                .filter(a -> a.getChildren().size() == 0)
//                .map(
//                        a -> a.getCatId()
//                ).collect(Collectors.toList());
        return this.getBaseMapper().deleteBatchIds(idList)>=1;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

}