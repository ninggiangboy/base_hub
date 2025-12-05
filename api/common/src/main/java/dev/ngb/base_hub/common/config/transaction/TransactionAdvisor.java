package dev.ngb.base_hub.common.config.transaction;

import dev.ngb.base_hub.base.annotation.QueryService;
import dev.ngb.base_hub.base.annotation.UseCaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

@Configuration
@EnableTransactionManagement
@RequiredArgsConstructor
public class TransactionAdvisor {

    private final TransactionManager txManager;

    @Bean
    public Advisor queryServiceTxAdvisor() {

        NameMatchTransactionAttributeSource txSource = new NameMatchTransactionAttributeSource();

        RuleBasedTransactionAttribute txAttr = new RuleBasedTransactionAttribute();
        txAttr.setReadOnly(true);
        txAttr.setPropagationBehavior(TransactionDefinition.PROPAGATION_SUPPORTS);

        txSource.addTransactionalMethod("*", txAttr);

        TransactionInterceptor interceptor = new TransactionInterceptor(txManager, txSource);

        return new DefaultPointcutAdvisor(
                new AnnotationMatchingPointcut(QueryService.class, true),
                interceptor
        );
    }

    @Bean
    public Advisor useCaseServiceTxAdvisor() {

        NameMatchTransactionAttributeSource txSource = new NameMatchTransactionAttributeSource();

        RuleBasedTransactionAttribute txAttr = new RuleBasedTransactionAttribute();
        txAttr.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        txSource.addTransactionalMethod("*", txAttr);

        TransactionInterceptor interceptor = new TransactionInterceptor(txManager, txSource);

        return new DefaultPointcutAdvisor(
                new AnnotationMatchingPointcut(UseCaseService.class, true),
                interceptor
        );
    }
}
