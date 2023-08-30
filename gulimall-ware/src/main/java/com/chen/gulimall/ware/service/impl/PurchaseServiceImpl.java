package com.chen.gulimall.ware.service.impl;

import com.chen.gulimall.base.utils.WareConstant;
import com.chen.gulimall.ware.VO.DoneVo;
import com.chen.gulimall.ware.VO.MergeVo;
import com.chen.gulimall.ware.VO.itemDoneVo;
import com.chen.gulimall.ware.entity.PurchaseDetailEntity;
import com.chen.gulimall.ware.service.PurchaseDetailService;
import com.chen.gulimall.ware.service.WareSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.gulimall.base.utils.PageUtils;
import com.chen.gulimall.base.utils.Query;

import com.chen.gulimall.ware.dao.PurchaseDao;
import com.chen.gulimall.ware.entity.PurchaseEntity;
import com.chen.gulimall.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {
    @Autowired
    PurchaseDetailService purchaseDetailService;
    @Autowired
    WareSkuService wareSkuService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageUnreceive(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>().eq("status",0).or().eq("status",1)
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void mergePurchase(MergeVo mergeVo) {

        Long purchaseId = mergeVo.getPurchaseId();
        if(purchaseId==null){
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setUpdateTime(new Date());
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setStatus(WareConstant.PURCHASE_STATUS.CREATED.getCode());
            this.save(purchaseEntity);
            purchaseId = purchaseEntity.getId();
        }

        List<Long> items = mergeVo.getItems();
        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> collect = items.stream().map(i -> {
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
            detailEntity.setId(i);
            detailEntity.setPurchaseId(finalPurchaseId);
            detailEntity.setStatus(WareConstant.PURCHASE_DETAIL_STATUS.ASSIGNED.getCode());
            return detailEntity;
            //todo 确认采购单状态
        }).collect(Collectors.toList());
        purchaseDetailService.updateBatchById(collect);

        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(purchaseId);
        purchaseEntity.setUpdateTime(new Date());
        this.updateById(purchaseEntity);
    }

    @Override
    public void received(List<Long> ids) {
        List<PurchaseEntity> collect = ids.stream().map(item -> this.getById(item)
                ).filter(item -> item.getStatus() == WareConstant.PURCHASE_STATUS.CREATED.getCode() || item.getStatus() == WareConstant.PURCHASE_STATUS.ASSIGNED.getCode())
                .map(item -> {
                    item.setStatus(WareConstant.PURCHASE_STATUS.RECEIVE.getCode());
                    item.setUpdateTime(new Date());
                    return item;
                }).collect(Collectors.toList());
        this.updateBatchById(collect);
        collect.forEach(item ->{
            List<PurchaseDetailEntity> entities= purchaseDetailService.listByPurchaseId(item.getId());
            List<PurchaseDetailEntity> collect1 = entities.stream().map(entity -> {
                PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
                purchaseDetailEntity.setStatus(WareConstant.PURCHASE_DETAIL_STATUS.BUYING.getCode());
                purchaseDetailEntity.setId(entity.getId());
                return purchaseDetailEntity;
            }).collect(Collectors.toList());
            purchaseDetailService.updateBatchById(collect1);
        });
    }

    @Override
    public void done(DoneVo doneVo) {
        Long id = doneVo.getId();
        Boolean flag = true;
        List<itemDoneVo> list = doneVo.getItemDoneVoList();
        ArrayList<PurchaseDetailEntity> updates = new ArrayList<>();
        for (itemDoneVo vo : list) {
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
            if(vo.getStatus() == WareConstant.PURCHASE_DETAIL_STATUS.FAILED.getCode()){
                flag = false;
                detailEntity.setStatus(vo.getStatus());
            }else{
                detailEntity.setStatus(WareConstant.PURCHASE_DETAIL_STATUS.FINISH.getCode());
                PurchaseDetailEntity entity = purchaseDetailService.getById(vo.getItemId());
                wareSkuService.addStock(entity.getSkuId(),entity.getWareId(),entity.getSkuNum());
            }
            detailEntity.setId(vo.getItemId());
            updates.add(detailEntity);
        }
        purchaseDetailService.updateBatchById(updates);
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(id);
        purchaseEntity.setStatus(flag?WareConstant.PURCHASE_STATUS.FINISH.getCode() : WareConstant.PURCHASE_STATUS.HASERROR.getCode());
        purchaseEntity.setUpdateTime(new Date());
        this.updateById(purchaseEntity);
    }
}