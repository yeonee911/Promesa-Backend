package com.promesa.promesa.common.advice;

import com.promesa.promesa.domain.member.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class ItemLogAdvice {
    @Pointcut("@annotation(com.promesa.promesa.common.advice.ItemLog)")
    public void itemLogRecord() {}

    @Around("itemLogRecord()")
    public Object logItemActions(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature sig = (MethodSignature) pjp.getSignature();
        Method method = sig.getMethod();

        Long memberId = null;
        Long itemId = null;

        Object[] args = pjp.getArgs();
        for (int i = 0; i < args.length; i++) {
            String paramName = method.getParameters()[i].getName();
            System.out.println(paramName);
            if ("itemId".equals(paramName)) {
                itemId = (Long) args[i];
            } else if ("member".equals(paramName)) {
                Member ud = (Member) args[i];
                if (ud != null) memberId = ud.getId();
            }
        }

        String memberLog = (memberId != null) ? "회원" + String.valueOf(memberId) : "비회원";

        try {
            Object result = pjp.proceed();
            log.info("{}이(가) {}에 대해 {} 완료", memberLog, itemId, method.getName());
            return result;
        } catch (Throwable t) {
            log.warn("{}이(가) {}에 대해 {} 실패", memberLog, itemId, method.getName());
            throw t;
        }
    }
}
