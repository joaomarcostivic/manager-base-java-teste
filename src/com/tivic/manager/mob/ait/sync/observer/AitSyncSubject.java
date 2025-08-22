package com.tivic.manager.mob.ait.sync.observer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.tivic.sol.cdi.BeansFactory;

public class AitSyncSubject implements IAitSyncSubject {
	
    private final List<IAitSyncObserver> observers;
    private final ExecutorService executorService;
    private final IAitSyncObserver imageConversionObserver;

    public AitSyncSubject() throws Exception {
    	observers = new ArrayList<>();
        executorService = Executors.newCachedThreadPool();
        imageConversionObserver = (IAitSyncObserver)  BeansFactory.get(IAitSyncObserver.class);
        observers.add(imageConversionObserver);
    }

    @Override
    public void notifyObservers(List<Integer> cdAitList) {
        if (!cdAitList.isEmpty()) {
            executorService.submit(() -> {
                for (IAitSyncObserver observer : observers) {
                    observer.onAitSyncPersisted(cdAitList);
                }
            });
        }
    }
}