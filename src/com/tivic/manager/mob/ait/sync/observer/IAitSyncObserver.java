package com.tivic.manager.mob.ait.sync.observer;

import java.util.List;

public interface IAitSyncObserver {
    void onAitSyncPersisted(List<Integer> cdAitList);
}
