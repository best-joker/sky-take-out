package com.sky.aspect;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;

// import javassist.bytecode.SignatureAttribute.MethodSignature;
import org.aspectj.lang.reflect.MethodSignature;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    /**
     * 切入点
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut() {}

    // 前置通知
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("开始填充公共字段");
        // 获取方法类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature(); // 获取签名
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class); //获得方法上的注解
        OperationType operationType = autoFill.value(); // 从注解中获得值
        // 获取参数
        // 约定第一个位置为参数
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return;
        }

        // 准备数据
        Object entity = args[0];
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        // 先更新Update，再if判断
        try {
            // Method setCreateTime = entity.getClass().getMethod(AutoFillConstant.SET_CREATE_TIME,LocalDateTime.class); //获取的函数名称和类型
            Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME,LocalDateTime.class); //获取的函数名称和类型
            Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
            Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
            Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
            setUpdateTime.invoke(entity, now);
            setUpdateUser.invoke(entity, currentId);
            if (operationType == OperationType.INSERT) {
                setCreateTime.invoke(entity, now);
                setCreateUser.invoke(entity, currentId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
