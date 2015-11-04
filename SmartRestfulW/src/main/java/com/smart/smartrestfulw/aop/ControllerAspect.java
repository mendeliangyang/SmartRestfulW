/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smart.smartrestfulw.aop;

import com.smart.common.model.OperateTypeEnum;
import com.smart.common.model.ReviveRSParamModel;
import com.smart.smartrestfulw.paramAnalyze.AnalyzeReviceParamByJackson;
import com.smart.smartrestfulw.paramAnalyze.IAnalyzeReviceParamModel;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 *
 * @author Administrator
 */
@Aspect
public class ControllerAspect {

//    @Around(value = "execution(* com.smart.smartrestfulw.controller.AutoODataController.*(..))")
//    public String aroundImpl(ProceedingJoinPoint joinPoint) throws Throwable {
//        try {
//            //  log
//            Object[] args = joinPoint.getArgs();
//            //Object objTarget = joinPoint.getTarget();
//            IAnalyzeReviceParamModel analyzeParam = new AnalyzeReviceParamByJackson();
//            ReviveRSParamModel paramModel = analyzeParam.transferReviveRSParamModel(args[0].toString(), null);
//            Object[] newArgs = new Object[]{paramModel};
//            
//            joinPoint.proceed(args);
//            
//            
//            //log
//            return "around";
//        } catch (Exception e) {
//            return "error";
//        } finally {
//            //log
//        }

//    }
}
