package com.chen.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.chen.gulimall.base.group.addGroup;
import com.chen.gulimall.base.group.updateGroup;
import com.chen.gulimall.base.valid.ListValue;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;


/**
 * 品牌
 * 
 * @author chen
 * @email 
 * @date 2023-08-13 23:35:04
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 品牌id
	 */
	@NotNull(groups = {updateGroup.class})
	@Null(groups = {addGroup.class})
	@TableId
	private Long brandId;
	/**
	 * 品牌名
	 */
	@NotBlank(message = "品牌名不能为空",groups = {updateGroup.class,addGroup.class})
	private String name;
	/**
	 * 品牌logo地址
	 */
	@NotBlank(groups = {addGroup.class})
	@URL(message="logo必须添加合法URL地址",groups = {updateGroup.class,addGroup.class})
	private String logo;
	/**
	 * 介绍
	 */
	private String descript;
	/**
	 * 显示状态[0-不显示；1-显示]
	 */
	@TableLogic
	@ListValue(vals = {0,1},groups = {addGroup.class})
	private Integer showStatus;
	/**
	 * 检索首字母
	 */
	@NotEmpty(groups = {addGroup.class})
	@Pattern(regexp = "^[a-zA-Z]$",message = "首字母必须是一个字母",groups = {updateGroup.class,addGroup.class})
	private String firstLetter;
	/**
	 * 排序
	 */
	@NotNull(groups = {addGroup.class})
	@Min(value = 0,message = "sort 最小值为0",groups = {updateGroup.class,addGroup.class})
	private Integer sort;

}
