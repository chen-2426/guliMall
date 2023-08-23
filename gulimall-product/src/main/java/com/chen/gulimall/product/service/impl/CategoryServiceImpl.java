package com.chen.gulimall.product.service.impl;

import com.chen.gulimall.product.VO.CategoryVO;
import com.chen.gulimall.product.service.CategoryBrandRelationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
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


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;
    /**
     * 三级查找
     *
     * @return 封装为数据传输类型的三级查询结果
     */
    @Override
    public List<CategoryVO> listWithTree() {
        List<CategoryEntity> list = this.list();
        ;
        List<CategoryVO> res = list.stream().filter(CategoryEntity -> CategoryEntity.getParentCid() == 0)
                .map(a -> {
                    CategoryVO categoryVO = new CategoryVO();
                    BeanUtils.copyProperties(a, categoryVO);
                    categoryVO.setChildren(this.getChildren(a.getCatId(), list));
                    return categoryVO;
                })
                .sorted((a, b) -> (a.getSort() == null ? a.getSort() : 0) - (b.getSort() == null ? b.getSort() : 0))
                .collect(Collectors.toList());
        return res;
    }

    @Override
    public Long[] findCatelogPath(Long catelogId) {
        CategoryEntity category = this.getById(catelogId);
        ArrayList<Long> longs = new ArrayList<>();
        longs.add(catelogId);
        while (category.getParentCid() != 0) {
            longs.add(0, category.getParentCid());
            category = this.getById(category.getParentCid());
        }
        return longs.toArray(new Long[longs.size()]);
    }

    /**
     * todo 级联更新关联数据
     * @param category
     */
    @Override
    public void updateCascade(CategoryEntity category) {
        updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(),category.getName());
    }

    public List<CategoryVO> getChildren(Long id, List<CategoryEntity> list) {

        List<CategoryVO> res = list.stream().filter(CategoryEntity -> CategoryEntity.getParentCid() == id)
                .map(a -> {
                    CategoryVO categoryVO = new CategoryVO();
                    BeanUtils.copyProperties(a, categoryVO);
                    categoryVO.setChildren(this.getChildren(a.getCatId(), list));
                    return categoryVO;
                })
                .sorted((a, b) -> (a.getSort() == null ? a.getSort() : 0) - (b.getSort() == null ? b.getSort() : 0))
                .collect(Collectors.toList());
        return res;
    }

    @Override
    public boolean removeMenuByIds(Collection<? extends Serializable> idList) {
        //todo 验证是否存在childrens,应用后会报出MySQLSyntaxErrorException错误，测试环境下无任何问题；
//        List<CategoryEntity> categoryEntities = this.listByIds(idList);
//
//        List<CategoryVO> categoryDTOS = categoryEntities.stream()
//                .map(a -> {
//                    CategoryVO categoryDTO = new CategoryVO();
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
        return this.getBaseMapper().deleteBatchIds(idList) >= 1;
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