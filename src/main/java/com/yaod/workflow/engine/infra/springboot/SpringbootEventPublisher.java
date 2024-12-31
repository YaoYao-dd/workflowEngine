package com.yaod.workflow.engine.infra.springboot;

import com.yaod.workflow.engine.core.event.Event;
import com.yaod.workflow.engine.core.event.EventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * @author Yaod
 **/
@Component("SpringbootPublisher")
public class SpringbootEventPublisher implements EventPublisher {

    private final ApplicationEventPublisher appEventPublisher;

    public SpringbootEventPublisher(ApplicationEventPublisher aep) {
        this.appEventPublisher=aep;
    }

    @Override
    public boolean publish(Event e) {
        this.appEventPublisher.publishEvent(e);
        return true;
    }
}
