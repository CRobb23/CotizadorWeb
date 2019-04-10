package com.digitalgeko.servicebus.audit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class AutoInspectionServiceAudit {

    private static final Logger log = LoggerFactory.getLogger(AutoInspectionServiceAudit.class);

    @Around("execution(* com.digitalgeko.servicebus.incoming.soap.AutoInspectionServiceSoapInbound.updateInspectionState())")
    public  void aroundInbound(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("INBOUND REQ: " + (joinPoint.getArgs().length > 0 ? joinPoint.getArgs()[0] : "NO REQUEST"));
        Object response = joinPoint.proceed();
        log.info("INBOUND RESP: " + (response != null ? response.toString() : "NO RESPONSE"));
    }

    @Around("execution(* com.digitalgeko.servicebus.outgoing.rest.AutoInspectionServiceRestOutbound.updateAutoInspectionState())")
    public  void aroundOutbound(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("OUTBOUND REQ: " + (joinPoint.getArgs().length > 0 ? joinPoint.getArgs()[0] : "NO REQUEST"));
        Object response = joinPoint.proceed();
        log.info("OUTBOUND RESP: " + (response != null ? response.toString() : "NO RESPONSE"));
    }

}
