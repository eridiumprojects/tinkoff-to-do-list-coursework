package com.example.todolistcoursework.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggerAspect {

    @Pointcut("execution(* com.example.todolistcoursework.service.*.*(..))")
    public void servicePackagePointcut() {}

    @Around("servicePackagePointcut()")
    public Object logServicePackagePointcut(ProceedingJoinPoint joinPoint) throws Throwable {
        return logDebugInOut(joinPoint, false);
    }

    @Pointcut("execution(* com.example.todolistcoursework.controller.*.*(..))")
    public void controllerPackagePointcut() {}

    @Around("controllerPackagePointcut()")
    public Object logControllerPackagePointcut(ProceedingJoinPoint joinPoint) throws Throwable {
        return logDebugInOut(joinPoint, true);
    }

    private static Object logDebugInOut(ProceedingJoinPoint joinPoint, boolean controller) throws Throwable {
        var jpSignature = joinPoint.getSignature();

        var methodName = jpSignature.getName();

        Object object;
        log.debug("{}#{}.in {}", jpSignature.getDeclaringType().getSimpleName(), methodName,
                    joinPoint.getArgs());

        object = joinPoint.proceed();
        if (controller) {
            ResponseEntity<?> a = (ResponseEntity<?>)object;
            log.debug("{}#{}.out {}", jpSignature.getDeclaringType().getSimpleName(), methodName, a.getBody());
        } else {
            log.debug("{}#{}.out {}", jpSignature.getDeclaringType().getSimpleName(), methodName, object);
        }

        return object;
    }
}
