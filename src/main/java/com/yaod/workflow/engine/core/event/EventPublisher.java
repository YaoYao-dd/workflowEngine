package com.yaod.workflow.engine.core.event;

/**
 * @author Yaod
 **/
public interface EventPublisher {
    boolean publish(Event e);
}
