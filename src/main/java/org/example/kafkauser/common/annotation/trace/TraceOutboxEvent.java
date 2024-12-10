package org.example.kafkauser.common.annotation.trace;

import org.springframework.transaction.event.TransactionPhase;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TraceOutboxEvent {
    TransactionPhase phase();
}
