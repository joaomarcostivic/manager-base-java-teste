package com.tivic.manager.mob.lotes.service.impressao.processamento;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ThreadManager {

    private final List<Thread> threads = new ArrayList<>();

    public void addThread(Thread thread) {
        threads.add(thread);
    }

    public void iniciarThread(String name, Runnable task, Thread.UncaughtExceptionHandler handler) {
        Thread thread = new Thread(task);
        thread.setName(name);
        thread.setUncaughtExceptionHandler(handler);
        addThread(thread);
        thread.start();
    }

    public void removeThread(Thread thread) {
        thread.interrupt();
        threads.remove(thread);
    }

    public Thread getThread(String name) {
        Optional<Thread> thread = threads.stream().filter(t -> t.getName().equals(name)).findFirst();
        return thread.orElse(null);
    }
}
