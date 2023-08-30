package com.chen.gulimall.base.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import java.util.HashSet;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/8/22 14:48
 * @description
 */
public class ListValueConstraintValidator implements ConstraintValidator<ListValue,Integer> {
    HashSet<Integer> integers = new HashSet<>();
    @Override
    public void initialize(ListValue constraintAnnotation) {
        int[] vals = constraintAnnotation.vals();
        for (int val : vals) {
            integers.add(val);
        }
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        return integers.contains(integer);
    }
}
