package com.tivic.manager.mob.ait.sync.observer;

import java.util.List;

public interface IAitSyncSubject {
    void notifyObservers(List<Integer> cdAitList);
}
