package site.minnan.rental.infrastructure.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Aspect
@Component
@Slf4j
public class ControllerLog {

    @Pointcut("execution(public * site.minnan.rental.userinterface.fascade..*..*(..))")
    private void controllerLog() {
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Around("controllerLog()")
    public Object logAroundController(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long time = System.currentTimeMillis();
        Object[] args = proceedingJoinPoint.getArgs();
        List<String> argStringList = Arrays.stream(args).map(Object::toString).collect(Collectors.toList());
        String argsString = objectMapper.writeValueAsString(argStringList);
        String methodFullName = proceedingJoinPoint.getTarget().getClass().getName()
                + "." + proceedingJoinPoint.getSignature().getName();
        log.info("controller调用{}，参数：{}", methodFullName, argsString);
        Object retValue = null;
        try {
            retValue = proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            log.error("调用接口异常", throwable);
        }
        time = System.currentTimeMillis() - time;
        String responseString = objectMapper.writeValueAsString(retValue);
        log.info("controller调用{}完成，返回数据:{}，用时{}ms", methodFullName, responseString, time);
        return retValue;
    }
}
