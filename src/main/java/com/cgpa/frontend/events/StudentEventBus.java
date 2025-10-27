package com.cgpa.frontend.events;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple event bus for notifying UI components of student data changes.
 */
public final class StudentEventBus {
    private static final List<Runnable> listeners = new ArrayList<>();
    
    private StudentEventBus() {}
    
    public static void register(Runnable listener) {
        listeners.add(listener);
    }
    
    public static void fireStudentsChanged() {
        for (Runnable listener : listeners) {
            listener.run();
        }
    }
} 