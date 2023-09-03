package com.chen.gulimall.product.web;

import com.baomidou.mybatisplus.core.mapper.Mapper;
import com.chen.gulimall.product.VO.Web.l2CategoryVO;
import com.chen.gulimall.product.entity.CategoryEntity;
import com.chen.gulimall.product.service.CategoryService;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/8/30 21:42
 * @description
 */
@Controller
public class IndexController {
    @Autowired
    CategoryService categoryService;

    @Autowired
    RedissonClient redisson;

    @GetMapping({"/","/index.html"})
    public String indexPage(Model model){
        List<CategoryEntity> categoryEntities= categoryService.getLevel1Categorys();
        model.addAttribute("categorys",categoryEntities);
        return "index";
    }

    @GetMapping("/index/catalog.json")
    @ResponseBody
    public Map<String,List<l2CategoryVO>> getCatalogJson(){
        Map<String,List<l2CategoryVO>> map =  categoryService.getCatalogJson();
        return map;
    }

    @RequestMapping("/temp")
    public String hello(){
        RLock lock = redisson.getLock("my-lock");
        lock.lock();
//        阻塞式等待，默认锁定30s
//        自动续期，业务超长则自动续期
//        运行完成，自动停止续期，可自动解锁
        try {

        }catch (Exception e){

        }finally {
            lock.unlock();
        }
        return "";
    }
}
