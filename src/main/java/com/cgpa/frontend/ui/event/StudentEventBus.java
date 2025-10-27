package com.cgpa.frontend.ui.event;

import java.util.ArrayList;
import java.util.List;

public class StudentEventBus {
    private static StudentEventBus instance;
    private List<Runnable> listeners = new ArrayList<>();

    private StudentEventBus() {}

    public static StudentEventBus getInstance() {
        if (instance == null) {
            instance = new StudentEventBus();
        }
        return instance;
    }

    public void addListener(Runnable listener) {
        listeners.add(listener);
    }

    public void removeListener(Runnable listener) {
        listeners.remove(listener);
    }

    public void notifyListeners() {
        for (Runnable listener : listeners) {
            listener.run();
        }
    }
} 