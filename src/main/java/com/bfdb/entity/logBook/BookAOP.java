package com.bfdb.entity.logBook;


import com.bfdb.entity.LogBook;
import com.bfdb.entity.User;
import com.bfdb.service.LogBookService;
import com.bfdb.untils.Operation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

@Component
@Aspect
public class BookAOP implements Ordered {

    @Autowired
    private LogBookService logBookService;

//    @Pointcut("execution(* com.bfdb.controller..*.*(..))")
    @Pointcut("execution(* com.bfdb.controller.BookAOPController.*(..))")
    public void method(){}
//
//    @After(pointcut = "webExceptionLog()", throwing = "e")
//    public void after(JoinPoint joinPoint){
//
//    }
    @Pointcut("execution(* com.bfdb.controller.BookAOPController.*(..))") //切点
    public void webExceptionLog(){}



//    @AfterReturning(value = "execution(*  com.zbk.controller.*.*(..)))", returning = "ex")
    @AfterReturning("method()")
    public void afterReturning(JoinPoint joinPoint){
        insertLogBook(joinPoint,"正常");
    }

    /** 
      * 异常通知 用于拦截异常日志 
      * 
      * @param joinPoint 
      * @param e 
      */
    @AfterThrowing(pointcut = "webExceptionLog()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        insertLogBook(joinPoint,"错误");
    }

    private void insertLogBook(JoinPoint joinPoint, String logType){

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        // 2:通过springAOP切面JoinPoint类对象，获取该类，或者该方法，或者该方法的参数
        Class<? extends Object> clazz =  joinPoint.getTarget().getClass();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        Method method = signature.getMethod();
        Method[] methods = clazz.getDeclaredMethods();
        String methodOperation = "";
        for (Method m : methods) {
            if (m.equals(method)) {
                methodOperation = m.getName();
                if (m.isAnnotationPresent(Operation.class)) {
                    methodOperation = m.getAnnotation(Operation.class).name();
                }
            }
        }

        if(!"".equals(methodOperation)){
            LogBook logBook = new LogBook();
            Date date = new Date();
            logBook.setCreateTime(date);
            logBook.setLogContent(methodOperation);
            if(user!=null){
                logBook.setUserOperation(user.getUserName());
            }
            logBook.setLogType(logType);
            logBookService.insertLogBook(logBook);
        }
    }

    @Override
    public int getOrder() {
        return 1001;
    }
}
