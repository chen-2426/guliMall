package com.chen.gulimall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.chen.gulimall.product.VO.CategoryVO;
import com.chen.gulimall.product.VO.Web.l2CategoryVO;
import com.chen.gulimall.product.service.CategoryBrandRelationService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.gulimall.base.utils.PageUtils;
import com.chen.gulimall.base.utils.Query;

import com.chen.gulimall.product.dao.CategoryDao;
import com.chen.gulimall.product.entity.CategoryEntity;
import com.chen.gulimall.product.service.CategoryService;
import org.springframework.util.StringUtils;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedissonClient redissonClient;

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
     *
     * @param category
     */
    @Override
    public void updateCascade(CategoryEntity category) {
        updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());
    }

    @Override
    public List<CategoryEntity> getLevel1Categorys() {
        List<CategoryEntity> list = this.list(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
        return list;
    }

    @Override
    public Map<String, List<l2CategoryVO>> getCatalogJson() {
//        String uuid = UUID.randomUUID().toString();
        //1.加入缓存逻辑
        //分布式锁


        //redisson分布式锁
        RLock lock = redissonClient.getLock("CatalogJson-lock");
        lock.lock();
        Map<String, List<l2CategoryVO>> dataFormDb;
        try{
            dataFormDb = getCatalogJsonFromDb();
        }finally {
            lock.unlock();
        }
        return dataFormDb;


//        原子加锁

//        Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent("lock", uuid,300,TimeUnit.SECONDS);
//        if(lock){
//// 非原子加锁           stringRedisTemplate.expire("lock",30,TimeUnit.SECONDS);
//            Map<String, List<l2CategoryVO>> catalogJsonFromDb = null;
//            try{
//                catalogJsonFromDb = getCatalogJsonFromDb();
//            }catch (Exception e){
////                lua脚本
//                String script = "if redis.call(\"get\",KEYS[1]) == ARGV[1]\n" +
//                        "then\n" +
//                        "    return redis.call(\"del\",KEYS[1])\n" +
//                        "else\n" +
//                        "    return 0\n" +
//                        "end";
//                Long lock1 = stringRedisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class), Arrays.asList("lock"), uuid);
//            }
//
//            stringRedisTemplate.delete("lock");
//            return catalogJsonFromDb;
//        }else{
//            return getCatalogJson();
//        }

        // 本地锁
//        String catalogJSON = stringRedisTemplate.opsForValue().get("catalogJSON");
//        if (StringUtils.hasLength(catalogJSON)) {
//            synchronized (this) {
//            Map<String, List<l2CategoryVO>> catalogJsonFromDb = getCatalogJsonFromDb();
//                return catalogJsonFromDb;
//            }
//        }
//        Map<String, List<l2CategoryVO>> res = JSON.parseObject(catalogJSON, new TypeReference<Map<String, List<l2CategoryVO>>>() {
//        });
//        return res;
    }

    /**
     * 数据库查询并封转业务逻辑
     *
     * @return
     */
    public Map<String, List<l2CategoryVO>> getCatalogJsonFromDb() {
        //todo 本地锁： synchronized，JUC（LOCK）
//        分布式环境中需要上锁则需要使用分布式锁

            String catalogJSON = stringRedisTemplate.opsForValue().get("catalogJSON");
            if(!StringUtils.hasLength(catalogJSON)){
                Map<String, List<l2CategoryVO>> res = JSON.parseObject(catalogJSON, new TypeReference<Map<String, List<l2CategoryVO>>>() {
                });
                return res;
            }
            List<CategoryEntity> CategoryEntities = this.list();


            Map<String, List<l2CategoryVO>> collect = CategoryEntities.stream()
                    .filter(categoryEntity -> categoryEntity.getCatLevel() == 1)
                    .collect(Collectors.toMap(k -> String.valueOf(k.getCatId()), v -> {
                                return CategoryEntities.stream()
                                        .filter(categoryEntity -> categoryEntity.getParentCid() == v.getCatId())
                                        .map(
                                                entity -> {
                                                    l2CategoryVO l2CategoryVO = new l2CategoryVO();
                                                    l2CategoryVO.setL1CategoryId(String.valueOf(entity.getParentCid()));
                                                    l2CategoryVO.setId(String.valueOf(entity.getCatId()));
                                                    l2CategoryVO.setName(entity.getName());
                                                    l2CategoryVO.setL3CategoryList(
                                                            CategoryEntities.stream()
                                                                    .filter(item -> item.getParentCid() == Long.valueOf(l2CategoryVO.getId()))
                                                                    .map(categoryEntity -> {
                                                                        com.chen.gulimall.product.VO.Web.l2CategoryVO.l3CategoryVO l3CategoryVO = new l2CategoryVO.l3CategoryVO();
                                                                        l3CategoryVO.setL2CategoryId(l2CategoryVO.getId());
                                                                        l3CategoryVO.setId(String.valueOf(categoryEntity.getCatId()));
                                                                        l3CategoryVO.setName(categoryEntity.getName());
                                                                        return l3CategoryVO;
                                                                    }).collect(Collectors.toList())
                                                    );
                                                    return l2CategoryVO;
                                                }
                                        ).collect(Collectors.toList());
                            }
                    ));
            //将查到的内容转为json放入redis(序列化）
            String s = JSON.toJSONString(collect);
            stringRedisTemplate.opsForValue().set("catalogJSON", s, 1 + (int) Math.random() * 5, TimeUnit.MINUTES);

            return collect;

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