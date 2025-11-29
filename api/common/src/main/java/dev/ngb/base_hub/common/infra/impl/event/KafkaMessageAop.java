package dev.ngb.base_hub.common.infra.impl.event;

import dev.ngb.base_hub.common.api.tenant.OrganizationContextHolder;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class KafkaMessageAop {

    private final OrganizationContextHolder organizationContextHolder;

    @Around("@annotation(org.springframework.kafka.annotation.KafkaListener)")
    public Object manageOrgContext(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } finally {
            organizationContextHolder.clear();
        }
    }
}
