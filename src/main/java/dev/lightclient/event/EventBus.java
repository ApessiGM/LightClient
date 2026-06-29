package dev.lightclient.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A lightweight, reflection based event bus.
 *
 * <p>Subscribers register objects whose methods are annotated with
 * {@link EventTarget}. Each such method must take a single parameter that is a
 * subclass of {@link Event}. Handlers are cached per event type and sorted by
 * descending priority for deterministic, low overhead dispatch.</p>
 */
public final class EventBus {
    private final Map<Class<? extends Event>, List<Handler>> handlers = new ConcurrentHashMap<>();

    /**
     * Registers every {@link EventTarget} annotated method declared on the
     * given object (including inherited methods).
     */
    public void subscribe(Object subscriber) {
        for (Method method : collectMethods(subscriber.getClass())) {
            if (!method.isAnnotationPresent(EventTarget.class)) {
                continue;
            }
            if (method.getParameterCount() != 1) {
                continue;
            }
            Class<?> param = method.getParameterTypes()[0];
            if (!Event.class.isAssignableFrom(param)) {
                continue;
            }
            method.setAccessible(true);
            @SuppressWarnings("unchecked")
            Class<? extends Event> eventType = (Class<? extends Event>) param;
            EventTarget annotation = method.getAnnotation(EventTarget.class);
            List<Handler> list = handlers.computeIfAbsent(eventType, k -> new ArrayList<>());
            synchronized (list) {
                list.add(new Handler(subscriber, method, annotation.priority()));
                list.sort(Comparator.comparingInt((Handler h) -> h.priority).reversed());
            }
        }
    }

    /** Removes all handlers belonging to the given subscriber. */
    public void unsubscribe(Object subscriber) {
        for (List<Handler> list : handlers.values()) {
            synchronized (list) {
                list.removeIf(h -> h.target == subscriber);
            }
        }
    }

    /**
     * Dispatches the event to every registered handler.
     *
     * @return the event for convenient inline use.
     */
    public <T extends Event> T post(T event) {
        List<Handler> list = handlers.get(event.getClass());
        if (list == null) {
            return event;
        }
        List<Handler> snapshot;
        synchronized (list) {
            snapshot = new ArrayList<>(list);
        }
        for (Handler handler : snapshot) {
            handler.invoke(event);
        }
        return event;
    }

    private static List<Method> collectMethods(Class<?> type) {
        List<Method> methods = new ArrayList<>();
        Class<?> current = type;
        while (current != null && current != Object.class) {
            for (Method method : current.getDeclaredMethods()) {
                methods.add(method);
            }
            current = current.getSuperclass();
        }
        return methods;
    }

    private static final class Handler {
        private final Object target;
        private final Method method;
        private final int priority;

        private Handler(Object target, Method method, int priority) {
            this.target = target;
            this.method = method;
            this.priority = priority;
        }

        private void invoke(Event event) {
            try {
                method.invoke(target, event);
            } catch (Exception exception) {
                throw new RuntimeException("Failed to dispatch event " + event.getClass().getSimpleName()
                        + " to " + target.getClass().getSimpleName() + "#" + method.getName(), exception);
            }
        }
    }
}
