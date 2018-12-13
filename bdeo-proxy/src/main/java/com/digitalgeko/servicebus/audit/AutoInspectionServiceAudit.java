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

    @Around("execution(* com.digitalgeko.servicebus.incoming.soap.BdeoInspectionServiceSoapInbound.updateInspectionState())")
    public  void aroundInbound(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("INBOUND REQ: " + (joinPoint.getArgs().length > 0 ? joinPoint.getArgs()[0] : "NO REQUEST"));
        Object response = joinPoint.proceed();
        log.info("INBOUND RESP: " + (response != null ? response.toString() : "NO RESPONSE"));
    }

    @Around("execution(* com.digitalgeko.servicebus.outgoing.rest.BdeoInspectionServiceRestOutbound.createAutoInspection())")
    public  void aroundCreateOutbound(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("OUTBOUND CREATE REQ: " + (joinPoint.getArgs().length > 0 ? joinPoint.getArgs()[0] : "NO REQUEST"));
        Object response = joinPoint.proceed();
        log.info("OUTBOUND CREATE RESP: " + (response != null ? response.toString() : "NO RESPONSE"));
    }

    @Around("execution(* com.digitalgeko.servicebus.outgoing.rest.BdeoInspectionServiceRestOutbound.queryAutoInspection())")
    public  void aroundQueryOutbound(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("OUTBOUND QUERY REQ: " + (joinPoint.getArgs().length > 0 ? joinPoint.getArgs()[0] : "NO REQUEST"));
        Object response = joinPoint.proceed();
        log.info("OUTBOUND QUERY RESP: " + (response != null ? response.toString() : "NO RESPONSE"));
    }

    @Around("execution(* com.digitalgeko.servicebus.outgoing.rest.BdeoInspectionServiceRestOutbound.deleteAutoInspection())")
    public  void aroundDeleteOutbound(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("OUTBOUND DELETE REQ: " + (joinPoint.getArgs().length > 0 ? joinPoint.getArgs()[0] : "NO REQUEST"));
        Object response = joinPoint.proceed();
        log.info("OUTBOUND DELETE RESP: " + (response != null ? response.toString() : "NO RESPONSE"));
    }

    @Around("execution(* com.digitalgeko.servicebus.outgoing.rest.BdeoInspectionServiceRestOutbound.updateAutoInspection())")
    public  void aroundUpdateOutbound(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("OUTBOUND UPDATE REQ: " + (joinPoint.getArgs().length > 0 ? joinPoint.getArgs()[0] : "NO REQUEST"));
        Object response = joinPoint.proceed();
        log.info("OUTBOUND UPDATE RESP: " + (response != null ? response.toString() : "NO RESPONSE"));
    }

}
