package com.saulo.event;

import java.util.*;
import java.util.function.Consumer;

public class EventBus {

    private final Map<Class<?>, List<Consumer<Object>>> subscribers = new HashMap<>();

    public <T> void subscribe(
            Class<T> eventType,
            Consumer<T> handler
    ) {
        subscribers
                .computeIfAbsent(eventType, key -> new ArrayList<>())
                .add(event -> handler.accept(eventType.cast(event)));
    }

    public void publish(Object event) {
        List<Consumer<Object>> handlers = subscribers.get(event.getClass());

        if (handlers == null) {
            return;
        }

        for (Consumer<Object> handler : handlers) {
            handler.accept(event);
        }
    }
}
