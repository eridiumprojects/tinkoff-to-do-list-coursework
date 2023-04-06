//package com.example.todolistcoursework.util;
//
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.slf4j.Logger;
//
//public class LogUtils {
//
//    public static void logInput(String operationName, Logger logger, ProceedingJoinPoint joinPoint) {
//        if (logger.isDebugEnabled()) {
//            logger.debug("Start processing {} operation with arguments {}", operationName, joinPoint.getArgs());
//        } else {
//            logger.info("Start processing {} operation", operationName);
//        }
//    }
//
//    public static void logOutput(String operationName, Logger logger, Object result) {
//        if (logger.isDebugEnabled()) {
//            if (result == null) {
//                logger.debug("Finish processing {} operation", operationName);
//            } else {
//                logger.debug("Finish processing {} operation with result {}", operationName, result);
//            }
//        } else {
//            logger.info("Finish processing {} operation", operationName);
//        }
//    }
//}
